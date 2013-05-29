package com.shandagames.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @file HeadsetPlugReceiver.java
 * @create 2012-8-31 上午10:11:29
 * @author lilong
 * @description 广播接收者监测耳机插入和拔出;
 * 此广播在AndroidManifest.xml中注册无效，需要在具体的Activity中注册和销毁，
 * 后台监测可采用service方式启动
 */
public class HeadsetPlugBroadcastReceiver extends BroadcastReceiver {

	public static interface OnHeadsetAvailableListener {
		public void onHeadsetAvailable();
		public void onHeadsetUnavailable();
	}

	private OnHeadsetAvailableListener onHeadsetAvailableListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra("state")) { // state 0代表拔出，1代表插入
			if (intent.getIntExtra("state", 0) == 0) {
				if (onHeadsetAvailableListener != null) onHeadsetAvailableListener.onHeadsetUnavailable();
			} else if (intent.getIntExtra("state", 0) == 1) {
				if (onHeadsetAvailableListener != null) onHeadsetAvailableListener.onHeadsetAvailable();
			}
		}
	}

	/** 注册广播接收者 */
	public void bind(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(this, intentFilter);
	}

	/** 解绑广播接收者 */
	public void unbind(Context context) {
		context.unregisterReceiver(this);
	}

	public void setOnHeadsetAvailableListener(OnHeadsetAvailableListener listener) {
		this.onHeadsetAvailableListener = listener;
	}
}
