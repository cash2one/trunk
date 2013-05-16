package com.shandagames.android.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * @author  lilong
 * @version 2012-7-2 下午2:44:53
 *
 */
public class DeflateDecompressingEntity extends HttpEntityWrapper {
	
	public DeflateDecompressingEntity(final HttpEntity wrapped) {
		super(wrapped);
	}

	@Override
	public InputStream getContent() throws IOException {
		InputStream wrapped = this.wrappedEntity.getContent();
		/* We read a small buffer to sniff the content. */
		byte[] peeked = new byte[6];
		PushbackInputStream pushback = new PushbackInputStream(wrapped,
				peeked.length);
		int headerLength = pushback.read(peeked);
		if (headerLength == -1) {
			throw new IOException("Unable to read the response");
		}
		/* We try to read the first uncompressed byte. */
		byte[] dummy = new byte[1];
		Inflater inf = new Inflater();
		try {
			int n;
			while ((n = inf.inflate(dummy)) == 0) {
				if (inf.finished()) {
					/* Not expecting this, so fail loudly. */
					throw new IOException("Unable to read the response");
				}
				if (inf.needsDictionary()) {
					// Need dictionary - then it must be zlib stream with
					// DICTID part
					break;
				}
				if (inf.needsInput()) {
					inf.setInput(peeked);
				}
			}
			if (n == -1) {
				throw new IOException("Unable to read the response");
			}
			/*
			 * We read something without a problem, so it's a valid zlib
			 * stream. Just need to reset and return an unused InputStream
			 * now.
			 */
			pushback.unread(peeked, 0, headerLength);
			return new InflaterInputStream(pushback);
		} catch (DataFormatException e) {
			/*
			 * Presume that it's an RFC1951 deflate stream rather than
			 * RFC1950 zlib stream and try again.
			 */
			pushback.unread(peeked, 0, headerLength);
			return new InflaterInputStream(pushback, new Inflater(true));
		}
	}

	@Override
	public long getContentLength() {
		/* Length of inflated content is unknown. */
		return -1;
	}
}