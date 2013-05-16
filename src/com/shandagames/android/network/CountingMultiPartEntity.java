/**
 * 
 */
package com.shandagames.android.network;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import com.shandagames.android.network.CountingOutputStream.Progress;

/**
 * @file CustomMultipartEntity.java
 * @create 2012-11-13 下午2:03:58
 * @author lilong
 * @description 文件上传，显示上传进度
 */
public class CountingMultiPartEntity extends MultipartEntity {

	private long totalSize;

	private Progress listener;

	public CountingMultiPartEntity() {
		super();
	}

	public CountingMultiPartEntity(final HttpMultipartMode mode) {
		super(mode);
	}

	public CountingMultiPartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset) {
		super(mode, boundary, charset);
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.totalSize, this.listener));
	}

	public void setProgressListener(final long totalSize, final Progress listener) {
		this.totalSize = totalSize;
		this.listener = listener;
	}
}
