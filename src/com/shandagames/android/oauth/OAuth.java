package com.shandagames.android.oauth;

import java.io.Serializable;

public class OAuth implements Serializable {

	private static final long serialVersionUID = -6939447877705891817L;

	protected String oauthConsumerKey = "";// AppKey(client credentials)
	protected String oauthConsumerSecret = "";// AppSecret
	protected String oauthCallback = "null";// 认证成功后浏览器会被重定向到这个url中
	protected String msg = "";

	public OAuth() {
		super();
	}

	/**
	 * @param oauthCallback
	 *            认证成功后浏览器会被重定向到这个地址
	 */
	public OAuth(String oauthCallback) {
		super();
		this.oauthCallback = oauthCallback;
	}

	/**
	 * 
	 * @param oauthConsumerKey
	 *            应用申请到的APP KEY
	 * @param oauthConsumerSecret
	 *            应用申请到的APP SECRET
	 * @param oauthCallback
	 *            认证成功后浏览器会被重定向到这个地址
	 */
	public OAuth(String oauthConsumerKey, String oauthConsumerSecret,
			String oauthCallback) {
		super();
		this.oauthConsumerKey = oauthConsumerKey;
		this.oauthConsumerSecret = oauthConsumerSecret;
		this.oauthCallback = oauthCallback;
	}

	/** 应用的APP KEY */
	public String getOauthConsumerKey() {
		return oauthConsumerKey;
	}

	/** 应用的APP KEY */
	public void setOauthConsumerKey(String oauthConsumerKey) {
		this.oauthConsumerKey = oauthConsumerKey;
	}

	/** 应用申请到的APP SECRET */
	public String getOauthConsumerSecret() {
		return oauthConsumerSecret;
	}

	/** 应用申请到的APP SECRET */
	public void setOauthConsumerSecret(String oauthConsumerSecret) {
		this.oauthConsumerSecret = oauthConsumerSecret;
	}

	/** 重定向地址 */
	public String getOauthCallback() {
		return oauthCallback;
	}

	/** 重定向地址 */
	public void setOauthCallback(String oauthCallback) {
		this.oauthCallback = oauthCallback;
	}

	/** 响应结果  */
	public String getMsg() {
		return this.msg;
	}

	/** 响应结果  */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
