package com.shandagames.android.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * @author  lilong
 * @version 2012-7-2 下午2:44:43
 *
 */
public class GzipDecompressingEntity extends HttpEntityWrapper {
	
	public GzipDecompressingEntity(HttpEntity entity) {
		super(entity);
	}

	@Override
	public InputStream getContent() throws IOException,
			IllegalStateException {
		// the wrapped entity's getContent() decides about repeatability
		return new GZIPInputStream(wrappedEntity.getContent());
	}

	@Override
	public long getContentLength() {
		// length of ungzipped content is not known
		return -1;
	}
}
