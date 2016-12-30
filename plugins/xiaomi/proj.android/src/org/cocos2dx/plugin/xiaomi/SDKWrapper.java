package org.cocos2dx.plugin.xiaomi;

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

import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SDKWrapper {
	private static final String LOG_TAG = "Xiaomi.SDKWrapper";
    private static final String PLUGIN_NAME = "xiaomi";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "4.6.11";
    private static SDKWrapper mInstance;
    private Activity mActivity;
    private boolean mDebug;
    private IAPAdapter mIAPAdapter;
    private boolean mInited;
    private UserAdapter mUserAdapter;
    private String mXiaomiAppID;
    private String mXiaomiAppKey;
    private String mUid;
    private String mSession;
    private boolean mLoggedIn;

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
        logD("start init SDK ");
    	if (adapter instanceof InterfaceUser) {
            mUserAdapter = (UserAdapter) adapter;
        } else if (adapter instanceof InterfaceIAP) {
            mIAPAdapter = (IAPAdapter) adapter;
        }
        if (mInited) {
            return true;
        }
        mActivity = act;
        mInited = false;
        mDebug = PluginHelper.getDebugMode();
        mXiaomiAppID = devInfo.get("XiaomiAppID");
        mXiaomiAppKey = devInfo.get("XiaomiAppKey");
        
        if (mXiaomiAppID == null || mXiaomiAppKey == null) {
            logD("getDeveloperInfo error!");
            return false;
        }

        MiAppInfo appInfo = new MiAppInfo();
        appInfo.setCtx((Context)mActivity);
        appInfo.setAppId(mXiaomiAppID);
        appInfo.setAppKey(mXiaomiAppKey);
        MiCommplatform.Init((Context)mActivity, appInfo);
        
        setPluginListener();
        mInited = true;
        
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
            	MiCommplatform.getInstance().miLogin(mActivity, new OnLoginProcessListener() {

					@Override
					public void finishLoginProcess(int code, MiAccountInfo accountInfo) {
						 switch( code )
				            {
				            case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS:// ��½�ɹ�
				            	long uid = accountInfo.getUid();//��ȡ�û��ĵ�½���UID�����û�Ψһ��ʶ��
				            	mUid = String.valueOf(uid);
				            	//����Ϊ��ȡsession��У�����̣������������Ϸ����У�飬����ǵ�����Ϸ��Ӧ�ÿ�ѡ
				            	//��ȡ�û��ĵ�½��Session����ο�[5.3.3����У��Session��Ч��](#8)��
				            	String session = accountInfo.getSessionId();
				            	mSession = session;
				            	//�뿪������ɽ�uid��session�ύ���������Լ�����������session��֤
				            	mLoggedIn = true;
				            	listener.onSuccessed(code, "Login Success");
				                break;
				            case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_LOGIN_FAIL:
				                // ��½ʧ��
				            	mLoggedIn = false;
				            	listener.onFailed(code, "Login Failed");
				                break;
				            case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_CANCEL:
				                // ȡ����¼
				            	mLoggedIn = false;
				            	listener.onFailed(code, "Login Cancel");
				                break;
				            case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED: 
				            	//��¼�������ڽ�����
				            	mLoggedIn = false;
				            	listener.onFailed(code, "Login Error");
				            	break;        
				            default:
				                // ��¼ʧ��
				                break;
				        }
						logD("mi Account: " +  mUid + "mSession:" + mSession);
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
