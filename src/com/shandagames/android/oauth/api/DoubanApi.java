package com.shandagames.android.oauth.api;

import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.oauth.OAuthV1;
import com.shandagames.android.oauth.OAuthV1Client;

/**
 * Douban(http://www.douban.com/) OAuth 1.0 based api.
 */
public class DoubanApi extends DefaultApi10 {

	private static final String REQUEST_TOKEN_URL = "http://www.douban.com/service/auth/request_token";
	private static final String AUTHORIZE_URL = "http://www.douban.com/service/auth/authorize";
	private static final String ACCESS_TOKEN_URL = "http://www.douban.com/service/auth/access_token";

	public DoubanApi(OAuthV1 oAuth) {
		super(oAuth);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getRequestTokenEndpoint() {
		// TODO Auto-generated method stub
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint() {
		// TODO Auto-generated method stub
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl() {
		// TODO Auto-generated method stub
		return OAuthV1Client.generateAuthorizationURL(mOauth, AUTHORIZE_URL);
	}

	@Override
	public String retrieveAccessToken(String url) {
		// TODO Auto-generated method stub
		try {
			OAuthV1Client.accessToken(mOauth, getAccessTokenEndpoint());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
