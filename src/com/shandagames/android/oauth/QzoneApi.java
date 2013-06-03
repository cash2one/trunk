package com.shandagames.android.oauth;

public class QzoneApi extends Api {

	private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=%1$s&display=mobile&redirect_uri=%2$s";;
	
	private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/me?access_token=";
	
	
	public QzoneApi(Token token) {
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

	@Override
	public String retrieveAccessToken(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
