package com.shandagames.android.oauth.api;

import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.oauth.OAuthV1;
import com.shandagames.android.oauth.OAuthV1Client;

public abstract class DefaultApi10 implements Api {
	
	protected OAuthV1 mOauth;
	
	DefaultApi10(OAuthV1 oAuth) {
		this.mOauth = oAuth;
	}
	
	public OAuth getOAuth() {
		return this.mOauth;
	}
	
	/**
	 * Returns the verb for the access token endpoint (defaults to POST)
	 * 
	 * @return access token endpoint verb
	 */
	public String getAccessTokenVerb() {
		return "POST";
	}

	/**
	 * Returns the verb for the request token endpoint (defaults to POST)
	 * 
	 * @return request token endpoint verb
	 */
	public String getRequestTokenVerb() {
		return "GET";
	}

	/**
	 * Returns the URL that receives the request token requests.
	 * 
	 * @return request token URL
	 */
	public abstract String getRequestTokenEndpoint();

	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	public abstract String getAccessTokenEndpoint();

	
	/**
	 * Returns the OAuthV1 object with requet token
	 * 
	 * @return oauthV1 object
	 */
	@Override
	public OAuthV1 retrieveRequestToken() {
		try {
			mOauth = OAuthV1Client.requestToken(mOauth, getRequestTokenEndpoint());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mOauth;
	}
}
