/**
 * 
 */
package com.shandagames.android.parser;

/**
 * @file Result.java
 * @create 2012-9-20 下午4:38:40
 * @author lilong
 * @description TODO
 */
public class Result {

	private Meta meta;
	private ResultType result;
	private Exception exception;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public ResultType getResult() {
		return result;
	}

	public void setResult(ResultType result) {
		this.result = result;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public static class Meta {
		private int code;
		private String etag;
		private String errorType;
		private String errorDetail;
		private String notification;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getErrorType() {
			return errorType;
		}

		public void setErrorType(String errorType) {
			this.errorType = errorType;
		}

		public String getErrorDetail() {
			return errorDetail;
		}

		public void setErrorDetail(String errorDetail) {
			this.errorDetail = errorDetail;
		}

		public String getNotification() {
			return notification;
		}

		public void setNotification(String notification) {
			this.notification = notification;
		}

		public String getEtag() {
			return etag;
		}

		public void setEtag(String etag) {
			this.etag = etag;
		}
	}
}
