package com.shandagames.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import com.shandagames.android.widget.WebViewWidget;

/**
 * @file WebPlayerActivity.java
 * @create 2013-4-10 下午05:13:22
 * @author lilong
 * @description TODO 网页视频播放
 */
public class WebPlayerActivity extends Activity {

	private WebViewWidget mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebView = new WebViewWidget(this);

		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
        } else {
        	mWebView.loadUrl("http://coop.fengyunlive.com/channel-list");
        }
		setContentView(mWebView.getLayout());
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.callWebViewMethod("onResume");
		mWebView.resumeTimers();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.callWebViewMethod("onPause");
		mWebView.pauseTimers();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mWebView.stopLoading();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.clearView();
		mWebView.destroy();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}