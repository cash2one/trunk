package com.shandagames.android.oauth;

import android.app.AlertDialog.Builder;
import android.content.Context;
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
	
	/** 腾讯微博平台  */
	public static final String TENCENT_APP_KEY = "801179720";
	public static final String TENCENT_APP_SECRET = "0417dc3b61da4d892bc3ba1dd3f46ec0";
	public static final String TENCENT_REDIRECT_URL = "http://www.csdn.net";

	/** QQ互联平台  */
	public static final String QQ_APP_KEY = "100425739";
	public static final String QQ_APP_SECRET = "15fcc80a7fa94c39a6afd7485b298cdb";
	public static final String QQ_REDIRECT_URL = "http://www.csdn.net/";
	
	/** 豆瓣分享平台  */
	public static final String DOUBAN_APP_KEY = "088683542dd3bb87199389fc897b3295";
	public static final String DOUBAN_APP_SECRET = "4e25cc60bd76dce8";
	public static final String DOUBAN_REDIRECT_URL = "http://www.csdn.net";
	
	/** 人人网分享平台  */
	public static final String RENREN_APP_KEY = "beb42ea1ea7a4dd6b0321ea0598a1e7d";
	public static final String RENREN_APP_SECRET = "23658454a49b4ad19113d8d5b6d82947";
	public static final String RENREN_REDIRECT_URL = "http://graph.renren.com/oauth/login_success.html";
	
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
	
}
