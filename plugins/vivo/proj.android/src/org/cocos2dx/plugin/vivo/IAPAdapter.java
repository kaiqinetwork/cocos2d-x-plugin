package org.cocos2dx.plugin.vivo;

import java.util.Hashtable;
import java.lang.reflect.Method;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.vivo.sdkplugin.aidl.VivoUnionManager;
import com.bbk.payment.payment.OnVivoSinglePayResultListener;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "vivo.IAPAdapter";
	
	private static boolean mDebug = false;
	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;
	
	private VivoUnionManager mVivoUnionManager;
	private OnVivoSinglePayResultListener payResultListener;
	
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
		        } else {payInSDK(productInfo);
		        }
            }
		});
	}
	
	private void payInSDK(Hashtable<String, String> payInfo) {
		try {
			Double totalPrice = 0.00d;
            if (payInfo.get("OrderAmount") != null) {
            	totalPrice = Double.parseDouble((String) payInfo.get("OrderAmount"));
            }
            else {
            	totalPrice = Double.parseDouble((String) payInfo.get("product_price")) * ((float) Integer.parseInt((String) payInfo.get("product_count")));
            }
            
            String rechargeOrderId;
            if(payInfo.get("VivoOrder") != null){
            	rechargeOrderId = (String) payInfo.get("VivoOrder");
            }
            else{
            	rechargeOrderId = (String) SDKWrapper.getInstance().getUserId();
            }
            
            String vivoSignature;
            if(payInfo.get("VivoSignature") != null){
            	vivoSignature = (String) payInfo.get("VivoSignature");
            }
            else{
            	vivoSignature = (String) SDKWrapper.getInstance().getUserId();
            }
            
            String productName = (String) payInfo.get("product_name");
            String productDes = (String) payInfo.get("coin_name");
            String packageName = mActivity.getPackageName();
            logD(packageName);
            
            mVivoUnionManager = new VivoUnionManager((Context)mActivity);
            
            logD("transNo:"+ rechargeOrderId + "signature:"+vivoSignature+"package :"+ packageName+"productName :"+ productName+"productDes :"+ productDes+"price:"+ totalPrice);
            Bundle localBundle = new Bundle();
            localBundle.putString("transNo", rechargeOrderId);//订单推送接口返回的vivo订单号
            localBundle.putString("signature",vivoSignature);//订单推送接口返回的vivoSignature
            localBundle.putString("package", packageName);//在开发者平台创建应用时填写的包名，务必一致，否则SDK界面不会被唤起
            localBundle.putBoolean("useWeixinPay", false);// 如果游戏曾单独找商务申请了开通微信支付，那么这里传true，否则传false（无需担心，传false时安装“vivo安全服务插件”也可使用微信支付方式）
            localBundle.putString("useMode", "00");//固定值
            localBundle.putString("productName", productName); //商品名称
            localBundle.putString("productDes", productDes);//商品描述
            localBundle.putDouble("price", totalPrice);//商品价格，单位为元，币种：人民币，必须精确到小数点后两位，如：0.01，100.00
            localBundle.putString("userId", "test");//固定值
            //调用支付接口进行支付
            mVivoUnionManager.singlePayment ((Context)mActivity, localBundle);

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
	
	public void payResult(int ret, String msg) {
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
