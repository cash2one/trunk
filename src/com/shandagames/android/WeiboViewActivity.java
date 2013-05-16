package com.shandagames.android;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DoubanApi;
import org.scribe.builder.api.QWeiboApi;
import org.scribe.builder.api.QzoneApi;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.model.Oauth2AccessToken;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import com.shandagames.android.R;
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
import android.widget.Toast;

/**
 * @file WeiboViewActivity.java
 * @create 2013-4-24 下午04:52:57
 * @author lilong
 * @description TODO
 */
public class WeiboViewActivity extends Activity implements TaskListener {
	private static final String TAG = "WeiboViewActivity.class";
	
	private static final String OAUTH2_SINA_TOKEN_TASK = "OAUTH2_SINA_TOKEN_TASK";
	private static final String OAUTH2_REQUEST_TOKEN_TASK = "OAUTH2_REQUEST_TOKEN_TASK";
	
	private WebView mWebView;
	private ProgressBar mProgressBar;
	
	private String type;
	
	private OAuthService service;
	private Token requestToken;
	
	private GetAccessTokenTask mAccessTokenTask;
	
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
		
		ensureUi();
		
		
		//http://www.douban.com/service/apidoc/auth#认证流程及访问资源流程
		mAccessTokenTask = new GetAccessTokenTask(OAUTH2_SINA_TOKEN_TASK, this);
	}
	
	private void ensureUi() {
		type = getIntent().getStringExtra("type");
		if (type.equals(Utility.SINA)) { 
			service = new ServiceBuilder().provider(SinaWeiboApi20.class)
	        .apiKey(Utility.SINA_APP_KEY).apiSecret(Utility.SINA_APP_SECRET)
	        .callback(Utility.SINA_REDIRECT_URL).debug().build();
			String authorizationUrl = service.getAuthorizationUrl(requestToken);
			mWebView.loadUrl(authorizationUrl);
		} else if (type.equals(Utility.TENCENT)) {
			service = new ServiceBuilder().provider(QWeiboApi.class)
	        .apiKey(Utility.TENCENT_APP_KEY).apiSecret(Utility.TENCENT_APP_SECRET)
	        .callback(Utility.TENCENT_REDIRECT_URL)
	        .signatureType(SignatureType.QueryString)
	        .debug().build();
			new GetAccessTokenTask(OAUTH2_REQUEST_TOKEN_TASK, this).execute();
		} else if (type.equals(Utility.QQ)) {
			service = new ServiceBuilder().provider(QzoneApi.class)
	        .apiKey(Utility.QQ_APP_KEY).apiSecret(Utility.QQ_APP_SECRET)
	        .callback(Utility.QQ_REDIRECT_URL).debug().build();
			new GetAccessTokenTask(OAUTH2_REQUEST_TOKEN_TASK, this).execute();
		}else if (type.equals(Utility.DOUBAN)) {
			service = new ServiceBuilder()
			.provider(DoubanApi.class)
	        .apiKey(Utility.DOUBAN_APP_KEY)
	        .apiSecret(Utility.DOUBAN_APP_SECRET)
	        .callback(Utility.SINA_REDIRECT_URL)
	        .signatureType(SignatureType.Header)
	        .debug().build();
			new GetAccessTokenTask(OAUTH2_REQUEST_TOKEN_TASK, this).execute();
		}
	}
	
	@Override
	public void onTaskStart(String taskName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTaskFinished(String taskName, Object result) {
		Token token = (Token) result;
		if (taskName.equals(OAUTH2_REQUEST_TOKEN_TASK)) {
			// 根据requestToken请求授权界面
			requestToken = token;
			String authorizationUrl = service.getAuthorizationUrl(requestToken); 
			mWebView.loadUrl(authorizationUrl);
		} else {
			// 获取到accessToken界面
			if (token != null) {
				//腾讯微博默认过期时间7天
	    		Toast.makeText(this, token.getRawResponse(), Toast.LENGTH_LONG).show();
	    		Log.d(TAG, "access token::"+token.getRawResponse());
	    		setResult(RESULT_OK);
	    		finish();
	    	}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAccessTokenTask.cancelTask(true);
		mAccessTokenTask=null;
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.w("onPageStarted", url);
			
			//如果授权成功url中包含之前设置的callbackurl包含：授权成功
			if (url.startsWith(Utility.SINA_REDIRECT_URL)) {
				Bundle bundle = Utility.parseUrl(url);
				mAccessTokenTask.execute(bundle.getString("code"));
				view.stopLoading();
			}
			
			if (url.startsWith(Utility.TENCENT_REDIRECT_URL)) {
				Bundle bundle = Utility.parseUrl(url);
				mAccessTokenTask.execute(bundle.getString("oauth_verifier"));
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

	
	private class GetAccessTokenTask extends GenericTask<Token> {

		public GetAccessTokenTask(String taskName, TaskListener taskListener) {
			super(taskName, taskListener);
		}
		
		@Override
		protected Token doInBackground(String... params) {
			Token token = null;
			if (getTaskName().equals(OAUTH2_REQUEST_TOKEN_TASK)) {
				//请求requestToken,新浪微博除外
				token = service.getRequestToken();
			} else { 
				//请求accessToken
				Verifier verifier = new Verifier(params[0]);
	        	token = service.getAccessToken(requestToken, verifier);
			}
			return token;
		}
		
	}
	
}
