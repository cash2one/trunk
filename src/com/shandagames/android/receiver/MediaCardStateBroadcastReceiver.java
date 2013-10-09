/**
 * 
 */
package com.shandagames.android.receiver;

import com.shandagames.android.log.LogUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @file MediaCardStateBroadcastReceiver.java
 * @create 2012-9-20 上午11:24:33
 * @author lilong
 * @description 监听SDCard装载状态
 */
public class MediaCardStateBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "MediaCardStateBroadcastReceiver";

	public static interface OnMediaCardAvailableListener {
		public void onMediaCardAvailable();
		public void onMediaCardUnavailable();
	}
	
	private OnMediaCardAvailableListener onMediaCardAvailableListener;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtils.d(TAG, "Media state changed, reloading resource managers");
		if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
			if (onMediaCardAvailableListener != null) onMediaCardAvailableListener.onMediaCardUnavailable();
		} else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
			if (onMediaCardAvailableListener != null) onMediaCardAvailableListener.onMediaCardAvailable();
		}
	}

	/** 注册广播接收者 */
	public void register(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addDataScheme("file");
		context.registerReceiver(this, intentFilter);
	}

	/** 解绑广播接收者 */
	public void unregister(Context context) {
		context.unregisterReceiver(this);
	}

	public void setOnMediaCardAvailableListener(OnMediaCardAvailableListener listener) {
		this.onMediaCardAvailableListener = listener;
	}
	
	
	/* <intent-filter>
	     <action android:name="android.intent.action.MEDIA_MOUNTED" />
	     <action android:name="android.intent.action.MEDIA_EJECT" />
	     <action android:name="android.intent.action.MEDIA_UNMOUNTED" />
	     <action android:name="android.intent.action.MEDIA_SHARED" />
	     <action android:name="android.intent.action.MEDIA_SCANNER_STARTED" />
	     <action android:name="android.intent.action.MEDIA_SCANNER_FINISHED" />
	     <action android:name="android.intent.action.MEDIA_REMOVED" />
	     <action android:name="android.intent.action.MEDIA_BAD_REMOVAL" />
	     <data android:scheme="file" />
     </intent-filter>*/
}
