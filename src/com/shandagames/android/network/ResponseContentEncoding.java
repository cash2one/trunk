package com.shandagames.android.network;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;


/**
 * @author  lilong
 * @version 2012-7-2 下午2:53:40
 *
 */
@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor {

	/**
     * Handles the following {@code Content-Encoding}s by
     * using the appropriate decompressor to wrap the response Entity:
     *
     * @param response the response which contains the entity
     * @param  context not currently used
     *
     * @throws HttpException if the {@code Content-Encoding} is none of the above
     */
    public void process(
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {
    	if (response == null) {
    		throw new IOException("response can not be null");
    	}
        HttpEntity entity = response.getEntity();

        // It wasn't a 304 Not Modified response, 204 No Content or similar
        if (entity != null) {
            Header header = entity.getContentEncoding();
            if (header != null) {
                HeaderElement[] codecs = header.getElements();
                for (HeaderElement codec : codecs) {
                    String codecname = codec.getName().toLowerCase(Locale.US);
                    if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
                        response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                        return;
                    } else if ("deflate".equals(codecname)) {
                        response.setEntity(new DeflateDecompressingEntity(response.getEntity()));
                        return;
                    } else if ("identity".equals(codecname)) {
                        /* Don't need to transform the content - no-op */
                        return;
                    } else {
                        throw new HttpException("Unsupported Content-Coding: " + codec.getName());
                    }
                }
            }
        }
    }
    
}
