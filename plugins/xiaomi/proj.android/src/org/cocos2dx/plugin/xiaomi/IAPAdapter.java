package org.cocos2dx.plugin.xiaomi;

import java.util.Hashtable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.ILoginCallback;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;

import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class IAPAdapter implements InterfaceIAP {
	private static final String LOG_TAG = "Xiaomi.IAPAdapter";
	
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
		        } else if (!SDKWrapper.getInstance().isLoggedIn()){
		        	SDKWrapper.getInstance().userLogin(mActivity,new ILoginCallback(){
						@Override
						public void onSuccessed(int code, String msg) {
							payInSDK(productInfo);
						}
						@Override
						public void onFailed(int code, String msg) {
							payResult(IAPWrapper.PAYRESULT_FAIL, "LoginFailed");
						}
		        	});
		        }else{
		        	payInSDK(productInfo);
		        }
            }
		});
	}
	
	private void payInSDK(Hashtable<String, String> payInfo) {
		try {
			
			final String payMode;
			if(payInfo.get("PayMode") != null){
				payMode = (String) payInfo.get("PayMode");
			} else {
				payMode = "paymoney";
			}
			
			final String cpOrderId;
			if(payInfo.get("CpOrderId") != null){
				cpOrderId = (String) payInfo.get("CpOrderId");
			}
			else {
				cpOrderId = (String) payInfo.get("product_id");
			}
			
			final String cpUserInfo;
			if(payInfo.get("CpUserInfo") != null){
				cpUserInfo = (String) payInfo.get("CpUserInfo");
			}
			else {
				cpUserInfo = (String) SDKWrapper.getInstance().getUserId();
			}
			
			final int productCount;
            if (payInfo.get("ProductCount") != null) {
            	productCount = Integer.parseInt((String) payInfo.get("ProductCount"));
            }
            else {
            	productCount = 1;
            }
			
            final String productCode;
			if(payInfo.get("ProductCode") != null) {
				productCode = (String) payInfo.get("ProductCode");
			}
			else {
				productCode = "";
			}
            
			final String uID;
			if(payInfo.get("Uid") != null) {
				uID = (String) payInfo.get("Uid");
			}
			else {
				uID = (String) SDKWrapper.getInstance().getUserId();
			}
			
			final float payFee;
            if (payInfo.get("PayFee") != null) {
            	payFee = Float.parseFloat((String) payInfo.get("PayFee"));
            }
            else {
            	payFee = Float.parseFloat((String) payInfo.get("product_price")) * (Float.parseFloat((String) payInfo.get("product_count")));
            }
            
            PluginWrapper.runOnMainThread(new Runnable() {
                public void run() {
                	MiBuyInfo miBuyInfo= new MiBuyInfo();
                	if (payMode == "payCodeNoRepeated"){
                		//按计费代码购买非可消耗商品（例如：关卡等不可重复购买的商品）
                		miBuyInfo.setCpOrderId(cpOrderId);
                		miBuyInfo.setCpUserInfo(cpUserInfo);
                		miBuyInfo.setProductCode(productCode);
                		miBuyInfo.setCount(1);
                		Bundle mBundle = new Bundle();
                		mBundle.putString( GameInfoField.GAME_USER_BALANCE, "" );   //用户余额
                		mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, "" );  //vip等级
                		mBundle.putString( GameInfoField.GAME_USER_LV, "" );           //角色等级
                		mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, "" );  //工会，帮派
                		mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, "" ); //角色名称
                		mBundle.putString( GameInfoField.GAME_USER_ROLEID, uID );    //角色id
                		mBundle.putString( GameInfoField.GAME_USER_SERVER_NAME, "1" );  //所在服务器
                		miBuyInfo.setExtraInfo( mBundle ); //设置用户信息
                	}
                	else if (payMode == "payCodeRepeated"){
                		//按计费代码购买可消耗商品（例如：血瓶，法瓶等可重复购买的商品）
                		miBuyInfo.setCpOrderId(cpOrderId);
                		miBuyInfo.setProductCode(productCode);
                		miBuyInfo.setCount(productCount);
                		Bundle mBundle = new Bundle();
                		mBundle.putString( GameInfoField.GAME_USER_BALANCE, "" );   //用户余额
                		mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, "" );  //vip等级
                		mBundle.putString( GameInfoField.GAME_USER_LV, "" );           //角色等级
                		mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, "" );  //工会，帮派
                		mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, "" ); //角色名称
                		mBundle.putString( GameInfoField.GAME_USER_ROLEID, uID );    //角色id
                		mBundle.putString( GameInfoField.GAME_USER_SERVER_NAME, "1" );  //所在服务器
                		miBuyInfo.setExtraInfo( mBundle ); //设置用户信息
                	}
                	else {
                		//按金额付费
                		miBuyInfo.setCpOrderId(cpOrderId);
                		miBuyInfo.setCpUserInfo(cpUserInfo);
                		miBuyInfo.setAmount((int)payFee);
                		Bundle mBundle = new Bundle();
                		mBundle.putString( GameInfoField.GAME_USER_BALANCE, "" );   //用户余额
                		mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, "" );  //vip等级
                		mBundle.putString( GameInfoField.GAME_USER_LV, "" );           //角色等级
                		mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, "" );  //工会，帮派
                		mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, "" ); //角色名称
                		mBundle.putString( GameInfoField.GAME_USER_ROLEID, uID );    //角色id
                		mBundle.putString( GameInfoField.GAME_USER_SERVER_NAME, "1" );  //所在服务器
                		miBuyInfo.setExtraInfo( mBundle ); //设置用户信息
                		
                	}
                	
                	MiCommplatform.getInstance().miUniPay(mActivity, miBuyInfo,new OnPayProcessListener(){

						@Override
						public void finishPayProcess(int code) {
							 switch( code ) {
						        case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS:
						        	payResult(IAPWrapper.PAYRESULT_SUCCESS, "pay success");
						        	//购买成功
						            break;
						        case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_CANCEL:
						        	payResult(IAPWrapper.PAYRESULT_CANCEL, "pay cancel");
						            //取消购买
						               break;
						        case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_FAILURE:
						        	payResult(IAPWrapper.PAYRESULT_FAIL, "pay failed");
						            //购买失败
						               break;       
						        case  MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED:  
						        	payResult(IAPWrapper.PAYRESULT_FAIL, "pay error: action executed");
						        	//操作正在进行中
						        	break;       
						        default:
						        	payResult(IAPWrapper.PAYRESULT_FAIL, "pay failed : unkonw error");
						             //购买失败
						            break;
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
