package com.shandagames.android.oauth;

import com.shandagames.android.network.QHttpClient;
import com.shandagames.android.network.Request;
import com.shandagames.android.network.RequestExecutor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @file ShareSupport.java
 * @create 2013-4-24 下午03:18:50
 * @author lilong
 * @description TODO
 */
public final class ShareSupport {

	private String APP_KEY = "";
	private String APP_SECRET = "";
	private String mRedirectUrl = "";
	
	private String URL_OAUTH2_AUTHORIZE = "";
	
	private static ShareSupport instance;
	
	private ShareSupport() {
	}
	
	public synchronized static ShareSupport getInstance() {
		if (instance == null) {
			instance = new ShareSupport();
		}
		return instance;
	}
	
	
	public void setupConsumerConfig(String consumer_key, String consumer_secret) {
        this.APP_KEY = consumer_key;
        this.APP_SECRET = consumer_secret;
    }

    public String getAppKey() {
        return this.APP_KEY;
    }

    public String getAppSecret() {
        return this.APP_SECRET;
    }
	
    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String mRedirectUrl) {
        this.mRedirectUrl = mRedirectUrl;
    }
	
    public void setAuthorizeUrl(String authorizeUrl) {
    	this.URL_OAUTH2_AUTHORIZE = authorizeUrl;
    }
    
	public void authorize(Activity activity, WebView webView, final OAuthListener listener) {
		CookieSyncManager.createInstance(activity);
		startWebViewAuth(activity, webView, listener);
	}

	private void startWebViewAuth(Context context, WebView mWebView, final OAuthListener listener) {
		if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Utility.showAlert(context, "Error", "Application requires permission to access the Internet");
		} else {
			mWebView.setVerticalScrollBarEnabled(false);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.requestFocus();
			mWebView.loadUrl(URL_OAUTH2_AUTHORIZE);
			mWebView.setWebViewClient(new WeiboWebViewClient(listener));
		}
	}

	private class WeiboWebViewClient extends WebViewClient {
		private OAuthListener mListener;

		public WeiboWebViewClient(OAuthListener listener) {
			this.mListener = listener;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.w("onPageStarted", url);
			
			//如果授权成功url中包含之前设置的callbackurl包含：授权成功
			if (url.startsWith(getRedirectUrl())) {
				if (mListener != null) {
					Bundle values = Utility.parseUrl(url);
					mListener.onComplete(values);
					view.stopLoading();
				}
			}
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 待后台增加对默认重定向地址的支持后修改下面的逻辑
			Log.w("shouldOverrideUrlLoading", url);
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (mListener != null) {
				mListener.onPageFinished();
			}
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			if (mListener != null) {
				mListener.onError(new OAuthError(description, errorCode, failingUrl));
			}
		}

		/**  由于腾讯授权页面采用https协议执行此方法接受所有证书 */
		@SuppressLint("NewApi")
		public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
			 handler.proceed() ;
		 }
	}
}
