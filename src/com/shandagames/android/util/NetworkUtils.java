package com.shandagames.android.util;

import java.net.Inet4Address;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * @file NetworkUtils.java
 * @create 2013-5-29 下午04:34:42
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO 执行网络相关操作
 */
public final class NetworkUtils {

	public static final int NETTYPE_UNKNOWN = 0x00;
	public static final int NETTYPE_WIFI 	= 0x01;
	public static final int NETTYPE_CMWAP 	= 0x02;
	public static final int NETTYPE_CMNET 	= 0x03;

	private NetworkUtils() {
	}

	/** 检查网络是否可用 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/** 获取设备IP地址 */
	public static String getIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return ipIntToString(wifiInfo.getIpAddress());
	}

	// see http://androidsnippets.com/obtain-ip-address-of-current-device
	public static String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取设备服务商信息  
	 * 需要加入权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>  
	 */
	public static String getProvidersName(Context ctx) {
		String providersName = "unknown";
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回唯一的用户ID;就是这张卡的编号 
		String IMSI = tm.getSubscriberId();
		if (IMSI!=null && IMSI.length()>0) {
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				providersName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				providersName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				providersName = "中国电信";
			}
		}
		return providersName;
	}
	
	/** 获取设备网络类型  */
	public int getNetworkType(Context ctx) {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			switch (networkInfo.getType()) {
				case ConnectivityManager.TYPE_MOBILE:
					String extraInfo = networkInfo.getExtraInfo();
					if (extraInfo!=null && extraInfo.length()>0) {
						if (extraInfo.toLowerCase().equals("cmnet")) 
							return NETTYPE_CMNET;
						else 
							return NETTYPE_CMWAP;
					}
				case ConnectivityManager.TYPE_WIFI:
					return NETTYPE_WIFI; 
			}
		}
		return NETTYPE_UNKNOWN;
	}
}
