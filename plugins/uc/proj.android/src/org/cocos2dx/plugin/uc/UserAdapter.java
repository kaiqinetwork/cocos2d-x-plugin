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

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import android.app.Activity;
import android.content.Context;

public class UserAdapter implements InterfaceUser {
	private static final String LOG_TAG = "UC.UserAdapter";
	private Activity mActivity;
	private UserAdapter mInstance;

	public UserAdapter(Context context) {
		mActivity = (Activity) context;
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
		logD("login() invoked!");
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					SDKWrapper.getInstance().userLogin(mActivity, new ILoginCallback() {
						public void onSuccessed(int code, String msg) {
							actionResult(code, msg);
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
		logD("logout() invoked!");
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					SDKWrapper.getInstance().userLogout(mActivity, new ILoginCallback() {
						public void onSuccessed(int code, String msg) {
							actionResult(code, msg);
						}

						public void onFailed(int code, String msg) {
							actionResult(code, msg);
						}
					});
				} else {
					actionResult(UserWrapper.ACTION_RET_LOGOUT_FAIL, "logout fail!");
				}
			}
		});
	}

	public void setUserInfo(Hashtable<String, String> userinfo) {
		logD("setUserInfo() invoked!");
		final Hashtable<String, String> info = userinfo;
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					String userId;
					if (info.get("userId") != null) {
						userId = (String) info.get("userId");
					} else {
						userId = "1";
					}
					String userName;
					if (info.get("nickName") != null) {
						userName = (String) info.get("nickName");
					} else {
						userName = "nickName";
					}
					SDKWrapper.getInstance().setUserId(userId);
					SDKWrapper.getInstance().setUserName(userName);
					SDKWrapper.getInstance().onGotUserInfo();
				}
			}
		});
	}

	public void exit() {
		logD("exit() invoked!");
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					SDKWrapper.getInstance().exit(mActivity, new ILoginCallback() {

						@Override
						public void onSuccessed(int code, String msg) {
							actionResult(UserWrapper.ACTION_RET_EXIT_PAGE, msg);
						}

						@Override
						public void onFailed(int code, String msg) {

						}
					});
				}
			}
		});
	}

	public void actionResult(int code, String msg) {
		logD("actionResult code=" + code + " msg=" + msg);
		UserWrapper.onActionResult(mInstance, code, "");
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

	public boolean canSwitchAccount() {
		return false;
	}

	public boolean canKillAppProcess() {
		return true;
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
