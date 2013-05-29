package com.shandagames.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @file NetworkUtils.java
 * @create 2013-5-29 下午04:34:42
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO 执行网络相关操作 
 */
public final class NetworkUtils {

	private NetworkUtils() {
	}

	/** 检查网络是否可用  */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} 
		return false;
	}

}
