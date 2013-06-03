package com.shandagames.android.oauth;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.os.Bundle;

public abstract class Api {

	protected Token mToken;

	Api(Token token) {
		this.mToken = token;
	}

	/**
	 * Returns the URL where you should redirect your users to authenticate your application.
	 * 
	 * @return the URL where you should redirect your users
	 */
	public abstract String getAuthorizationUrl();

	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	public abstract String getAccessTokenEndpoint();

	
	
	public abstract String retrieveAccessToken(String url);

	
	public Token getToken() {
		return this.mToken;
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
