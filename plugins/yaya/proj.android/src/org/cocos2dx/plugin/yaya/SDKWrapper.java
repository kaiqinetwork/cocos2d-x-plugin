package org.cocos2dx.plugin.yaya;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceVoice;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.ServiceWrapper;
import org.cocos2dx.plugin.VoiceWrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.yunva.im.sdk.lib.YvLoginInit;
import com.yunva.im.sdk.lib.core.YunvaImSdk;
import com.yunva.im.sdk.lib.event.MessageEvent;
import com.yunva.im.sdk.lib.event.MessageEventListener;
import com.yunva.im.sdk.lib.event.MessageEventSource;
import com.yunva.im.sdk.lib.event.RespInfo;
import com.yunva.im.sdk.lib.event.msgtype.MessageType;
import com.yunva.im.sdk.lib.model.login.ImLoginResp;
import com.yunva.im.sdk.lib.model.login.ImThirdLoginResp;
import com.yunva.im.sdk.lib.model.tool.ImAudioPlayResp;
import com.yunva.im.sdk.lib.model.tool.ImAudioRecordResp;
import com.yunva.im.sdk.lib.model.tool.ImUploadFileResp;

public class SDKWrapper implements MessageEventListener{
	private static final String LOG_TAG = "Yaya.SDKWrapper";
    private static final String PLUGIN_NAME = "Yaya";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "1.0.2";
    private static SDKWrapper mInstance;
    private Activity mActivity;
    private boolean mDebug;
    private boolean mInited;
    private boolean mCancel = false;
    private String mYayaAppId;
    private VoiceAdapter mVoiceAdapter;
    public static final String channel_wildcard="0x001";
    List<String> mChannelList;
    String	mTtInfo;
    String mUrl;
    
    public SDKWrapper() {
        this.mActivity = null;
        this.mDebug = false;
        this.mVoiceAdapter = null;
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
        mVoiceAdapter = (VoiceAdapter)adapter;
        
        if (mInited) {
            return true;
        }
        mActivity = act;
        mDebug = PluginHelper.getDebugMode();
        setPluginListener();
        mInited = true;
        mTtInfo = "";
        
        logD("init yaya SDK ");
        mYayaAppId = devInfo.get("YayaAppId");
        boolean isTest=false;
        boolean isOversea = false;
    	
        com.yunva.im.sdk.lib.YvLoginInit.context = mActivity.getApplicationContext();
        mInited = com.yunva.im.sdk.lib.YvLoginInit.initApplicationOnCreate(mActivity.getApplication(), mYayaAppId);
        logD(Boolean.toString(mInited));
        
        //String path = ((Context)mActivity).getFilesDir().getAbsolutePath()
		//		+ File.separator + "voice"+ File.separator;

        String path = Environment.getExternalStorageDirectory().toString() + "/yunva_sdk_lite";
        
        YunvaImSdk.getInstance().init(mActivity, mYayaAppId, path, isTest,isOversea);
		logD(Boolean.toString(mInited));
		YunvaImSdk.getInstance().setAppVesion(SDK_VERSION);
		
		mChannelList  =new ArrayList<String>();
		mChannelList.add(channel_wildcard);
        
        return mInited;
    }
    
    public void setUserInfo(Hashtable<String, String> devInfo) {
    	String userId = devInfo.get("userId");
    	String nickName = devInfo.get("nickName");
    	TtInfo tt = new TtInfo();
    	tt.uid = userId;
    	tt.nickname = nickName;
    	mTtInfo = "{\"uid\":\""+tt.uid+"\",\"nickname\":\""+tt.nickname+"\"}";
    }
    
    public void startRecord() {
    	VoiceUtil.muteAudioFocus(mActivity, true);
    	YunvaImSdk.getInstance().stopPlayAudio();
    	YunvaImSdk.getInstance().stopAudioRecord();

    	mUrl = "";
		boolean start = YunvaImSdk.getInstance().startAudioRecord("", 
				"lite",(byte) 0
				);
		if(!start){
			mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_RECORD_ERROR, "record error");
		}
    }
    
    public void cancelRecord(){
    	YunvaImSdk.getInstance().stopAudioRecord();
    	mCancel = true;
    }
    
    public void stopRecord() {
    	YunvaImSdk.getInstance().stopAudioRecord();
    	mCancel = false;
    }
    
    public void login(){
    	if (mTtInfo == "")
    	{
    		mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_LOGIN_FAIL, "");
    		exit();
    		return;
    	}
    	MessageEventSource.getSingleton().addLinstener(MessageType.IM_LOGIN_RESP, this);
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_THIRD_LOGIN_RESP, this);
    	YunvaImSdk.getInstance().Binding(mTtInfo,"1",mChannelList);
    }
    
    public void exit() {
    	YunvaImSdk.getInstance().logout();
    	MessageEventSource.getSingleton().removeLinstener(this);
		YunvaImSdk.getInstance().clearCache();
		YunvaImSdk.getInstance().release();
		YvLoginInit.release();
    }
    
    public void playVoice(String url) {
    	mUrl = url;
    	PluginWrapper.runOnMainThread(new Runnable() {
	        public void run() {
	        	YunvaImSdk.getInstance().playAudio(mUrl, "", "");
	        }
    	});
   	}
    
    public void registLinstner(){
    	YunvaImSdk.getInstance().setRecordMaxDuration(60, false);
		//注册响应事件:重连
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_RECONNECTION_NOTIFY, this);    

		MessageEventSource.getSingleton().addLinstener(MessageType.IM_RECORD_STOP_RESP, this);
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_RECORD_FINISHPLAY_RESP, this);
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_SPEECH_STOP_RESP, this);
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_NET_STATE_NOTIFY, this);
		MessageEventSource.getSingleton().addLinstener(MessageType.IM_UPLOAD_FILE_RESP, this);
