package com.shandagames.android.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.util.Log;

/**
 * @project trunk
 * @file LogUtils.java
 * @create 2012-11-9 下午9:15:58
 * @author selience
 * @description Helper methods that make logging more consistent throughout the app.
 */
public class LogUtils {

    private static boolean debug = false;
    
    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    public static String makeLogTag(Class<?> clazz){
        return clazz.getSimpleName();
    }
    
    public static void isDebug(boolean isDebug){
        debug = isDebug;
    }
    
    public static void d(final String tag, String message) {
        if (debug && Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void d(final String tag, String message, Throwable cause) {
        if (debug && Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);
        }
    }

    public static void v(final String tag, String message) {
        if (debug && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    public static void v(final String tag, String message, Throwable cause) {
        if (debug && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message, cause);
        }
    }
    
    public static void i(final String tag, String message) {
    	if (debug && Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, message);
        }
    }

    public static void i(final String tag, String message, Throwable cause) {
        if (debug && Log.isLoggable(tag, Log.INFO)) {
        	Log.i(tag, message, cause);
        }
    }
    
    
    public static void w(String tag, String message){
    	 if (debug && Log.isLoggable(tag, Log.WARN)) {
         	Log.w(tag, message);
         }
    }
    
    public static void w(String tag, String message, Throwable cause){
   	 if (debug && Log.isLoggable(tag, Log.WARN)) {
        	Log.w(tag, message, cause);
        }
   }
    
    public static void e(String tag, String message){
        if(debug && Log.isLoggable(tag, Log.ERROR)){
            Log.e(tag, message);
        }
    } 
    
    public static void e(String tag, String message, Throwable cause){
        if(debug && Log.isLoggable(tag, Log.ERROR)){
            Log.e(tag, message);
        }
    } 
    
    public static void out(Object message) {
    	if(debug) {
    		System.out.println(message);
    	}
    }
    
    public static void out(Object message, Throwable cause) {
    	if(debug) {
    		System.out.println(message);
    		cause.printStackTrace();
    	}
    }
    
    private LogUtils() {
    }
    
    /*
     * capture application log. add android.permission.READ_LOGS;
     */
    public static String captureAppLog() {
    	Process mLogcatProc = null; 
    	BufferedReader reader = null;
    	
    	try {
    		mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});          
    		reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));         
    		String line;         
    		StringBuffer log = new StringBuffer();         
    		String separator = System.getProperty("line.separator");           
    		while ((line = reader.readLine()) != null) {                 
    			log.append(line);                 
    			log.append(separator);         
    		} 
    		return log.toString();
    		
    	} catch(IOException ex) {
    		out("capture application log occur error :" + ex.getMessage());
    	} finally {
    		if(reader != null) {
    			try {
    				reader.close();
    			} catch(IOException ex){
    			}
    		}
    	}
    	return "";
    }
}
