package org.cocos2dx.plugin.qh360;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONObject;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UserAdapter implements InterfaceUser {
	private static final String LOG_TAG = "QH360.UserAdapter";
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
		logD("logout() invoked!");
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					Intent intent = new Intent();
			        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGOUT);
					
					Matrix.execute(mActivity, intent, new IDispatcherCallback() {
						@Override
						public void onFinished(String data) {
							SDKWrapper.getInstance().setLoggedIn(false);
							actionResult(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
						}
					});
				} else {
					actionResult(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout fail!");
				}
			}
		});
	}
	
	public void setUserInfo(Hashtable<String, String> userinfo){
		logD("setUserInfo() invoked!");
		final Hashtable<String, String> info = userinfo;
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				
				String userId;
				if (info.get("userId") != null) {
					userId = (String) info.get("userId");
				} else {
					userId = "1";
				}
				SDKWrapper.getInstance().setUserId(userId);

				String userName;
				if (info.get("nickName") != null) {
					userName = (String) info.get("nickName");
				} else {
					userName = "nickName";
				}
				SDKWrapper.getInstance().setUserName(userName);
				
				SDKWrapper.getInstance().onGotUserInfo("enterServer");
			}
		});
	}

	public void exit() {
		logD("exit() invoked!");
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				Bundle bundle = new Bundle();
		        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);
		        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);
		        Intent intent = new Intent(mActivity, ContainerActivity.class);
		        intent.putExtras(bundle);

		        Matrix.invokeActivity(mActivity, intent, new IDispatcherCallback() {
		            @Override
		            public void onFinished(String data) {
		                JSONObject json;
		                try {
		                    json = new JSONObject(data);
		                    int which = json.optInt("which", -1);
		                    String label = json.optString("label");
		                    logD("exit     " + label);
		                    switch (which) {
		                        case 0: // 用户关闭退出界面
		                            break;
		                        case 1:
		                        	break;
		                        case 2:
		                        	SDKWrapper.getInstance().onGotUserInfo("exitServer");
		                        	actionResult(UserWrapper.ACTION_RET_EXIT_PAGE, "exit");
		                        	break;
		                        default:// 退出游戏
		                        	mActivity.finish();
		                        	break;
		                    }
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        });
			}
		});
	}
	
	public void getUserInfoByCP(String type) {
		logD("getUserInfoByCP()  invoked!");
		PluginWrapper.runOnGLThread(new Runnable() {
			public void run() {
				
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
	
//	public boolean canSwitchAccount() {
//		return false;
//	}
	
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
