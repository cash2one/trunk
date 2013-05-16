package org.scribe.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


/**
 * @file Utility.java
 * @create 2013-4-24 上午11:09:01
 * @author lilong
 * @description TODO
 */
public final class Utility {

	private Utility() {
	}
	
	
	/**
     * Save current context cookies .
     * 
     * @param context
     *            : current activity context.
     * 
     * @return void
     */
	public static void storeCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieSyncManager.getInstance().sync();
	}
	
	/**
     * Clear current context cookies .
     * 
     * @param context
     *            : current activity context.
     * 
     * @return void
     */
    public static void clearCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
	
	/**
     * Display a simple alert dialog with the given text and title.
     * 
     * @param context
     *            Android context in which the dialog should be displayed
     * @param title
     *            Alert dialog title
     * @param text
     *            Alert dialog message
     */
    public static void showAlert(Context context, String title, String text) {
        Builder alertBuilder = new Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(text);
        alertBuilder.create().show();
    }
	
    
    /**
     * 解析queryString，取得Bundle格式存储的参数队列
     * 
     * @param queryString 查询字符串
     * @return 以Bundle格式存储的参数队列.
     */
    public static Bundle parseUrl(String url) {
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        } 
    }
    
    public static Bundle decodeUrl(String str) {
        Bundle params = new Bundle();
        if (str != null && !str.equals("")) {
        	String array[] = str.split("&");
        	for (String s : array) {
                if (s != null && !s.equals("")) {
                    if (s.indexOf('=') > -1) {
                        String[] v = s.split("=");
                        if (v.length > 1) {
                        	 params.putString(v[0], decode(v[1]));
                        }
                    }
                }
            }
        }
        return params;
    }
    
    /**
     * 根据Bundle格式存储的参数队列，生成queryString
     * @param QueryParamsList
     * @return 不包括？的queryString
     */
    public static String getQueryString(Bundle bundle) {
    	if (bundle != null && !bundle.isEmpty()) {
	    	StringBuilder queryString=new StringBuilder();
	    	Iterator<String> params = bundle.keySet().iterator();
	    	while (params.hasNext()) {
	    		String key = params.next();
	    		queryString.append('&');
	            queryString.append(key);
	            queryString.append('=');
	            queryString.append(encode(bundle.getString(key)));
	    	}
	    	//去掉第一个&号
	        return queryString.substring(1);
    	}
		return null;
    }
    
    public static String encode(String s) {
    	try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public static String decode(String s) {
    	try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}
