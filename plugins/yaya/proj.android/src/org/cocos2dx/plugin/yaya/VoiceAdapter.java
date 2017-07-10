package org.cocos2dx.plugin.yaya;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceVoice;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.VoiceWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

public class VoiceAdapter implements InterfaceVoice {
	private static final String LOG_TAG = "YaYa.VoiceAdapter";
    private Activity mActivity;
    private VoiceAdapter mInstance;

    public VoiceAdapter(Context context) {
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
                        actionResult(VoiceWrapper.ACTION_RET_INIT_SUCCESS, msg);
                    }

                    public void onFailed(int code, String msg) {
                        actionResult(VoiceWrapper.ACTION_RET_INIT_FAIL, msg);
                    }
                })) {
                    actionResult(VoiceWrapper.ACTION_RET_INIT_FAIL, "SDKWrapper.getInstance().initSDK return false");
                }
            }
        });
	}
	
	@Override
	public void startRecord() {
		logD("Yaya Record started ");
		SDKWrapper.getInstance().startRecord();
	}
    
	@Override
	public void stopRecord() {
		logD("Yaya Record  stoped");
		SDKWrapper.getInstance().stopRecord();
	}
	
	@Override
	public void cancelRecord() {
		logD("Yaya Record  stoped");
		SDKWrapper.getInstance().cancelRecord();
	}
	
    public void exit() {
    	logD("Yaya plugin exit ");
		SDKWrapper.getInstance().exit();
    }
    
    @Override
	public void setUserInfo(Hashtable<String, String> userInfo) {
		SDKWrapper.getInstance().setUserInfo(userInfo);
	}
    
    @Override
	public void playVoice(String Url) {
    	SDKWrapper.getInstance().playVoice(Url);
	}

    @Override
	public void login() {
		logD("login Voice plugin");
		SDKWrapper.getInstance().login();
	}
   
    public void actionResult(int code, String msg) {
        logD("actionResult code=" + code + " msg=" + msg);
        VoiceWrapper.onActionResult(mInstance, code, msg);
    }

    @Override
	public String getVoiceUrl() {
		return SDKWrapper.getInstance().getVoiceUrl();
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

	@Override
	public boolean isSupportFunction(String funcName) {
		Method[] methods = VoiceAdapter.class.getMethods();
        for (Method name : methods) {
            if (name.getName().equals(funcName)) {
                return true;
            }
        }
        return false;
	}

}
