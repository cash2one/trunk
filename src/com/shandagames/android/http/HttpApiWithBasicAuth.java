package com.shandagames.android.http;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import com.shandagames.android.parser.Parser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.ResultType;
import java.io.IOException;

/**
 * @file HttpApiWithBasicAuth.java
 * @create 2012-10-11 下午2:08:58
 * @author Jacky.Lee
 * @description TODO
 */
public class HttpApiWithBasicAuth extends AbstractHttpApi {

	/** 定义抢先认证拦截  */
	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
		@Override
		public void process(final HttpRequest request, final HttpContext context)
				throws HttpException, IOException {
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

			// If not auth scheme has been initialized yet
			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
				// Obtain credentials matching the target host
				Credentials creds = credsProvider.getCredentials(authScope);
				// If found, generate BasicScheme preemptively
				if (creds != null) {
					//使用BasicScheme认证，首次请求都不会携带认证信息，在被拒绝后重新发送认证信息进行认证，从而导致重复请求问题;
					//http://blog.csdn.net/henry121212/article/details/7355020
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}

	};

	public HttpApiWithBasicAuth(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);
		httpClient.addRequestInterceptor(preemptiveAuth, 0);
	}

	public Result doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends ResultType> parser) {
		return executeHttpRequest(httpRequest, parser);
	}
}
