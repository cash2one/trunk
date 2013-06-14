package com.shandagames.android.oauth.api;

import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import com.shandagames.android.network.QHttpClient;
import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.support.StrOperate;

public class SinaWeiboApi20 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize?client_id=%1$s&response_type=code&redirect_uri=%2$s&display=mobile";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

	
	public SinaWeiboApi20(OAuth token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAuthorizationUrl() {
		// TODO Auto-generated method stub
		return String.format(AUTHORIZE_URL, mOauth.getOauthConsumerKey(), StrOperate.paramEncode(mOauth.getOauthCallback()));
	}

	@Override
	public String getAccessTokenEndpoint() {
		// TODO Auto-generated method stub
		return ACCESS_TOKEN_URL;
	}

	public String retrieveAccessToken(String url) {
		Bundle bundle = StrOperate.parseUrl(url);
		String oauthVerifier = bundle.getString("code");
		QHttpClient httpClient = QHttpClient.getInstance();
		String content = httpClient.doHttpPost(getAccessTokenEndpoint(),
				new BasicNameValuePair("client_id", mOauth.getOauthConsumerKey()),
				new BasicNameValuePair("client_secret", mOauth.getOauthConsumerSecret()),
				new BasicNameValuePair("redirect_uri", mOauth.getOauthCallback()),
				new BasicNameValuePair("grant_type", "authorization_code"),
				new BasicNameValuePair("code", oauthVerifier));
		return content;
	}
	
}
