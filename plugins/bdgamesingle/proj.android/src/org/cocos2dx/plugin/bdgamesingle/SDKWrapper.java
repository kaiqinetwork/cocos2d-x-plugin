package org.cocos2dx.plugin.bdgamesingle;

import java.util.Hashtable;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.json.JSONObject;

import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.DKPlatformSettings.SdkMode;
import com.duoku.platform.single.DkErrorCode;
import com.duoku.platform.single.DkProtocolKeys;
import com.duoku.platform.single.callback.IDKSDKCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SDKWrapper {
	private static final String LOG_TAG = "bdgamesingle.SDKWrapper";
    private static final String PLUGIN_NAME = "BDGameSingle";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "2.0.7";
    private static SDKWrapper mInstance;
    private String mBaiduGameAppId;
    private String mBaiduGameAppKey;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private boolean mLoggedIn;
    private String mUid;
    private String mAccessToken;
    private String mIAPDebugCallbackUrl;

    public SDKWrapper() {
        this.mActivity = null;
        this.mDebug = false;
        this.mUserAdapter = null;
        this.mIAPAdapter = null;
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
        mDebug = PluginHelper.getDebugMode();
        setPluginListener();
        
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	DKPlatform.getInstance().init(mActivity, PluginHelper.getAppOrientation().equals("landscape"), SdkMode.SDK_PAY, null, null, new IDKSDKCallBack() {
                    public void onResponse(String paramString) {
                        logD("init onResponse: " + paramString);
                        try {
                            if (new JSONObject(paramString).getInt(DkProtocolKeys.FUNCTION_CODE) == DkErrorCode.BDG_CROSSRECOMMEND_INIT_FINSIH) {
                            	mInited = true;
                                listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, paramString);
                                return;
                            }
                            mInited = false;
                            listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, paramString);
                        } catch (Exception e) {
                            logE("init onResponse error", e);
                            mInited = false;
                            listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, paramString);
                        }
                    }
                });
                DKPlatform.getInstance().bdgameInit(mActivity, new IDKSDKCallBack() {
                    public void onResponse(String paramString) {
                        PluginHelper.logI(LOG_TAG, "DKPlatform.getInstance().bdgameInit onResponse");
                    }
                });
            }
        });
        
        return mInited;
    }

    private void setPluginListener() {
        logD("setPluginListener");
        PluginWrapper.addListener(new PluginListener() {
        	public void onStop() {
            }

            public void onResume() {
                logD("onResume");
                DKPlatform.getInstance().resumeBaiduMobileStatistic((Context)mActivity);
            }

            public void onRestart() {
            }

            public void onPause() {
                logD("onPause");
                DKPlatform.getInstance().pauseBaiduMobileStatistic((Context)mActivity);
            }

            public void onNewIntent(Intent intent) {
            }

            public void onDestroy() {
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
            	DKPlatform.getInstance().invokeBDInit(mActivity, new IDKSDKCallBack() {
                    public void onResponse(String paramString) {
                        try {
                            JSONObject jsonObject = new JSONObject(paramString);
                            int functionCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_CODE);
                            String bduid = jsonObject.getString(DkProtocolKeys.BD_UID);
                            if (functionCode == DkErrorCode.DK_ACCOUNT_LOGIN_SUCCESS) {
                                mUid = bduid;
                            } else if (functionCode == DkErrorCode.DK_ACCOUNT_LOGIN_FAIL) {
                                login(listener);
                            } else if (functionCode != DkErrorCode.DK_ACCOUNT_QUICK_REG_SUCCESS) {
                            } else {
                                login(listener);
                            }
                        } catch (Exception e) {
                            logE("invokeBDInit onResponse error", e);
                            listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "invokeBDInit onResponse error");
                        }
                    }
                });

            }
    	});
    }
    
    private void login(final ILoginCallback listener) {
    	PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	 DKPlatform.getInstance().invokeBDLogin(mActivity, new IDKSDKCallBack() {
                     public void onResponse(String paramString) {
                         try {
                             JSONObject jsonObject = new JSONObject(paramString);
                             int functionCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_CODE);
                             String bduid = jsonObject.getString(DkProtocolKeys.BD_UID);
                             if (functionCode == DkErrorCode.DK_ACCOUNT_LOGIN_SUCCESS) {
                                 mUid = bduid;
                             } else if (functionCode == DkErrorCode.DK_ACCOUNT_LOGIN_FAIL) {
                                 listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, paramString);
                             } else if (functionCode != DkErrorCode.DK_ACCOUNT_QUICK_REG_SUCCESS) {
                             } else {
                                 listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "not support trial");
                             }
                         } catch (Exception e) {
                             logE("invokeBDLogin onResponse error", e);
                             listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "invokeBDInit onResponse error");
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
        return mAccessToken;
    }
    
    public String getIAPDebugCallbackUrl() {
    	return mIAPDebugCallbackUrl;
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
}
