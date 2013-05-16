/**
 * 
 */
package com.shandagames.android.network;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @file CountingOutputStream.java
 * @create 2012-11-13 下午2:54:08
 * @author lilong
 * @description 过滤上传/下载进度
 */
public final class CountingOutputStream extends FilterOutputStream {

	private long totalSize;
	private long transferred;
	private Progress listener;

	/**
	 * 默认构造方法
	 * @param out  OutputStream输出流
	 * @param totalSize 输出流总长度大小size
	 */
	public CountingOutputStream(OutputStream out, long totalSize) {
		this(out, totalSize, null);
	}
	
	public CountingOutputStream(OutputStream out, long totalSize, Progress progress) {
		super(out);
		this.totalSize = totalSize;
		this.transferred = 0;
		this.listener = progress;
	}

	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		if (this.listener != null) {
			this.transferred += len;
			this.listener.transferred(this.transferred, this.totalSize);
		}
	}

	public void write(int b) throws IOException {
		out.write(b);
		if (this.listener != null) {
			this.transferred++;
			this.listener.transferred(this.transferred, this.totalSize);
		}
	}

	/** 计算下载/上传进度  */
	public static interface Progress {
		/**
		 * 计算写入的字节数，更新进度条
		 */
		void transferred(long num, long totalSize);
	}
}
