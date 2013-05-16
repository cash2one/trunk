package com.shandagames.android.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 */
public class AccessTokenKeeper {
	public static final String PREFERENCES_NAME = "com_weibo_sdk_android";
	
	public static final String SINA_ACCESS_TOKEN = "sina_accessToken";
	public static final String SINA_EXPIRE_TIME = "sina_expiresTime";
	
	public static final String TENCENT_ACCESS_TOKEN = "tencent_accessToken";
	public static final String TENCENT_EXPIRE_TIME = "tencent_expiresTime";
	
	/**
	 * 保存accesstoken到SharedPreferences
	 * @param context Activity 上下文环境
	 * @param token Oauth2AccessToken
	 */
	public static void keepSinaAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(SINA_ACCESS_TOKEN, token.getToken());
		editor.putLong(SINA_EXPIRE_TIME, token.getExpiresTime());
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readSinaAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString(SINA_ACCESS_TOKEN, ""));
		token.setExpiresTime(pref.getLong(SINA_EXPIRE_TIME, 0));
		return token;
	}
	
	public static void keepTencentAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(TENCENT_ACCESS_TOKEN, token.getToken());
		editor.putLong(TENCENT_EXPIRE_TIME, token.getExpiresTime());
		editor.commit();
	}
	
	public static Oauth2AccessToken readTencentAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString(TENCENT_ACCESS_TOKEN, ""));
		token.setExpiresTime(pref.getLong(TENCENT_EXPIRE_TIME, 0));
		return token;
	}
	
	/**
	 * 清空sharepreference
	 * @param context
	 */
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
}
