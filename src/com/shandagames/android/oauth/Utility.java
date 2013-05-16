package com.shandagames.android.oauth;

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
	
	/** 第三方分享平台标识  */
	public static final String TWITTER = "twitter";
	public static final String FACEBOOK = "facebook";
	public static final String RENREN = "renren";
	public static final String SINA = "sina";
	public static final String TENCENT = "tencent";
	public static final String DOUBAN = "douban";
	public static final String QQ = "qq";
	
	
	/** 新浪微博平台  */
	public static final String SINA_APP_KEY = "3275598438";
	public static final String SINA_APP_SECRET = "8d60dedab2c572e6f7603ea9f47fa729";
	public static final String SINA_REDIRECT_URL = "http://weibo.com/selience";
	public static final String SINA_OAUTH2_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";
	public static final String SINA_OAUTH2_REQUEST_TOKEN = "https://api.weibo.com/oauth2/request_token";
	public static final String SINA_OAUTH2_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";
	public static final String SINA_API_V1_BASE_URL = "https://api.weibo.com/2";
	
	/** 腾讯微博平台  */
	public static final String TENCENT_APP_KEY = "801179720";
	public static final String TENCENT_APP_SECRET = "0417dc3b61da4d892bc3ba1dd3f46ec0";
	public static final String TENCENT_REDIRECT_URL = "http://www.csdn.net";
	public static final String TENCENT_OAUTH2_AUTHORIZE = "https://open.t.qq.com/cgi-bin/oauth2/authorize";
	public static final String TENCENT_OAUTH2_REQUEST_TOKEN = "https://open.t.qq.com/cgi-bin/request_token";
	public static final String TENCENT_OAUTH2_ACCESS_TOKEN = "https://open.t.qq.com/cgi-bin/oauth2/access_token";
	public static final String TENCENT_API_V1_BASE_URL = "http://open.t.qq.com/api";
	
	/** QQ空间分享平台  */
//	public static final String QQ_APP_KEY = "100425739";
//	public static final String QQ_APP_SECRET = "15fcc80a7fa94c39a6afd7485b298cdb";
	public static  final String QQ_APP_KEY = "6da0c3353d8c4cbcb9b12e15474f3855";
	public static  final String QQ_APP_SECRET = "4a3932db3faa9bf8475cb0ad19f3f38d";
	public static final String QQ_REDIRECT_URL = "http://www.csdn.net";
	public static final String QQ_OAUTH2_REQUEST_TOKEN = "https://graph.qq.com/oauth2.0/request_token";
	public static final String QQ_OAUTH2_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/access_token";
	public static final String QQ_OAUTH2_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
	
	/** 豆瓣分享平台  */
	public static final String DOUBAN_APP_KEY = "088683542dd3bb87199389fc897b3295";
	public static final String DOUBAN_APP_SECRET = "4e25cc60bd76dce8";
	public static final String DOUBAN_OAUTH2_REQAUEST_TOKEN = "http://www.douban.com/service/auth/request_token";
	public static final String DOUBAN_OAUTH2_ACCESS_TOKEN = "http://www.douban.com/service/auth/access_token";
	public static final String DOUBAN_OAUTH2_AUTHORIZE = "http://www.douban.com/service/auth/authorize";
	
	
	public static String requestSinaAuthorize() {
		Bundle bundle = new Bundle();
		bundle.putString("client_id", SINA_APP_KEY);
		bundle.putString("response_type", "token");
		bundle.putString("redirect_uri", SINA_REDIRECT_URL);
		bundle.putString("display", "mobile");
		String url = SINA_OAUTH2_AUTHORIZE + "?" + getQueryString(bundle);
		return url;
	}
	
	public static String requestTencentAuthorize() {
		Bundle bundle = new Bundle();
		bundle.putString("client_id", TENCENT_APP_KEY);
		bundle.putString("redirect_uri", TENCENT_REDIRECT_URL);
		bundle.putString("response_type", "code");
		String url = TENCENT_OAUTH2_AUTHORIZE + "?" + getQueryString(bundle);
		return url;
	}
	
	public static String requestTencentAccessToken(String code) {
		Bundle bundle = new Bundle();
		bundle.putString("client_id", TENCENT_APP_KEY);
		bundle.putString("client_secret", TENCENT_APP_SECRET);
		bundle.putString("redirect_uri", TENCENT_REDIRECT_URL);
		bundle.putString("grant_type", "authorization_code");
		bundle.putString("code", code);
		String url = TENCENT_OAUTH2_ACCESS_TOKEN + "?" + getQueryString(bundle);
		return url;
	}
	
	public static String requestQQAuthorize() {
		Bundle bundle = new Bundle();
		bundle.putString("client_id", QQ_APP_KEY);
		bundle.putString("response_type", "token");
		bundle.putString("display", "mobile");
		bundle.putString("redirect_uri", QQ_REDIRECT_URL);
		String url = QQ_OAUTH2_AUTHORIZE + "?" + getQueryString(bundle);
		return url;
	}
	
	public static String requestDoubanAuthorize() {
		Bundle bundle = new Bundle();
		return "";
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
