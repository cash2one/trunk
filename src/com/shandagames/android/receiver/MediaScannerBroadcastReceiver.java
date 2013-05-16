/**
 * 
 */
package com.shandagames.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

/**
 * @file MediaScannerBroadcastReceiver.java
 * @create 2012-10-16 下午6:07:38
 * @author lilong
 * @description 扫描SDCard文件
 */
public class MediaScannerBroadcastReceiver extends BroadcastReceiver {
	
	public static interface OnMediaScannerListener {
		public void onMediaScannerStarted();
		public void onMediaScannerFinished();
	}

	private OnMediaScannerListener onMediaScannerListener;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
		}
		if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(intent.getAction())) { //开始扫描
			if (onMediaScannerListener != null) onMediaScannerListener.onMediaScannerStarted();
		} else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(intent.getAction())) { //结束扫描
			if (onMediaScannerListener != null) onMediaScannerListener.onMediaScannerFinished();
		}
	}

	/** 注册广播接收者 */
	public void bind(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		context.registerReceiver(this, intentFilter);
	}

	/** 解绑广播接收者 */
	public void unbind(Context context) {
		context.unregisterReceiver(this);
	}
	
	/** 发送广播接受者，扫描指定目录  */
	public void scanMediaDir(Context context, Uri data) {
		//扫描整个SD卡上的多媒体文件  
		Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");  
		intent.setData(data);  
		context.sendBroadcast(intent);
	}
	
	/** 发送广播接受者，扫描指定文件  */
	public void scanMediaFile(Context context, Uri data) {
		//扫描整个SD卡上的多媒体文件  
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  
		intent.setData(data);  
		context.sendBroadcast(intent);
	}
	
	public void setOnMediaScannerListener(OnMediaScannerListener listener) {
		this.onMediaScannerListener = listener;
	}
}
