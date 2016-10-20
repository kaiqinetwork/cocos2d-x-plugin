package org.cocos2dx.plugin.txysdk;

import java.util.Hashtable;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import com.tencent.connect.common.Constants;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

public class SDKWrapper {
	private static final String CHANNEL = "TXYSDK";
    private static final String LOG_TAG = "txysdk.SDKWrapper";
    private static final String PLUGIN_NAME = "TXYSDK";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "1.2.2";
    private static SDKWrapper mInstance;
    private ILoginCallback mLoginCallback;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private boolean mLoggedIn;
    private String mUid;
    private String mAccessToken;
    private boolean mMultizone;
    private boolean mChangeMoney;
    private int mRate;
    private boolean mShowToast;

    public SDKWrapper() {
    	mActivity = null;
    	mDebug = false;
    	mUserAdapter = null;
    	mIAPAdapter = null;
    	mUid = Constants.STR_EMPTY;
    	mChangeMoney = false;
        mMultizone = false;
        mRate = 1;
        mShowToast = false;
        
        this.mLoginCallback = new ILoginCallback() {
            public void onFailed(int code, String msg) {
                if (mUserAdapter != null) {
                    mUserAdapter.actionResult(code, msg);
                } else {
                    logD("mUserAdapter is null.ILoginCallback onFailed call actionResult fail.");
                }
            }

            public void onSuccessed(int code, String msg) {
                if (mUserAdapter != null) {
                    mUserAdapter.actionResult(code, msg);
                } else {
                    logD("mUserAdapter is null.ILoginCallback onSuccessed call actionResult fail.");
                }
            }
        };

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
    
    public boolean initSDK(Activity act, Hashtable<String, String> devInfo, Object adapter, final ILoginCallback listener) {
        if (adapter instanceof InterfaceUser) {
            mUserAdapter = (UserAdapter) adapter;
        } else if (adapter instanceof InterfaceIAP) {
            mIAPAdapter = (IAPAdapter) adapter;
        }
        if (mInited) {
            return true;
        }
        mActivity = act;
        mInited = true;
        mDebug = Boolean.parseBoolean((String) devInfo.get("TXYSDKDebug"));
        //tokenInvalidLogin = Boolean.parseBoolean((String) cpInfo.get("TXYSDKTokenInvalidLogin"));
        setMultizone(Boolean.parseBoolean((String) devInfo.get("TXYSDKMultizone")));
        setShowToast(Boolean.parseBoolean((String) devInfo.get("TXYSDKShowToast")));
        setChangeMoney(Boolean.parseBoolean((String) devInfo.get("TXYSDKChangeMoney")));
        String strRate = (String) devInfo.get("TXYSDKRate");
        if (!(strRate == null || strRate.isEmpty())) {
            setRate(Integer.parseInt(strRate));
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            init(listener);
        } else {
            PluginWrapper.runOnMainThread(new Runnable() {
    	        public void run() {
    	        	init(listener);
    	            YSDKApi.onResume(mActivity);
    	        }
    	    });
        }

        return mInited;
    }
    
    public void init(ILoginCallback listener) {
        setPluginListener();
        YSDKApi.onCreate(mActivity);
        setUserListener();
        setBuglyListener();
        YSDKApi.handleIntent(mActivity.getIntent());
        listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "init success");
    }

