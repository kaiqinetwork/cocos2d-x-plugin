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
package org.cocos2dx.plugin.testin;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.InterfaceCrash;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import android.content.Context;

import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;

public class CrashAdapter implements InterfaceCrash {
    
	protected static String LOG_TAG = "Testin.CrashAdapter";
	private static final String PLUGIN_NAME = "Testin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String SDK_VERSION = "2.1.0";
    
    private InterfaceCrash mInstance;
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

    public CrashAdapter(Context context) {
        mContext = context;
        mInstance = this;
        mDebug = false;
        configDeveloperInfo(PluginWrapper.getDeveloperInfo());
    }
    
    @Override
    public void configDeveloperInfo(Hashtable<String, String> devInfo) {
    	logD("configDeveloperInfo(" + devInfo.toString() + ")invoked!");
    	final Hashtable<String, String> curDevInfo = devInfo;
        PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				boolean collectNDKCrash = Boolean.parseBoolean(curDevInfo.get("TestinCollectNDKCrash"));
				boolean openCrash = Boolean.parseBoolean(curDevInfo.get("TestinOpenCrash"));
				boolean reportOnlyWifi = Boolean.parseBoolean(curDevInfo.get("TestinReportOnlyWifi"));
				boolean reportOnBack = Boolean.parseBoolean(curDevInfo.get("TestinReportOnBack"));
				boolean errorActivity = Boolean.parseBoolean(curDevInfo.get("TestinErrorActivity"));
				String channelKey = PluginWrapper.getAppChannel() == null ? 
		    			(curDevInfo.get("TestinChannelKey") == null ? "release" : curDevInfo.get("TestinChannelKey")) :
		    			PluginWrapper.getAppChannel();
		    	boolean debugMode = Boolean.parseBoolean(curDevInfo.get("TestinDebugMode"));
				try {
				    TestinAgentConfig config = new TestinAgentConfig.Builder(mContext)
				    		.withAppKey(curDevInfo.get("TestinAppKey"))
				    		.withAppChannel(channelKey)
				    		.withDebugModel(debugMode)
				    		.withErrorActivity(errorActivity)
				    		.withCollectNDKCrash(collectNDKCrash)
				    		.withOpenCrash(openCrash)
				    		.withReportOnlyWifi(reportOnlyWifi)
				    		.withReportOnBack(reportOnBack)
				    		.build();
				    TestinAgent.init(config);
				} catch (Exception e) {
					logE(LOG_TAG, e);
				}
			}
        });
    }

	@Override
	public void setUserIdentifier(String identifier) {
		logD("setUserIdentifier(" + identifier + ") invoked!");
        TestinAgent.setUserInfo(identifier);
	}

	@Override
	public void reportException(String message, String exception) {
		logD("reportException(" + message + "," + message + ") invoked!");
        TestinAgent.uploadException(mContext, message, new Throwable(exception));
	}

	@Override
	public void leaveBreadcrumb(String breadcrumb) {
		logD("leaveBreadcrumb(" + breadcrumb + ") invoked!");
        TestinAgent.leaveBreadcrumb(breadcrumb);
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
	public boolean isFunctionSupported(String funcName) {
		logD("isFunctionSupported(" + funcName + ")invoked!");
        Method[] methods = CrashAdapter.class.getMethods();
        for (Method name : methods) {
            if (name.getName().equals(funcName)) {
                return true;
            }
        }
        return false;
	}

	@Override
	public String getPluginName() {
		return PLUGIN_NAME;
	}
}
