package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.oauth.OAuth;
import com.shandagames.android.oauth.OAuthV1;
import com.shandagames.android.oauth.Utility;
import com.shandagames.android.oauth.api.Api;
import com.shandagames.android.oauth.api.DefaultApi10;
import com.shandagames.android.oauth.api.DoubanApi;
import com.shandagames.android.oauth.api.QzoneApi;
import com.shandagames.android.oauth.api.RenrenApi;
import com.shandagames.android.oauth.api.SinaWeiboApi20;
import com.shandagames.android.oauth.api.TencentApi;
import com.shandagames.android.task.GenericTask;
import com.shandagames.android.task.TaskListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @file WeiboViewActivity.java
 * @create 2013-4-24 下午04:52:57
 * @author lilong
 * @description TODO
 */
public class WeiboViewActivity extends Activity implements TaskListener {
	private static final String TAG = "WeiboViewActivity.class";
	
	private static final String REQUEST_TOKEN_TASK = "REQUEST_TOKEN_TASK";
	private static final String ACCESS_TOKEN_TASK = "ACCESS_TOKEN_TASK";
	
	private WebView mWebView;
	private ProgressBar mProgressBar;
	
	private Api api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		mWebView = (WebView) findViewById(R.id.webview);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.requestFocus();
		mWebView.setWebViewClient(new WeiboWebViewClient());
		
		//http://www.douban.com/service/apidoc/auth#认证流程及访问资源流程
		
		OAuth mOauth = new OAuth();
		String type = getIntent().getStringExtra("type");
		if (type.equals("sina")) {
			mOauth.setOauthConsumerKey(Utility.SINA_APP_KEY);
			mOauth.setOauthConsumerSecret(Utility.SINA_APP_SECRET);
			mOauth.setOauthCallback(Utility.SINA_REDIRECT_URL);
			api = new SinaWeiboApi20(mOauth);
		} else if (type.equals("tencent")) {
			mOauth.setOauthConsumerKey(Utility.TENCENT_APP_KEY);
			mOauth.setOauthConsumerSecret(Utility.TENCENT_APP_SECRET);
			mOauth.setOauthCallback(Utility.TENCENT_REDIRECT_URL);
			api = new TencentApi(mOauth);
		} else if (type.equals("qq")) {
			mOauth.setOauthConsumerKey(Utility.QQ_APP_KEY);
			mOauth.setOauthConsumerSecret(Utility.QQ_APP_SECRET);
			mOauth.setOauthCallback(Utility.QQ_REDIRECT_URL);
			api = new QzoneApi(mOauth);
		} else if (type.equals("renren")) {
			mOauth.setOauthConsumerKey(Utility.RENREN_APP_KEY);
			mOauth.setOauthConsumerSecret(Utility.RENREN_APP_SECRET);
			mOauth.setOauthCallback(Utility.RENREN_REDIRECT_URL);
			api = new RenrenApi(mOauth);
		} else if (type.equals("douban")) {
			mOauth = new OAuthV1();
			mOauth.setOauthConsumerKey(Utility.DOUBAN_APP_KEY);
			mOauth.setOauthConsumerSecret(Utility.DOUBAN_APP_SECRET);
			mOauth.setOauthCallback(Utility.DOUBAN_REDIRECT_URL);
			api = new DoubanApi((OAuthV1)mOauth);
			
			new GetAccessTokenTask(REQUEST_TOKEN_TASK, this).execute();
			
			return;
		} 
		
		mWebView.loadUrl(api.getAuthorizationUrl());
	}
	
	
	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	public void onTaskFinished(String taskName, Object result) {
		if (taskName.equals(REQUEST_TOKEN_TASK)) {
			// 根据requestToken请求授权页面
			mWebView.loadUrl(api.getAuthorizationUrl());
		} else if (taskName.equals(ACCESS_TOKEN_TASK)) {
			// 获取授权成功的accessToken
			Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.w("onPageStarted", url);
			
			//如果授权成功url中包含之前设置的callbackurl包含：授权成功
			if (url.startsWith(api.getOAuth().getOauthCallback())) {
				new GetAccessTokenTask(ACCESS_TOKEN_TASK, WeiboViewActivity.this).execute(url);
				view.stopLoading();
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
			mProgressBar.setVisibility(View.GONE);
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		/**  由于腾讯授权页面采用https协议执行此方法接受所有证书 */
		@SuppressLint("NewApi")
		public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
			 handler.proceed() ;
		 }
	}

	
	private class GetAccessTokenTask extends GenericTask<String> {

		public GetAccessTokenTask(String taskName, TaskListener taskListener) {
			super(taskName, taskListener);
		}
		
		@Override
		protected String doInBackground(String... params) {
			if (taskName.equals(REQUEST_TOKEN_TASK)) {
				return api.retrieveRequestToken().getOauthToken();
			} else if (taskName.equals(ACCESS_TOKEN_TASK)) {
				return api.retrieveAccessToken(params[0]);
			}
			return null;
		}
		
	}
	
}
