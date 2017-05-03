package org.cocos2dx.plugin.sobot;

import java.util.Hashtable;
import java.util.Map;

import org.apache.http.conn.routing.HttpRouteDirector;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceService;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.ServiceWrapper;

import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.utils.LogUtils;
import com.sobot.chat.utils.ZhiChiConstant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SDKWrapper {
	private static final String LOG_TAG = "sobot.SDKWrapper";
    private static final String PLUGIN_NAME = "sobot";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "2.1.0";
    private static SDKWrapper mInstance;
    private String mSobotAppKey;
    private Activity mActivity;
    private boolean mDebug;
    private boolean mInited;
    private ServiceAdapter mServiceAdapter;
    private Information mInfo;

    public SDKWrapper() {
        this.mActivity = null;
        this.mDebug = false;
        this.mServiceAdapter = null;
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
        mServiceAdapter = (ServiceAdapter)adapter;
        
        if (mInited) {
            return true;
        }
        mActivity = act;
        mDebug = PluginHelper.getDebugMode();
        setPluginListener();
        mInited = true;
        
        mSobotAppKey = devInfo.get("SobotAppKey");
        SobotApi.initSobotChannel((Context)mActivity);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        mActivity.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int noReadNum = intent.getIntExtra("noReadCount", 0);
		        String content = intent.getStringExtra("content");
		        PluginWrapper.runOnMainThread(new Runnable() {
		            public void run() {
		            	mServiceAdapter.actionResult(ServiceWrapper.ACTION_RET_HAS_UNREAD_MSG, "has new message");
		            }
		        });
			}
        	
        }, filter);
    	
        return mInited;
    }
    
    private void setPluginListener() {
        logD("setPluginListener");
        PluginWrapper.addListener(new PluginListener() {
        	public void onStop() {
                logD("onStop");
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
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
            }            
        });
    }
    
    public void setUserInfo(Hashtable<String, String> devInfo) {
    	mInfo = new Information();
    	mInfo.setAppkey(mSobotAppKey);
    	mInfo.setUid(devInfo.get("userId"));
    	mInfo.setUname(devInfo.get("nickName"));
    	Map<String, String> userinfo = new Hashtable<String, String>();
    	userinfo.put("\u7528\u6237\u8d26\u53f7", devInfo.get("userAccount"));
    	userinfo.put("\u8bbe\u5907\u578b\u53f7", android.os.Build.MODEL);
    	userinfo.put("Android SDK \u7248\u672c", android.os.Build.VERSION.SDK);
    	userinfo.put("Android \u7248\u672c", android.os.Build.VERSION.RELEASE);
    	userinfo.put("\u6e38\u620f\u540d\u79f0", devInfo.get("gameName"));
    	userinfo.put("\u6e38\u620f\u7248\u672c", devInfo.get("gameVersion"));
    	userinfo.put("ID\u5217\u8868", devInfo.get("userAccounts"));
    	mInfo.setCustomInfo(userinfo);
    	logD("SDKWrapper:userInfo setted");
    }
    
    public void startChat() {
    	logD("SDKWrapper start chat");
    	SobotApi.startSobotChat((Context)mActivity, mInfo);
    }
    
    public int getUnreadMsgCount(){
    	return SobotApi.getUnreadMsg((Context)mActivity);
    }
    
    public boolean isInited() {
        return mInited;
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
