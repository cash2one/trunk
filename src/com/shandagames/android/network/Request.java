package com.shandagames.android.network;

import java.io.File;
import java.util.List;
import android.os.Process;

/**
 * 
 * @file Request.java
 * @create 2012-9-14 上午9:18:32
 * @author lilong
 * @description The network request thread to the server.
 */
public class Request implements Runnable {

	private static final boolean DEBUG = true;
	
	private String url;

	private Verb method;
	
	private String queryString;

	private List<File> attachments;

	public Request(String url) {
		this(url, null);
	}

	public Request(String url, String queryString) {
		this(url, Verb.GET, queryString);
	}

	public Request(String url, Verb method, String queryString) {
		this(url, method, queryString, null);
	}

	public Request(String url, Verb method, String queryString,
			List<File> attachments) {
		this.url = url;
		this.method = method;
		this.queryString = queryString;
		this.attachments = attachments;
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		
		String responseData = null;
		QHttpClient httpClient = QHttpClient.getInstance();
		if (method.equals(Verb.GET)) {
			responseData = httpClient.doHttpGet(url, queryString);
		} else if (method.equals(Verb.POST)) {
			if (attachments == null || attachments.size() == 0) {
				responseData = httpClient.doHttpPost(url, queryString);
			} else {
				responseData = httpClient.doHttpPostWithFile(url, queryString, attachments);
			}
		}

		if (responseData == null) {
			onFailure("the response data may not be null");
			if (DEBUG) {
				throw new NullPointerException("response data is null");
			} 
		} else {
			onSuccess(responseData);
		}
	}

	
	/** This method will be invoked when request success. */
	protected void onSuccess(String content) {
	}

	/** This method will be invoked when request failure. */
	protected void onFailure(String message) {
	}
	
	
	/** 枚举方法类型 */
	public enum Verb {
		GET, POST
	}
	
	public interface ResponseHandler {
		
		/** This method will be invoked when request success. */
		void onSuccess(String content);

		/** This method will be invoked when request failure. */
		void onFailure(String message);
	}
}
