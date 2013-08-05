package com.shandagames.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.util.ToastUtil;

public class WiFiSettingActivity extends BaseActivity implements View.OnClickListener {

	//监听端口
	private static final int CONNECTION_POST = 4392; 
	
	private ServerSocket serverSocket;
	private WifiManager wifiManager;
	private WifiInfo wifiInfo;
	
	private Sensor sensor;
	private SensorManager sensorManager;
	
	private TextView mTextView;
	private LinearLayout container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		container = new LinearLayout(this);
		container.setOrientation(LinearLayout.VERTICAL);
		setContentView(container);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.topMargin = 10;
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo(); //当前连接的WiFi网络
		
		TextView txtView = new TextView(this);
		txtView.setTextSize(16);
		//显示手机可访问网址，通过该网址访问webserver
		txtView.setText("访问地址：http://" + ipIntToString(wifiInfo.getIpAddress()) + ":" + CONNECTION_POST);
		Linkify.addLinks(txtView, Linkify.WEB_URLS);
		container.addView(txtView, lp);
		
		//监视客户端请求
		new Thread(new ServerThread()).start();
		
		
		Button btnGtalk = new Button(this);
		btnGtalk.setId(0x1000);
		btnGtalk.setText("连接Gtalk服务器");
		container.addView(btnGtalk, lp);
		btnGtalk.setOnClickListener(this);
		
		
		mTextView = new TextView(this);
		mTextView.setTextSize(16);
		container.addView(mTextView, lp);
		
		// 获取方向传感器
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorEventListener, 
				sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		sensorManager.unregisterListener(sensorEventListener);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 0x1000:
			new Thread(new Runnable() {
				@Override
				public void run() {
					connectGtalkServer();
				}
			}).start();
			break;
		}
		
	}

	private void connectGtalkServer() {
		try {
			//指定Gtalk服务器地址和端口号
			ConnectionConfiguration configuration = new ConnectionConfiguration(
					"talk.google.com", 5222, "gmail.com");
			//根据配置的地址和端口号创建xmpp对象
			XMPPConnection xmppConnection = new XMPPConnection(configuration);
			// 连接gtalk服务器
			xmppConnection.connect(); 
			// 使用账号和密码登陆服务器
			xmppConnection.login("dreamxsky@gmail.com", "5340Selience");
			
			Presence presence = new Presence(Presence.Type.available);
			//登陆成功向gtalk服务器发送一条消息，表明当前处于活动状态
			xmppConnection.sendPacket(presence);
			
			StringBuilder sb = new StringBuilder();
			Collection<RosterEntry> entries = xmppConnection.getRoster().getEntries();
			for (RosterEntry entry : entries) {
				sb.append(entry.getName()+":"+entry.getUser()+"\n");
			}
			if (sb.length()>0) sb=sb.deleteCharAt(sb.length()-1);
			
			ToastUtil.showMessage(this, sb.toString());
		} catch (XMPPException ex) {
			ToastUtil.showMessage(this, "登陆失败");
		}
	}
	
	private String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte)(0xff & ip);
			bytes[1] = (byte)((0xff00 & ip) >> 8);
			bytes[2] = (byte)((0xff0000 & ip) >> 16);
			bytes[3] = (byte)((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private String listUsingNetWork() {
		//扫描所有配置的网络，找到当前正在使用的网络
		StringBuilder sb = new StringBuilder();
		for (WifiConfiguration config : wifiManager.getConfiguredNetworks()) {
			sb.append(" "+config.SSID.replaceAll("\"", "") + (config.status==0?"[已连接]":"[未连接]"));
		}
		return sb.toString();
	}
	
	private String readHtml() {
		try {
			InputStream is = getResources().getAssets().open("info.html");
			byte[] buffer = new byte[2048]; //assets目录单个文件小于1024kb，一次性读入
			int count = is.read(buffer); //实际读取字节数
			return new String(buffer, 0, count, "utf-8");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private final SensorEventListener sensorEventListener = new SensorEventListener() {
		
		//方向发生改变
		@Override
		public void onSensorChanged(SensorEvent event) {
			//x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			mTextView.setText("方向传感器：" + x+","+y+","+z);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
	
	class ServerChannel implements Runnable {

		@Override
		public void run() {
			Selector selector = null;
			ServerSocketChannel serverSocketChannel = null;
			try {
				//http://www.cnblogs.com/cpcpc/archive/2011/06/27/2123005.html
				selector = Selector.open();//实例化selector
				serverSocketChannel = ServerSocketChannel.open(); //实例化ServerSocketChannel 对象
				serverSocketChannel.configureBlocking(false); //设置为非阻塞模式
				serverSocket = serverSocketChannel.socket();
				serverSocket.bind(new InetSocketAddress(CONNECTION_POST));//绑定端口为1987
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册关心的事件，对于Server来说主要是accpet了
				
				while (true) {
					int n= selector.select(); //获取感兴趣的selector数量
					if(n < 1) continue; //如果没有则一直轮训检查
				    Iterator<SelectionKey> it = selector.selectedKeys().iterator(); //有新的链接，我们返回一个SelectionKey集合
				    while (it.hasNext()) {
					     SelectionKey key = it.next(); //使用迭代器遍历
					     it.remove(); //删除迭代器
					     if (key.isAcceptable()) { //如果是我们注册的OP_ACCEPT事件
						      ServerSocketChannel ssc2 = (ServerSocketChannel) key.channel();
						      SocketChannel channel = ssc2.accept();
						      channel.configureBlocking(false); //同样是非阻塞
						      channel.register(selector, SelectionKey.OP_READ); //本次注册的是read事件，即receive接受
						      System.out.println("CWJ Client :" + channel.socket().getInetAddress().getHostName() + ":"  + channel.socket().getPort());
					     }  else if (key.isReadable()) { //如果为读事件
						      SocketChannel channel = (SocketChannel) key.channel();
						      ByteBuffer buffer = ByteBuffer.allocate(1024); //1KB的缓冲区
						      channel.read(buffer); //读取到缓冲区
						      buffer.flip(); //准备写入
						      System.out.println("android123 receive info:" + buffer.toString());
						      channel.write(ByteBuffer.wrap("it works".getBytes())); //返回给客户端
					     }
				    }
				   }
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					selector.close();
					serverSocketChannel.close();
				} catch (IOException ex) {
				}
			}
		}
		
	}
	
	class ServerThread implements Runnable {

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(CONNECTION_POST);
				while (true) {
					String wifiStatus = wifiManager.isWifiEnabled()?"WiFi已开启":"WiFi已关闭";
					String speed = wifiInfo.getLinkSpeed() + "Mbps";
					String listNetWork = listUsingNetWork();
					String bssid = wifiInfo.getBSSID()!=null?wifiInfo.getBSSID():"";
					// 替换占位符值
					String html = readHtml().replaceAll("#mac#", wifiInfo.getMacAddress())
								  .replaceAll("#ip#", ipIntToString(wifiInfo.getIpAddress()))
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
