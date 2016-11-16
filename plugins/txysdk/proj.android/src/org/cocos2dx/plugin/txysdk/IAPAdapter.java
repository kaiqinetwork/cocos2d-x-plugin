package org.cocos2dx.plugin.txysdk;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.txysdk.SDKWrapper;

import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;


public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "txysdk.IAPAdapter";
	
	private static boolean mDebug = false;
	private static IAPAdapter mInstance = null;
	private Activity mActivity = null;
	private boolean mPaySuccess = false;
	private String mCallBackUrl = "";
	private String mRechargeOrderNo = "";
	
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
                    payResult(IAPWrapper.PAYRESULT_FAIL, "init fail");
                } else if (!PluginHelper.networkReachable(mActivity)) {
                    payResult(IAPWrapper.PAYRESULT_TIMEOUT, "Network not available!");
                } else if (SDKWrapper.getInstance().isLoggedIn()) {
                	payInSDK(productInfo);
                } else {
                    SDKWrapper.getInstance().userLogin(new ILoginCallback() {
                        public void onFailed(int code, String msg) {
                            payResult(IAPWrapper.PAYRESULT_FAIL, "login fail,msg:" + msg);
                        }

                        public void onSuccessed(int code, String msg) {
                        	payInSDK(productInfo);
                        }
                    });
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
	
	public String getPlatform(){
		return SDKWrapper.getInstance().getPlatform(); 
	}
	
	public String getOrderInfo() throws UnsupportedEncodingException{
		UserLoginRet ret = new UserLoginRet();
		YSDKApi.getLoginRecord(ret);
		if (ret.flag != 0) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pf" , ret.pf);
			jsonObject.put("pfkey", ret.pf_key);
			jsonObject.put("openid", ret.open_id);
			jsonObject.put("zoneid", "1");
			if(ret.platform == 1){
				jsonObject.put("openkey", ret.getPayToken());
			}else{
				jsonObject.put("openkey", ret.getAccessToken());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String jsonStr = jsonObject.toString();
		String encodeStr = null;
		if(jsonStr != null){
			encodeStr = Base64.encodeToString(jsonStr.getBytes(), Base64.DEFAULT);
		}
		return URLEncoder.encode(encodeStr, "UTF-8");
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
	
	private void payInSDK(Hashtable<String, String> info){
		final Hashtable<String, String> productInfo = info;
		mRechargeOrderNo = (String) productInfo.get("RechargeOrderNo");
		float amount = Float.parseFloat((String) productInfo.get("RechargeAmount"));
		mCallBackUrl = (String) productInfo.get("CallbackUrl");
		boolean IsEnough = Boolean.valueOf(productInfo.get("IsEnough")).booleanValue();
		
		if(amount != 0.0f && !IsEnough){
			payInSDK(amount);
		}
		else
			payForProduct(mCallBackUrl, mRechargeOrderNo);
	}
	
	private boolean payInSDK(float count){
		logD("payInSDK(" + count + ") invoked");
		Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), mActivity.getResources().getIdentifier("game_coin_icon", "drawable", mActivity.getPackageName()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, baos);
        String ysdkExt = "ysdkExt";
        UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        logD(ret.toString());
        if (ret.flag == 0) {
        	try{
        		YSDKApi.recharge("1", String.valueOf(count), SDKWrapper.getInstance().isChangeMoney(), baos.toByteArray(), ysdkExt, new PayListener(){
        			  public void OnPayNotify(PayRet ret) {
        				  logD(ret.toString());
        				  if (ret.ret == 0) {
                              int resultCode = ret.ret;
                              int payChannel = ret.payChannel;
                              int payState = ret.payState;
                              int provideState = ret.provideState;
                              int saveNum = ret.realSaveNum;
                              String resultMsg = ret.msg;
                              String extendInfo = ret.extendInfo;
                              logD("UnipayCallBack:resultCode=" + resultCode + ",payChannel=" + payChannel + ",payState=" + payState + ",provideState=" + provideState + ",saveNum=" + saveNum + ",resultMsg=" + resultMsg + ",extendInfo=" + extendInfo);
                              switch (ret.payState) {
                              case PayRet.PAYSTATE_PAYSUCC:
                            	  mPaySuccess = true;
                            	  payForProduct(mCallBackUrl, mRechargeOrderNo);
                            	  break;
                              case PayRet.PAYSTATE_PAYCANCEL:
                            	  mPaySuccess = false;
                            	  payResult(IAPWrapper.PAYRESULT_CANCEL, "payState:" + payState + " Msg:" + resultMsg);
                            	  break;
                              case PayRet.PAYSTATE_PAYUNKOWN:
                            	  mPaySuccess = false;
                            	  payResult(IAPWrapper.PAYRESULT_FAIL, "payState:" + payState + " Msg:" + resultMsg);
                            	  break;
                              case PayRet.PAYSTATE_PAYERROR:
                            	  mPaySuccess = false;
                            	  payResult(IAPWrapper.PAYRESULT_FAIL, "payState:" + payState + " Msg:" + resultMsg);
                            	  break;
                              default:
                            	  mPaySuccess = false;
                            	  payResult(IAPWrapper.PAYRESULT_FAIL, "payState:" + payState + " Msg:" + resultMsg);
                            	  break;
                          }
                      }else{
                      switch (ret.flag) {
                      case eFlag.Login_TokenInvalid/*3100*/:
                    	  mPaySuccess = false;
                    	  logD("\u767b\u9646\u6001\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u9646\uff1a" + ret.toString());
                    	  payResult(IAPWrapper.PAYRESULT_FAIL, " Msg:" + ret.toString());
                    	  break;
                      case eFlag.Pay_User_Cancle /*4001*/:
                    	  mPaySuccess = false;
                    	  logD("\u7528\u6237\u53d6\u6d88\u652f\u4ed8\uff1a" + ret.toString());
                    	  payResult(IAPWrapper.PAYRESULT_CANCEL, " Msg:" + ret.toString());
                    	  break;
                      case eFlag.Pay_Param_Error /*4002*/:
                    	  mPaySuccess = false;
                    	  logD("\u652f\u4ed8\u5931\u8d25\uff0c\u53c2\u6570\u9519\u8bef" + ret.toString());
                    	  payResult(IAPWrapper.PAYRESULT_FAIL, " Msg:" + ret.toString());
                    	  break;
                      default:
                    	  logD("\u652f\u4ed8\u5f02\u5e38" + ret.toString());
                    	  mPaySuccess = false;
                    	  payResult(IAPWrapper.PAYRESULT_FAIL, " Msg:" + ret.toString());
                    	  break;  
                      }
        			}
        			  }
        		});
        	} catch (Exception e) {
                logE("payInSDK  error", e);
                mPaySuccess = false;
                payResult(IAPWrapper.PAYRESULT_FAIL, "payInSDK  error");
        	}
        }
        else {
            payResult(IAPWrapper.PAYRESULT_FAIL, "get LoginRet fail");
        }
        logD("pay finish");
        return true;
	}
	
	private void payForProduct(String callBackUrl, String rechargeOrderNo){
		try {
			OkHttpClient client = new OkHttpClient();
			UserLoginRet ret = new UserLoginRet();
			YSDKApi.getLoginRecord(ret);
			logD(ret.toString());
			RequestBody formBody = new FormBody.Builder().add("RechargeOrderNo", rechargeOrderNo)
                 .add("pf", ret.pf).add("pfkey", ret.pf_key)
                 .add("openid", ret.open_id).add("openkey", ret.getPayToken())
                 .add("zoneid", "1")
                 .build();
			logD("form body:" + formBody.toString());
			Request request = new Request.Builder().url(callBackUrl).post(formBody).build();
			client.newCall(request).enqueue(new Callback() {
				@Override
			 	public void onFailure(Call call, IOException e) {
					payResult(IAPWrapper.PAYRESULT_FAIL, " Msg: Requset Fail");
				}
				@Override
			 	public void onResponse(Call call, Response response) throws IOException {
					logD("Pay Result: " + response.body().toString());
					payResult(IAPWrapper.PAYRESULT_SUCCESS, " Msg: success");
				}
			});
		} catch (Exception e) {
            e.printStackTrace();
        }
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
