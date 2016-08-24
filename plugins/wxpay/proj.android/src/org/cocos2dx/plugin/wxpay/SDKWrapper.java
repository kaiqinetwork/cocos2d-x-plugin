package org.cocos2dx.plugin.wxpay;

import java.util.Hashtable;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import com.tencent.mm.sdk.modelmsg.SendAuth.Req;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;

public class SDKWrapper {
	private static final String CHANNEL = "wxpay";
    private static final String LOG_TAG = "wxpay.SDKWrapper";
    private static final String PLUGIN_NAME = "Wxpay";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "3.1.1";
    private static SDKWrapper mInstance;
    private static String mAppId;
    private IWXAPI mApi;
    private boolean mInited;
    private boolean mLoggedIn;
    private ILoginCallback mLoginCallback;
    private Context mContext;
    private boolean mDebug;
    private InterfaceIAP mIAPAdapter;
    private InterfaceUser mUserAdapter;
    private String mUid;
    private String mAccessToken;

    public SDKWrapper() {
        mUserAdapter = null;
        mDebug = false;
        mIAPAdapter = null;
        mContext = null;
        mLoggedIn = false;
        mInited = false;
        mUid = "";
        mAccessToken = "";
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

    public boolean initSDK(Context context, final Hashtable<String, String> devInfo, Object adapter, final ILoginCallback callback) {
        if (adapter instanceof InterfaceUser) {
            mUserAdapter = (InterfaceUser) adapter;
        } else if (adapter instanceof InterfaceIAP) {
            mIAPAdapter = (InterfaceIAP) adapter;
        }
        if (mInited) {
            return mInited;
        }
        mInited = true;
        mContext = context;
        PluginWrapper.runOnMainThread(new Runnable() {
	        public void run() {
	        	mAppId = (String)devInfo.get("WXAppId");
	        	logD("appID=" + mAppId);
	        	mApi = WXAPIFactory.createWXAPI(mContext, mAppId, true);
	        	mApi.registerApp(mAppId);
	            mInited = true;
	            callback.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "init success");

	        }
        });
        return mInited;
    }
    
    public void userLogin(final Activity act, final ILoginCallback callback) {
    	PluginWrapper.runOnMainThread(new Runnable() {
    		public void run() {
				mLoginCallback = callback;
				if (!mApi.isWXAppInstalled()) {
					callback.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "wx app is not installed");
				} else if (mApi.isWXAppSupportAPI()) {
				    Req req = new Req();
				    req.scope = "snsapi_userinfo";
				    req.state = "wechat_sdk_demo";
				    mApi.sendReq(req);
				} else {
					callback.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "isWXAppSupportAPI=false");
				}
    		}
        });
    }
    
    public void loginResult(int result, String code) {
        if (result == UserWrapper.ACTION_RET_LOGIN_SUCCESS) {
        	mAccessToken = code;
        	mLoginCallback.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
            logD("login success");
        } else {
            mLoggedIn = false;
            mLoginCallback.onFailed(result, "login fail");
        }
    }

    public boolean isInited() {
        return mInited;
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
    
    public void setAppId(String appId) {
        mAppId = appId;
    }

    public String getAppId() {
        return mAppId;
    }

    public IWXAPI getApi() {
        return mApi;
    }

    public void setApi(IWXAPI api) {
    	mApi = api;
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
