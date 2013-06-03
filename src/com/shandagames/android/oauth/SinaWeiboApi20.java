package com.shandagames.android.oauth;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.shandagames.android.network.QHttpClient;


public class SinaWeiboApi20 extends Api {

	private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize?client_id=%1$s&response_type=code&redirect_uri=%2$s&display=mobile";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

	
	public SinaWeiboApi20(Token token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAuthorizationUrl() {
		// TODO Auto-generated method stub
		return String.format(AUTHORIZE_URL, mToken.getApiKey(), mToken.getRedirectUrl());
	}

	@Override
	public String getAccessTokenEndpoint() {
		// TODO Auto-generated method stub
		return ACCESS_TOKEN_URL;
	}

	public String retrieveAccessToken(String url) {
		Bundle bundle = parseUrl(url);
		String oauthVerifier = bundle.getString("code");
		QHttpClient httpClient = QHttpClient.getInstance();
		String content = httpClient.doHttpPost(getAccessTokenEndpoint(),
				new BasicNameValuePair("client_id", mToken.getApiKey()),
				new BasicNameValuePair("client_secret", mToken.getApiSecret()),
				new BasicNameValuePair("redirect_uri", mToken.getRedirectUrl()),
				new BasicNameValuePair("grant_type", "authorization_code"),
				new BasicNameValuePair("code", oauthVerifier));
		return content;
	}
	
}
