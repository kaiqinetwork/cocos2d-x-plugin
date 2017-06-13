package org.cocos2dx.plugin.qh360;

import java.util.HashMap;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONObject;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr.MatrixCallBack;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class SDKWrapper {
	private static final String LOG_TAG = "QH360.SDKWrapper";
	private static final String PLUGIN_NAME = "qh360";
	private static final String PLUGIN_VERSION = "1.0.0";
	private static final String SDK_VERSION = "1.8.0";
	private static SDKWrapper mInstance;
	private Activity mActivity;
	@SuppressWarnings("unused")
	private boolean mDebug;
	@SuppressWarnings("unused")
	private IAPAdapter mIAPAdapter;
	private boolean mInited;
	private UserAdapter mUserAdapter;
	private String mUid = null;
	private String mUname = null;
	private String mAccessToken = null;
	private boolean mLoggedIn;
	private String mQH360UserinfoType = null;
	
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

	public boolean initSDK(Activity act, Hashtable<String, String> devInfo, Object adapter,
			final ILoginCallback listener) {
		logD("start init SDK ");
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

		Matrix.setActivity(mActivity, new MatrixCallBack() {

			public void execute(Context context, int funtionCode, String funtionParams) {
				if (funtionCode == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
					doSdkSwitchAccount(); 
				} else if (funtionCode == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
					listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "SDKWrapper.getInstance().initSDK success");
					mInited = true;
				}
			}

		});
		
		Matrix.setKillAppTag(true);

		setPluginListener();
		logD("init SDK finish");
		return mInited;
	}

	private void setPluginListener() {
		logD("setPluginListener");
		PluginWrapper.addListener(new PluginListener() {
			public void onStop() {
				Matrix.onStop(mActivity);
			}

			public void onResume() {
				logD("onResume");
				Matrix.onResume(mActivity);
			}

			public void onRestart() {
				Matrix.onRestart(mActivity);
			}

			public void onPause() {
				logD("onPause");
				Matrix.onPause(mActivity);
			}

			public void onNewIntent(Intent intent) {
				Matrix.onNewIntent(mActivity,intent);
			}

			public void onDestroy() {
				Matrix.destroy(mActivity);
			}

			public void onActivityResult(int requestCode, int resultCode, Intent data) {
				Matrix.onActivityResult(mActivity, requestCode, resultCode, data);
			}
		});
	}

	public boolean isInited() {
		return mInited;
	}

	public void userLogin(final Activity act, final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				logD("login invoked!");
				Intent intent = new Intent(mActivity, ContainerActivity.class); 
				intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN); 
				intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);
				
				Matrix.execute(mActivity, intent, new IDispatcherCallback() {
					@Override
					public void onFinished(String data) {
						if (isCancelLogin(data)) {
							listener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "Login Cancel");
							return;
						}
			            mAccessToken = parseAccessTokenFromLoginResult(data);
			            if (!TextUtils.isEmpty(mAccessToken)) {
			            	mLoggedIn = true;
			            	listener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "Login success");
			            } else {
			            	listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "Login get access_token fail");
			            }
					}
				});
			}
		});
	}

	protected void doSdkSwitchAccount() {
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				logD("login invoked!");
				Intent intent = new Intent(mActivity, ContainerActivity.class); 
				intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT); 
				intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true); 
				
		        Matrix.invokeActivity(mActivity, intent, new IDispatcherCallback(){
					@Override
					public void onFinished(String data) {
						if (isCancelLogin(data)) {
							mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_FAIL, "SwitchAccount Cancel");
							return;
						}
			            mAccessToken = parseAccessTokenFromLoginResult(data);
			            if (!TextUtils.isEmpty(mAccessToken)) {
			            	mLoggedIn = true;
			            	mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_SUCCESS, "SwitchAccount success");
			            } else {
			            	mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_FAIL, "SwitchAccount get access_token fail");
			            }	
					}
		        });
			}
		});
    }
	
    private boolean isCancelLogin(String data) {
        try {
            JSONObject joData = new JSONObject(data);
            int errno = joData.optInt("errno", -1);
            if (-1 == errno) {
            	logD(data);
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
    
    private String parseAccessTokenFromLoginResult(String loginRes) {
        try {

            JSONObject joRes = new JSONObject(loginRes);
            JSONObject joData = joRes.getJSONObject("data");
            return joData.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void onGotUserInfo(String type) {
    	mQH360UserinfoType = type;
    	
    	logD("mUid  " + mUid + "    mUname  " + mUname);
    	
		PluginWrapper.runOnMainThread(new Runnable() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
		    	HashMap eventParams=new HashMap();
				eventParams.put("type",mQH360UserinfoType);
				eventParams.put("zoneid",0);
				eventParams.put("zonename","\u65e0");
				eventParams.put("roleid",mUid);
				eventParams.put("rolename",mUname);
				eventParams.put("professionid",0);
				eventParams.put("profession","\u65e0");
				eventParams.put("gender","\u65e0");
				eventParams.put("rolelevel",0);
				eventParams.put("power",0);
				eventParams.put("vip",0);
				eventParams.put("partyid",0);
				eventParams.put("partyname","\u65e0");
				eventParams.put("partyroleid",0);
				eventParams.put("partyrolename","\u65e0");
				eventParams.put("friendlist","\u65e0");		
				
				Matrix.statEventInfo(mActivity, eventParams);
				logD("Matrix.statEventInfo");
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

	public void setUserId(String id) {
		mUid = id;
	}
	
	public String getUserName() {
		return mUname;
	}

	public void setUserName(String name) {
		mUname = name;
	}
	
	public String getAccessToken() {
		return mAccessToken;
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
