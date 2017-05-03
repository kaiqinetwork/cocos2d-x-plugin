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
package org.cocos2dx.plugin.meizu;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;
import com.meizu.gamesdk.online.model.model.MzBuyInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class IAPAdapter implements InterfaceIAP {

	private static final String LOG_TAG = "meizu.IAPAdapter";

	private static boolean mDebug;
	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;

	public IAPAdapter(Context context) {
		mActivity = (Activity) context;
		mInstance = this;
		mDebug = true;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		logD("configDeveloperInfo invoked " + devInfo.toString());
		final Hashtable<String, String> curDevInfo = devInfo;
		mDebug = PluginHelper.getDebugMode();
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
			int buyCount;
			if (payInfo.get("Buy_amount") != null) {
				buyCount = Integer.parseInt((String) payInfo.get("Buy_amount"));
			} else {
				buyCount = 1;
			}
			String cpUserInfo;
			if (payInfo.get("User_info") != null) {
				cpUserInfo = (String) payInfo.get("User_info");
			} else {
				cpUserInfo = "K7游戏中心";
			}
			String amount;
			if (payInfo.get("Total_price") != null) {
				amount = (String) payInfo.get("Total_price");
			} else {
				amount = "1";
			}
			String orderId;
			if (payInfo.get("Cp_order_id") != null) {
				orderId = (String) payInfo.get("Cp_order_id");
			} else {
				orderId = System.currentTimeMillis() + "";
			}
			String perPrice;
			if (payInfo.get("Product_per_price") != null) {
				perPrice = (String) payInfo.get("Product_per_price");
			} else {
				perPrice = "1";
			}
			String productBoby;
			if (payInfo.get("Product_body") != null) {
				productBoby = (String) payInfo.get("Product_body");
			} else {
				productBoby = "1";
			}
			String productId;
			if (payInfo.get("Product_id") != null) {
				productId = (String) payInfo.get("Product_id");
			} else {
				productId = "1";
			}
			String productSubject;
			if (payInfo.get("Product_subject") != null) {
				productSubject = (String) payInfo.get("Product_subject");
			} else {
				productSubject = "1";
			}
			String productUnit;
			if (payInfo.get("Product_unit") != null) {
				productUnit = (String) payInfo.get("Product_unit");
			} else {
				productUnit = "";
			}
			String sign;
			if (payInfo.get("Sign") != null) {
				sign = (String) payInfo.get("Sign");
			} else {
				sign = "1";
			}
			String signType;
			if (payInfo.get("Sign_type") != null) {
				signType = (String) payInfo.get("Sign_type");
			} else {
				signType = "md5";
			}
			long createTime;
			if (payInfo.get("Create_time") != null) {
				createTime = Long.parseLong((String) payInfo.get("Create_time"));
			} else {
				createTime = System.currentTimeMillis();
			}
			String appid;
			if (payInfo.get("App_id") != null) {
				appid = (String) payInfo.get("App_id");
			} else {
				appid = SDKWrapper.getInstance().getUserId();
			}
			String uid;
			if (payInfo.get("Uid") != null) {
				uid = (String) payInfo.get("Uid");
			} else {
				uid = SDKWrapper.getInstance().getUserId();
			}
			int payType;
			if (payInfo.get("Pay_type") != null) {
				payType = Integer.parseInt((String) payInfo.get("Pay_type"));
			} else {
				payType = 0;
			}
			Bundle buyBundle = new MzBuyInfo().setBuyCount(buyCount).setCpUserInfo(cpUserInfo).setOrderAmount(amount)
					.setOrderId(orderId).setPerPrice(perPrice).setProductBody(productBoby).setProductId(productId)
					.setProductSubject(productSubject).setProductUnit(productUnit).setSign(sign).setSignType(signType)
					.setCreateTime(createTime).setAppid(appid).setUserUid(uid).setPayType(payType).toBundle();
			
			MzGameCenterPlatform.payOnline(mActivity, buyBundle, new MzPayListener() {	
				@Override
				public void onPayResult(int code, Bundle info, String errorMsg) {
					switch (code) {
					case PayResultCode.PAY_SUCCESS:
						payResult(IAPWrapper.PAYRESULT_SUCCESS, "支付成功");
						break;
					case PayResultCode.PAY_ERROR_CANCEL:
						payResult(IAPWrapper.PAYRESULT_CANCEL, "支付取消");
						break;
					default:
						payResult(IAPWrapper.PAYRESULT_FAIL, "支付失败");
						break;
					}
				}
			});
			
		} catch (Exception e) {
			logE("payInSDK error", e);
			payResult(IAPWrapper.PAYRESULT_FAIL, "payInSDK error");
		}
	}

	@Override
	public void setDebugMode(boolean debug) {
		mDebug = debug;
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
		if (mDebug) {
			PluginHelper.logE(LOG_TAG, msg, e);
		}
	}

	protected static void logD(String msg) {
		if (mDebug) {
			PluginHelper.logD(LOG_TAG, msg);
		}
	}

	private void payResult(int ret, String msg) {
		logD("payResult: " + ret + " msg : " + msg);
		IAPWrapper.onPayResult(mInstance, ret, "");
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
