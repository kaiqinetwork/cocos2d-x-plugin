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
package org.cocos2dx.plugin.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginWrapper;

import com.alipay.sdk.app.PayTask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class IAPAdapter implements InterfaceIAP {

	private static final String LOG_TAG = "alipay.IAPAdapter";
	private static final int SDK_PAY_FLAG = 1;
	
	private static Activity mContext = null;
	private static boolean mDebug = false;
	private static IAPAdapter mAdapter = null;

	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				LogD("handle msg : " + msg.toString());

				switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					// 从通知中获取参数
					try {
						if (payResult.getResultStatus().equals("9000")) {
							payResult(IAPWrapper.PAYRESULT_SUCCESS, "支付成功");
						} else {
							payResult(IAPWrapper.PAYRESULT_FAIL, "支付失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						payResult(IAPWrapper.PAYRESULT_FAIL, "结果解析失败");
					}
				}
					break;
				default:
					payResult(IAPWrapper.PAYRESULT_FAIL, "支付失败");
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	protected static void LogE(String msg, Exception e) {
		Log.e(LOG_TAG, msg, e);
		e.printStackTrace();
	}

	protected static void LogD(String msg) {
		if (mDebug) {
			Log.d(LOG_TAG, msg);
		}
	}

	public IAPAdapter(Context context) {
		mContext = (Activity) context;
		mAdapter = this;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		LogD("initDeveloperInfo invoked " + devInfo.toString());
		try {
			AlipayConfig.PARTNER = devInfo.get("AlipayPartner");
			AlipayConfig.SELLER_ID = devInfo.get("AlipaySeller");
			AlipayConfig.RSA_PRIVATE = devInfo.get("AlipayRsaPrivate");
			AlipayConfig.RSA_ALIPAY_PUBLIC = devInfo.get("AlipayPublic");
			AlipayConfig.NOTIFY_URL = ((null == devInfo.get("AlipayNotifyUrl")) ? "" : devInfo.get("AlipayNotifyUrl"));
		} catch (Exception e) {
			LogE("Developer info is wrong!", e);
		}
	}

	@Override
	public void payForProduct(Hashtable<String, String> info) {
		LogD("payForProduct invoked " + info.toString());
		if (! networkReachable()) {
			payResult(IAPWrapper.PAYRESULT_FAIL, "网络不可用");
			return;
		}

		final Hashtable<String, String> productInfo = info;
		PluginWrapper.runOnMainThread(new Runnable() {
			@Override
			public void run() {
				// 准备订单信息
				String orderInfo = getOrderInfo(productInfo);
				/**
				 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
				 */
				String sign = productInfo.get("sign");
				if (sign == null) {
					sign = sign(orderInfo);
				}
				try {
					/**
					 * 仅需对sign 做URL编码
					 */
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				String signType = productInfo.get("sign_type");
				if (signType == null) {
					signType = getSignType();
				}
				
				/**
				 * 完整的符合支付宝参数规范的订单信息
				 */
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"" + signType+ "\"";
				
				Runnable payRunnable = new Runnable() {

					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(mContext);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo, true);

						Message msg = new Message();
						msg.what = SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			}
		});
	}

	@Override
	public void setDebugMode(boolean debug) {
		mDebug = debug;
	}

	@Override
	public String getSDKVersion() {
		return "Unknown version";
	}

	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
	private String getOrderInfo(Hashtable<String, String> info) {
		String strRet = null;
		try {
			String price = info.get("total_fee") == null ? info.get("product_price") : info.get("total_fee");
			String productName = info.get("subject") == null ? info.get("product_name") : info.get("subject");
			String productDesc = info.get("body") == null ? info.get("product_desc") : info.get("body");
			String outTradeNo = info.get("out_trade_no");
			String partner = info.get("partner");
			String sellerId = info.get("seller_id");
			String notifyUrl = info.get("notify_url");
			String inputCharset = info.get("_input_charset");
			String itBPay = info.get("it_b_pay");

			strRet = "_input_charset=\"" + (inputCharset == null ? "utf-8" : inputCharset) + "\""
					+ "&body=\"" + productDesc + "\""
					+ "&it_b_pay=\"" + (itBPay == null ? "30m" : itBPay) + "\""
					+ "&notify_url=\"" + (notifyUrl == null ? AlipayConfig.NOTIFY_URL : notifyUrl) + "\""
					+ "&out_trade_no=\"" + (outTradeNo == null ? getOutTradeNo() : outTradeNo) + "\""
					+ "&partner=\"" + (partner == null ? AlipayConfig.PARTNER : partner) + "\""
					+ "&payment_type=\"1\""
					+ "&seller_id=\"" + (sellerId == null ? AlipayConfig.SELLER_ID : sellerId) + "\""
					+ "&service=\"mobile.securitypay.pay\""
					+ "&subject=\"" + productName + "\""
					+ "&total_fee=\"" + price + "\""
					;
		} catch (Exception e) {
			LogE("Product info parse error", e);
		}

		LogD("order info : " + strRet);
		return strRet;
	}

	/**
	 * get the out_trade_no for an order.
	 * 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt(10000);
		return strKey;
	}

	//
	//
	/**
	 *  sign the order info.
	 *  对订单信息进行签名
	 *  
	 * @param signType	签名方式 
	 * @param content		待签名订单信息
	 * @return
	 */
	private String sign(String content) {
		return SignUtils.sign(content, AlipayConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use.
	 * 获取签名方式
	 * 
	 * @return
	 */
	private String getSignType() {
		return "RSA";
	}

	private boolean networkReachable() {
		boolean bRet = false;
		try {
			ConnectivityManager conn = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conn.getActiveNetworkInfo();
			bRet = (null == netInfo) ? false : netInfo.isAvailable();
		} catch (Exception e) {
			LogE("Fail to check network status", e);
		}
		LogD("NetWork reachable : " + bRet);
		return bRet;
	}

	private static void payResult(int ret, String msg) {
		IAPWrapper.onPayResult(mAdapter, ret, msg);
		LogD("Alipay result : " + ret + " msg : " + msg);
	}

	@Override
	public String getPluginVersion() {
		return "0.2.0";
	}
	
	@Override
	public String getPluginName() {
		return "Alipay";
	}
}
