package com.shandagames.android.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.shandagames.android.util.NetworkUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;

/**
 * @file SocketService.java
 * @create 2013-8-21 上午11:09:44
 * @author lilong
 * @description TODO 监听服务器端口4392
 */
public class SocketService extends Service implements Runnable {
	public static final int CONNECTION_POST = 4392; 
	
	private Context context;
	private WifiInfo wifiInfo;
	private WifiManager wifiManager;
	private ServerSocket serverSocket;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.context = getBaseContext();
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();  //当前连接的WiFi网络
		System.out.println("Service Start...");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(this).start();
		return START_STICKY;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(CONNECTION_POST);
			while (true) {
				System.out.println("Service Listen 4392...");
				String ipString = NetworkUtils.ipIntToString(wifiInfo.getIpAddress());
				String wifiStatus = wifiManager.isWifiEnabled()?"WiFi已开启":"WiFi已关闭";
				String speed = wifiInfo.getLinkSpeed() + "Mbps";
				String listNetWork = listUsingNetWork();
				String bssid = wifiInfo.getBSSID()!=null?wifiInfo.getBSSID().toUpperCase():"";
				// 替换占位符值
				String html = readHtmlString().replaceAll("#mac#", wifiInfo.getMacAddress())
							  .replaceAll("#ip#", ipString)
							  .replaceAll("#wifi_status#", wifiStatus)
							  .replaceAll("#speed#", speed)
							  .replaceAll("#bssid#", bssid)
							  .replaceAll("#network#", listNetWork);
				//必须向客户端浏览器输出http格式响应头,否则浏览器无法解析数据
				html = "HTTP/1.1 200 OK\r\nContent-Type:text/html\r\nContent-Length:"+html.getBytes("utf-8").length+"\r\n\r\n"+html;
				
				Socket socket = serverSocket.accept();
				OutputStream os = socket.getOutputStream();
				os.write(html.getBytes("utf-8"));
				os.flush();
				os.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String listUsingNetWork() {
		//扫描所有配置的网络，找到当前正在使用的网络
		StringBuilder sb = new StringBuilder();
		for (WifiConfiguration config : wifiManager.getConfiguredNetworks()) {
			String connected = (config.status == WifiConfiguration.Status.CURRENT ? "[已连接]":"[未连接]");
			sb.append(" "+config.SSID.replaceAll("\"", "") + connected);
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	private String readHtmlString() {
		try {
			InputStream is = context.getResources().getAssets().open("info.html");
			byte[] buffer = new byte[2048]; //assets目录单个文件小于1024kb，一次性读入
			int count = is.read(buffer); //实际读取字节数
			return new String(buffer, 0, count, "utf-8");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service Stop...");
	}
}
