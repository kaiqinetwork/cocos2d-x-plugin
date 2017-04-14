package org.cocos2dx.plugin.mumayi;

import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.mumayi.paymentmain.business.FindUserDataListener;
import com.mumayi.paymentmain.business.ResponseCallBack;
import com.mumayi.paymentmain.business.onLoginListener;
import com.mumayi.paymentmain.business.onTradeListener;
import com.mumayi.paymentmain.ui.PaymentCenterInstance;
import com.mumayi.paymentmain.ui.PaymentUsercenterContro;
import com.mumayi.paymentmain.ui.pay.MMYInstance;
import com.mumayi.paymentmain.ui.usercenter.PaymentFloatInteface;
import com.mumayi.paymentmain.util.PaymentConstants;
import com.mumayi.paymentmain.vo.UserBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SDKWrapper {
	private static final String LOG_TAG = "mumayi.SDKWrapper";
	private static final String PLUGIN_NAME = "Mumayi";
	private static final String PLUGIN_VERSION = "1.0.0";
	private static final String SDK_VERSION = "3.3.0";
	private static SDKWrapper mInstance;
	private String mMumayiAppKey;
	private String mMumayiAppSecret;
	private Activity mActivity;
	private boolean mDebug;
	private IAPAdapter mIAPAdapter;
	private boolean mInited;
	private UserAdapter mUserAdapter;
	private boolean mLoggedIn;
	private String mUid;
	private String mAccessToken;
	private String mIAPDebugCallbackUrl;
	private ILoginCallback mLoginListener;
	private ILoginCallback mLogoutListener;
	private ILoginCallback mPayListener;
	private PaymentCenterInstance mPaymentInstance;
	private PaymentFloatInteface mFloatInteface;
	private PaymentUsercenterContro mUserCenter;

	public SDKWrapper() {
		this.mActivity = null;
		this.mDebug = false;
		this.mUserAdapter = null;
		this.mIAPAdapter = null;
		this.mPaymentInstance = null;
		this.mFloatInteface = null;
		this.mUserCenter = null;
		this.mLoginListener = null;
		this.mLogoutListener = null;
		this.mPayListener = null;
	}

	static {
		mInstance = null;
	}

	public static SDKWrapper getInstance() {
		if (mInstance == null) {
			mInstance = new SDKWrapper();
		}
		return mInstance;
	}

	public boolean initSDK(Activity act, Hashtable<String, String> devInfo, Object adapter,final ILoginCallback listener) {
		if (adapter instanceof InterfaceUser) {
			mUserAdapter = (UserAdapter) adapter;
		} else if (adapter instanceof InterfaceIAP) {
			mIAPAdapter = (IAPAdapter) adapter;
		}
		if (mInited) {
			return true;
		}

		mActivity = act;
		mInited = false;
		mDebug = PluginHelper.getDebugMode();

		mMumayiAppKey = devInfo.get("mumayiAppKey");

		if (mMumayiAppKey == null) {
			logD("getDeveloperInfo error!");
			return false;
		}
		// 初始化支付SDK用户中心
		mPaymentInstance = PaymentCenterInstance.getInstance(mActivity);
		mPaymentInstance.initial(mMumayiAppKey, "K7游戏中心");
		mUserCenter = mPaymentInstance.getUsercenterApi(mActivity);
		mPaymentInstance.setTestMode(false);
		mPaymentInstance.findUserData(new FindUserDataListener() {
			@Override
			public void findUserDataComplete() {	
			}
		});
		mPaymentInstance.setListeners(new onLoginListener() {
			@Override
			public void onLoginFinish(String loginResult) {
				try {
					if (null != loginResult) {
						JSONObject loginJson = new JSONObject(loginResult);
						String loginState = loginJson.getString(PaymentConstants.LOGIN_STATE);
						logD("loginJson:" + loginJson);
						if (loginState != null && loginState.equals(PaymentConstants.STATE_SUCCESS)) {
							// 登录成功
							mLoggedIn = true;
							if (mLoginListener != null)
								mLoginListener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "Login Success");					
							mUid = loginJson.getString("uid");
							mAccessToken = loginJson.getString("token");
							logD("login success,uid:" + mUid + ",accessToken:" + mAccessToken);
						} else {
							// 登录失败
							mLoggedIn = false;
							String error = loginJson.getString("error");
							if (error != null && error.trim().length() > 0 && error.equals("cancel_login")){
								logD("login calcel");
								mLoginListener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "Login Cancel");
							 }else{
								if (mLoginListener != null)
									mLoginListener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "Login Failed");
								logD("login fail");
							 }	
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					mLoggedIn = false;
					if (mLoginListener != null)
						mLoginListener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "Login Failed");
				}
			}

			@Override
			public void onLoginOut(String logoutResult) {
				try {
					JSONObject json = new JSONObject(logoutResult);
					String logoutState = json.getString("loginOutCode");
					if (logoutState.equals(PaymentConstants.STATE_SUCCESS)) {
						if(mLogoutListener != null)
							mLogoutListener.onSuccessed(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
						logD("logout success！" + logoutResult);
					} else {
						if(mLogoutListener != null)
							mLogoutListener.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout failed");
						logD("logout fail!" + logoutResult);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(mLogoutListener != null)
						mLogoutListener.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout failed");
					logD("logout fail!" + logoutResult);
				}
			}
		});

		mPaymentInstance.setTradeListener(new onTradeListener() {
			@Override
			public void onTradeFinish(String tradeType, int tradeCode, Intent intent) {
				if (tradeCode == MMYInstance.PAY_RESULT_SUCCESS) {
					logD("pay success");
					Bundle bundle = intent.getExtras();
					String orderId = bundle.getString("orderId");
					String productName = bundle.getString("prodctName");
					String productPrice = bundle.getString("productPrice");
					String productDesc = bundle.getString("productDesc");

					logD("orderId:" + orderId + ",prodName:" + productName + ",prodPrice:" + productPrice + "prodDesc:"+ productDesc);
					mUserCenter.checkUserState(mActivity); // 检测用户是否完善了资料
					if(mPayListener != null)
						mPayListener.onSuccessed(IAPWrapper.PAYRESULT_SUCCESS, "pay success");
				} else {
					logD("pay failed");
					if(mPayListener != null)
						mPayListener.onFailed(IAPWrapper.PAYRESULT_FAIL, "pay fail");
				}
			}
		});

		// 设置切换完账号后是否自动跳转登陆
		mPaymentInstance.setChangeAccountAutoToLogin(true);
		// 显示悬浮球
		mFloatInteface = mPaymentInstance.createFloat();
		mFloatInteface.show();

		setPluginListener();
		mInited = true;
		logD("init SDK finish");

		return mInited;
	}

	private void setPluginListener() {
		logD("setPluginListener");
		PluginWrapper.addListener(new PluginListener() {
			public void onStop() {

			}

			public void onResume() {
				logD("onResume");
			}

			public void onRestart() {

			}

			public void onPause() {
				logD("onPause");
			}

			public void onNewIntent(Intent intent) {

			}

			public void onDestroy() {
				if (null != mFloatInteface) {
					mFloatInteface.close();
				}

				if (null != mPaymentInstance) {
					mPaymentInstance.finish();
					mPaymentInstance.exit();
				}
			}

			public void onActivityResult(int requestCode, int resultCode, Intent data) {
			}
		});
	}

	public boolean isInited() {
		return mInited;
	}

	public void userLogin(final Activity act, final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				mLoginListener = listener;
				mPaymentInstance.findUserData(new FindUserDataListener() {
					@Override
					public void findUserDataComplete() {
						mPaymentInstance.go2Login(mActivity);
					}
				});
			}
		});
	}

	public void userLogout(final Activity act, final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				mLogoutListener = listener;
				if (null != mUserCenter) {
					UserBean user = PaymentConstants.NOW_LOGIN_USER;
					mUserCenter.loginOut(mActivity, user.getName(), new ResponseCallBack() {
						@Override
						public void onSuccess(Object obj) {
							listener.onSuccessed(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
						}

						@Override
						public void onFail(Object obj) {
							listener.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout failed");
						}
					});
				}
			}
		});
	}

	public void pay(final Activity act, final String productName,final String productPrice,final String productDesc,final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				mPayListener = listener;
				mUserCenter.pay(mActivity,productName,productPrice,productDesc);
			}
		});
	}
	
	public void setLoggedIn(boolean value) {
		mLoggedIn = value;
	}

	public boolean isLoggedIn() {
		return mLoggedIn;
	}

	public String getUserId() {
		return mUid;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public String getIAPDebugCallbackUrl() {
		return mIAPDebugCallbackUrl;
	}

	public String getSDKVersion() {
		return SDK_VERSION;
	}

	public String getPluginVersion() {
		return PLUGIN_VERSION;
	}

	public String getPluginName() {
		return PLUGIN_NAME;
	}

	protected void logE(String msg, Exception e) {
		if (e == null) {
			PluginHelper.logE(LOG_TAG, msg);
		} else {
			PluginHelper.logE(LOG_TAG, msg, e);
		}
	}

	protected void logD(String msg) {
		try {
			PluginHelper.logD(LOG_TAG, msg);
		} catch (Exception e) {
			logE("logD error", e);
		}
	}

	protected void logI(String msg) {
		try {
			PluginHelper.logI(LOG_TAG, msg);
		} catch (Exception e) {
			logE("logD error", e);
		}
	}
}
