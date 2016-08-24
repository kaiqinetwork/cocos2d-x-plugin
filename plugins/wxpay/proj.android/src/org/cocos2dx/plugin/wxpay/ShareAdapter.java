package org.cocos2dx.plugin.wxpay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import org.cocos2dx.plugin.IAPWrapper;
import org.cocos2dx.plugin.InterfaceShare;
import org.cocos2dx.plugin.PluginHelper;
import org.cocos2dx.plugin.PluginWrapper;
import org.cocos2dx.plugin.ShareWrapper;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ShareAdapter implements InterfaceShare {
	private static final String LOG_TAG = "wxpay.ShareAdapter";
	
    private static boolean mDebug = false;
	private static ShareAdapter mInstance = null;
	private Context mContext = null;
	
	public ShareAdapter(Context context) {
		mContext = (Activity) context;
		mInstance = this;
		mDebug = false;
		configDeveloperInfo(PluginWrapper.getDeveloperInfo());
	}
	
	public void configDeveloperInfo(Hashtable<String, String> devInfo) {
        logD("configDeveloperInfo(" + devInfo.toString() + ")invoked!");
        Hashtable<String, String> curDevInfo = devInfo;
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            }
        });
    }
	
	public void share(final Hashtable<String, String> info) {
        logD("share(" + info.toString() + ") invoked!");
        PluginWrapper.runOnMainThread(new Runnable() {
            public void run() {
            	logD("\u5206\u4eab\u5f00\u59cb");
                int scene = Integer.parseInt((String) info.get("scene"));
                int mediaType = Integer.parseInt((String) info.get("media_type"));
                int thumbSize = Integer.parseInt((String) info.get("thumb_size"));
                switch (mediaType) {
                
                }
                if (mediaType == 0) {
                    ShareText((String) info.get("text"), scene);
                }
                if (mediaType == 1) {
                    ShareBitmap((String) info.get("image_path"), thumbSize, scene);
                }
                if (mediaType == 3) {
                    ShareMusic((String) info.get("url"), (String) info.get("title"), (String) info.get("text"), (String) info.get("image_path"), scene);
                }
                if (mediaType == 4) {
                    ShareVideo((String) info.get("url"), (String) info.get("title"), (String) info.get("text"), (String) info.get("image_path"), scene);
                }
                if (mediaType == 2) {
                    ShareWebpage((String) info.get("url"), (String) info.get("title"), (String) info.get("text"), (String) info.get("image_path"), scene);
                }
            }
        });
    }
	
	public void ShareText(String text, int scene) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msgText = new WXMediaMessage();
        msgText.mediaObject = textObj;
        msgText.description = text;
        Req req = new Req();
        req.transaction = buildTransaction("text");
        req.message = msgText;
        req.scene = scene;
        SDKWrapper.getInstance().getApi().sendReq(req);
    }
	
	public void ShareBitmap(String imagePath, int thumbSize, int scene) {
		Bitmap bmp = getImage(imagePath);
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        
        // 设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, thumbSize, thumbSize, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        Req req = new Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = scene;
        SDKWrapper.getInstance().getApi().sendReq(req);
    }
	
	private Bitmap getImage(String path) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > 480.0f) {
            be = (int) (((float) opts.outWidth) / 480.0f);
        } else if (w < h && ((float) h) > 800.0f) {
            be = (int) (((float) opts.outHeight) / 800.0f);
        }
        if (be <= 0) {
            be = 1;
        }
        opts.inSampleSize = be;
        return compressImage(BitmapFactory.decodeFile(path, opts));
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            image.compress(CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
    }
    
    public void ShareMusic(String musicUrl, String musicTitle, String musicDescription, String imagePath, int scene) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = musicTitle;
        msg.description = musicDescription;
        Bitmap bmp = getImage(imagePath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 10, 10, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        Req req = new Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = scene;
        SDKWrapper.getInstance().getApi().sendReq(req);
    }
    
    public void ShareVideo(String videoUrl, String videoTitle, String videoDescription, String imagePath, int scene) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = videoTitle;
        msg.description = videoDescription;
        Bitmap bmp = getImage(imagePath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 10, 10, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        Req req = new Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = scene;
        SDKWrapper.getInstance().getApi().sendReq(req);
    }
    
    public void ShareWebpage(String webpageUrl, String webpageTitle, String webpageDescription, String imagePath, int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = webpageTitle;
        msg.description = webpageDescription;
        Bitmap bmp = getImage(imagePath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 10, 10, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        Req req = new Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        SDKWrapper.getInstance().getApi().sendReq(req);
    }

	protected static void logE(String msg, Exception e) {
		PluginHelper.logE(LOG_TAG, msg, e);
	}

	protected static void logD(String msg) {
		PluginHelper.logD(LOG_TAG, msg);
	}

	private String buildTransaction(String type) {
        return type == null ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
	
	@Override
	public void setDebugMode(boolean debug) {
		mDebug = debug;
	}

	@Override
	public String getSDKVersion() {
		return SDKWrapper.getInstance().getSDKVersion();
	}

	public static void shareResult(int ret, String msg) {
		logD("Share result : " + ret + " msg : " + msg);
		ShareWrapper.onShareResult(mInstance, ret, msg);		
	}

	@Override
	public String getPluginVersion() {
		return SDKWrapper.getInstance().getPluginVersion();
	}
	
	@Override
	public String getPluginName() {
		return SDKWrapper.getInstance().getPluginName();
	}
}
