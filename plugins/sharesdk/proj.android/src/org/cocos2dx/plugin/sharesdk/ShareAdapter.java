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
package org.cocos2dx.plugin.sharesdk;

import java.util.HashMap;
import java.util.Hashtable;

import org.cocos2dx.plugin.InterfaceShare;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.ShareWrapper;

import android.app.Activity;
import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareAdapter implements InterfaceShare {

	protected static String LOG_TAG = "sharesdk.ShareAdapter";
	private static final String PLUGIN_NAME = "ShareSDK";
	private static final String PLUGIN_VERSION = "1.0.0";
	private static final String SDK_VERSION = "2.7.10";
	private static final int SHAREPLATFORM_NULL = 0;
	private static final int SHAREPLATFORM_WECHAT = 1;
	private static final int SHAREPLATFORM_WECHATMOMENTS = 2;
	private static final int SHAREPLATFORM_WECHATFAVORITE = 3;
	private static final int SHAREPLATFORM_QQ = 4;
	private static final int SHAREPLATFORM_QZONE = 5;
	private static final int SHAREPLATFORM_SINAWEIBO = 6;
	private static final int SHAREPLATFORM_SHORTMESSAGE = 7;

	private static boolean mDebug = false;
	private static ShareAdapter mInstance = null;
	private Activity mContext = null;

	protected static void LogE(String msg, Exception e) {
		PluginHelper.logE(LOG_TAG, msg, e);
	}

	protected static void logD(String msg) {
		PluginHelper.logD(LOG_TAG, msg);
	}

	public ShareAdapter(Context context) {
		mContext = (Activity) context;
		mInstance = this;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}

	public boolean isValid() {
		return mContext != null;
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				ShareSDK.initSDK(mContext);
			}
		});
	}

	@Override
	public void setDebugMode(boolean debug) {
		mDebug = debug;
	}

	@Override
	public String getSDKVersion() {
		return SDK_VERSION;
	}

	@Override
	public String getPluginVersion() {
		return PLUGIN_VERSION;
	}

	@Override
	public String getPluginName() {
		return PLUGIN_NAME;
	}

	@Override
	public void share(final Hashtable<String, String> cpInfo) {
		PluginWrapper.runOnMainThread(new Runnable() {
			public void run() {
				if (cpInfo == null) {
					shareResult(ShareWrapper.SHARERESULT_FAIL, "share param is null");
				} else if (cpInfo.containsKey("text")) {
					int mSharePlatform;
					Platform mPlatform = null;
					if (cpInfo.containsKey("platform")) {
						mSharePlatform = Integer.parseInt(cpInfo.get("platform"));
					}else{
						mSharePlatform = SHAREPLATFORM_NULL;
					}
					if (mSharePlatform == SHAREPLATFORM_NULL) { //默认没有指定品台 使用快捷分享
						OnekeyShare oks = new OnekeyShare();
						oks.setText(cpInfo.get("text"));
						if (cpInfo.containsKey("title")) {
							oks.setTitle(cpInfo.get("title"));
						}
						if (cpInfo.containsKey("titleUrl")) {
							oks.setTitleUrl(cpInfo.get("titleUrl"));
						}
						if (cpInfo.containsKey("site")) {
							oks.setSite(cpInfo.get("site"));
						}
						if (cpInfo.containsKey("siteUrl")) {
							oks.setSiteUrl(cpInfo.get("siteUrl"));
						}
						if (cpInfo.containsKey("address")) {
							oks.setAddress(cpInfo.get("address"));
						}
						int mShareType;
						if (cpInfo.containsKey("type")){
							mShareType = Integer.parseInt(cpInfo.get("type"));
						}else{
							mShareType = Platform.SHARE_WEBPAGE;
						}
						switch (mShareType) {
						case Platform.SHARE_TEXT:
							oks.setShareType(Platform.SHARE_TEXT);
							break;
						case Platform.SHARE_IMAGE:
							oks.setShareType(Platform.SHARE_IMAGE);
							if (cpInfo.containsKey("imagePath")) {
								oks.setImagePath(cpInfo.get("imagePath"));
							}
							if (cpInfo.containsKey("imageUrl")) {
								oks.setImageUrl(cpInfo.get("imageUrl"));
							}
							break;
						case Platform.SHARE_WEBPAGE:
							oks.setShareType(Platform.SHARE_WEBPAGE);
							if (cpInfo.containsKey("imagePath")) {
								oks.setImagePath(cpInfo.get("imagePath"));
							}
							if (cpInfo.containsKey("imageUrl")) {
								oks.setImageUrl(cpInfo.get("imageUrl"));
							}
							if (cpInfo.containsKey("url")) {
								oks.setUrl(cpInfo.get("url"));
							}
							break;
						default:
							oks.setShareType(Platform.SHARE_TEXT);
							break;
						}
						if (cpInfo.containsKey("theme")) {
							oks.setTheme(OnekeyShareTheme.fromValue(Integer.parseInt(cpInfo.get("theme"))));
						} else {
							oks.setTheme(OnekeyShareTheme.CLASSIC);
						}
						oks.setSilent(false);
						oks.disableSSOWhenAuthorize();
						oks.setCallback(new PlatformActionListener() {
							public void onError(Platform arg0, int arg1, Throwable arg2) {
								shareResult(ShareWrapper.SHARERESULT_FAIL, arg2.getMessage());
							}

							public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
								shareResult(ShareWrapper.SHARERESULT_SUCCESS, arg2.toString());
							}

							public void onCancel(Platform arg0, int arg1) {
								shareResult(ShareWrapper.SHARERESULT_CANCEL, "shareSdk oncancel");
							}
						});
						oks.show(mContext);
					}else{
						switch (mSharePlatform) {
						case SHAREPLATFORM_WECHAT:
							mPlatform = ShareSDK.getPlatform(Wechat.NAME);
							break;
						case SHAREPLATFORM_WECHATMOMENTS:
							mPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
							break;
						case SHAREPLATFORM_WECHATFAVORITE:
							mPlatform = ShareSDK.getPlatform(WechatFavorite.NAME);
							break;
						case SHAREPLATFORM_QQ:
							mPlatform = ShareSDK.getPlatform(QQ.NAME);
							break;
						case SHAREPLATFORM_QZONE:
							mPlatform = ShareSDK.getPlatform(QZone.NAME);
							break;
						case SHAREPLATFORM_SINAWEIBO:
							mPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
							break;
						case SHAREPLATFORM_SHORTMESSAGE:
							mPlatform = ShareSDK.getPlatform(ShortMessage.NAME);
							break;
						default:
							mPlatform = ShareSDK.getPlatform(Wechat.NAME); //沒找到指定平台  使用微信
							break;
						}
						ShareParams sp = new ShareParams();
						sp.setText(cpInfo.get("text"));
						if (cpInfo.containsKey("title")) {
							sp.setTitle(cpInfo.get("title"));
						}
						if (cpInfo.containsKey("titleUrl")) {
							sp.setTitleUrl(cpInfo.get("titleUrl"));
						}
						if (cpInfo.containsKey("site")) {
							sp.setSite(cpInfo.get("site"));
						}
						if (cpInfo.containsKey("siteUrl")) {
							sp.setSiteUrl(cpInfo.get("siteUrl"));
						}
						if (cpInfo.containsKey("address")) {
							sp.setAddress(cpInfo.get("address"));
						}
						int mShareType;
						if (cpInfo.containsKey("type")){
							mShareType = Integer.parseInt(cpInfo.get("type"));
						}else{
							mShareType = Platform.SHARE_WEBPAGE;
						}
						switch (mShareType) {
						case Platform.SHARE_TEXT:
							sp.setShareType(Platform.SHARE_TEXT);
							break;
						case Platform.SHARE_IMAGE:
							sp.setShareType(Platform.SHARE_IMAGE);
							if (cpInfo.containsKey("imagePath")) {
								sp.setImagePath(cpInfo.get("imagePath"));
							}
							if (cpInfo.containsKey("imageUrl")) {
								sp.setImageUrl(cpInfo.get("imageUrl"));
							}
							break;
						case Platform.SHARE_WEBPAGE:
							sp.setShareType(Platform.SHARE_WEBPAGE);
							if (cpInfo.containsKey("imagePath")) {
								sp.setImagePath(cpInfo.get("imagePath"));
							}
							if (cpInfo.containsKey("imageUrl")) {
								sp.setImageUrl(cpInfo.get("imageUrl"));
							}
							if (cpInfo.containsKey("url")) {
								sp.setUrl(cpInfo.get("url"));
							}
							break;
						default:
							sp.setShareType(Platform.SHARE_TEXT);
							break;
						}
						mPlatform.setPlatformActionListener(new PlatformActionListener() {
							public void onError(Platform arg0, int arg1, Throwable arg2) {
								shareResult(ShareWrapper.SHARERESULT_FAIL, arg2.getMessage());
							}

							public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
								shareResult(ShareWrapper.SHARERESULT_SUCCESS, arg2.toString());
							}

							public void onCancel(Platform arg0, int arg1) {
								shareResult(ShareWrapper.SHARERESULT_CANCEL, "shareSdk oncancel");
							}
						});
						mPlatform.share(sp);
					}	
				} else {
					shareResult(ShareWrapper.SHARERESULT_FAIL, "share param is incomplete");
				}
			}
		});
	}

	protected void shareResult(int ret, String msg) {
		logD("shareResult: " + ret + " msg : " + msg);
		ShareWrapper.onShareResult(mInstance, ret, msg);
	}

}
