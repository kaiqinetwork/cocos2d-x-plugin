package org.cocos2dx.plugin;

import java.util.Arrays;
import java.util.Iterator;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphObject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class UserFacebook implements InterfaceUser{

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private static Activity mContext = null;
    private static InterfaceUser mAdapter = null;
    public static Session session = null;
    private static boolean bDebug = true;
    private static boolean isLogined = false;
    private final static String LOG_TAG = "UserFacebook";
    private static String userID = "";
    
    protected static void LogE(String msg, Exception e) {
        Log.e(LOG_TAG, msg, e);
        e.printStackTrace();
    }

    protected static void LogD(String msg) {
        if (bDebug) {
            Log.d(LOG_TAG, msg);
        }
    }
    
    public UserFacebook(Context context) {

        mContext = (Activity)context;
        mAdapter = this;
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        session = Session.getActiveSession();
        if (session == null) {
            session = new Session(context);
        
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest((Activity) context).setCallback(statusCallback));
            }
        }
        
    }

    @Override
    public void configDeveloperInfo(Hashtable<String, String> cpInfo) {
        // TODO Auto-generated method stub
        LogD("not supported in Facebook pluign");
    }

    @Override
    public void login() {
        PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Session session = Session.getActiveSession();
                
                if (!session.isOpened() && !session.isClosed()) {
                    OpenRequest request = new Session.OpenRequest(mContext);
                    request.setCallback(statusCallback);
                    request.setPermissions(Arrays.asList("publish_actions"));
                    session.openForPublish(request);
                    //session.openForPublish(new Session.OpenRequest(mContext).setCallback(statusCallback));
                } else {
                    Session.openActiveSession(mContext, true, statusCallback);
                }
            } 
        });
    }

    @Override
    public void logout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            isLogined = false;
        }
        
    }

    @Override
    public boolean isLogined() {
        return isLogined;
    }

    @Override
    public String getSessionID() {
        return null;
    }

    @Override
    public void setDebugMode(boolean debug) {
        bDebug = debug;     
    }

    @Override
    public String getSDKVersion() {
        return "3.14.1";
    }

    @Override
    public String getPluginVersion() {
        return "0.2.0";
    }
    
    public String getAccessToken(){
        return Session.getActiveSession().getAccessToken();
    }
    
    public String getUserId(){
        return userID;
    }
    
    public String getLoginStatus(){
        return Session.getActiveSession().getState().name();
    }
    
    public void request(final JSONObject info /*String path, int method, JSONObject params, int nativeCallback*/ ){
        PluginWrapper.runOnMainThread(new Runnable(){

            @Override
            public void run() {
                try {
                    String path = info.getString("Param1");
                    
                    int method = info.getInt("Param2");
                    HttpMethod httpmethod = HttpMethod.values()[method];
                    
                    JSONObject jsonParameters = info.getJSONObject("Param3");
                    Bundle parameter = new Bundle();
                    Iterator it = jsonParameters.keys();
                    while(it.hasNext()){
                        String key = it.next().toString();
                        String value = jsonParameters.getString(key);
                        parameter.putString(key, value);
                    }
                    
                    final int nativeCallback = info.getInt("Param4");
                    
                    Request request = new Request(Session.getActiveSession(), path, parameter, httpmethod, new Request.Callback() {
                        
                        @Override
                        public void onCompleted(Response response) {
                            LogD(response.toString());
                            
                            FacebookRequestError error = response.getError();
                            int resultCode = error == null? 0 : error.getErrorCode();
                            
                            GraphObject graphObject = response.getGraphObject();
                            JSONObject json = graphObject == null ? new JSONObject() : graphObject.getInnerJSONObject();
                            
                            nativeRequestCallback(resultCode, json.toString(), nativeCallback);
                        }
                    });
                    request.executeAsync();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
        });
                
    }
        
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if(false == isLogined && com.facebook.SessionState.OPENED == state)
            {
                isLogined = true;
                Request request = new Request(session, "/me", null, HttpMethod.GET, new Request.Callback() {

                    @Override
                    public void onCompleted(Response response) {
                        GraphObject graphObject = response.getGraphObject();
                        userID = (String) graphObject.getProperty("id");
                        JSONObject resobj = graphObject.getInnerJSONObject();
                        LogD(resobj.toString());
                        UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_SUCCEED,/* "user " + graphObject.getProperty("name") + "logined",*/ resobj.toString());
                    }
                });
                request.executeAsync();
                
            }
        }
    }
        
    private native void nativeRequestCallback(int ret, String msg,int cbIndex);
}
