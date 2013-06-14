package com.shandagames.android.oauth.api;

import org.apache.http.message.BasicNameValuePair;
import com.shandagames.android.network.QHttpClient;
import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.support.StrOperate;

import android.os.Bundle;

public class QzoneApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%1$s&redirect_uri=%2$s&state=%3$s&display=mobile";
	
	private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";
	
	
	public QzoneApi(OAuth token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAuthorizationUrl() {
		// TODO Auto-generated method stub
		return String.format(AUTHORIZE_URL, mOauth.getOauthConsumerKey(), StrOperate.paramEncode(mOauth.getOauthCallback()), "qiyu");
	}

	@Override
	public String getAccessTokenEndpoint() {
		// TODO Auto-generated method stub
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String retrieveAccessToken(String url) {
		// TODO Auto-generated method stub
		Bundle bundle = StrOperate.parseUrl(url);
		String oauthVerifier = bundle.getString("code");
		QHttpClient httpClient = QHttpClient.getInstance();
		String content = httpClient.doHttpGet(getAccessTokenEndpoint(), 
				new BasicNameValuePair("client_id", mOauth.getOauthConsumerKey()),
				new BasicNameValuePair("client_secret", mOauth.getOauthConsumerSecret()),
				new BasicNameValuePair("redirect_uri", mOauth.getOauthCallback()),
				new BasicNameValuePair("grant_type", "authorization_code"),
				new BasicNameValuePair("code", oauthVerifier));
		return content;
	}

}
