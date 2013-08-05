package com.shandagames.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * @file RingtoneHelper.java
 * @create 2013-6-27 下午03:38:50
 * @author lilong
 * @description TODO 封装常用的铃声设置
 */
public class RingtoneHelper {

	private RingtoneHelper() {
	}

	/**
	 * 获取的是铃声的Uri
	 * 
	 * @param ctx
	 * @param type
	 * @return
	 */
	public static Uri getDefaultRingtoneUri(Context ctx, int type) {
		return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);

	}

	/**
	 * 获取的是铃声相应的Ringtone
	 * 
	 * @param ctx
	 * @param type
	 */
	public Ringtone getDefaultRingtone(Context ctx, int type) {
		return RingtoneManager.getRingtone(ctx,
				RingtoneManager.getActualDefaultRingtoneUri(ctx, type));

	}

	/**
	 * 设置默认的铃声
	 * 
	 * @param ctx
	 * @param type
	 * @param ringtoneUri
	 */
	public static void setDefaultRingtoneUri(Context ctx, int type,
			Uri ringtoneUri) {
		RingtoneManager.setActualDefaultRingtoneUri(ctx, type, ringtoneUri);
	}

	/**
	 * 播放铃声
	 * 
	 * @param ctx
	 * @param type
	 */

	public static void playRingTone(Context ctx, int type) {
		MediaPlayer mMediaPlayer = MediaPlayer.create(ctx,
				getDefaultRingtoneUri(ctx, type));
		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();
	}

	public static void doPickRingtone(Activity act, int type, String uriString, int requestCode) {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);  
        // Allow user to pick 'Default'  
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);  
        // Show only ringtones  
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, type);  
        //set the default Notification value  
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(type));  
        // Don't show 'Silent'  
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);  
  
        Uri notificationUri;  
        if (uriString != null) {  
            notificationUri = Uri.parse(uriString);  
            // Put checkmark next to the current ringtone for this contact  
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);  
        } else {  
            // Otherwise pick default ringtone Uri so that something is selected.  
            notificationUri = RingtoneManager.getDefaultUri(type);  
            // Put checkmark next to the current ringtone for this contact  
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);  
        }  
  
        // Launch!  
        act.startActivityForResult(intent, requestCode);  
	}
	
	
	public static void doPickSdcardRingtone(Activity act, int type, String uriString, int requestCode){  
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);  
        innerIntent.setType("audio/*");  
        // Put checkmark next to the current ringtone for this contact  
        innerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(uriString));  
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);  
        act.startActivityForResult(wrapperIntent, requestCode);  
    }  
}
