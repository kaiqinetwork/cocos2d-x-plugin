package org.cocos2dx.plugin.vivo;

import java.util.Hashtable;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONObject;

import com.vivo.sdkplugin.aidl.VivoUnionManager;
import com.vivo.sdkplugin.accounts.OnVivoAccountChangedListener;
import com.bbk.payment.payment.OnVivoSinglePayResultListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SDKWrapper {
	private static final String LOG_TAG = "vivo.SDKWrapper";
    private static final String PLUGIN_NAME = "Vivo";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "2.0.9";
    private static SDKWrapper mInstance;
    public String mVivoAppId;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private boolean mLoggedIn;
    private String mUid;
    private String mAccessToken;
    private String mIAPDebugCallbackUrl;
    private ILoginCallback mAccountSwitchListener;
    private VivoUnionManager mVivoUnionManager;
    private boolean callLogin;
    OnVivoAccountChangedListener mOnVivoAccountChangedListener;
    OnVivoSinglePayResultListener mOnVivoPayResultListener;
    private ILoginCallback mLoginListener;

    public SDKWrapper() {
        this.mActivity = null;
        this.mDebug = false;
        this.mUserAdapter = null;
        this.mIAPAdapter = null;
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
    
    public boolean initSDK(Activity act, Hashtable<String, String> devInfo, Object adapter, final ILoginCallback listener) {
        if (adapter instanceof InterfaceUser) {
            mUserAdapter = (UserAdapter) adapter;
        } else if (adapter instanceof InterfaceIAP) {
            mIAPAdapter = (IAPAdapter) adapter;
        }
        if (mInited) {
            return true;
        }
        mActivity = act;
        mInited = true;
        mDebug = PluginHelper.getDebugMode();
        mVivoAppId = (String) devInfo.get("VivoAppId");
        mAccountSwitchListener = new ILoginCallback() {

			@Override
			public void onSuccessed(int code, String msg) {
				mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_SUCCESS, msg);
			}

			@Override
			public void onFailed(int code, String msg) {
				mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_FAIL, msg);
			}
        };
        mInited = true;
        mVivoUnionManager = new VivoUnionManager((Context)mActivity);
		mVivoUnionManager.singlePaymentInit((Context)mActivity);
		logD("singlePaymentInit end");
        mVivoUnionManager.registVivoAccountChangeListener(mOnVivoAccountChangedListener);
        mVivoUnionManager.bindUnionService();
        mOnVivoPayResultListener = new OnVivoSinglePayResultListener() {
        	
			@Override
			public void payResult(String transNo, boolean pay_result, String result_code,
					String pay_msg) {
				logD("transNo:" + transNo + "pay_result:" + pay_result + "result_code" + result_code + "msg" + pay_msg);
				switch(Integer.parseInt(result_code)){
				case 9000://支付成功
					mIAPAdapter.payResult(IAPWrapper.PAYRESULT_SUCCESS, "msg:" + pay_msg);
					break;
				case 1000://请求参数错误
				case 4000://支付系统异常
				case 4006://支付失败(返回4006时有可能是调用第三方支付已经成功，但由于网络原因第三方的支付控件没有接收到支付成功的信息，所以接收到4006的状态码时以后台异步通知为准，或者调用后台订单查询接口查询支付结果)
				case 4012://订单信息无效
					mIAPAdapter.payResult(IAPWrapper.PAYRESULT_FAIL, "msg:" + pay_msg);
					break;
				case 6000://支付服务正在进行升级操作
				case 6001://用户取消支付
					mIAPAdapter.payResult(IAPWrapper.PAYRESULT_CANCEL, "msg:" + pay_msg);
					break;
				}
				
			}
        	
        };
        mVivoUnionManager.initVivoSinglePayment((Context)mActivity, mOnVivoPayResultListener);
        setPluginListener();
       
        return mInited;
    }

    private void setPluginListener() {
        logD("setPluginListener");
        PluginWrapper.addListener(new PluginListener() {
        	public void onStop() {
            }

            public void onResume() {
                logD("onResume");
                mVivoUnionManager.showVivoAssitView((Context)mActivity);
            }

            public void onRestart() {
            }

            public void onPause() {
                logD("onPause");
                
            }

            public void onNewIntent(Intent intent) {
            }

            public void onDestroy() {
            	mVivoUnionManager.hideVivoAssitView((Context)mActivity);
                mVivoUnionManager.unRegistVivoAccountChangeListener(mOnVivoAccountChangedListener);
                mVivoUnionManager.cancelVivoSinglePayment(mOnVivoPayResultListener);
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
            }            
        });
    }
    
    public boolean isInited() {
        return mInited;
    }

    public void userLogin(final Activity act, final ILoginCallback listener) {
    	callLogin = true;
    	mLoginListener = listener;
    	mVivoUnionManager.startLogin(this.mVivoAppId);
    }

    public void setLoggedIn(boolean value) {
        mLoggedIn = value;
    }
    
    public void paymentExit() {
		mVivoUnionManager.singlePaymentExit ((Context)mActivity);
		logD("PaymentExit Success");
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
