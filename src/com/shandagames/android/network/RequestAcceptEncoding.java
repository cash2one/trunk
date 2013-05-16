package com.shandagames.android.network;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;


/**
 * @author  lilong
 * @version 2012-7-2 下午2:53:28
 *
 */
@Immutable
public class RequestAcceptEncoding implements HttpRequestInterceptor {

	private static final String HEADER_GZIP_ENCODING_VALUE = "gzip, deflate";
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	
	/**
     * Adds the header {@code "Accept-Encoding: gzip,deflate"} to the request.
     */
    public void process(
            final HttpRequest request,
            final HttpContext context) throws HttpException, IOException {

        /* Signal support for Accept-Encoding transfer encodings. */
        request.addHeader(HEADER_ACCEPT_ENCODING, HEADER_GZIP_ENCODING_VALUE);
    }

}
