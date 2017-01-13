/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
 */
package org.cocos2dx.plugin.huawei;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.huawei.gamebox.buoy.sdk.util.BuoyConstant;
import com.huawei.gamebox.buoy.sdk.util.DebugConfig;

/**
 * 接收切换帐号的广播，这里提供的是静态注册的方式， 开发者可根据自己的实际需求是否能满足决定是否要把注册方式改为动态注册
 * 要求：要保证能接收到SDK发出的切换帐号的广播和跳转到游戏首页
 * 
 * @author c00206870
 * @version [版本号, 2014-6-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ChangeUserReceiver extends BroadcastReceiver
{
    private static final String TAG = ChangeUserReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        DebugConfig.d(TAG, "onReceive action=" + action);
        if (BuoyConstant.CHANGE_USER_LOGIN_ACTION.equals(action))
        {
            Bundle bundle = intent.getBundleExtra(BuoyConstant.GAMEBOX_EXTRA_DATA);
            int value = 0;
            if(null != bundle)
            {
                value = bundle.getInt(BuoyConstant.KEY_GAMEBOX_CHANGEUSERLOGIN);
            }
            DebugConfig.d(TAG, "onReceive value=" + value);
            if (BuoyConstant.VALUE_CHANGE_USER == value)
            {
            	SDKWrapper.getInstance().switchAccount();
            }
        }
    }
    
}
