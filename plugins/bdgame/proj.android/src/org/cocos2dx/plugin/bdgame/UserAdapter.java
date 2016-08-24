package org.cocos2dx.plugin.bdgame;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.mobilesecuritysdk.constant.ConfigConstant;
import com.alipay.sdk.cons.GlobalDefine;
import com.alipay.sdk.cons.MiniDefine;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.wallet.core.beans.BeanConstants;
import com.duoku.platform.download.Downloads;

import android.app.Activity;
import android.content.Context;

public class UserAdapter implements InterfaceUser {
	private static final String LOG_TAG = "bdgame.UserAdapter";
    private Activity mActivity;
    private UserAdapter mAdapter;

    public UserAdapter(Context context) {
        mActivity = (Activity)context;
        mAdapter = this;
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		// TODO Auto-generated method stub
		
	}

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
		 PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
			    BDGameSDK.logout();
			    SDKWrapper.getInstance().setLoggedIn(false);
			    actionResult(UserWrapper.ACTION_RET_LOGOUT_SUCCESS, "logout success");
			}
		 });
	}
	
	public void showToolBar(int position) {
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                if (SDKWrapper.getInstance().isLoggedIn()) {
                    BDGameSDK.showFloatView(mActivity);
                } else {
                	logD("not login");
                }
            }
        });
    }

    public void hideToolBar() {
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                BDGameSDK.closeFloatView(mActivity);
            }
        });
    }

    public void getAnnouncementInfo() {
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                BDGameSDK.getAnnouncementInfo(mActivity);
            }
        });
    }
    
    public void exit() {
       logD("exit() invoked!");
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                BDGameSDK.gameExit(mActivity, new OnGameExitListener() {
                    public void onGameExit() {
                        actionResult(UserWrapper.ACTION_RET_EXIT_PAGE, "exit");
                    }
                });
            }
        });
    }

    public void antiAddictionQuery() {
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                BDGameSDK.queryLoginUserAuthenticateState(mActivity, new IResponse<Integer>() {
                    public void onResponse(int resultCode, String resultDesc, Integer extraData) {
                        try {
                            logD("resultCode=" + resultCode + ";resultDesc=" + resultDesc + ";extraData=" + extraData.intValue());
                            JSONObject resultJson = new JSONObject();
                            if (resultCode == 0) {
                                resultJson.put(Downloads.COLUMN_STATUS, "success");
                                resultJson.put(GlobalDefine.g, extraData.intValue() + ConfigConstant.WIRELESS_FILENAME);
                                resultJson.put(MiniDefine.c, resultDesc);
                            } else {
                                resultJson.put(Downloads.COLUMN_STATUS, "fail");
                                resultJson.put(GlobalDefine.g, BeanConstants.WALLET_PLUGIN_NON_UPDATE_FLAG);
                                resultJson.put(MiniDefine.c, resultDesc);
                            }
                            actionResult(UserWrapper.ACTION_RET_ANTIADDICTIONQUERY, resultJson.toString());
                        } catch (JSONException e) {
                            logE("antiAddictionQuery exception", e);
                        }
                    }
                });
            }
        });
    }

    public void actionResult(int code, String msg) {
        logD("actionResult code=" + code + " msg=" + msg);
        UserWrapper.onActionResult(mAdapter, code, msg);
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
