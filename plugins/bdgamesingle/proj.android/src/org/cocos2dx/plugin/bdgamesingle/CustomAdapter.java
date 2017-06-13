package org.cocos2dx.plugin.bdgamesingle;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.CustomWrapper;
import org.cocos2dx.plugin.InterfaceCustom;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.callback.IDKSDKCallBack;

import android.app.Activity;
import android.content.Context;

public class CustomAdapter implements InterfaceCustom {

	private static final String LOG_TAG = "bdsingle.CustomAdapter";
	private Activity mActivity;
	private CustomAdapter mInstance;

	public CustomAdapter(Context context) {
		mActivity = (Activity) context;
		mInstance = this;
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo invoked " + devInfo.toString());
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

	public void exit() {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				DKPlatform.getInstance().bdgameExit(mActivity, new IDKSDKCallBack() {
					public void onResponse(String paramString) {
						actionResult("exit", "");
					}
				});
			}
		});
	}

	protected void logD(String msg) {
		try {
			PluginHelper.logD(LOG_TAG, msg);
		} catch (Exception e) {
			logE("logD error", e);
		}
	}

	protected void logE(String msg, Exception e) {
		if (e == null) {
			PluginHelper.logE(LOG_TAG, msg);
		} else {
			PluginHelper.logE(LOG_TAG, msg, e);
		}
	}

	@Override
	public boolean isSupportFunction(String funcName) {
		logD("Support Function" + funcName);
		Method[] methods = CustomAdapter.class.getMethods();
		for (Method name : methods) {
			if (name.getName().equals(funcName)) {
				return true;
			}
		}
		return false;
	}

	public void actionResult(String strRet, String msg) {
		logD("actionResult strRet=" + strRet + " msg=" + msg);
		CustomWrapper.onActionResult(mInstance, strRet, msg);
	}
}
