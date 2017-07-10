package org.cocos2dx.plugin.yaya;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public  class VoiceUtil {

	/**
	 * 
	 * @Title: 播放本地声音的方�?
	 * @Description: TODO  
	 * @param @param context     
	 * @return void
	 */
	@SuppressWarnings("unused")
	public static void playNotifyRing(Context context,int rid){
		//监测系统铃声音量是否正常�?启，如音量大小太小则�?�?
		final AudioManager mAudioManager = (AudioManager)context. getSystemService(Context.AUDIO_SERVICE);
		int 	max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		final int	current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    
		if(current<3){
			//音量太小则修改声音为�?大�??/2
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, 0);
		}
	
      try {
			final MediaPlayer	 mp= MediaPlayer.create(context,rid);
			// mp.setDataSource(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));//系统声音
			if(mp != null){
				mp.stop();
			}    
			
			mp.prepare();
			mp.start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(mp.isPlaying()){
						try {
							Thread.sleep(100);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					//如果修改过则还原系统铃声大小
					if(current<3){
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
					}

				}
			}).start();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
	    
	public static void playVoid(Context context,File voice) {
		try {
			
				// 监测系统铃声音量是否正常�?启，如音量大小太小则�?�?
				final AudioManager mAudioManager = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				final int current = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
		 
				// if(current<3){
				// //音量太小则修改声音为�?大�??/2
				// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, 0);
				// }
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);

				final MediaPlayer mp = MediaPlayer.create(context,
						Uri.fromFile(voice));
				// mp.setDataSource(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));//系统声音
				if (mp != null) {
					mp.stop();
					mp.prepare();
					mp.start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							while (mp.isPlaying()) {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							// 如果修改过则还原系统铃声大小
							if (current < 3) {
								mAudioManager.setStreamVolume(
										AudioManager.STREAM_MUSIC, current, 0);
							}

						}
					}).start();
				} 
		   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	/**
	 * �?启关闭背景音�?
	 * @param context
	 * @param bMute true-关闭；false-�?�?
	 * @return
	 */
	public static boolean muteAudioFocus(Context context, boolean bMute) {  
	    if(context == null){  
	        Log.d("ANDROID_LAB", "context is null.");  
	        return false;  
	    }  
	    boolean bool = false;  
	    AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);  
	    if(bMute){  
	        int result = am.requestAudioFocus(null,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);  
	        bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;  
	    }else{  
	        int result = am.abandonAudioFocus(null);  
	        bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;  
	    }  
	    Log.d("ANDROID_LAB", "pauseMusic bMute="+bMute +" result="+bool);  
	    return bool;  
	}  
}
