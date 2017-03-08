package org.cocos2dx.plugin.meizu;

import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;

import android.app.Activity;
import android.content.Intent;

public class SDKWrapper {
	private static final String LOG_TAG = "meizu.SDKWrapper";
	private static final String PLUGIN_NAME = "meizu";
	private static final String PLUGIN_VERSION = "1.0.0";
	private static final String SDK_VERSION = "3.0.1.";
	private static SDKWrapper mInstance;
	private String mMeizuAppId;
	private String mMeizuAppKey;
	private MzGameBarPlatform mMzGameBarPlatform;
	private Activity mActivity;
	private boolean mDebug;
	private boolean mInited;

	private String mUid = null;
	private String mSession = null;
	private boolean mLoggedIn = false;
	
	public SDKWrapper() {
		this.mActivity = null;
		this.mDebug = true;
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

		if (mInited) {
			return true;
		}
		mActivity = act;
		mDebug = PluginHelper.getDebugMode();
		if ((devInfo.get("MeizuAppID") != null) && (devInfo.get("MeizuAppKey") != null)) {
			mMeizuAppId = devInfo.get("MeizuAppID");
			mMeizuAppKey = devInfo.get("MeizuAppKey");
		} else {
			listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "devInfo is null");
		}

		MzGameCenterPlatform.init(mActivity, mMeizuAppId, mMeizuAppKey);
		mMzGameBarPlatform = new MzGameBarPlatform(mActivity, MzGameBarPlatform.GRAVITY_LEFT_TOP);
		mMzGameBarPlatform.onActivityCreate();
		listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "initsdk success");
		mInited = true;
		setPluginListener();
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
				mMzGameBarPlatform.onActivityResume();
			}

			public void onRestart() {

			}

			public void onPause() {
				logD("onPause");
				mMzGameBarPlatform.onActivityPause();
			}

			public void onNewIntent(Intent intent) {

			}

			public void onDestroy() {
				logD("onDestroy");
				mMzGameBarPlatform.onActivityDestroy();
				MzGameCenterPlatform.logout(mActivity);
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
				logD("login invoked!");
				MzGameCenterPlatform.login(mActivity, new MzLoginListener() {
					@Override
					public void onLoginResult(int code, MzAccountInfo accountInfo, String errorMsg) {
						switch (code) {
						case LoginResultCode.LOGIN_SUCCESS:
							mUid = accountInfo.getUid();
							mSession = accountInfo.getSession();
							mLoggedIn = true;
							listener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "登陆成功");
							break;
						case LoginResultCode.LOGIN_LOGOUT:
							mLoggedIn = false;
							listener.onFailed(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "登出成功");
							break;
						case LoginResultCode.LOGIN_ERROR_CANCEL:
							mLoggedIn = false;
							listener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "登陆取消");
							break;
						default:
							mLoggedIn = false;
							listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "登陆失败");
							break;
						}

					}
				});
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
		return mSession;
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
	
	public String getPluginAppId() {
		return mMeizuAppId;
	}

	protected void logE(String msg, Exception e) {
		if (e == null && mDebug) {
			PluginHelper.logE(LOG_TAG, msg);
		} else {
			PluginHelper.logE(LOG_TAG, msg, e);
		}
	}

	protected void logD(String msg) {
		if (mDebug) {
			PluginHelper.logD(LOG_TAG, msg);
		}

	}

	protected void logI(String msg) {
		if (mDebug) {
			PluginHelper.logI(LOG_TAG, msg);
		}

	}
}
