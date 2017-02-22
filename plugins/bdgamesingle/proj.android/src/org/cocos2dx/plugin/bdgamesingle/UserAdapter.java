package org.cocos2dx.plugin.bdgamesingle;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.callback.IDKSDKCallBack;

import android.app.Activity;
import android.content.Context;

public class UserAdapter implements InterfaceUser {
	private static final String LOG_TAG = "bdgamesingle.UserAdapter";
    private Activity mActivity;
    private UserAdapter mInstance;

    public UserAdapter(Context context) {
        mActivity = (Activity)context;
        mInstance = this;
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo invoked " + devInfo.toString());
		final Hashtable<String, String> curDevInfo = devInfo;
		PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	if (!SDKWrapper.getInstance().initSDK(mActivity, curDevInfo, mInstance, new ILoginCallback() {
                    public void onSuccessed(int code, String msg) {
                        actionResult(UserWrapper.ACTION_RET_INIT_SUCCESS, msg);
                    }

                    public void onFailed(int code, String msg) {
                        actionResult(UserWrapper.ACTION_RET_INIT_FAIL, msg);
                    }
                })) {
                    actionResult(UserWrapper.ACTION_RET_INIT_FAIL, "SDKWrapper.getInstance().initSDK return false");
                }
            }
        });
	}
	
	@Override
	public void login() {
	    PluginWrapper.runOnMainThread(new Runnable() {
	        public void run() {
	            if (SDKWrapper.getInstance().isInited()) {
	            	SDKWrapper.getInstance().userLogin(mActivity, new ILoginCallback() {
	                    public void onSuccessed(int code, String msg) {
	                    	actionResult(UserWrapper.ACTION_RET_LOGIN_SUCCESS, msg);
	                    }
	
	                    public void onFailed(int code, String msg) {
	                    	actionResult(code, msg);
	                    }
	                });
	            } else {
	            	actionResult(UserWrapper.ACTION_RET_LOGIN_FAIL, "initSDK fail!");
	            }
	        }
	    });
	}

	@Override
	public void logout() {
	}
	
	public void exit() {
    	logD("exit() invoked!");
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	DKPlatform.getInstance().bdgameExit(mActivity, new IDKSDKCallBack() {
                    public void onResponse(String paramString) {
                        actionResult(UserWrapper.ACTION_RET_EXIT_PAGE, "onResponse");
                    }
                });
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
    
    public boolean canSwitchAccount() {
		return false;
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
