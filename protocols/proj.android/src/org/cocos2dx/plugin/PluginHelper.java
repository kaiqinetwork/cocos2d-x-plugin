package org.cocos2dx.plugin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class PluginHelper {
	public PluginHelper()
    {
    }
	
	public static String getAppOrientation()
    {
        String orientation = PluginWrapper.getAppInfo().get("Orientation");
        if (orientation == null)
        	orientation = "auto";
        return orientation;
    }
	
	public static boolean getDebugMode()
    {
        String s = PluginWrapper.getAppInfo().get("DebugMode");
        if (s != null && s.equals("true"))
            return true;
        return false;
    }
	
	public static boolean networkReachable(Context context) {
		boolean bRet = false;
		try {
			ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conn.getActiveNetworkInfo();
			bRet = (null == netInfo) ? false : netInfo.isAvailable();
		} catch (Exception e) {
			PluginHelper.logE("PluginHelper", "Fail to check network status", e);
		}
		PluginHelper.logD("PluginHelper", "Network reachable : " + bRet);
		return bRet;
	}

	public static void logV(String tag, String msg) {
		Log.v(tag, msg);
	}
	
	public static void logE(String tag, String msg) {
		Log.e(tag, msg);
	}
	
	public static void logE(String tag, String msg, Exception e) {
		Log.e(tag, msg, e);
		e.printStackTrace();
	}

	public static void logD(String tag, String msg) {
		Log.d(tag, msg);
	}
	
	public static void logI(String tag, String msg) {
		Log.i(tag, msg);
	}
}
