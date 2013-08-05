package com.shandagames.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * @file HomeWatcherBroadcastReceiver.java
 * @create 2013-7-8 上午11:58:24
 * @author lilong
 * @description TODO Home键监听封装 
 */
public class HomeWatcherBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "HomeWatcherBroadcastReceiver";

	private final String SYSTEM_DIALOG_REASON_KEY = "reason";
	private final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
	private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
	private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

	private Context mContext;
	private IntentFilter mFilter;
	private OnHomePressedListener mListener;

	public HomeWatcherBroadcastReceiver(Context context) {
		mContext = context;
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
			if (reason != null) {
				Log.e(TAG, "action:" + action + ",reason:" + reason);
				if (mListener != null) {
					if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						// 短按home键
						mListener.onHomePressed();
					} else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
						// 长按home键
						mListener.onHomeLongPressed();
					}
				}
			}
		}
	}

	/**
	 * 设置监听
	 */
	public void setOnHomePressedListener(OnHomePressedListener listener) {
		this.mListener = listener;
	}

	/**
	 * 开始监听，注册广播
	 */
	public void register() {
		mContext.registerReceiver(this, mFilter);
	}

	/**
	 * 停止监听，注销广播
	 */
	public void unregister() {
		mContext.unregisterReceiver(this);
	}

	// 回调接口
	public interface OnHomePressedListener {

		public void onHomePressed();

		public void onHomeLongPressed();
	}
}
