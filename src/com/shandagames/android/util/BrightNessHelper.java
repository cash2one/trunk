package com.shandagames.android.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

/**
 * @file BrightNessHelper.java
 * @create 2013-2-18 下午03:09:26
 * @author lilong
 * @description TODO 屏幕亮度执行操作
 */
public class BrightNessHelper {

	/** 判断是否开启了自动亮度调节  */
	public static boolean isAutoBrightness(ContentResolver contentResolver) {
	    boolean automicBrightness = false;
	    try {
	        automicBrightness = Settings.System.getInt(contentResolver,
	                Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	    } catch (SettingNotFoundException e) {
	        e.printStackTrace();
	    }
	    return automicBrightness;
	}
	
	/** 获取当前屏幕亮度  */
	public static int getScreenBrightness(Activity activity) {
	    int nowBrightnessValue = 0;
	    ContentResolver resolver = activity.getContentResolver();
	    try {
	        nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return nowBrightnessValue;
	}
	
	/** 设置屏幕亮度  注意：需要判断是否开启自动调节亮度，否则设置无效 */
	public static float setBrightness(Activity activity, int brightness) {
		if (isAutoBrightness(activity.getContentResolver())) {
			stopAutoBrightness(activity);
		}
	    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
	    //float nowBrightnessValue = Float.valueOf(brightness) * (1.0f / 255f);
	    float nowBrightnessValue = Float.valueOf(brightness) / (1.0f * 255f);
	    lp.screenBrightness = nowBrightnessValue;
	    activity.getWindow().setAttributes(lp);
	    return nowBrightnessValue;
	}
	
	/** 开启亮度自动调节 */
	public static void startAutoBrightness(Activity activity) {
	    Settings.System.putInt(activity.getContentResolver(),
	            Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
	
	/** 停止自动亮度调节 */
	public static void stopAutoBrightness(Activity activity) {
	    Settings.System.putInt(activity.getContentResolver(),
	            Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	
	/** 保存调整亮度状态 */
	public static void saveBrightness(ContentResolver resolver, int brightness) {
	    Uri uri = Settings.System.getUriFor("screen_brightness");
	    Settings.System.putInt(resolver, "screen_brightness", brightness);
	    // resolver.registerContentObserver(uri, true, myContentObserver);
	    resolver.notifyChange(uri, null);
	}
	
	/** 开启LED灯光  */
	public static void turnFlashLEDLight(Camera camera, boolean isActive) {
		try {
			Parameters parameter = camera.getParameters();
			if (isActive) {		
				parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameter);				
				camera.startPreview();
			} else {  
				parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameter);
				camera.stopPreview();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
