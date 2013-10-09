package com.shandagames.android.log;

import java.util.Locale;
import android.util.Log;

/**
 * @project trunk
 * @file LogUtils.java
 * @create 2012-11-9 下午9:15:58
 * @author selience
 * @description Helper methods that make logging more consistent throughout the app.
 */
public class LogUtils {

    private static boolean DEBUG = false;
    
    private static String TAG = "makeLog";
    
    
    public static void enableDebugLogging(boolean enabled) {
    	DEBUG = enabled;
    }
    
    public static void setTag(String tag) {
    	TAG = tag;
    }
    
    public static void setTag(Class<?> clazz) {
    	TAG = clazz.getSimpleName();
    }
    
    public static void v(String msgFormat, Object... args) {
    	if (DEBUG) {
    		Log.v(TAG, buildMessage(msgFormat, args));
    	}
    }
    
    public static void d(String msgFormat, Object... args) {
    	if (DEBUG) {
    		Log.d(TAG, buildMessage(msgFormat, args));
    	}
    }
    
    public static void i(String msgFormat, Object... args) {
    	if (DEBUG) {
    		Log.i(TAG, buildMessage(msgFormat, args));
    	}
    }
    
    public static void w(String msgFormat, Object... args) {
    	if (DEBUG) {
    		Log.w(TAG, buildMessage(msgFormat, args));
    	}
    }
    
    public static void e(String msgFormat, Object... args) {
    	if (DEBUG) {
    		Log.e(TAG, buildMessage(msgFormat, args));
    	}
    }
    
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String traceInfo = ste.getClassName() + "::";
		traceInfo += ste.getMethodName();
		traceInfo += "@" + ste.getLineNumber() + ">>>";
        return String.format(Locale.US, "[%d] %s",
                Thread.currentThread().getId(), traceInfo + msg);
    }
    
}
