package org.cocos2dx.plugin.txysdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.ePlatform;
import java.util.Hashtable;

import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.PluginHelper;

public class LoginChangeActivity extends Activity {
    private static final String LOG_TAG = "LoginChangeActivity";
    private ILoginCallback callback;
    final Hashtable<String, Boolean> isClick;

    public LoginChangeActivity() {
        this.callback = null;
        this.isClick = new Hashtable();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("plugin_activity_login_change", "layout", getPackageName()));
        Button btnWX = (Button) findViewById(getResources().getIdentifier("plugin_wx_btn", "id", getPackageName()));
        ((Button) findViewById(getResources().getIdentifier("plugin_qq_btn", "id", getPackageName()))).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                logD("login with qq");
                isClick.put("isClick", Boolean.valueOf(true));
                LoginChangeActivity.this.finish();
                YSDKApi.login(ePlatform.QQ);
            }
        });
        btnWX.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginChangeActivity.this.LogD("login with wx");
                LoginChangeActivity.this.isClick.put("isClick", Boolean.valueOf(true));
                LoginChangeActivity.this.finish();
                YSDKApi.login(ePlatform.WX);
            }
        });
        this.isClick.put("isClick", Boolean.valueOf(false));
        this.callback = TXYSDKWrapper.getInstance().getLoginCallback();
    }

    protected void onDestroy() {
        if (!(this.isClick.containsKey("isClick") && ((Boolean) this.isClick.get("isClick")).booleanValue())) {
            this.callback.onFailed(6, "cancel to change platform");
        }
        super.onDestroy();
    }

    protected void logE(String msg, Exception e) {
        if (e == null) {
            PluginHelper.logE(LOG_TAG, msg);
        } else {
            PluginHelper.logE(LOG_TAG, msg, e);
        }
    }

    protected void logD(String msg) {
        try {
            PluginHelper.logD(LOG_TAG, msg);
        } catch (Exception e) {
            logE("LogD error", e);
        }
    }
}