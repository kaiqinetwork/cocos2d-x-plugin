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
package org.cocos2dx.plugin.weixin;

import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;

public class IAPAdapter implements InterfaceIAP {

	private static final String LOG_TAG = "weixin.IAPAdapter";
	
    private static boolean mDebug = false;
	private static IAPAdapter mInstance = null;
	private Context mContext = null;
	
	protected static void logE(String msg, Exception e) {
		PluginHelper.logE(LOG_TAG, msg, e);
	}

	protected static void logD(String msg) {
		PluginHelper.logD(LOG_TAG, msg);
	}

	public IAPAdapter(Context context) {
		mContext = (Activity) context;
		mInstance = this;
		mDebug = false;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}

	@Override
	public void configDeveloperInfo(final Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo invoked " + devInfo.toString());
		PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	if (!SDKWrapper.getInstance().initSDK(mContext, devInfo, mInstance, new ILoginCallback() {
                    public void onFailed(int code, String msg) {
                        payResult(IAPWrapper.PAYRESULT_INIT_FAIL, msg);
                    }

                    public void onSuccessed(int code, String msg) {
                        payResult(IAPWrapper.PAYRESULT_INIT_SUCCESS, msg);
                    }
                })) {
                    payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "initSDK false");
                }
            }
        });
	}

	@Override
	public void payForProduct(Hashtable<String, String> info) {
		logD("payForProduct invoked " + info.toString());
		final Hashtable<String, String> productInfo = info;
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				if (!SDKWrapper.getInstance().isInited()) {
	                payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "init fail");
	            } else if (!PluginHelper.networkReachable(mContext)) {
	                payResult(IAPWrapper.PAYRESULT_TIMEOUT, "Network not available!");
	            } else if (isWXAppInstalledAndSupported()) {
                    PayReq req = new PayReq();
                    req.appId = productInfo.get("app_id");
                    req.partnerId = productInfo.get("partner_id");
                    req.prepayId = productInfo.get("prepay_id");
                    req.nonceStr = productInfo.get("nonce_str");
                    req.timeStamp = productInfo.get("timestamp");
                    req.packageValue = productInfo.get("package");
                    req.sign = productInfo.get("sign");
                    SDKWrapper.getInstance().getApi().sendReq(req);
	            } else {
	                payResult(IAPWrapper.PAYRESULT_FAIL, "Wechat client has not installed");
	            }
			}
		});
	}
	
	private boolean isWXAppInstalledAndSupported() {
		IWXAPI api = SDKWrapper.getInstance().getApi();
        return api.isWXAppInstalled() && api.isWXAppSupportAPI();
    }

	@Override
	public void setDebugMode(boolean debug) {
		mDebug = debug;
	}

	@Override
	public String getSDKVersion() {
		return SDKWrapper.getInstance().getSDKVersion();
	}

	public static void payResult(int ret, String msg) {
		logD("Pay result : " + ret + " msg : " + msg);
		IAPWrapper.onPayResult(mInstance, ret, msg);		
	}

	@Override
	public String getPluginVersion() {
		return SDKWrapper.getInstance().getPluginVersion();
	}
	
	@Override
	public String getPluginName() {
		return SDKWrapper.getInstance().getPluginName();
	}
}
