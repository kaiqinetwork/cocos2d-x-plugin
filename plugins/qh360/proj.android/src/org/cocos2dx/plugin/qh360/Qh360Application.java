package org.cocos2dx.plugin.qh360;

import com.qihoo.gamecenter.sdk.matrix.Matrix;

import android.app.Application;

public class Qh360Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 此处必须先初始化360SDK
        Matrix.initInApplication(this);
    }
}
