package com.shandagames.android.fragment;

import com.shandagames.android.app.BaseFragment;
import com.shandagames.android.support.accessor.ViewAccessor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewFragment extends BaseFragment {
	public static final String INTENT_KEY_URI = "uri";
	
	private WebView mWebView;

	public final WebView getWebView() {
		return mWebView;
	}

	public final void loadUrl(final String url) {
		mWebView.loadUrl(url == null ? "about:blank" : url);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ViewAccessor.setLayerType(mWebView, View.LAYER_TYPE_SOFTWARE, null);
		mWebView.setWebViewClient(new DefaultWebViewClient(getActivity()));
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		final Bundle bundle = getArguments();
		if (bundle != null) {
			final String url = bundle.getString(INTENT_KEY_URI);
			loadUrl(url);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return mWebView = new WebView(getActivity());
	}

	@Override
	public boolean onBackPressProcess() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}

	public final void setWebViewClient(final WebViewClient client) {
		mWebView.setWebViewClient(client);
	}

	public static class DefaultWebViewClient extends WebViewClient {

		private final FragmentActivity mActivity;

		public DefaultWebViewClient(final FragmentActivity activity) {
			mActivity = activity;
		}

		@Override
		public void onPageFinished(final WebView view, final String url) {
			super.onPageFinished(view, url);
			mActivity.setTitle(view.getTitle());
			mActivity.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mActivity.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		@TargetApi(Build.VERSION_CODES.FROYO)
		public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
			handler.proceed();
		}

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
