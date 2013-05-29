package com.shandagames.android.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * @file MediaButtonBroadcastReceiver.java
 * @create 2013-5-29 下午06:07:50
 * @author lilong
 * @description TODO 广播接收者监测媒体播放按键； 有线和无线耳机上都有媒体播放按键，如播放、暂停、停止、快进和快退，
 *              当用户操作这些键时，系统会广播一个含有ACTION_MEDIA_BUTTON动作的intent。
 */
@SuppressLint("NewApi")
public class MediaButtonBroadcastReceiver extends BroadcastReceiver {

	public static interface OnMediaButtonStateListener {

        void onMediaButtonStateChanged(Intent intent, KeyEvent event);
    }
	
	private OnMediaButtonStateListener onMediaButtonStateListener;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			if (onMediaButtonStateListener != null) {
				onMediaButtonStateListener.onMediaButtonStateChanged(intent, event);
				this.abortBroadcast();
			}
		}
	}

	/** Start listening for button presses */
	public void register(AudioManager am, String packageName) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
		ComponentName component = new ComponentName(packageName, MediaButtonBroadcastReceiver.class.getName());  
		am.registerMediaButtonEventReceiver(component);
	}

	/** Stop listening for button presses */
	public void unregister(AudioManager am, String packageName) {
		ComponentName component = new ComponentName(packageName, MediaButtonBroadcastReceiver.class.getName());  
		am.unregisterMediaButtonEventReceiver(component);
	}
	
	public void setOnMediaButtonStateListener(OnMediaButtonStateListener listener) {
		this.onMediaButtonStateListener = listener;
	}
}
