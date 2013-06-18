package com.shandagames.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import com.shandagames.android.http.ssl.EasySSLSocketFactory;
import com.shandagames.android.network.core.BetterHttpRequestRetryHandler;
import android.os.Build;

/**
 * @file BetterHttp.java
 * @create 2013-3-14 下午05:16:14
 * @author lilong
 * @description TODO 封装常用的Http网络请求操作
 */
public class BetterHttp {

	private static final int DEFAULT_MAX_CONNECTIONS = 5;
	private static final int DEFAULT_MAX_RETRIES = 3; //自动恢复异常次数
	private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;
	
	private static final String DEFAULT_HTTP_USER_AGENT = "Android/Agent";
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

    private DefaultHttpClient httpClient;
    private final Map<String, String> clientHeaderMap = new HashMap<String, String>();
    
    public BetterHttp() {
    	setupHttpClient(DEFAULT_MAX_CONNECTIONS, DEFAULT_SOCKET_TIMEOUT, DEFAULT_HTTP_USER_AGENT);
    }
    
    public BetterHttp(int maxConnections, int socketTimeout, String httpUserAgent) {
    	setupHttpClient(maxConnections, socketTimeout, httpUserAgent);
    }
    
    private void setupHttpClient(int maxConnections, int socketTimeout, String httpUserAgent) {
        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,  new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUserAgent(httpParams, httpUserAgent);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        if (Build.VERSION.SDK_INT >= 7) {
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        } else {
            // used to work around a bug in Android 1.6:
            // http://code.google.com/p/android/issues/detail?id=1946
            // TODO: is there a less rigorous workaround for this?
            schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        }

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, httpParams);
        
        addRequestInterceptor();
        setRequestRetryHandler(DEFAULT_MAX_RETRIES);
    }
    
    
    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }
    
    /**
    * Sets the User-Agent header to be sent with each request. By default,
    * "Android Asynchronous Http Client/VERSION (http://loopj.com/android-async-http/)" is used.
    * @param userAgent the string to use in the User-Agent header.
    */
    public void setUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(httpClient.getParams(), userAgent);
    }
    
    /**
    * Sets an optional CookieStore to use when making requests
    * @param cookieStore The CookieStore implementation to use, usually an instance of {@link PersistentCookieStore}
    */
    public void setCookieStore(CookieStore cookieStore) {
    	httpClient.setCookieStore(cookieStore);
    }
    
    /**
    * Sets headers that will be added to all requests this client makes (before sending).
    * @param header the name of the header
    * @param value the contents of the header
    */
    public void addHeader(String header, String value) {
        clientHeaderMap.put(header, value);
    }
    
    /** 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复 */
    public void setRequestRetryHandler(int maxRetries) {
    	httpClient.setHttpRequestRetryHandler(new BetterHttpRequestRetryHandler(maxRetries));
    }
    
    /**
    * Sets basic authentication for the request. Uses AuthScope.ANY. This is the same as
    * setBasicAuth('username','password',AuthScope.ANY)
    * @param username
    * @param password
    */
    public void setBasicAuth(String username, String password){
        AuthScope scope = AuthScope.ANY;
        setBasicAuth(username, password, scope);
    }
        
       /**
    * Sets basic authentication for the request. You should pass in your AuthScope for security. It should be like this
    * setBasicAuth("username","password", new AuthScope("host",port,AuthScope.ANY_REALM))
    * @param username
    * @param password
    * @param scope - an AuthScope object
    *
    */
    public void setBasicAuth(String user, String pass, AuthScope scope){
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user,pass);
        this.httpClient.getCredentialsProvider().setCredentials(scope, credentials);
    }
    
    /**
     * Intercept requests to have them ask for GZip encoding and intercept responses to
     * automatically wrap the response entity for reinflation. This code is based on code from
     * SyncService in the Google I/O 2010 {@linkplain http://code.google.com/p/iosched/ scheduling
     * app}.
     */
    public void addRequestInterceptor() {
    	httpClient.addRequestInterceptor(new ClientHeaderRequestInterceptor());
        httpClient.addRequestInterceptor(new GZIPHttpRequestInterceptor());
        httpClient.addResponseInterceptor(new GZIPHttpResponseInterceptor());
    }
    
    private class ClientHeaderRequestInterceptor implements HttpRequestInterceptor {
		@Override
		public void process(HttpRequest request, HttpContext context) {
			// TODO Auto-generated method stub
			if (clientHeaderMap.size() > 0) {
				for (String header : clientHeaderMap.keySet()) {
	                request.addHeader(header, clientHeaderMap.get(header));
	            }
			}
		}
    }
    
    /**
     * Simple {@link HttpRequestInterceptor} that adds GZIP accept encoding header.
     */
    private class GZIPHttpRequestInterceptor implements HttpRequestInterceptor {
        public void process(final HttpRequest request, final HttpContext context) {
            // Add header to accept gzip content
            if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
            }
        }
    }

    /**
     * Simple {@link HttpResponseInterceptor} that inflates response if GZIP encoding header.
     */
    private class GZIPHttpResponseInterceptor implements HttpResponseInterceptor {
        public void process(final HttpResponse response, final HttpContext context) {
            // Inflate any responses compressed with gzip
            final HttpEntity entity = response.getEntity();
            final Header encoding = entity.getContentEncoding();
            if (encoding != null) {
                for (HeaderElement element : encoding.getElements()) {
                    if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                        response.setEntity(new GZIPInflatingEntity(response.getEntity()));
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Simple {@link HttpEntityWrapper} that inflates the wrapped {@link HttpEntity} by passing it
     * through {@link GZIPInputStream}.
     */
    private class GZIPInflatingEntity extends HttpEntityWrapper {
        public GZIPInflatingEntity(final HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }
    
}
