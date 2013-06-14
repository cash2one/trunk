package com.shandagames.android.oauth.api;

import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.oauth.OAuthV1;

public abstract class DefaultApi20 implements Api {

	protected OAuth mOauth;

	DefaultApi20(OAuth oAuth) {
		this.mOauth = oAuth;
	}

	public OAuth getOAuth() {
		return mOauth;
	}

	/**
	 * Returns the verb for the access token endpoint (defaults to GET)
	 * 
	 * @return access token endpoint verb
	 */
	public String getAccessTokenVerb() {
		return "GET";
	}

	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	public abstract String getAccessTokenEndpoint();

	
	
	public OAuthV1 retrieveRequestToken() {
		return null;
	}
}
