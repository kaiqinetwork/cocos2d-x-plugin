package org.cocos2dx.plugin;

import java.util.Hashtable;

public interface InterfaceCrash {
	public static final int PluginType = 128;
	
	public void configDeveloperInfo(Hashtable<String, String> devInfo);
	public void setUserIdentifier(String identifier);
    public void reportException(String message, String exception);
    public void leaveBreadcrumb(String breadcrumb);
    public void setDebugMode(boolean debug);
    public String getSDKVersion();
    public String getPluginVersion();
    public String getPluginName();
    public boolean isFunctionSupported(String funcName);
}
