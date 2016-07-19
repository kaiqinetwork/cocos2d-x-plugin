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
package org.cocos2dx.plugin.sharesdk;

import java.util.HashMap;
import java.util.Hashtable;

import org.cocos2dx.plugin.InterfaceShare;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.ShareWrapper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class ShareAdapter implements InterfaceShare {
    
	protected static String LOG_TAG = "sharesdk.ShareAdapter";
	private static final String PLUGIN_NAME = "ShareSDK";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "2.7.3";
    
	private static boolean mDebug = false;
    private static ShareAdapter mInstance = null;
    private Activity mContext = null;
    
    protected static void LogE(String msg, Exception e) {
    	PluginHelper.logE(LOG_TAG, msg, e);
    }

    protected static void logD(String msg) {
        PluginHelper.logD(LOG_TAG, msg);
    }

    public ShareAdapter(Context context) {
        mContext = (Activity)context;
        mInstance = this;
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }
    
    public boolean isValid() {
        return mContext != null;
    }
    
    @Override
    public void configDeveloperInfo(Hashtable<String, String> devInfo) {
    	PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
                ShareSDK.initSDK(mContext);
            }
        });
    }
    
    @Override
    public void setDebugMode(boolean debug) {
    	mDebug = debug;
    }

    @Override
    public String getSDKVersion() {
        return SDK_VERSION;
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
	public void share(final Hashtable<String, String> cpInfo) {
		PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	 if (cpInfo == null) {
                     shareResult(1, "share param is null");
                 } else if (cpInfo.containsKey("title") && cpInfo.containsKey("titleUrl") && 
                		 cpInfo.containsKey("site") && cpInfo.containsKey("siteUrl")) {
                     OnekeyShare oks = new OnekeyShare();
                     oks.setTitle(cpInfo.get("title"));
                     oks.setTitleUrl(cpInfo.get("titleUrl"));
                     oks.setSite(cpInfo.get("site"));
                     oks.setSiteUrl(cpInfo.get("siteUrl"));
                     if (cpInfo.containsKey("imagePath")) {
                         oks.setImagePath(cpInfo.get("imagePath"));
                     }
                     if (cpInfo.containsKey("imageUrl")) {
                         oks.setImageUrl(cpInfo.get("imageUrl"));
                     }
                     if (cpInfo.containsKey("text")) {
                         oks.setText(cpInfo.get("text"));
                     }
                     if (cpInfo.containsKey("comment")) {
                         oks.setComment(cpInfo.get("comment"));
                     }
                     if (cpInfo.containsKey("url")) {
                         oks.setUrl(cpInfo.get("url"));
                     }
                     if (cpInfo.containsKey("latitude")) {
                         oks.setLatitude(Float.parseFloat(cpInfo.get("latitude")));
                     }
                     if (cpInfo.containsKey("longitude")) {
                         oks.setLongitude(Float.parseFloat(cpInfo.get("longitude")));
                     }
                     if (cpInfo.containsKey("theme")) {
                    	 oks.setTheme(OnekeyShareTheme.fromValue(Integer.parseInt(cpInfo.get("theme"))));
                     } else {
                         oks.setTheme(OnekeyShareTheme.CLASSIC);
                     }
                     oks.setSilent(false);
                     oks.disableSSOWhenAuthorize();
                     oks.setCallback(new PlatformActionListener() {
                         public void onError(Platform arg0, int arg1, Throwable arg2) {
                             shareResult(1, arg2.getMessage());
                         }

                         public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                             shareResult(0, arg2.toString());
                         }

                         public void onCancel(Platform arg0, int arg1) {
                             shareResult(2, "shareSdk oncancel");
                         }
                     });
                     oks.show(mContext);
                 } else {
                     shareResult(1, "share param is incomplete");
                 }
            }
        });
	}
    
	protected void shareResult(int ret, String msg) {
        logD("shareResult: " + ret + " msg : " + msg);
        ShareWrapper.onShareResult(mInstance, ret, msg);
    }

}
