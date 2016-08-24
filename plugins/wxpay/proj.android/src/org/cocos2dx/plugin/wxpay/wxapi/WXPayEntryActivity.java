package org.cocos2dx.plugin.wxpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.wxpay.IAPAdapter;
import org.cocos2dx.plugin.wxpay.SDKWrapper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "wxpay.wxapi.WXPayEntryActivity";
    private IWXAPI api;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "App Id: " + SDKWrapper.getInstance().getAppId());
        this.api = WXAPIFactory.createWXAPI(this, SDKWrapper.getInstance().getAppId());
        this.api.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.api.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
    }

    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode=" + resp.errCode + ";openId=" + resp.openId + ";transaction=" + resp.transaction);
        Log.d(TAG, "onPayFinish, errStr=" + resp.errStr);
        if (resp.getType() == 5) {
            int result = resp.errCode;
            if (result == 0) {
                Log.d(TAG, "\u5fae\u4fe1\u652f\u4ed8\u6210\u529f");
                IAPAdapter.payResult(IAPWrapper.PAYRESULT_SUCCESS, "pay success");
            } else if (result == -1) {
                Log.d(TAG, "\u5fae\u4fe1\u652f\u4ed8\u5931\u8d25");
                IAPAdapter.payResult(IAPWrapper.PAYRESULT_FAIL, "pay fail");
            } else {
                Log.d(TAG, "\u5fae\u4fe1\u652f\u4ed8\u53d6\u6d88");
                IAPAdapter.payResult(IAPWrapper.PAYRESULT_CANCEL, "pay cancel");
            }
            finish();
        }
    }
}