//		myHandler.sendEmptyMessage(SET_RECIVER_MSG);
    }
    
    public boolean isInited() {
        return mInited;
    }

    public class  TtInfo{
		String uid;
		String nickname;
		String iconUrl;
		String vip;
		String level;
		String ext;
	}
    
    @Override
	public void handleMessageEvent(MessageEvent event) {
    	RespInfo  msg = null;
		switch (event.getbCode()) { 
		case MessageType.IM_LOGIN_RESP:
			//授权回应处理：  
			msg=event.getMessage();
			/**
			 * 获取回应数据（回应数据被封装为Message 对象，
			 * 其中msg.arg1 SDK定义为响应状态码[10001：表示成功；20001：表示失败]；
			 * msg.obj 为响应数据）
			 */
			final ImLoginResp  user=  (ImLoginResp) msg.getResultBody();
			if(msg.getResultCode()==MessageType.RESP_CODE_SUCCESS){
				
				YunvaImSdk.getInstance().setSpeech_language(1,0);
				//如果msg.arg1等于LibMessageType.RESP_CODE_SUCCESS 则登陆成功，否则登陆失败
				/**
				 * 登陆授权请求成功后会直接返回登陆账号信息
				 */ 
				//登录成功，
				logD("id:"+user.getUserId()+",nickname:"+user.getNickname());
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
				registLinstner();
			}else{
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_LOGIN_FAIL, "login error");
				}
			break;
		case MessageType.IM_THIRD_LOGIN_RESP:
			msg=event.getMessage();
			final ImThirdLoginResp thirdUser = (ImThirdLoginResp) msg.getResultBody();
					if(msg.getResultCode()==MessageType.RESP_CODE_SUCCESS){
						//如果msg.arg1等于LibMessageType.RESP_CODE_SUCCESS 则登陆成功，否则登陆失败
						/**
						 * 登陆授权请求成功后会直接返回登陆账号信息
						 */ 
						//登录成功，
						logD("id:"+thirdUser.getOpenId()+",nickname:"+thirdUser.getThirdUserName());
						mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_LOGIN_SUCCESS, "login success");
						registLinstner();
					}else{
						mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_LOGIN_FAIL, "login error");
					}
		case MessageType.IM_RECORD_STOP_RESP:
			msg=event.getMessage();
			VoiceUtil.muteAudioFocus(mActivity, false);
			ImAudioRecordResp imAudioRecordResp = (ImAudioRecordResp) msg.getResultBody();
			if (!mCancel)
			{
				String strFilePath = imAudioRecordResp.getStrfilepath();
				YunvaImSdk.getInstance().uploadFile(strFilePath, "123");
			}
			mCancel = false;
			break;
		case MessageType.IM_UPLOAD_FILE_RESP:
			msg=event.getMessage();
			ImUploadFileResp imUploadFileResp = (ImUploadFileResp) msg.getResultBody();
			if (imUploadFileResp.getResult() == 0)
			{
				mUrl = imUploadFileResp.getFileUrl();
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_UPLOAD_SUCCESS, "upload success");
			}
			else
			{
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_UPLOAD_FAIL, imUploadFileResp.getMsg());
			}
		case MessageType.IM_RECORD_FINISHPLAY_RESP:
			msg=event.getMessage();
			ImAudioPlayResp imAudioPlayResp = (ImAudioPlayResp)msg.getResultBody();
			if (imAudioPlayResp.getResult() == 0)
			{
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_PLAY_SUCCESS, "play success");
			}
			else
			{
				mVoiceAdapter.actionResult(VoiceWrapper.ACTION_RET_PLAY_FAIL, imAudioPlayResp.getDescribe());
			}
		default:
			break;
		}
		
	}
    
    public String getVoiceUrl() {
    	return mUrl;
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
}
