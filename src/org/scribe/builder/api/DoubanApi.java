package org.scribe.builder.api;

import org.scribe.model.Token;

public class DoubanApi extends DefaultApi10a {
	private static final String REQUEST_TOKEN_URL = "http://www.douban.com/service/auth/request_token";
	private static final String ACCESS_TOKEN_URL = "http://www.douban.com/service/auth/access_token";
	private static final String AUTHORIZE_URL = "http://www.douban.com/service/auth/authorize?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint() 
	{
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint() 
	{
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) 
	{
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

}
