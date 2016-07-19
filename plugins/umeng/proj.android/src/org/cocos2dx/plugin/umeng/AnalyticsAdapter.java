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
package org.cocos2dx.plugin.umeng;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.cocos2dx.plugin.InterfaceAnalytics;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;

public class AnalyticsAdapter implements InterfaceAnalytics, PluginListener {
    
	protected static String LOG_TAG = "umeng.AnalyticsAdapter";
	private static final String PLUGIN_NAME = "Umeng";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "6.0.0";
    
    private Context mContext = null;
    private static boolean mDebug = false;

    protected static void logE(String msg, Exception e) {
    	PluginHelper.logE(LOG_TAG, msg, e);
    }

    protected static void logD(String msg) {
    	PluginHelper.logD(LOG_TAG, msg);
    }
    
    protected static void logI(String msg) {
    	PluginHelper.logI(LOG_TAG, msg);
    }

    public AnalyticsAdapter(Context context) {
        mContext = context;
        PluginWrapper.addListener(this);
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }
    
    protected void finalize() {
    	PluginWrapper.removeListener(this);
    }
    
    public boolean isValid() {
        return mContext != null;
    }
    
    @Override
    public void configDeveloperInfo(Hashtable<String, String> devInfo) {
    	String appkey = devInfo.get("UmengAppKey");
    	String channelKey = PluginWrapper.getAppChannel() == null ? 
    			(devInfo.get("UmengChannelKey") == null ? "release" : devInfo.get("UmengChannelKey")) :
    			PluginWrapper.getAppChannel();
    	MobclickAgent.EScenarioType scenarioType = devInfo.get("UmengScenarioType") == null ? 
    			MobclickAgent.EScenarioType.E_UM_GAME : MobclickAgent.EScenarioType.values()[Integer.parseInt(devInfo.get("UmengScenarioType"))]; 
    	Boolean enableCrashCatch = devInfo.get("UmengEnableCrashCatch") == null ? 
    			true : Boolean.parseBoolean(devInfo.get("UmengEnableCrashCatch"));
    	Boolean debugMode = devInfo.get("UmengDebugMode") == null ? 
    			true : Boolean.parseBoolean(devInfo.get("UmengDebugMode"));
    	
    	logI("ChannelKey: " + channelKey);
    	
    	if (appkey != null) {
    		UMAnalyticsConfig cfg = new MobclickAgent.UMAnalyticsConfig(mContext, appkey, channelKey, scenarioType);
        	MobclickAgent.startWithConfigure(cfg);
    	}    	
    	MobclickAgent.enableEncrypt(true);
    	MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_GAME);
    	MobclickAgent.setCatchUncaughtExceptions(enableCrashCatch);
    	MobclickAgent.setDebugMode(debugMode);
    }
    
    @Override
    public void startSession(String appKey) {
        logD("startSession invoked!");
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void stopSession() {
        logD("stopSession invoked!");
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void setSessionContinueMillis(int millis) {
        logD("setSessionContinueMillis invoked!");
        MobclickAgent.setSessionContinueMillis(millis);
    }

    @Override
    public void setCaptureUncaughtException(boolean enable) {
        logD("setCaptureUncaughtException invoked!");
        MobclickAgent.setCatchUncaughtExceptions(enable);
    }

    @Override
    public void setDebugMode(boolean debug) {
    	mDebug = debug;
        MobclickAgent.setDebugMode(debug);
    }

    @Override
    public void logError(String errorId, String message) {
        logD("logError invoked!");
        MobclickAgent.reportError(mContext, message);
    }

    @Override
    public void logEvent(String eventId) {
        logD("logEvent(" + eventId + ") invoked!");
        MobclickAgent.onEvent(mContext, eventId);
    }

    @Override
    public void logEvent(String eventId, Hashtable<String, String> paramMap) {
        logD("logEvent(" + eventId + "," + paramMap.toString() + ") invoked!");
        HashMap<String, String> curParam = changeTableToMap(paramMap);
        if (curParam.isEmpty()) {
        	MobclickAgent.onEvent(mContext, eventId);
        }
        else {
        	MobclickAgent.onEvent(mContext, eventId, curParam);
        }        
    }

    @Override
    public void logTimedEventBegin(String eventId) {
        logD("logTimedEventBegin(" + eventId + ") invoked!");
        //MobclickAgent.onEventBegin(mContext, eventId);
    }

    @Override
    public void logTimedEventEnd(String eventId) {
        logD("logTimedEventEnd(" + eventId + ") invoked!");
        //MobclickAgent.onEventEnd(mContext, eventId);
    }

    @Override
    public String getSDKVersion() {
        return SDK_VERSION;
    }

    protected void logEventWithLabel(JSONObject eventInfo) {
        logD("logEventWithLabel invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            MobclickAgent.onEvent(mContext, eventId, label);
        } catch(Exception e){
            logE("Exception in logEventWithLabel", e);
        }
    }
    
    protected void logEventWithDurationLabel(JSONObject eventInfo) {
        logD("logEventWithDurationLabel invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try {
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
            if (eventInfo.has("Param3")) {
                String label = eventInfo.getString("Param3");
                //MobclickAgent.onEventDuration(mContext, eventId, label, duration);
            } else {
                //MobclickAgent.onEventDuration(mContext, eventId, duration);
            }
        } catch (Exception e) {
            logE("Exception in logEventWithDurationLabel", e);
        }
    }

    protected void logEventWithDurationParams(JSONObject eventInfo) {
        logD("logEventWithDurationParams invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try {
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
            if (eventInfo.has("Param3")) {
                JSONObject params = eventInfo.getJSONObject("Param3");
                HashMap<String, String> curMap = getMapFromJson(params);
                //MobclickAgent.onEventDuration(mContext, eventId, curMap, duration);
            } else {
                //MobclickAgent.onEventDuration(mContext, eventId, duration);
            }
        } catch (Exception e) {
            logE("Exception in logEventWithDurationParams", e);
        }
    }

    protected void logEventWithDuration(JSONObject eventInfo) {
        logD("logEventWithDuration invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
            //MobclickAgent.onEventDuration(mContext, eventId, duration);
        } catch(Exception e){
            logE("Exception in logEventWithDuration", e);
        }
    }

    protected void logTimedEventWithLabelBegin(JSONObject eventInfo) {
        logD("logTimedEventWithLabelBegin invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            //MobclickAgent.onEventBegin(mContext, eventId, label);
        } catch(Exception e){
            logE("Exception in logTimedEventWithLabelBegin", e);
        }
    }
    
    protected void logTimedEventWithLabelEnd(JSONObject eventInfo) {
        logD("logTimedEventWithLabelEnd invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            //MobclickAgent.onEventEnd(mContext, eventId, label);
        } catch(Exception e){
            logE("Exception in logTimedEventWithLabelEnd", e);
        }
    }
    
    protected void logTimedKVEventBegin(JSONObject eventInfo) {
        logD("logTimedKVEventBegin invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            JSONObject params = eventInfo.getJSONObject("Param3");
            
            if (params != null) {
                HashMap<String, String> curMap = getMapFromJson(params);
                //MobclickAgent.onKVEventBegin(mContext, eventId, curMap, label);
            }
        } catch(Exception e){
            logE("Exception in logTimedKVEventBegin", e);
        }
    }
    
    protected void logTimedKVEventEnd(JSONObject eventInfo) {
        logD("logTimedKVEventEnd invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            //MobclickAgent.onKVEventEnd(mContext, eventId, label);
        } catch(Exception e){
            logE("Exception in logTimedKVEventEnd", e);
        }
    }

    private HashMap<String, String> changeTableToMap(Hashtable<String, String> param) {
        HashMap<String, String> retParam = new HashMap<String, String>();
        for(Iterator<String> it = param.keySet().iterator(); it.hasNext(); ) {   
            String key = it.next();
            String value = param.get(key);

            retParam.put(key, value);
        }

        return retParam;
    }

    private HashMap<String, String> getMapFromJson(JSONObject json) {
        HashMap<String, String> curMap = new HashMap<String, String>();
        try {
            @SuppressWarnings("rawtypes")
            Iterator it = json.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = json.getString(key);
                curMap.put(key, value);
            }
        } catch (Exception e) {
            logE("Error when get HashMap from JSONObject", e);
        }

        return curMap;
    }

    @Override
    public String getPluginVersion() {
        return PLUGIN_VERSION;
    }
    
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }
    
    @Override
    public void onResume() {
    	MobclickAgent.onResume(mContext);
    }
    
    @Override
	public void onPause() {
    	MobclickAgent.onPause(mContext);
    }
    
    @Override
	public void onDestroy() {
    	
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
    
    @Override
	public void onNewIntent(Intent intent) {
    	
    }
    
    @Override
	public void onStop() {
    	
    }
    
    @Override
	public void onRestart() {
    	
    }
}
