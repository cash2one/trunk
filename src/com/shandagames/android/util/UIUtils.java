package com.shandagames.android.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.shandagames.android.log.LogUtils;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

/**
 * @file ApkUtils.java
 * @create 2012-9-19 下午6:15:39
 * @author lilong
 * @description 封装UI界面常用操作
 */
public final class UIUtils {

	public static int sdkVersion() {
    	return Build.VERSION.SDK_INT;
    }

	public static File URItoFile(String URI) {
        return new File(Uri.decode(URI).replace("file://", ""));
    }

    public static String URItoFileName(String URI) {
        int sep=URI.lastIndexOf('/');
        int dot=URI.lastIndexOf('.');
        String name=dot>=0 ? URI.substring(sep+1, dot) : URI;
        return Uri.decode(name);
    }
    
    /** 计算字宽  */
    public static float getTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }
    
	/** 获取标题栏高度:注意 在onCreate()中获取为0  */
	public static int getTitleBarHeight(Activity activity) {
		// 获取windows最顶层view
		View decorView = activity.getWindow().getDecorView();
		Rect outRect = new Rect();
		// 获取程序显示区域(1.标题栏;2.自定义布局区域)
		decorView.getWindowVisibleDisplayFrame(outRect);
		// 获取程序内容区域
		View customView = decorView.findViewById(Window.ID_ANDROID_CONTENT);
		
		final int decorBarHeight = outRect.top;
		final int contentTop = customView.getTop();
		final int titleBarHeight = decorBarHeight - contentTop;

		return titleBarHeight;
	}
	
	/** 获取状态栏高度 */
	public static int getStatusBarHight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
		    c = Class.forName("com.android.internal.R$dimen");
		    obj = c.newInstance();
		    field = c.getField("status_bar_height");
		    x = Integer.parseInt(field.get(obj).toString());
		    sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception ex) {
		    LogUtils.out("get status bar height failure");
		}  
		return sbar;
	}
	
	
	/** 获取设备IP地址 */
	// see http://androidsnippets.com/obtain-ip-address-of-current-device
	public static String getIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ip = ipIntToString(wifiInfo.getIpAddress());
		return ip;
	}
	
	public static String ipIntToString(int ip) {
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
	
    /** 匹配手机号码  */
    public static boolean isMobileNum(String mobile) {
    	 Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");        
         Matcher m = p.matcher(mobile);        
         return m.matches(); 
    }
    
    /** 检查是否首次运行已应用 */
    public static boolean checkIfIsFirstRun(Context context) {
    	final String PACKAGE_NAME = context.getPackageName();
        File file = new File(
        		"/data/data/"+PACKAGE_NAME+"/shared_prefs/"+PACKAGE_NAME+"_preferences.xml");
        return !file.exists();
    }
    
	/** 强制隐藏输入法窗口  */
	public static void hideSoftInputFromWindow(View view) {
		//实例化输入法控制对象，通过hideSoftInputFromWindow来控制
		InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	/** 显示输入法窗口 */
	public static void showSoftInputFromInputMethod(View view) {
		showSoftkeyboard(view, null);
	}
    
	/** 显示输入法窗口 */
	public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
		Configuration config = view.getContext().getResources().getConfiguration();
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	
			if (resultReceiver != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
			} else {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
	
	/** 更改系统选项菜单的默认背景 */
	public static void setMenuBackground(final Activity activity, final int resId) {
		activity.getLayoutInflater().setFactory(new LayoutInflater.Factory() {
			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
    				try { // Ask our inflater to create the view
    					LayoutInflater f = activity.getLayoutInflater();
    					final View view = f.createView(name, null, attrs);
    					/* 
    					 * The background gets refreshed each time a new item is added the options menu. 
    					 * So each time Android applies the default background we need to set our own background. 
    					 * This is done using a thread giving the background change as runnable object
    					 */
    					activity.runOnUiThread(new Runnable() {
    						@Override
    						public void run() {
    							view.setBackgroundResource(resId);
    						}
    					});
    					return view;
    				}
    				catch (InflateException e) {}
    				catch (ClassNotFoundException e) {}
    			}
    			return null;
			}
		});
	}

	// And to convert the image URI to the direct file system path of the image file 
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {   
		// can post image         
		String [] proj={MediaStore.Images.Media.DATA};         
		Cursor cursor = activity.managedQuery(contentUri,                  
						proj, 	// Which columns to return                      
						null,   // WHERE clause; which rows to return (all rows)            
						null,   // WHERE clause selection arguments (none)           
						null);  // Order-by clause (ascending by name)      
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);        
		cursor.moveToFirst();          
		return cursor.getString(column_index); 
	}
	
	/*
	 * Getting the Type of File as String 
	 */
	public static String getExtensionFromMimeType(Context cxt, Uri uri) {
		ContentResolver cR = cxt.getContentResolver(); 
		MimeTypeMap mime = MimeTypeMap.getSingleton(); 
		String type = mime.getExtensionFromMimeType(cR.getType(uri));
		return type;
	}
	
	/** 开启或关闭WiFi网络 */
	public static void toggleNetWork(Context ctx, boolean status) {
		//<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
		//<uses-permission android:name="android.permission.UPDATE_DEVICE_STATE" />
		//<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
		WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(status);
	}
	
	/** 检查GPS开关的状态 */
	public static boolean isGPSEnable(ContentResolver cr) {
		String str = Settings.Secure.getString(cr, Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) {
			return str.contains("gps");
		} else{
			return false;
		}
	}
	
	/** 开启或关闭GPS */
	@TargetApi(8)
	public static void toggleGPS(Context ctx) {
		if (sdkVersion() >= 8) {
			boolean status = isGPSEnable(ctx.getContentResolver());
			System.out.println("current gps status:"+status);
			Settings.Secure.setLocationProviderEnabled(ctx.getContentResolver(), "gps", !status);
		} else {
			Intent gpsIntent = new Intent();
			gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
			gpsIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(ctx, 0, gpsIntent, 0).send();
			} catch (CanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** 移动动画  */
	public static void moveFrontAnimation(View v, int startX, int toX, int startY, int toY) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
        anim.setDuration(200);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }
	
	/**
	 * This code loads a custom HTML from a string which references a local image
	 * for this to work, simply place the image in the directory /assets/
	 * see http://androidsnippets.com/webview-with-custom-html-and-local-images
	 */
	public static void loadHTML(WebView wv, String html) {
	    final String mimeType = "text/html";
	    final String encoding = "utf-8";
	    //String html = "<h1>Header</h1><p><img src=\"file:///android_asset/image1.jpg\" /></p>";
	    	
	    wv.loadDataWithBaseURL("fake://not/needed", html, mimeType, encoding, "");
	}
	
    
}
