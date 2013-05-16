package com.shandagames.android.network.processor.gzip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;

public class GZipProcessor implements HttpProcessor {
	
	private static final String HEADER_GZIP_ENCODING_VALUE = "gzip";
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
        if (response == null) {
            throw new IOException("response can not be null");
        }
        final HttpEntity entity = response.getEntity();
        if(entity == null) {
        	return;
        }
        final Header header = entity.getContentEncoding();
        if (header == null) {
        	return;
        }
        for (HeaderElement h : header.getElements()) {
            if (h.getName().equalsIgnoreCase(HEADER_GZIP_ENCODING_VALUE)) {
                response.setEntity(new GZipEntity(response.getEntity()));
                return;
            }
        }
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    	if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
    		request.addHeader(HEADER_ACCEPT_ENCODING, HEADER_GZIP_ENCODING_VALUE);
    	}
    }

    public class GZipEntity extends HttpEntityWrapper {
        public GZipEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }
    }
}
