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
package org.cocos2dx.plugin.qh360;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

public class IAPAdapter implements InterfaceIAP {

	private static final String LOG_TAG = "QH360.IAPAdapter";

	@SuppressWarnings("unused")
	private static boolean mDebug = false;
	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;
	protected static boolean isAccessTokenValid = true;
	protected static boolean isQTValid = true;

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
				} else if (SDKWrapper.getInstance().isLoggedIn() && isAccessTokenValid && isQTValid) {
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
			String qhUserId;
			if (payInfo.get("QihooUid") != null) {
				qhUserId = (String) payInfo.get("QihooUid");
			} else {
				qhUserId = "1";
			}
			String amount;
			if (payInfo.get("Amount") != null) {
				amount = (String) payInfo.get("Amount");
			} else {
				amount = "200";
			}
			String productName;
			if (payInfo.get("product_name") != null) {
				productName = (String) payInfo.get("product_name");
			} else {
				productName = "product_name";
			}
			String productId;
			if (payInfo.get("productId") != null) {
				productId = (String) payInfo.get("productId");
			} else {
				productId = "1";
			}
			String notifyUri;
			if (payInfo.get("NotifyUrl") != null) {
				notifyUri = (String) payInfo.get("NotifyUrl");
			} else {
				notifyUri = "http://api.qipai007.com/Recharge/QH360/Callback";
			}
			String appName = "K7游戏中心";
			String appUserName;
			if (payInfo.get("role_name") != null) {
				appUserName = (String) payInfo.get("role_name");
			} else {
				appUserName = "1";
			}
			String appUserId;
			if (payInfo.get("role_id") != null) {
				appUserId = (String) payInfo.get("role_id");
			} else {
				appUserId = "1";
			}
			String appOrderId;
			if (payInfo.get("AppOrderId") != null) {
				appOrderId = (String) payInfo.get("AppOrderId");
			} else {
				appOrderId = SystemClock.currentThreadTimeMillis() + "";
			}
			int productCount;
			if (payInfo.get("product_count") != null) {
				productCount = Integer.parseInt((String) payInfo.get("product_count"));
			} else {
				productCount = 1;
			}
			String serverId;
			if (payInfo.get("server_id") != null) {
				serverId = (String) payInfo.get("server_id");
			} else {
				serverId = "1";
			}
			String serverName = "1";
			int exchangeRate;
			if (payInfo.get("coin_rate") != null) {
				exchangeRate = Integer.parseInt((String) payInfo.get("coin_rate"));
			} else {
				exchangeRate = 10000;
			}
			String gameMoneyName;
			if (payInfo.get("coin_name") != null) {
				gameMoneyName = (String) payInfo.get("coin_name");
			} else {
				gameMoneyName = "金蛋";
			}
			String roleId;
			if (payInfo.get("role_id") != null) {
				roleId = (String) payInfo.get("role_id");
			} else {
				roleId = "1";
			}
			String roleName;
			if (payInfo.get("role_name") != null) {
				roleName = (String) payInfo.get("role_name");
			} else {
				roleName = "1";
			}
			int roleGrade;
			if (payInfo.get("role_grade") != null) {
				roleGrade = Integer.parseInt((String) payInfo.get("role_grade"));
			} else {
				roleGrade = 1;
			}
			int roleBalance;
			if (payInfo.get("role_balance") != null) {
				roleBalance = Integer.parseInt((String) payInfo.get("role_balance"));
			} else {
				roleBalance = 0;
			}
			String roleVip = "1";
			String roleUserparty = "1";
			
			Bundle bundle = new Bundle();
			bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);
			bundle.putString(ProtocolKeys.QIHOO_USER_ID, qhUserId);
			bundle.putString(ProtocolKeys.AMOUNT, amount + "");
			bundle.putString(ProtocolKeys.PRODUCT_NAME, productName);
			bundle.putString(ProtocolKeys.PRODUCT_ID, productId);
			bundle.putString(ProtocolKeys.NOTIFY_URI, notifyUri);
			bundle.putString(ProtocolKeys.APP_NAME, appName);
			bundle.putString(ProtocolKeys.APP_USER_NAME, appUserName);
			bundle.putString(ProtocolKeys.APP_USER_ID, appUserId);
			bundle.putString(ProtocolKeys.APP_ORDER_ID, appOrderId);
			bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);
			bundle.putInt(ProtocolKeys.PRODUCT_COUNT, productCount);
			bundle.putString(ProtocolKeys.SERVER_ID, serverId);
			bundle.putString(ProtocolKeys.SERVER_NAME, serverName);
			bundle.putInt(ProtocolKeys.EXCHANGE_RATE, exchangeRate);
			bundle.putString(ProtocolKeys.GAMEMONEY_NAME, gameMoneyName);
			bundle.putString(ProtocolKeys.ROLE_ID, roleId);
			bundle.putString(ProtocolKeys.ROLE_NAME, roleName);
			bundle.putInt(ProtocolKeys.ROLE_GRADE, roleGrade);
			bundle.putInt(ProtocolKeys.ROLE_BALANCE, roleBalance);
			bundle.putString(ProtocolKeys.ROLE_VIP, roleVip);
			bundle.putString(ProtocolKeys.ROLE_USERPARTY, roleUserparty);

			Intent intent = new Intent(mActivity, ContainerActivity.class);
			intent.putExtras(bundle);

			Matrix.invokeActivity(mActivity, intent, new IDispatcherCallback() {
				@Override
				public void onFinished(String data) {
					if (TextUtils.isEmpty(data)) {
						payResult(IAPWrapper.PAYRESULT_FAIL, "IDispatcherCallback data is empty");
						return;
					}

					boolean isCallbackParseOk = false;
					JSONObject jsonRes;
					try {
						jsonRes = new JSONObject(data);
						int errorCode = jsonRes.optInt("error_code");
						String errorMsg = jsonRes.optString("error_msg");
						isCallbackParseOk = true;
						switch (errorCode) {
						case 0:           //支付成功
							isAccessTokenValid = true;
							isQTValid = true;
							payResult(IAPWrapper.PAYRESULT_SUCCESS, errorMsg);
							break;
						case 1:			 //支付取消
							isAccessTokenValid = true;
							isQTValid = true;
							payResult(IAPWrapper.PAYRESULT_FAIL, errorMsg);
							break;
						case -1:            //1支付失败
							isAccessTokenValid = true;
							isQTValid = true;
							payResult(IAPWrapper.PAYRESULT_CANCEL, errorMsg);
							break;
						case -2:            //支付进行中
							isAccessTokenValid = true;
							isQTValid = true;
							payResult(IAPWrapper.PAYRESULT_FAIL, errorMsg);
							break;
						case 4010201:      //登录状态已失效，引导用户重新登录
							isAccessTokenValid = false;
							payResult(IAPWrapper.PAYRESULT_FAIL, errorMsg);
							break;
						case 4009911:         //登录状态已失效，引导用户重新登录
							isQTValid = false;
							payResult(IAPWrapper.PAYRESULT_FAIL, errorMsg);
							break;
						default:
							payResult(IAPWrapper.PAYRESULT_FAIL, errorMsg);
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (!isCallbackParseOk) {
						payResult(IAPWrapper.PAYRESULT_FAIL, "data is not ok");
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
		PluginHelper.logE(LOG_TAG, msg, e);
	}

	protected static void logD(String msg) {
		PluginHelper.logD(LOG_TAG, msg);
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