    public void userLogin(ILoginCallback listener) {
        mLoginCallback = listener;
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                showLoginView();
            }
        });
    }
    
    public void setUserListener() {
        YSDKApi.setUserListener(new UserListener() {
            public void OnWakeupNotify(WakeupRet ret) {
                logD("WakeupRet" + ret);
            }

            public void OnRelationNotify(UserRelationRet ret) {
                logD("UserRelationRet" + ret);
            }

            public void OnLoginNotify(com.tencent.ysdk.module.user.UserLoginRet ret) {
            	
            }
        });
    }


    public void showMsg(int flag) {
        switch (flag) {
            case eFlag.QQ_UserCancel /*1001*/:
                showToastTips("plugin_msg_QQ_UserCancel");
                break;
            case eFlag.QQ_LoginFail /*1002*/:
                showToastTips("plugin_msg_QQ_LoginFail");
                break;
            case eFlag.QQ_NetworkErr /*1003*/:
                showToastTips("plugin_msg_QQ_NetworkErr");
                break;
            case eFlag.QQ_NotInstall /*1004*/:
                showToastTips("plugin_msg_QQ_NotInstall");
                break;
            case eFlag.QQ_NotSupportApi /*1005*/:
                showToastTips("plugin_msg_QQ_NotSupportApi");
                break;
            case eFlag.WX_NotInstall /*2000*/:
                showToastTips("plugin_msg_WX_NotInstall");
                break;
            case eFlag.WX_NotSupportApi /*2001*/:
                showToastTips("plugin_msg_WX_NotSupportApi");
                break;
            case eFlag.WX_UserCancel /*2002*/:
                showToastTips("plugin_msg_WX_UserCancel");
                break;
            case eFlag.WX_UserDeny /*2003*/:
                showToastTips("plugin_msg_WX_UserDeny");
                break;
            case eFlag.WX_LoginFail /*2004*/:
                showToastTips("plugin_msg_WX_LoginFail");
                break;
            case eFlag.Login_TokenInvalid /*3100*/:
                showToastTips("plugin_msg_Login_TokenInvalid");
                break;
            case eFlag.Login_NotRegisterRealName /*3101*/:
                showToastTips("plugin_msg_Login_NotRegisterRealName");
                break;
            default:
            	break;
        }
    }

    public void showToastTips(String tipsName) {
        logD("show toast tips by " + tipsName);
        try {
            int resId = mActivity.getResources().getIdentifier(tipsName, "string", mActivity.getPackageName());
            if (resId > 0) {
                final String tips = mActivity.getResources().getString(resId);
                if (!tips.equals(Constants.STR_EMPTY)) {
                    PluginWrapper.runOnMainThread(new Runnable() {
                    	public void run() {
                    		Toast.makeText(mActivity, tips, 1).show();
                    	}
                    });
                }
            }
        } catch (Exception e) {
            logE("showToastTips error", e);
        }
    }
    
    public void setBuglyListener() {
        YSDKApi.setBuglyListener(new BuglyListener() {
            public String OnCrashExtMessageNotify() {
                return null;
            }

            public byte[] OnCrashExtDataNotify() {
                return null;
            }
        });
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

    public String getSDKVersion() {
        return SDK_VERSION;
    }

    public String getPluginVersion() {
        return PLUGIN_VERSION;
    }

    public String getPluginName() {
        return PLUGIN_NAME;
    }
    
    public String getChannel() {
        return CHANNEL;
    }
    
    protected void setPluginListener() {
        PluginWrapper.addListener(new PluginListener() {
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                YSDKApi.onActivityResult(requestCode, resultCode, data);
            }

            public void onDestroy() {
                YSDKApi.onDestroy(mActivity);
            }

            public void onNewIntent(Intent intent) {
                YSDKApi.handleIntent(intent);
            }

            public void onPause() {
                YSDKApi.onPause(mActivity);
            }

            public void onRestart() {
                YSDKApi.onRestart(mActivity);
            }

            public void onResume() {
                YSDKApi.onResume(mActivity);
            }

            public void onStop() {
                YSDKApi.onStop(mActivity);
            }
        });
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
    
    private void showLoginView() {
        Intent intent = new Intent();
        intent.setClass(mActivity, LoginChangeActivity.class);
        mActivity.startActivity(intent);
    }

    public boolean isChangeMoney() {
        return mChangeMoney;
    }

    public void setChangeMoney(boolean changeMoney) {
    	mChangeMoney = changeMoney;
    }

    public int getRate() {
        return mRate;
    }

    public void setRate(int rate) {
        mRate = rate;
    }
    
    public boolean isMultizone() {
        return mMultizone;
    }

    public void setMultizone(boolean multizone) {
        mMultizone = multizone;
    }

    public boolean isShowToast() {
        return mShowToast;
    }

    public void setShowToast(boolean showToast) {
        mShowToast = showToast;
    }

}
