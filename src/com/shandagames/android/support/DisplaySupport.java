package com.shandagames.android.support;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class DisplaySupport {

	public static final int LAYER_TYPE_SOFTWARE = 1;
	public static final int FLAG_HARDWARE_ACCELERATED = 0x01000000; 
	
    public static final int SCREEN_DENSITY_LOW = 120;
    public static final int SCREEN_DENSITY_MEDIUM = 160;
    public static final int SCREEN_DENSITY_HIGH = 240;

    private static int screenDensity = -1;

    /** 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）  */
    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    /** 判断是否是模拟器  */
	public static boolean isEmulator() {
		return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
	}
    
	public static boolean isGoogleTV(Context context) {
    	return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

	/** Android 2.2 */
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    /** Android 2.2 */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    /** Android 3.0. */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    /** Android 3.0. */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    /** Android 3.0. */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    /** 判断是否是平板（官方用法） */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }
	
	
    /** dip转化成px */
	public static int dip2px(Context context, float dipValue) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final float scale = dm.density;
		return (int) (dipValue * scale + 0.5f);
	}

	/** px转化成dip */
	public static int px2dip(Context context, float pxValue) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final float scale = dm.density;
		return (int) (pxValue / scale + 0.5f);
	}
	
    /** 获取当前设备的densityDpi值  */
    public static int getScreenDensity(Context context) {
        if (screenDensity == -1) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            try {
                screenDensity = DisplayMetrics.class.getField("densityDpi").getInt(displayMetrics);
            } catch (Exception e) {
                screenDensity = SCREEN_DENSITY_MEDIUM;
            }
        }
        return screenDensity;
    }
    
    /** 3.0以上设备启用硬件加速  */
    public static void setHardwareAccelerated(Window window) {
    	if (Build.VERSION.SDK_INT >= 11) {
    		try {
    		    Method method = Window.class.getMethod("setFlags", int.class, int.class);
		        method.invoke(window, FLAG_HARDWARE_ACCELERATED, FLAG_HARDWARE_ACCELERATED);
		    } catch (Exception e) {
		        Log.e("HardwareAccelerated", "Hardware Acceleration not supported on API " + Build.VERSION.SDK_INT, e);
		    }
    	}
    }

    /** 3.0以上设备禁用硬件加速  */
    public static void setHardwareAccelerated(View view) {
    	if (Build.VERSION.SDK_INT >= 11) {
    		try {
    		    Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
		        method.invoke(view, LAYER_TYPE_SOFTWARE, null);
		    } catch (Exception e) {
		        Log.e("HardwareAccelerated", "Hardware Acceleration not supported on API " + Build.VERSION.SDK_INT, e);
		    }
    	}
    }
    
    
    /**
     * Enumeration of the currently known SDK version codes.  These are the
     * values that can be found in {@link VERSION#SDK}.  Version numbers
     * increment monotonically with each official platform release.
     */
    public static class VERSION_CODES {
        /**
         * Magic version number for a current development build, which has
         * not yet turned into an official release.
         */
        public static final int CUR_DEVELOPMENT = 10000;
        
        /**
         * October 2008: The original, first, version of Android.  Yay!
         */
        public static final int BASE = 1;
        
        /**
         * February 2009: First Android update, officially called 1.1.
         */
        public static final int BASE_1_1 = 2;
        
        /**
         * May 2009: Android 1.5.
         */
        public static final int CUPCAKE = 3;
        
        /**
         * September 2009: Android 1.6.
         */
        public static final int DONUT = 4;
        
        /**
         * November 2009: Android 2.0
         */
        public static final int ECLAIR = 5;
        
        /**
         * December 2009: Android 2.0.1
         */
        public static final int ECLAIR_0_1 = 6;
        
        /**
         * January 2010: Android 2.1
         */
        public static final int ECLAIR_MR1 = 7;
        
        /**
         * June 2010: Android 2.2
         */
        public static final int FROYO = 8;
        
        /**
         * November 2010: Android 2.3
         */
        public static final int GINGERBREAD = 9;
        
        /**
         * February 2011: Android 2.3.3.
         */
        public static final int GINGERBREAD_MR1 = 10;

        /**
         * February 2011: Android 3.0.
         */
        public static final int HONEYCOMB = 11;
        
        /**
         * May 2011: Android 3.1.
         */
        public static final int HONEYCOMB_MR1 = 12;
        
        /**
         * June 2011: Android 3.2.
         */
        public static final int HONEYCOMB_MR2 = 13;

        /**
         * October 2011: Android 4.0.
         */
        public static final int ICE_CREAM_SANDWICH = 14;

        /**
         * Android 4.0.3.
         */
        public static final int ICE_CREAM_SANDWICH_MR1 = 15;
        
        /**
         * Android 4.1.
         */
        public static final int JELLY_BEAN = 16;
    }
}