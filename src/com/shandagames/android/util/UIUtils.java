package com.shandagames.android.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.ResultReceiver;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.Log;
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

    /** 获取屏幕的宽度  */
    public static float getScreenWidth(Context context) {
    	return context.getResources().getDisplayMetrics().widthPixels;
    }
    
    /** 获取屏幕的高度  */
    public static float getScreenHeight(Context context) {
    	return context.getResources().getDisplayMetrics().heightPixels;
    }
    
    /** dip转化成px */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/** px转化成dip */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
    
	/** px转化成sp */
	public static float px2sp(Context ctx, float px) {
    	float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
    	return (px / scaledDensity);
    }
 
	/** sp转化成px */
    public static float sp2px(Context ctx, float sp) {
        float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
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
		    System.out.println("get status bar height failure");
		}  
		return sbar;
	}
	
	/** 设置震动频率  */
	public static void vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	
	/** 设置震动频率 ，isRepeat 是否重复震动  */
	public static void vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
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
	
	/** 读取照片的方向  */ 
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("BackwardSupportUtil", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}
}
