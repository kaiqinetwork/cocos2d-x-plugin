package org.cocos2dx.plugin.bdgamesingle;

import com.duoku.platform.single.DKPlatform;

import android.app.Application;

public class BDApplication extends Application {
	public void onCreate() {
        super.onCreate();
        DKPlatform.getInstance().invokeBDInitApplication(this);
    }
}
