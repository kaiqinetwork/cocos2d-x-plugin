/****************************************************************************
Copyright (c) 2012-2013 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.plugin.uc;

import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import android.app.Activity;
import android.content.Intent;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.AliLackActivityException;
import cn.uc.gamesdk.exception.AliNotInitException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCOrientation;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;

public class SDKWrapper {
	private static final String LOG_TAG = "UC.SDKWrapper";
	private static final String PLUGIN_NAME = "UC";
	private static final String PLUGIN_VERSION = "1.0.0";
	private static final String SDK_VERSION = "6.1.0";
	private static SDKWrapper mInstance;
	private String mUCAppId;
	private String mUCCpId;
	private Activity mActivity;
	private boolean mInited;
	private String mUid;
	private String mUname;
	private String mAccessToken;
	private boolean mLoggedIn;
	private ILoginCallback mInitCallback;
	private ILoginCallback mLoginCallback;
	private ILoginCallback mLogoutCallback;
	private ILoginCallback mExitCallback;
	private ILoginCallback mPayCallback;

	private SDKEventReceiver eventReceiver = new SDKEventReceiver() {

		@Subscribe(event = SDKEventKey.ON_INIT_SUCC)
		private void onInitSucc() {
			mInitCallback.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "initsdk success");
			mInited = true;
		}

		@Subscribe(event = SDKEventKey.ON_INIT_FAILED)
		private void onInitFailed(String desc) {
			mInitCallback.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "initsdk fail");
			mInited = false;
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
		private void onLoginSucc(String sid) {
			mAccessToken = sid;
			mLoggedIn = true;
			mLoginCallback.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
		private void onLoginFailed(String desc) {
			mLoginCallback.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "login fail");
			mLoggedIn = false;
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
		private void onLogoutSucc() {
			mLogoutCallback.onSuccessed(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
			mLoggedIn = false;
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
		private void onLogoutFailed() {
			mLogoutCallback.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout fail");
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
		private void onExitSucc() {
			mExitCallback.onSuccessed(UserWrapper.ACTION_RET_EXIT_PAGE, "sdk exit success");
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
		private void onExitCanceled() {
			logD("SDK exit cancel ");
		}

		@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
		private void onCreateOrderSucc(OrderInfo orderInfo) {
			logD("pay onCreateOrderSucc");
		}

		@Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
		private void onPayUserExit(OrderInfo orderInfo) {
			mPayCallback.onSuccessed(IAPWrapper.PAYRESULT_SUCCESS, "\u652f\u4ed8\u9000\u51fa");
		}
	};

	public SDKWrapper() {
		this.mActivity = null;
		this.mUCAppId = null;
		this.mUCCpId = null;
		this.mUid = null;
		this.mUname = null;
		this.mAccessToken = null;
		this.mLoggedIn = false;
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
		mInitCallback = listener;
		if ((devInfo.get("UCAppID") != null) && (devInfo.get("UCCpID") != null)) {
			mUCAppId = devInfo.get("UCAppID");
			mUCCpId = devInfo.get("UCCpID");
		} else {
			mInitCallback.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "devInfo is null");
		}

		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				UCGameSdk.defaultSdk().registeSDKEventReceiver(eventReceiver);
				GameParamInfo gpi = new GameParamInfo();
				gpi.setCpId(Integer.parseInt(mUCCpId));
				gpi.setServerId(0);
				gpi.setGameId(Integer.parseInt(mUCAppId));
				gpi.setEnablePayHistory(true);
				gpi.setEnableUserChange(true);
				gpi.setOrientation(UCOrientation.LANDSCAPE);
				SDKParams sdkParams = new SDKParams();
				sdkParams.put(SDKParamKey.DEBUG_MODE, false);
				sdkParams.put(SDKParamKey.GAME_PARAMS, gpi);

				try {
					UCGameSdk.defaultSdk().initSdk(mActivity, sdkParams);
				} catch (AliLackActivityException e) {
					mInitCallback.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "initsdk fail");
					e.printStackTrace();
				}
			}
		});

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
			}

			public void onRestart() {

			}

			public void onPause() {
				logD("onPause");
			}

			public void onNewIntent(Intent intent) {

			}

			public void onDestroy() {
				logD("onDestroy");
				UCGameSdk.defaultSdk().unregisterSDKEventReceiver(eventReceiver);
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
				mLoginCallback = listener;
				try {
					UCGameSdk.defaultSdk().login(mActivity, null);
				} catch (AliLackActivityException e) {
					mLoginCallback.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "login fail");
					e.printStackTrace();
				} catch (AliNotInitException e) {
					mLoginCallback.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "login fail");
					e.printStackTrace();
				}
			}
		});
	}

	public void userLogout(final Activity act, final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				logD("login invoked!");
				mLogoutCallback = listener;
				try {
					UCGameSdk.defaultSdk().logout(mActivity, null);
				} catch (AliLackActivityException e) {
					mLogoutCallback.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout fail");
					e.printStackTrace();
				} catch (AliNotInitException e) {
					mLogoutCallback.onFailed(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout fail");
					e.printStackTrace();
				}
			}
		});
	}

	public void onGotUserInfo() {
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				logD("onGotUserInfo invoked!  mUid  " + mUid + "    mUname  " + mUname);
				SDKParams params = new SDKParams();
				long roleLevel = 1;
				params.put(SDKParamKey.STRING_ROLE_ID, mUid);
				params.put(SDKParamKey.STRING_ROLE_NAME, mUname);
				params.put(SDKParamKey.LONG_ROLE_LEVEL, roleLevel);
				params.put(SDKParamKey.LONG_ROLE_CTIME, Long.parseLong(mUid,10));
				params.put(SDKParamKey.STRING_ZONE_ID, "1");
				params.put(SDKParamKey.STRING_ZONE_NAME, "1");

				try {
					UCGameSdk.defaultSdk().submitRoleData(mActivity, params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (AliNotInitException e) {
					e.printStackTrace();
				} catch (AliLackActivityException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void exit(final Activity act, final ILoginCallback listener) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				logD("exit invoked!");
				mExitCallback = listener;
				try {
					UCGameSdk.defaultSdk().exit(mActivity, null);
				} catch (AliLackActivityException e) {
					e.printStackTrace();
				} catch (AliNotInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void pay(final Activity act, final ILoginCallback listener, final SDKParams params) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				logD("pay invoked!");
				mPayCallback = listener;
				try {
					UCGameSdk.defaultSdk().pay(mActivity, params);
				} catch (IllegalArgumentException e) {
					mPayCallback.onFailed(IAPWrapper.PAYRESULT_FAIL, " payresult IllegalArgumentException");
					e.printStackTrace();
				} catch (AliLackActivityException e) {
					mPayCallback.onFailed(IAPWrapper.PAYRESULT_FAIL, " payresult AliLackActivityException");
					e.printStackTrace();
				} catch (AliNotInitException e) {
					mPayCallback.onFailed(IAPWrapper.PAYRESULT_FAIL, " payresult AliNotInitException");
					e.printStackTrace();
				}
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
		PluginHelper.logD(LOG_TAG, msg);
	}

	protected void logI(String msg) {
		PluginHelper.logI(LOG_TAG, msg);
	}
}
