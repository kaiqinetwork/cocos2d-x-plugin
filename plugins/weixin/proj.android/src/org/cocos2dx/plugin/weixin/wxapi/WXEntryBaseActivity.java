package org.cocos2dx.plugin.weixin.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.cocos2dx.plugin.ShareWrapper;
import org.cocos2dx.plugin.UserWrapper;
import org.cocos2dx.plugin.weixin.SDKWrapper;
import org.cocos2dx.plugin.weixin.ShareAdapter;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.wxop.stat.StatAccount;

public class WXEntryBaseActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "weixin.wxapi.WXEntryActivity";
    private static final int TIMELINE_SUPPORTED_VERSION = 553779201;
    private IWXAPI mApi;
    BaseResp mResp;

    public WXEntryBaseActivity() {
        mResp = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "App Id: " + SDKWrapper.getInstance().getAppId());
        mApi = WXAPIFactory.createWXAPI(this, SDKWrapper.getInstance().getAppId(), false);
        mApi.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
        switch (req.getType()) {
        }
    }

    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case ErrCode.ERR_USER_CANCEL /*-2*/:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                	SDKWrapper.getInstance().loginResult(UserWrapper.ACTION_RET_LOGIN_CANCEL, "");
                }
                else {
                	ShareAdapter.shareResult(ShareWrapper.SHARERESULT_CANCEL, "share cancel");
                }
                Log.d("BaseResp.ERR_USER_CANCEL:", "\u53d6\u6d88");
                break;
            case StatAccount.DEFAULT_TYPE /*0*/:
                if (!(resp instanceof Resp)) {
                	ShareAdapter.shareResult(ShareWrapper.SHARERESULT_SUCCESS, "share success");
                    break;
                }
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                	SDKWrapper.getInstance().loginResult(UserWrapper.ACTION_RET_LOGIN_SUCCESS, ((Resp) resp).code);
                }
                else {
                	ShareAdapter.shareResult(ShareWrapper.SHARERESULT_SUCCESS, "share success");
                }
                Log.d("BaseResp.ErrCode.ERR_OK:", "\u6210\u529f");
                break;
            default:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                	SDKWrapper.getInstance().loginResult(UserWrapper.ACTION_RET_LOGIN_FAIL, "");
                }
                else {
                	ShareAdapter.shareResult(ShareWrapper.SHARERESULT_FAIL, "share success");
                }
                Log.d("BaseResp.ERR_AUTH_DENIED:", "\u5931\u8d25");
                break;
        }
        finish();
    }
}