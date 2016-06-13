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
package org.cocos2dx.plugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;


public class PluginWrapper {

	private static final String TAG = "PluginWrapper";
	protected static Context sContext = null;
    protected static GLSurfaceView sGLSurfaceView = null; 
    protected static Handler sMainThreadHandler = null;
    protected static Handler sGLThreadHandler = null;
    protected static Set<PluginListener> sListeners = new LinkedHashSet<PluginListener>();
    private static Hashtable<String, String> sPluginParams = new Hashtable<String, String>();
    private static Vector<String> sSupportPlugins = new Vector<String>();
    protected static String sAppChannel = null;
    
    public static void init(Context context) {
        sContext = context;
        if (null == sMainThreadHandler) {
            sMainThreadHandler = new Handler();
        }
        PluginWrapper.analysisDeveloperInfo();
    }

    public static void setGLSurfaceView(GLSurfaceView value) {
        sGLSurfaceView = value;
    }
    
    protected static void initFromNativeActivity(Activity act) {
        sContext = act;
    }
    
    public static void onResume() {
    	for (PluginListener listener : sListeners) {
    		listener.onResume();
    	}
    }
    
    public static void onPause() {
    	for (PluginListener listener : sListeners) {
    		listener.onPause();
    	}
    }
    
    public static void onDestroy() {
    	Iterator<PluginListener> i = sListeners.iterator();
    	while(i.hasNext()){
    		PluginListener p = i.next();
    		p.onDestroy();
    	}
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    	boolean result = true;
    	
        for (PluginListener listener : sListeners) {
            boolean activityResult = listener.onActivityResult(requestCode, resultCode, data);
            result = result && activityResult;
        }
        
        return result;
    }
    
    public static void onNewIntent(Intent intent) { 
    	for (PluginListener listener : sListeners) {
    		listener.onNewIntent(intent);
    	}
	} 
    
    public static void onStop() {
    	for (PluginListener listener : sListeners) {
    		listener.onStop();
    	}
    }
    
    public static void onRestart() { 
    	for (PluginListener listener : sListeners) {
    		listener.onRestart();
    	}
  	} 
    
    public static void addListener(PluginListener listener) {
    	sListeners.add(listener);
    }
    
    public static void removeListener(PluginListener listener) {
    	sListeners.remove(listener);
    }
    
    protected static Object initPlugin(String classFullName) {
        Log.i(TAG, "class name : ----" + classFullName + "----");
        Class<?> c = null;
        try {
            String fullName = classFullName.replace('/', '.');
            c = Class.forName(fullName);
        } catch (ClassNotFoundException e) {  
            Log.e(TAG, "Class " + classFullName + " not found.");
            e.printStackTrace();
            return null;
        }

        try {
            Context ctx = getContext();
            if (ctx != null) {
                Object o = c.getDeclaredConstructor(Context.class).newInstance(ctx);
                return o;
            } else {
                Log.e(TAG, "Plugin " + classFullName + " wasn't initialized.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static int getPluginType(Object obj) {
        int nRet = -1;
        try
        {
            Field filedID = obj.getClass().getField("PluginType");
            Integer nObj = (Integer) filedID.get(obj);
            nRet = nObj.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nRet;
    }

    public static Context getContext() {
        return sContext;
    }
    
    public static void runOnGLThread(Runnable r) {
        if (null != sGLSurfaceView) {
            sGLSurfaceView.queueEvent(r);
        } else
        if (null != sGLThreadHandler) {
            sGLThreadHandler.post(r);
        } else {
            Log.i(TAG, "call back invoked on main thread");
            r.run();
        }
    }

    public static void runOnMainThread(Runnable r) {
        if (null != sMainThreadHandler) {
            sMainThreadHandler.post(r);
        } else
        if (null != sContext && sContext instanceof Activity) {
            Activity act = (Activity) sContext;
            act.runOnUiThread(r);
        }
    }
    
    public static void analysisDeveloperInfo() {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try {
    		sSupportPlugins = new Vector<String>();
    		sPluginParams = new Hashtable<String, String>();
        	String fileName = "plugins/DeveloperInfo.xml";
        	DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(sContext.getAssets().open(fileName));
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("PluginList");
            if (items != null) {
            	NodeList childItems = ((Element)items.item(0)).getElementsByTagName("Plugin");
            	if (childItems != null) {
            		for (int i = 0; i < childItems.getLength(); i++) {
            			Node node = childItems.item(i).getAttributes().getNamedItem("className");
            			if (node != null) {
            				sSupportPlugins.add(node.getNodeValue());
            			}
            		}
            	}
            }
            items = root.getElementsByTagName("ParamList");
            if (items != null) {
            	NodeList childItems = ((Element)items.item(0)).getElementsByTagName("Param");
            	if (childItems != null) {
            		for (int i = 0; i < childItems.getLength(); i++) {
            			NamedNodeMap nodes = childItems.item(i).getAttributes();
            			Node nameNode = nodes.getNamedItem("name");
            			Node valueNode = nodes.getNamedItem("value");
            			if (nameNode != null && valueNode != null) {
            				sPluginParams.put(nameNode.getNodeValue(), valueNode.getNodeValue());
            			}
            		}
            	}
            }
            
        } catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
    public static Hashtable<String, String> getDeveloperInfo()
    {
        if(sPluginParams == null)
            return new Hashtable<String, String>();
        else
            return sPluginParams;
    }
    
    public static Vector<String> getSupportPlugins()
    {
        if(sSupportPlugins == null)
            return new Vector<String>();
        else
            return sSupportPlugins;
    }
    
    public static String getAppChannel()
    {
        if(sAppChannel != null)
            return sAppChannel;
        
        ApplicationInfo appInfo = sContext.getApplicationInfo();
		String sourceDir = appInfo.sourceDir;
		String v = "";
		
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith("AppChannel")) {
                    v = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = v.split("_");
        if (split != null && split.length >= 2) {
            sAppChannel = v.substring(split[0].length() + 1);

        } else {
        	try {
				appInfo = sContext.getPackageManager()
			        .getApplicationInfo(sContext.getPackageName(), PackageManager.GET_META_DATA);
				sAppChannel = appInfo.metaData.getString("APP_CHANNEL");  
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
        }

		return sAppChannel;
    }
}
