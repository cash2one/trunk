package com.shandagames.android.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class DownloadCastReceiver extends BroadcastReceiver {

	public static final String INTENT_EXTRAS_DOWNLOAD_TYPE  = "_type";
	public static final String INTENT_EXTRAS_DOWNLOAD_VALUE = "_value";
	
	public static final String NOTIFICATIONI_PREPARE_DOWNLOAD = "NOTIFICATIONI_PREPARE_DOWNLOAD";
	public static final String NOTIFICATIONI_UPDATE_PROGRESS = "NOTIFICATIONI_UPDATE_PROGRESS";
	public static final String NOTIFICATIONI_FINISHED_DOWNLOAD = "NOTIFICATIONI_FINISHED_DOWNLOAD";
	public static final String NOTIFICATIONI_ERROR_DOWNLOAD = "NOTIFICATIONI_ERROR_DOWNLOAD";
	
	public static final String INTENT_ACTION_DOWNLOAD_NOTIFICATION = "com.shandagames.android.intent.action.download";
	
	private OnDownloadTaskListener mDownloadTaskListener;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (INTENT_ACTION_DOWNLOAD_NOTIFICATION.equals(intent.getAction())) {
			
		}
	}

	/** 注册广播接收者 */
	public void bind(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(INTENT_ACTION_DOWNLOAD_NOTIFICATION);
		context.registerReceiver(this, intentFilter);
	}

	/** 解绑广播接收者 */
	public void unbind(Context context) {
		context.unregisterReceiver(this);
	}
	
	public static interface OnDownloadTaskListener {
		
		public void onDownloadStateChanged(Intent intent);
	}
}
