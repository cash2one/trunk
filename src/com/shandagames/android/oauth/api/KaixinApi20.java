package com.shandagames.android.oauth.api;

import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.support.StrOperate;

/**
 * Kaixin(http://www.kaixin001.com/) open platform api based on OAuth 2.0.
 */
public class KaixinApi20 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "http://api.kaixin001.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code";
	
	private static final String ACCESS_TOKEN_URL = "https://api.kaixin001.com/oauth2/access_token?grant_type=authorization_code";
	
	
	public KaixinApi20(OAuth token) {
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

	@Override
	public String retrieveAccessToken(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
