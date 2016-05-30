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
package org.cocos2dx.plugin.alicloud;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.cocos2dx.plugin.InterfaceAnalytics;
import org.cocos2dx.plugin.PluginListener;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.sdk.android.man.MANAnalytics;
import com.alibaba.sdk.android.man.MANHitBuilders;
import com.alibaba.sdk.android.man.MANHitBuilders.MANCustomHitBuilder;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;

public class AnalyticsAdapter implements InterfaceAnalytics, PluginListener {
    
	protected static String TAG = "alicloud.AnalyticsAdapter";
    private Activity mContext = null;
    private static boolean mDebug = false;

    protected static void LogE(String msg, Exception e) {
        Log.e(TAG, msg, e);
        e.printStackTrace();
    }

    protected static void LogD(String msg) {
        if (mDebug) {
            Log.d(TAG, msg);
        }
    }

    public AnalyticsAdapter(Context context) {
        mContext = (Activity)context;
        
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().init(mContext.getApplication(), mContext.getApplicationContext());
        
        MANAnalytics manAnalytics = manService.getMANAnalytics(); 
        manAnalytics.turnOffAutoPageTrack();
    }
    
    public boolean isValid() {
        return mContext != null;
    }
    
    @Override
    public void configDeveloperInfo(Hashtable<String, String> devInfo) {
    	
    }
    
    @Override
    public void startSession(String appKey) {
        LogD("startSession invoked!");
    }

    @Override
    public void stopSession() {
        LogD("stopSession invoked!");
    }

    @Override
    public void setSessionContinueMillis(int millis) {
        LogD("setSessionContinueMillis invoked!");
    }

    @Override
    public void setCaptureUncaughtException(boolean enable) {
        LogD("setCaptureUncaughtException invoked!");
        MANService manService = MANServiceProvider.getService();
        MANAnalytics manAnalytics = manService.getMANAnalytics();
    }

    @Override
    public void setDebugMode(boolean debug) {
    	mDebug = debug;
    }

    @Override
    public void logError(String errorId, String message) {
        LogD("logError invoked!");
    }

    @Override
    public void logEvent(String eventId) {
        LogD("logEvent(" + eventId + ") invoked!");
        MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(eventId);
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
    }

    @Override
    public void logEvent(String eventId, Hashtable<String, String> paramMap) {
        LogD("logEvent(" + eventId + "," + paramMap.toString() + ") invoked!");
        HashMap<String, String> properties = changeTableToMap(paramMap);
        MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(eventId);
        hitBuilder.setProperties(properties);
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
    }

    @Override
    public void logTimedEventBegin(String eventId) {
        LogD("logTimedEventBegin(" + eventId + ") invoked!");
    }

    @Override
    public void logTimedEventEnd(String eventId) {
        LogD("logTimedEventEnd(" + eventId + ") invoked!");
    }

    @Override
    public String getSDKVersion() {
        LogD("getSDKVersion invoked!");
        return "AliCloud no version info";
    }

    protected void logEventWithLabel(JSONObject eventInfo) {
        LogD("logEventWithLabel invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
        } catch(Exception e){
            LogE("Exception in logEventWithLabel", e);
        }
    }
    
    protected void logEventWithDurationLabel(JSONObject eventInfo) {
        LogD("logEventWithDurationLabel invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try {
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
            if (eventInfo.has("Param3")) {
                String label = eventInfo.getString("Param3");
            } else {
            }
        } catch (Exception e) {
            LogE("Exception in logEventWithDurationLabel", e);
        }
    }

    protected void logEventWithDurationParams(JSONObject eventInfo) {
        LogD("logEventWithDurationParams invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try {
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
            if (eventInfo.has("Param3")) {
                JSONObject params = eventInfo.getJSONObject("Param3");
                HashMap<String, String> curMap = getMapFromJson(params);
            } else {
            }
        } catch (Exception e) {
            LogE("Exception in logEventWithDurationParams", e);
        }
    }

    protected void logEventWithDuration(JSONObject eventInfo) {
        LogD("logEventWithDuration invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            int duration = eventInfo.getInt("Param2");
        } catch(Exception e){
            LogE("Exception in logEventWithDuration", e);
        }
    }

    protected void logTimedEventWithLabelBegin(JSONObject eventInfo) {
        LogD("logTimedEventWithLabelBegin invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
        } catch(Exception e){
            LogE("Exception in logTimedEventWithLabelBegin", e);
        }
    }
    
    protected void logTimedEventWithLabelEnd(JSONObject eventInfo) {
        LogD("logTimedEventWithLabelEnd invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
        } catch(Exception e){
            LogE("Exception in logTimedEventWithLabelEnd", e);
        }
    }
    
    protected void logTimedKVEventBegin(JSONObject eventInfo) {
        LogD("logTimedKVEventBegin invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
            JSONObject params = eventInfo.getJSONObject("Param3");
            
            if (params != null) {
                HashMap<String, String> curMap = getMapFromJson(params);
            }
        } catch(Exception e){
            LogE("Exception in logTimedKVEventBegin", e);
        }
    }
    
    protected void logTimedKVEventEnd(JSONObject eventInfo) {
        LogD("logTimedKVEventEnd invoked! event : " + eventInfo.toString());
        if (!isValid()) return;
        try{
            String eventId = eventInfo.getString("Param1");
            String label = eventInfo.getString("Param2");
        } catch(Exception e){
            LogE("Exception in logTimedKVEventEnd", e);
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
            LogE("Error when get HashMap from JSONObject", e);
        }

        return curMap;
    }

    @Override
    public String getPluginVersion() {
        return "0.2.0";
    }
    
    @Override
    public String getPluginName() {
        return "AliCloud";
    }
    
    @Override
    public void onResume() {
    }
    
    @Override
	public void onPause() {
    }
    
    @Override
	public void onDestroy() {
    	
    }
    
    @Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    	return false;
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
