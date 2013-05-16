package com.shandagames.android.http;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.shandagames.android.http.cookie.PersistentCookieStore;
import com.shandagames.android.parser.Parser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.ResultType;

/**
 * @file HttpApiWithToken.java
 * @create 2013-3-18 下午02:11:59
 * @author lilong
 * @description TODO
 */
public class HttpApiWithToken extends AbstractHttpApi {

	public HttpApiWithToken(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);
		//httpClient.setCookieStore(new PersistentCookieStore(null));
		// TODO Auto-generated constructor stub
	}

	@Override
	public Result doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends ResultType> parser) {
		// TODO Auto-generated method stub
		return executeHttpRequest(httpRequest, parser);
	}

}
