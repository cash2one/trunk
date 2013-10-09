package com.shandagames.android.util;

import java.net.Inet4Address;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
	public static String ipToString(Context context) {
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
	
	
	/** 设置网络代理APN  */
    public static void setProxy(Context context, DefaultHttpClient httpClient) {
		try {
			// APN网络的API是隐藏的,获取手机的APN设置,需要通过ContentProvider来进行数据库查询
			// 取得全部的APN列表：content://telephony/carriers；
			// 取得当前设置的APN：content://telephony/carriers/preferapn；
			// 取得current=1的APN：content://telephony/carriers/current；
			ContentValues values = new ContentValues();
			Cursor cursor = context.getContentResolver().query(
					Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					int colCount = cursor.getColumnCount();
					for (int i = 0; i < colCount; i++) {
						values.put(cursor.getColumnName(i), cursor.getString(i));
					}
				}
				cursor.close();
			}
            // 中国移动WAP设置：  APN：CMWAP；代理：10.0.0.172；端口：80;   
            // 中国联通WAP设置：  APN：UNIWAP；代理：10.0.0.172；端口：80;   
            // 中国联通3GWAP设置  APN：3GWAP；代理：10.0.0.172；端口：80; 
			// 中国电信WAP设置：  APN: CTWAP；代理：10.0.0.200；端口：80;
			String proxyHost = (String) values.get("proxy");
			if (proxyHost!=null && proxyHost.length()>0 && !isWiFiConnected(context)) {
				int prot = Integer.parseInt(String.valueOf(values.get("port")));
				httpClient.getCredentialsProvider().setCredentials(
						new AuthScope(proxyHost, prot),
						new UsernamePasswordCredentials((String) values.get("user"), (String) values.get("password")));
				HttpHost proxy = new HttpHost(proxyHost, prot);
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /** 判断网络状态  */
    public static boolean isWiFiConnected(Context context) {
		boolean isWifiEnable = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo!=null&&activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			isWifiEnable = true;
		}
		return isWifiEnable;
	}
}
