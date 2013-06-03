package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.oauth.Api;
import com.shandagames.android.oauth.QzoneApi;
import com.shandagames.android.oauth.SinaWeiboApi20;
import com.shandagames.android.oauth.TencentApi;
import com.shandagames.android.oauth.Token;
import com.shandagames.android.oauth.Utility;
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

/**
 * @file WeiboViewActivity.java
 * @create 2013-4-24 下午04:52:57
 * @author lilong
 * @description TODO
 */
public class WeiboViewActivity extends Activity implements TaskListener {
	private static final String TAG = "WeiboViewActivity.class";
	
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
		
		Token token = new Token();
		String type = getIntent().getStringExtra("type");
		if (type.equals("sina")) {
			token.setApiKey(Utility.SINA_APP_KEY);
			token.setApiSecret(Utility.SINA_APP_SECRET);
			token.setRedirectUrl(Utility.SINA_REDIRECT_URL);
			api = new SinaWeiboApi20(token);
		} else if (type.equals("tencent")) {
			token.setApiKey(Utility.TENCENT_APP_KEY);
			token.setApiSecret(Utility.TENCENT_APP_SECRET);
			token.setRedirectUrl(Utility.TENCENT_REDIRECT_URL);
			api = new TencentApi(token);
		} else if (type.equals("qq")) {
			token.setApiKey(Utility.QQ_APP_KEY);
			token.setApiSecret(Utility.QQ_APP_SECRET);
			token.setRedirectUrl(Utility.QQ_REDIRECT_URL);
			api = new QzoneApi(token);
		}
		
		mWebView.loadUrl(api.getAuthorizationUrl());
	}
	
	
	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	public void onTaskFinished(String taskName, Object result) {
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.w("onPageStarted", url);
			
			//如果授权成功url中包含之前设置的callbackurl包含：授权成功
			if (url.startsWith(api.getToken().getRedirectUrl())) {
				new GetAccessTokenTask("", WeiboViewActivity.this).execute(url);
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
			return api.retrieveAccessToken(params[0]);
		}
		
	}
	
}
