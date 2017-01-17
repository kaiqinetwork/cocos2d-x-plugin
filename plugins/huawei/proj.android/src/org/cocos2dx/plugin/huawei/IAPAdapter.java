package org.cocos2dx.plugin.huawei;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.android.huawei.pay.plugin.IHuaweiPay;
import com.android.huawei.pay.plugin.IPayHandler;
import com.android.huawei.pay.plugin.MobileSecurePayHelper;
import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;

import android.app.Activity;
import android.content.Context;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "huawei.IAPAdapter";
	
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
			String userName;
			if(payInfo.get("UserName") != null){
				userName = (String) payInfo.get("UserName");
			}
			else {
				userName = (String) payInfo.get("product_name");
			}
			
			String requestId;
			if(payInfo.get("RequestId") != null){
				requestId = (String) payInfo.get("RequestId");
			}
			else {
				requestId = (String) payInfo.get("product_id");
			}
			
			String amount ;
            if (payInfo.get("Amount") != null) {
            	amount = (String) payInfo.get("Amount");
            }
            else {
            	amount = String.valueOf(Float.parseFloat((String) payInfo.get("product_price")) * ((float) Integer.parseInt((String) payInfo.get("product_count"))));
            }
            
            String productName;
            if(payInfo.get("ProductName") != null){
            	productName = (String) payInfo.get("ProductName");
            }
            else{
            	productName = (String) payInfo.get("product_name");
            }
            
            String productDesc;
            if(payInfo.get("ProductDesc") != null){
            	productDesc = (String) payInfo.get("ProductDesc");
            }
            else{
            	productDesc = (String) payInfo.get("product_name");
            }
            
            String sign = "";
            if(payInfo.get("Sign") != null){
            	sign = (String) payInfo.get("Sign");
            }
            else {
            	Map<String, String> siginparams = new HashMap<String, String>();
            	siginparams.put("userID", SDKWrapper.getInstance().getPayID());
            	siginparams.put("applicationID", SDKWrapper.getInstance().getAppID());
            	siginparams.put("amount", amount);
            	siginparams.put("productName", productName);
            	siginparams.put("productDesc", productDesc);
            	siginparams.put("requestId", requestId);

                String noSign = HuaweiPayUtil.getSignData(siginparams);
            	String privateKey = "";
            	sign = Rsa.sign(noSign, privateKey);
            	logD("noSign: " + noSign);
            	logD("Sign:" + sign);
            	if (Rsa.doCheck(noSign, sign, ""))
            	{
            		logD("check success");
            	}
            }
            
            final Map<String, Object> params = new HashMap<String, Object>();
           	params.put("userName", userName);
           	params.put("userID", SDKWrapper.getInstance().getPayID());
           	params.put("applicationID", SDKWrapper.getInstance().getAppID());
           	params.put("amount", amount);
           	params.put("productName", productName);
           	params.put("requestId", requestId);
            params.put("productDesc", productDesc);
            params.put("sign", sign);
            params.put("serviceCatalog", "X6");
            //params.put("showLog", true);
            
            PluginWrapper.runOnMainThread(new Runnable() {
                public void run() {
                	IHuaweiPay payHelper = new MobileSecurePayHelper();
                    payHelper.startPay(mActivity, params, new IPayHandler(){

						@Override
						public void onFinish(Map<String, String> payResp) {

				            logD("pay finish, payResp= " + payResp);
				            logD("pay finish£¬returnCode=" + payResp.get(PayParameters.returnCode));
				            if ("0".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_SUCCESS, "pay success");
				            }
				            else if ("100001".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_CANCEL, "install pay server");
				            }
				            else if ("100002".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_CANCEL, "user cancel");
				            }
				            else if ("200001".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_CANCEL, "user cancel");
				            }
				            else if ("30000".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_CANCEL, "pay cancel");
				            }
				            else if ("30002".equals(payResp.get(PayParameters.returnCode)))
				            {
				            	payResult(IAPWrapper.PAYRESULT_TIMEOUT, "pay time out");
				            }
				            else {
				            	payResult(IAPWrapper.PAYRESULT_FAIL, "pay failed");
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
	
	public String getOrderInfo(){
		return SDKWrapper.getInstance().getAppID();
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
