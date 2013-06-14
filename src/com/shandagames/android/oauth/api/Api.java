package com.shandagames.android.oauth.api;

import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.oauth.OAuthV1;

public interface Api {

	OAuth getOAuth();

	/**
	 * Returns the URL where you should redirect your users to authenticate your
	 * application.
	 * 
	 * @return the URL where you should redirect your users
	 */
	String getAuthorizationUrl();

	/**
	 * The responsibility of this method is to ontact the service provider at
	 * the given endpoint URL and fetch a request or request token. What kind of
	 * token is retrieved solely depends on the URL being used.
	 */
	OAuthV1 retrieveRequestToken();

	/**
	 * The responsibility of this method is to ontact the service provider at
	 * the given endpoint URL and fetch a request or access token. What kind of
	 * token is retrieved solely depends on the URL being used.
	 */
	String retrieveAccessToken(String url);
}
