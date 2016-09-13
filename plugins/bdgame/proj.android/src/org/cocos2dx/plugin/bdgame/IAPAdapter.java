package org.cocos2dx.plugin.bdgame;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.ResultCode;
import com.baidu.platformsdk.PayOrderInfo;

import android.app.Activity;
import android.content.Context;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "bdgame.IAPAdapter";
	
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
            String productName = (String) payInfo.get("product_name");
            String orderNo = (String) payInfo.get("RechargeOrderNo");
            float totalPrice = 0.0f;
            if (payInfo.get("TotalPrice") != null) {
            	totalPrice = Float.parseFloat((String) payInfo.get("TotalPrice"));
            }
            else {
            	totalPrice = Float.parseFloat((String) payInfo.get("product_price")) * ((float) Integer.parseInt((String) payInfo.get("product_count")));
            }
            String extInfo = (String) payInfo.get("ExtInfo");

            PayOrderInfo payOrderInfo = new PayOrderInfo();
            payOrderInfo.setCooperatorOrderSerial(orderNo);
            payOrderInfo.setProductName(productName);
            payOrderInfo.setTotalPriceCent((long) (100.0f * totalPrice));
            payOrderInfo.setExtInfo(extInfo);
            BDGameSDK.pay(payOrderInfo, SDKWrapper.getInstance().getIAPDebugCallbackUrl(), new IResponse<PayOrderInfo>() {
                public void onResponse(int resultCode, String resultDesc, PayOrderInfo extraData) {
                    switch (resultCode) {
                        case ResultCode.PAY_SUBMIT_ORDER /*-32*/:
                            payResult(IAPWrapper.PAYRESULT_SUCCESS, resultDesc);
                            break;
                        case ResultCode.PAY_FAIL /*-31*/:
                            payResult(IAPWrapper.PAYRESULT_FAIL, resultDesc);
                            break;
                        case ResultCode.PAY_CANCEL /*-30*/:
                            payResult(IAPWrapper.PAYRESULT_CANCEL, resultDesc);
                            break;
                        case ResultCode.PAY_SUCCESS /*0*/:
                            payResult(IAPWrapper.PAYRESULT_SUCCESS, resultDesc);
                            break;
                        default:
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
