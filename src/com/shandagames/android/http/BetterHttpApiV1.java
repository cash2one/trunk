/**
 * 
 */
package com.shandagames.android.http;

import java.io.IOException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.util.Log;

import com.shandagames.android.constant.Config;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.parser.GroupParser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.json.PlaceParser;

/**
 * @file HttpApiV1.java
 * @create 2012-10-11 下午3:11:30
 * @author lilong
 * @description TODO
 */
public class BetterHttpApiV1 {
	private static final boolean DEBUG = Config.DEBUG;
	
	// Google API Key
	private static final String API_KEY = "AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc"; // place your API key here
	
	// Google Places serach url's
	private static final String URL_API_VENUE_SEARCH_PLACES = "https://maps.googleapis.com/maps/api/place/search/json";
	private static final String URL_API_VANUE_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json";
	
	private HttpApi mHttpApi;
	private final String  mApiBaseUrl;
	private final AuthScope mAuthScope; 
	private DefaultHttpClient mHttpClient;
	
	private static BetterHttpApiV1 instance;
	
	public static BetterHttpApiV1 getInstance() { return instance; }
	
	public static void init(Context context, String clientVersion, boolean useOAuth) {
		instance = new BetterHttpApiV1(context, clientVersion, useOAuth);
	}
	
	private BetterHttpApiV1(Context context, String clientVersion, boolean useOAuth) {
		//配置测试/正式服务器访问地址
		String domain = Constants.RELEASE_SERVER;
		mApiBaseUrl = "https://" + domain +"/v1";
		mAuthScope = new AuthScope(domain, 80);
		
		BetterHttp httpClient = new BetterHttp();
		mHttpClient = httpClient.getHttpClient();
		
		if (useOAuth) {
			mHttpApi = new HttpApiWithOAuth(mHttpClient, clientVersion);
		} else {
			//mHttpApi = new HttpApiWithBasicAuth(mHttpClient, clientVersion);
			mHttpApi = new HttpApiWithToken(mHttpClient, clientVersion);
		}
	}

	/** Setup Credentials for HTTP Basic Auth */
	public void setCredentials(String userName, String password) {
        if (userName == null || userName.length() == 0 || password == null || password.length() == 0) {
            if (DEBUG) Log.d("setCredentials", "Clearing Credentials");
            mHttpClient.getCredentialsProvider().clear();
        } else {
            if (DEBUG) Log.d("setCredentials", "Setting userName/Password: " + userName + "/******");
            mHttpClient.getCredentialsProvider().setCredentials(mAuthScope,
                    new UsernamePasswordCredentials(userName, password));
        }
    }

    public boolean hasCredentials() {
        return mHttpClient.getCredentialsProvider().getCredentials(mAuthScope) != null;
    }
	
    /** Setup Credentials for HTTP OAuth */
    public void setOAuthConsumerCredentials(String oAuthConsumerKey, String oAuthConsumerSecret) {
        if (DEBUG) Log.d("setOAuthConsumerCredentials", "Setting consumer key/secret: " + oAuthConsumerKey + " " + oAuthConsumerSecret);
        ((HttpApiWithOAuth) mHttpApi).setOAuthConsumerCredentials(oAuthConsumerKey, oAuthConsumerSecret);
    }

    public void setOAuthTokenWithSecret(String token, String secret) {
        if (DEBUG) Log.d("setOAuthTokenWithSecret", "Setting oauth token/secret: " + token + " " + secret);
        ((HttpApiWithOAuth) mHttpApi).setOAuthTokenWithSecret(token, secret);
    }

    public boolean hasOAuthTokenWithSecret() {
        return ((HttpApiWithOAuth) mHttpApi).hasOAuthTokenWithSecret();
    }
    
    
	public String fullUrl(String url) {
		return mApiBaseUrl + url;
	}

	public HttpApi getRequest() {
		return mHttpApi;
	}
	
	public void doPing() {
		try {
			// 首次预加载服务器URL
			mHttpApi.createHttpURLConnection(mApiBaseUrl, "GET").connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Result searchPlaces(double latitude, double longitude, double radius, boolean sensor, String types) {
		HttpGet httpGet = mHttpApi.createHttpGet(URL_API_VENUE_SEARCH_PLACES,
				new BasicNameValuePair("key", API_KEY),
				new BasicNameValuePair("location", latitude + "," + longitude),
				new BasicNameValuePair("radius", String.valueOf(radius)),
				new BasicNameValuePair("sensor", Boolean.toString(sensor)),
				new BasicNameValuePair("types", types));
		return mHttpApi.doHttpRequest(httpGet, new GroupParser(new PlaceParser()));
	}

	public Result getPlaceDetails(String reference, boolean sensor) {
		HttpGet httpGet = mHttpApi.createHttpGet(URL_API_VANUE_DETAILS, 
				new BasicNameValuePair("key", API_KEY),
				new BasicNameValuePair("reference", reference),
				new BasicNameValuePair("sensor", Boolean.toString(sensor)));
		return mHttpApi.doHttpRequest(httpGet, new PlaceParser());
	}
	
}
