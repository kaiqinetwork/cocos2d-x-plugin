package org.cocos2dx.plugin.txysdk;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.ysdk.api.YSDKApi;

import android.app.Activity;
import android.content.Context;

public class UserAdapter implements InterfaceUser {
	private static final String LOG_TAG = "txysdk.UserAdapter";
    private Activity mActivity;
    private UserAdapter mInstance;

    public UserAdapter(Context context) {
        mActivity = (Activity)context;
        mInstance = this;
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo(" + devInfo.toString() + ")invoked!");
        if (!SDKWrapper.getInstance().initSDK(mActivity, devInfo, mInstance, new ILoginCallback() {
            public void onFailed(int code, String msg) {
                actionResult(UserWrapper.ACTION_RET_INIT_FAIL, msg);
            }

            public void onSuccessed(int code, String msg) {
                actionResult(UserWrapper.ACTION_RET_INIT_SUCCESS, msg);
            }
        })) {
            actionResult(UserWrapper.ACTION_RET_INIT_FAIL, "initSDK false");
        }
	}

	public void login() {
	    PluginWrapper.runOnMainThread(new Runnable() {
	        public void run() {
	            if (SDKWrapper.getInstance().isInited()) {
	            	logD("login() invoked!");
	            	SDKWrapper.getInstance().userLogin(new ILoginCallback() {
	                    public void onSuccessed(int code, String msg) {
	                    	actionResult(UserWrapper.ACTION_RET_LOGIN_SUCCESS, msg);
	                    	SDKWrapper.getInstance().setLoggedIn(true);
	                    }
	
	                    public void onFailed(int code, String msg) {
	                    	actionResult(code, msg);
	                    	SDKWrapper.getInstance().setLoggedIn(false);
	                    }
	                });
	            } else {
	            	actionResult(UserWrapper.ACTION_RET_LOGIN_FAIL, "initSDK fail!");
	            	SDKWrapper.getInstance().setLoggedIn(false);
	            }
	        }
	    });
	}

	@Override
	public void logout() {
		logD("logout() invoked!");
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                YSDKApi.logout();
                SDKWrapper.getInstance().setLoggedIn(false);
                actionResult(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
            }
        });

	}
	
	public void actionResult(int code, String msg) {
        logD("actionResult code=" + code + " msg=" + msg);
        UserWrapper.onActionResult(mInstance, code, msg);
    }

	@Override
	public boolean isLoggedIn() {
		return SDKWrapper.getInstance().isLoggedIn();
	}

	@Override
	public String getUserId() {
		return SDKWrapper.getInstance().getUserId();
	}
	
	@Override
	public String getAccessToken() {
		return SDKWrapper.getInstance().getAccessToken();
	}

	@Override
	public void setDebugMode(boolean debug) {
		logD("setDebugMode(" + debug + ") invoked! it is not used.");
	}

	@Override
	public String getSDKVersion() {
		return SDKWrapper.getInstance().getSDKVersion();
	}

	@Override
	public String getPluginVersion() {
		return SDKWrapper.getInstance().getPluginVersion();
	}

	@Override
	public String getPluginName() {
		return SDKWrapper.getInstance().getPluginName();
	}
	
	public String getPlatform(){
	return SDKWrapper.getInstance().getPlatform();    	
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

	@Override
	public boolean isSupportFunction(String funcName) {
		Method[] methods = UserAdapter.class.getMethods();
        for (Method name : methods) {
            if (name.getName().equals(funcName)) {
                return true;
            }
        }
        return false;
	}
}
