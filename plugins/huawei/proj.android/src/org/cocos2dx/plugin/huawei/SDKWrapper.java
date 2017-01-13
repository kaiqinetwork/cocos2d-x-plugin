package org.cocos2dx.plugin.huawei;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
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

import com.huawei.gamebox.buoy.sdk.impl.BuoyOpenSDK;
import com.huawei.gamebox.buoy.sdk.inter.UserInfo;
import com.huawei.opensdk.OpenSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class SDKWrapper {
	private static final String LOG_TAG = "huawei.SDKWrapper";
    private static final String PLUGIN_NAME = "HuaWei";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "7.1.0.305";
    private static SDKWrapper mInstance;
    private String mHuaweiAppId;
    private String mHuaweiAppKey;
    private String mHuaweiPayId;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private boolean mLoggedIn;
    private String mUid;
    private String mAccessToken;
    private String mIAPDebugCallbackUrl;
    private String mCheckTokenUrl;

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
        mHuaweiAppId = devInfo.get("HuaweiAppId");
        mHuaweiPayId = devInfo.get("HuaweiPayId");
        mHuaweiAppKey = devInfo.get("HuaweiAppKey");
        mCheckTokenUrl = devInfo.get("HuaweiCheckTokenUrl");
        mDebug = PluginHelper.getDebugMode();
        setPluginListener();
        
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	BuoyOpenSDK.getIntance().showSmallWindow((Context)mActivity);
            	int retCode = OpenSDK.showAppinfo(
            			mActivity,
            			mHuaweiAppId,
            			mHuaweiPayId,
            			mHuaweiAppKey);
            	String msg;
            	switch (retCode){
            		case 0: 	//初始化成功
            			logD("init success");
            			listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "Success");
            			break;
            		case 1001:	//参数Activity非法
            			msg = "Activity is null";
            		case 1002:	//参数appId为空
            			msg = "appId is null";
            		case 1003:	//参数cpId为空
            			msg = "cpId is null";
            		case 1004:	//参数privateKey为空
            			msg = "privateKey is null";
            			listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, msg);
            			mInited = false;
            			break;
            		default:
            			listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "Unknown Error");
            			mInited = false;
            			break;
            	}
            }
        });
        
        return mInited;
    }

    private void setPluginListener() {
        logD("setPluginListener");
        PluginWrapper.addListener(new PluginListener() {
        	public void onStop() {
            }

            public void onResume() {
                logD("onResume");
                BuoyOpenSDK.getIntance().showSmallWindow((Context)mActivity);
            }

            public void onRestart() {
            }

            public void onPause() {
                logD("onPause");
                BuoyOpenSDK.getIntance().hideSmallWindow((Context)mActivity);
            }

            public void onNewIntent(Intent intent) {
            }

            public void onDestroy() {
            	BuoyOpenSDK.getIntance().destroy((Context)mActivity);
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
            	mLoggedIn = false;
            	if (!isInited()) {
            		listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "inited fialed!");
		        } else if (!PluginHelper.networkReachable(mActivity)) {
		        	listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "Network not available!");
		        }
            	OpenSDK.login(mActivity,
            			mHuaweiAppId,
            			mHuaweiPayId,
            			mHuaweiAppKey, new UserInfo() {

							@Override
							public void dealUserInfo(
									HashMap<String, String> userInfo) {
								// 用户信息为空，登录失败
								if (null == userInfo)
								{
									logD("用户信息为空，登录失败");
									listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "user info is null, login failed");
								}
								// 使用华为账号登录且成功，进行accessToken验证
								else if ("1".equals((String)userInfo.get("loginStatus")))
								{
									logD("使用华为账号登录且成功");
									logD("UserID:" + (String)userInfo.get("userID"));
									String accessToken = (String)userInfo.get("accesstoken");
									requestCheckToken(listener, accessToken);
								}else if ("0".equals((String)userInfo.get("loginStatus"))){
									// 取消登录，点击返回以及登录界面右上角叉号的返回值
									listener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, " login cancel");
									
								}else if ("2".equals((String)userInfo.get("loginStatus"))){
									// 使用游戏账号登录，用户点击“使用已有游戏账号登录”的返回值
									listener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "");
								}else if ("3".equals((String)userInfo.get("loginStatus"))){
									// 登录过程中， SDK内部异常
									listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "login error");
								}
							}
            	}, false);
            }
    	});
    }
    
    private void requestCheckToken( final ILoginCallback listener, String token) {
    	try{
    		logD("start request check token");
    		OkHttpClient client = new OkHttpClient();
    		long a = Calendar.getInstance().getTimeInMillis();
    		RequestBody formBody = new FormBody.Builder().add("nsp_svc", "OpenUP.User.getInfo")
    				.add("nsp_ts", Long.toString(Calendar.getInstance().getTimeInMillis()))
    				.add("access_token", token)
    				.build();
    		logD("form body:" + formBody.toString());
			Request request = new Request.Builder().url(mCheckTokenUrl).post(formBody).build();
			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					logD("request check token failed");
					listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "request check token failed");
				}

				@Override
				public void onResponse(Call call, Response response)
						throws IOException {
					String responseBody = response.body().string();
					try {
						JSONObject jsonObj = new JSONObject(responseBody);
						logD(jsonObj.toString());
						int strUserState = jsonObj.getInt("userState");
						int strUserValidStatus = jsonObj.getInt("userValidStatus");
						String strUserID = jsonObj.getString("userID");
						if(strUserID == ""){
							listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "check token failed : userID is null");
						}
						else {
							mLoggedIn = true;
							mUid = strUserID;
							mAccessToken = "";
							BuoyOpenSDK.getIntance().showSmallWindow((Context)mActivity);
							listener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "Login Success");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}});
    	}catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void switchAccount() {
    	mUserAdapter.switchAccountBefore();
    }

    public String getAppID(){
    	return mHuaweiAppId;
    };
    
    public String getPayID(){
    	return mHuaweiPayId;
    };
    
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
