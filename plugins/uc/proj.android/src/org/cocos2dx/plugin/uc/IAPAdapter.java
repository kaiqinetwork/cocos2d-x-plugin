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
package org.cocos2dx.plugin.uc;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import android.app.Activity;
import android.content.Context;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;

public class IAPAdapter implements InterfaceIAP {

	private static final String LOG_TAG = "UC.IAPAdapter";

	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;

	public IAPAdapter(Context context) {
		mActivity = (Activity) context;
		mInstance = this;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo invoked " + devInfo.toString());
		final Hashtable<String, String> curDevInfo = devInfo;
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (!SDKWrapper.getInstance().initSDK(mActivity, curDevInfo, mInstance, new ILoginCallback() {
					public void onSuccessed(int code, String msg) {
						payResult(IAPWrapper.PAYRESULT_INIT_SUCCESS, msg);
					}

					public void onFailed(int code, String msg) {
						payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "initSDK failed! " + msg);
					}
				})) {
					payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "SDKWrapper.getInstance().initSDK return false");
				}
			}
		});
	}

	@Override
	public void payForProduct(Hashtable<String, String> info) {
		logD("payForProduct");
		final Hashtable<String, String> productInfo = info;
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (!SDKWrapper.getInstance().isInited()) {
					payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "inited fialed!");
				} else if (!PluginHelper.networkReachable(mActivity)) {
					payResult(IAPWrapper.PAYRESULT_TIMEOUT, "Network not available!");
				} else if (productInfo == null) {
					payResult(IAPWrapper.PAYRESULT_FAIL, "ProductInfo error!");
				} else if (SDKWrapper.getInstance().isLoggedIn()) {
					payInSDK(productInfo);
				} else {
					SDKWrapper.getInstance().userLogin(mActivity, new ILoginCallback() {
						@Override
						public void onSuccessed(int code, String msg) {
							payInSDK(productInfo);
						}

						@Override
						public void onFailed(int code, String msg) {
							payResult(IAPWrapper.PAYRESULT_FAIL, "LoginFailed");
						}
					});
				}
			}
		});
	}

	private void payInSDK(Hashtable<String, String> payInfo) {
		try {
			String callbackInfo;
			if (payInfo.get("CallbackInfo") != null) {
				callbackInfo = (String) payInfo.get("CallbackInfo");
			} else {
				callbackInfo = "K7游戏中心";
			}
			String amount;
			if (payInfo.get("Amount") != null) {
				amount = (String) payInfo.get("Amount");
			} else {
				amount = "1";
			}
			String notifyUrl;
			if (payInfo.get("NotifyUrl") != null) {
				notifyUrl = (String) payInfo.get("NotifyUrl");
			} else {
				notifyUrl = "1";
			}
			String cpOrderId;
			if (payInfo.get("CpOrderId") != null) {
				cpOrderId = (String) payInfo.get("CpOrderId");
			} else {
				cpOrderId = "1";
			}
			String accountId;
			if (payInfo.get("AccountId") != null) {
				accountId = (String) payInfo.get("AccountId");
			} else {
				accountId = "1";
			}
			String sign;
			if (payInfo.get("Sign") != null) {
				sign = (String) payInfo.get("Sign");
			} else {
				sign = "1";
			}

			SDKParams sdkParams = new SDKParams();
			sdkParams.put(SDKParamKey.CALLBACK_INFO, callbackInfo);
			sdkParams.put(SDKParamKey.NOTIFY_URL, notifyUrl);
			sdkParams.put(SDKParamKey.AMOUNT, amount);
			sdkParams.put(SDKParamKey.CP_ORDER_ID, cpOrderId);
			sdkParams.put(SDKParamKey.ACCOUNT_ID, accountId);
			sdkParams.put(SDKParamKey.SIGN_TYPE, "MD5");
			sdkParams.put(SDKParamKey.SIGN, sign);
			SDKWrapper.getInstance().pay(mActivity, new ILoginCallback() {
				@Override
				public void onSuccessed(int code, String msg) {
					payResult(code, msg);
				}

				@Override
				public void onFailed(int code, String msg) {
					payResult(code, msg);
				}
			}, sdkParams);

		} catch (Exception e) {
			logE("payInSDK error", e);
			payResult(IAPWrapper.PAYRESULT_FAIL, "payInSDK error");
		}
	}

	@Override
	public void setDebugMode(boolean debug) {
		logD("setDebugMode(" + debug + ") invoked! it is not used.");
	}

	@Override
	public String getSDKVersion() {
		return SDKWrapper.getInstance().getSDKVersion();
	}

	@Override
	public String getPluginVersion() {
		return SDKWrapper.getInstance().getPluginVersion();
	}

	@Override
	public String getPluginName() {
		return SDKWrapper.getInstance().getPluginName();
	}

	protected static void logE(String msg, Exception e) {
		PluginHelper.logE(LOG_TAG, msg, e);
	}

	protected static void logD(String msg) {
		PluginHelper.logD(LOG_TAG, msg);
	}

	private void payResult(int ret, String msg) {
		logD("payResult: " + ret + " msg : " + msg);
		IAPWrapper.onPayResult(mInstance, ret, msg);
	}

	@Override
	public boolean isSupportFunction(String funcName) {
		Method[] methods = IAPAdapter.class.getMethods();
		for (Method name : methods) {
			if (name.getName().equals(funcName)) {
				return true;
			}
		}
		return false;
	}

}
