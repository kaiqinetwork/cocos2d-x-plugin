package org.cocos2dx.plugin.bdgamesingle;

import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONObject;

import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.DkErrorCode;
import com.duoku.platform.single.DkProtocolKeys;
import com.duoku.platform.single.callback.IDKSDKCallBack;
import com.duoku.platform.single.item.GamePropsInfo;

import android.app.Activity;
import android.content.Context;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "bdgamesingle.IAPAdapter";
	
	private static boolean mDebug = false;
	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;
	
	public IAPAdapter(Context context) {
		mActivity = (Activity)context;
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
		                public void onSuccessed(int code, String msg) {
		                	payInSDK(productInfo);
		                }
		
		                public void onFailed(int code, String msg) {
		                    payResult(IAPWrapper.PAYRESULT_FAIL, "Login failed");
		                }
		            });
		        }
            }
		});
	}
	
	private void payInSDK(Hashtable<String, String> payInfo) {
		try {
            final GamePropsInfo info = new GamePropsInfo(payInfo.get("product_dd"), 
            		String.valueOf(Float.parseFloat(payInfo.get("product_price")) * ((float) Integer.parseInt((String) payInfo.get("product_count")))), 
            		payInfo.get("product_name"), 
            		SDKWrapper.getInstance().getUserId());
            info.setThirdPay("qpfangshua");
            logD("pay params:" + info.toString());
            PluginWrapper.runOnMainThread(new Runnable() {
                public void run() {
                	DKPlatform.getInstance().invokePayCenterActivity((Context)mActivity, info, null, null, null, null, new IDKSDKCallBack() {
                        public void onResponse(String paramString) {
                            logD("invokePayCenterActivity onResponse : " + paramString);
                            try {
                                JSONObject jsonObject = new JSONObject(paramString);
                                int statusCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_STATUS_CODE);
                                if (statusCode == DkErrorCode.BDG_RECHARGE_SUCCESS) {
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_STATUS)) {
                                        String orderStatus = jsonObject.getString(DkProtocolKeys.BD_ORDER_STATUS);
                                    }
                                    String orderId = null;
                                    String orderProductId = null;
                                    String orderPrice = null;
                                    String orderPayChannel = null;
                                    String orderPriceOriginal = null;
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_ID)) {
                                        orderId = jsonObject.getString(DkProtocolKeys.BD_ORDER_ID);
                                    }
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_PRODUCT_ID)) {
                                        orderProductId = jsonObject.getString(DkProtocolKeys.BD_ORDER_PRODUCT_ID);
                                    }
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_PRICE)) {
                                        orderPrice = jsonObject.getString(DkProtocolKeys.BD_ORDER_PRICE);
                                    }
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_PAY_CHANNEL)) {
                                        orderPayChannel = jsonObject.getString(DkProtocolKeys.BD_ORDER_PAY_CHANNEL);
                                    }
                                    if (jsonObject.has(DkProtocolKeys.BD_ORDER_PAY_ORIGINAL)) {
                                        orderPriceOriginal = jsonObject.getString(DkProtocolKeys.BD_ORDER_PAY_ORIGINAL);
                                    }
                                    logD("OrderId=" + orderId + "OrderProductId=" + orderProductId + "OrderPrice=" + orderPrice + "OrderPayChannel=" + orderPayChannel + "OrderPriceOriginal=" + orderPriceOriginal);
                                    payResult(IAPWrapper.PAYRESULT_SUCCESS, paramString);
                                } else if (statusCode == DkErrorCode.BDG_RECHARGE_USRERDATA_ERROR) {
                                    payResult(IAPWrapper.PAYRESULT_FAIL, "\u7528\u6237\u900f\u4f20\u6570\u636e\u4e0d\u5408\u6cd5");
                                } else if (statusCode == DkErrorCode.BDG_RECHARGE_ACTIVITY_CLOSED) {
                                    payResult(IAPWrapper.PAYRESULT_CANCEL, "\u73a9\u5bb6\u5173\u95ed\u652f\u4ed8\u4e2d\u5fc3");
                                } else if (statusCode == DkErrorCode.BDG_RECHARGE_FAIL) {
                                	payResult(IAPWrapper.PAYRESULT_FAIL, "\u8d2d\u4e70\u5931\u8d25");
                                } else if (statusCode == DkErrorCode.BDG_RECHARGE_EXCEPTION) {
                                	payResult(IAPWrapper.PAYRESULT_FAIL, "\u8d2d\u4e70\u51fa\u73b0\u5f02\u5e38");
                                } else if (statusCode == DkErrorCode.BDG_RECHARGE_CANCEL) {
                                    payResult(IAPWrapper.PAYRESULT_CANCEL, "\u73a9\u5bb6\u53d6\u6d88\u652f\u4ed8");
                                } else {
                                	payResult(IAPWrapper.PAYRESULT_FAIL, "\u672a\u77e5\u60c5\u51b5");
                                }
                            } catch (Exception e) {
                                logE("invokePayCenterActivity onResponse error", e);
                                payResult(IAPWrapper.PAYRESULT_FAIL, "\u5f02\u5e38");
                            }
                        }
                    });

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
		IAPWrapper.onPayResult(mInstance, ret, msg);
    }

}
