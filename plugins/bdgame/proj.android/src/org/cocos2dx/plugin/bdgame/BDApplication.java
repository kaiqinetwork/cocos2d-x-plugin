package org.cocos2dx.plugin.bdgame;

import android.app.Application;
import com.baidu.gamesdk.BDGameSDK;

public class BDApplication extends Application {
	public void onCreate() {
        super.onCreate();
        BDGameSDK.initApplication(this);
    }
}
