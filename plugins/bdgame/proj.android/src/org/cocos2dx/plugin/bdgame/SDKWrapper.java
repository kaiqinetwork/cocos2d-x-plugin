package org.cocos2dx.plugin.bdgame;

import java.util.Hashtable;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.InterfaceUser;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import com.baidu.gamesdk.ActivityAdPage;
import com.baidu.gamesdk.ActivityAdPage.Listener;
import com.baidu.gamesdk.ActivityAnalytics;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.BDGameSDKSetting;
import com.baidu.gamesdk.BDGameSDKSetting.Domain;
import com.baidu.gamesdk.BDGameSDKSetting.Orientation;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.ResultCode;
import com.duoku.platform.download.utils.PackageUtils;
import com.duoku.platform.util.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SDKWrapper {
	private static final String CHANNEL = "bdgame";
    private static final String LOG_TAG = "bdgame.SDKWrapper";
    private static final String PLUGIN_NAME = "BDGame";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "3.7.1";
    private static SDKWrapper mInstance;
    private String mBaiduGameAppId;
    private String mBaiduGameAppKey;
    private String mBaiduGameOrientation;
    private ILoginCallback mAccontSwitchListener;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private boolean mLoggedIn;
    private String mUid;
    private String mAccessToken;

    public SDKWrapper() {
        this.mAccontSwitchListener = null;
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
        mBaiduGameAppId = devInfo.get("BDGameAppId");
        mBaiduGameAppKey = devInfo.get("BDGameAppKey");
        if (mBaiduGameAppId == null || mBaiduGameAppKey == null || listener == null) {
            logD("DeveloperInfo something is null.");
            return false;
        }
        mInited = true;
        final boolean showAnnouncement = Boolean.parseBoolean(devInfo.get("BDGameAnnouncement"));
        setDuoKuSDK(devInfo.get("BDGameDKAppId"), devInfo.get("BDGameDKAppKey"));
        //mBaiduGameOrientation = PluginHelper.getApplicationOrientation();
        BDGameSDKSetting bdGameSDKSetting = new BDGameSDKSetting();
        if ("landscape".equals(mBaiduGameOrientation)) {
        	bdGameSDKSetting.setOrientation(Orientation.LANDSCAPE);
        } else {
        	bdGameSDKSetting.setOrientation(Orientation.PORTRAIT);
        }
        bdGameSDKSetting.setAppID(Integer.parseInt(mBaiduGameAppId));
        bdGameSDKSetting.setAppKey(mBaiduGameAppKey);
        if (mDebug) {
            bdGameSDKSetting.setDomain(Domain.DEBUG);
        }
        BDGameSDK.init(act, bdGameSDKSetting, new IResponse<Void>() {
        	public void onResponse(int resultCode, String resultDesc, Void extraData) {
                switch (resultCode) {
                    case ResultCode.INIT_SUCCESS:
                    	logI("isSupportScreenRecord=" + BDGameSDK.isSupportScreenRecord(mActivity));
                        mInited = true;
                        if (showAnnouncement) {
                            mUserAdapter.getAnnouncementInfo();
                        }
                        listener.onSuccessed(UserWrapper.ACTION_RET_INIT_SUCCESS, "INIT_SUCCESS");
                        break;
                    default:
                        mInited = false;
                        listener.onFailed(UserWrapper.ACTION_RET_INIT_FAIL, "INIT_FAIL");
                        break;
                }
            }

        });
        setAccoutSwitch();
        setSessionInvalid();
        setPluginListener();
        return mInited;
    }
    
    private void setDuoKuSDK(String dkAppId, String dkAppKey) {
        logD("setDuoKuSDK dkAppId=" + dkAppId + " dkAppKey" + dkAppKey);
        if (dkAppId != null && dkAppId.length() > 0 && dkAppKey != null && dkAppKey.length() > 0) {
            try {
                BDGameSDK.oldDKSdkSetting(dkAppId, dkAppKey);
            } catch (Exception e) {
                logE("call oldDKSdkSetting error", e);
            }
        }
    }

    private void setPluginListener() {
        logD("setPluginListener");
        final ActivityAdPage activityAdPage = new ActivityAdPage(mActivity, new Listener() {
        	public void onClose() {
                mUserAdapter.actionResult(UserWrapper.ACTION_RET_PAUSE_PAGE, "action pause page");
            }
        });
        final ActivityAnalytics activityAnalytics = new ActivityAnalytics(mActivity);
        PluginWrapper.addListener(new PluginListener() {
        	public void onStop() {
                logD("onStop");
                activityAdPage.onStop();
            }

            public void onResume() {
                logD("onResume");
                activityAnalytics.onResume();
                activityAdPage.onResume();
                BDGameSDK.onResume(mActivity);
            }

            public void onRestart() {
            }

            public void onPause() {
                logD("onPause");
                activityAdPage.onPause();
                activityAnalytics.onPause();
                BDGameSDK.onPause(mActivity);
            }

            public void onNewIntent(Intent intent) {
            }

            public void onDestroy() {
                logD("onDestroy");
                activityAdPage.onDestroy();
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
            }            
        });
    }

    private void setAccoutSwitch() {
        logD("setAccoutSwitch");
        if (mAccontSwitchListener == null) {
            mAccontSwitchListener = new ILoginCallback() {
                public void onSuccessed(int code, String msg) {
                    mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_SUCCESS, msg);
                }

                public void onFailed(int code, String msg) {
                    mUserAdapter.actionResult(UserWrapper.ACTION_RET_ACCOUNTSWITCH_FAIL, msg);
                }
            };
        }
        BDGameSDK.setSuspendWindowChangeAccountListener(new IResponse<Void>() {
            public void onResponse(int resultCode, String resultDesc, Void extraData) {
                logD("onResponse:resultDesc" + resultDesc);
                if (mAccontSwitchListener != null) {
                    switch (resultCode) {
                    	case ResultCode.LOGIN_CANCEL:
                        	mLoggedIn = false;
                        	mAccontSwitchListener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "login cancel");
                            break;
                    	case ResultCode.LOGIN_SUCCESS:
                            logD("\u5207\u6362\u8d26\u53f7 LOGIN_SUCCESS");
                            mUid = BDGameSDK.getLoginUid();
                            mAccessToken = BDGameSDK.getLoginAccessToken();
                            mAccontSwitchListener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
                            break;
                        case ResultCode.LOGIN_FAIL:
                        default:
                            mLoggedIn = false;
                            mAccontSwitchListener.onFailed(UserWrapper.ACTION_RET_ACCOUNTSWITCH_FAIL, resultDesc);
                            break;
                    }
                }
            }
        });
    }

    private void setSessionInvalid() {
        logD("setSessionInvalid");
        BDGameSDK.setSessionInvalidListener(new IResponse<Void>() {
            public void onResponse(int resultCode, String resultDesc, Void extraData) {
                if (resultCode == 0) {
                    mLoggedIn = false;
                    logD("SESSION_INVALID:" + resultDesc);
                    mUserAdapter.actionResult(UserWrapper.ACTION_RET_LOGIN_FAIL, "SESSION_INVALID");
                }
            }
        });
    }

    
    public boolean isInited() {
        return mInited;
    }

    public void userLogin(final Activity act, final ILoginCallback listener) {
        BDGameSDK.login(new IResponse<Void>() {
        	
        	@Override
        	public void onResponse(int resultCode, String resultDesc, Void extraData) {
        		logD("resultCode:" + resultCode + "resultDesc:" + resultDesc);
                if (listener != null) {
                    switch (resultCode) {
                        case ResultCode.LOGIN_CANCEL:
                            mLoggedIn = false;
                            listener.onFailed(UserWrapper.ACTION_RET_LOGIN_CANCEL, "login cancel");
                            break;
                        case ResultCode.LOGIN_SUCCESS:
                            mUid = BDGameSDK.getLoginUid();
                            mAccessToken = BDGameSDK.getLoginAccessToken();
                            listener.onSuccessed(UserWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
                            break;
                        case ResultCode.LOGIN_FAIL:
                        default:
                            mLoggedIn = false;
                            listener.onFailed(UserWrapper.ACTION_RET_LOGIN_FAIL, "login fail");
                            break;
                    }
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
