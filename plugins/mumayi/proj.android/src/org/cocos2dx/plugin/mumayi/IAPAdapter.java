package org.cocos2dx.plugin.mumayi;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.UserWrapper;

import android.app.Activity;
import android.content.Context;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "mumayi.IAPAdapter";

	private static boolean mDebug = false;
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
				} else {
					payInSDK(productInfo);
				}
			}
		});
	}

	private void payInSDK(Hashtable<String, String> payInfo) {
		final String productName;
		final String productPrice;
		final String productDesc;

		if (payInfo.get("ProductName") != null) {
			productName = (String) payInfo.get("ProductName");
		} else {
			productName = "prodectName";
		}

		if (payInfo.get("ProductPrice") != null) {
			productPrice = payInfo.get("ProductPrice");
		} else {
			productPrice = "1.00";
		}

		if (payInfo.get("ProductDesc") != null) {
			productDesc = payInfo.get("ProductDesc");
		} else {
			productDesc = "prodectDesc";
		}

		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				if (SDKWrapper.getInstance().isInited()) {
					SDKWrapper.getInstance().pay(mActivity, productName,productPrice,productDesc,new ILoginCallback() {
						@Override
						public void onSuccessed(int code, String msg) {
							logD("pay success");
							payResult(IAPWrapper.PAYRESULT_SUCCESS, msg);
						}

						@Override
						public void onFailed(int code, String msg) {
							logD("pay failed");
							payResult(IAPWrapper.PAYRESULT_FAIL, msg);
						}
					});
				} else {
					logD("pay failed");
					payResult(IAPWrapper.PAYRESULT_INIT_FAIL, "initSDK fail!");
				}
			}
		});
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
