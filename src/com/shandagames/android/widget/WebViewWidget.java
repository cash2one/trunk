package com.shandagames.android.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * @file ProgressWebView.java
 * @create 2013-4-10 下午04:49:37
 * @author lilong
 * @description TODO 自定义WebView组件
 */
public class WebViewWidget extends WebView {
	private static final String TAG = "WebViewWidget";
	
	private LinearLayout mContentView;
	private ProgressBar mProgressbar;
	
	public WebViewWidget(Context context) {
		super(context);
		initialize(context);
	}
	
	public WebViewWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	private void initialize(Context context) {
		mContentView = new LinearLayout(context);
		mContentView.setBackgroundColor(Color.WHITE);
		mContentView.setOrientation(LinearLayout.VERTICAL);
		addViewWithProgress(context);
		mContentView.addView(this);
		
		// Configure the webview
	    WebSettings s = getSettings();
	    s.setJavaScriptEnabled(true);
	    s.setBuiltInZoomControls(true);
	    s.setUseWideViewPort(true);
	    s.setLoadWithOverviewMode(true);
	    s.setSavePassword(true);
	    s.setSaveFormData(true);
	    s.setPluginsEnabled(true);
	    s.setAllowFileAccess(true);
		s.setRenderPriority(RenderPriority.HIGH);
//		s.setCacheMode(WebSettings.LOAD_NO_CACHE);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
	    
	    // enable navigator.geolocation 
	    s.setGeolocationEnabled(true);
	    s.setGeolocationDatabasePath("/data/data/"+context.getPackageName()+"/databases/");
	    
	    // enable Web Storage: localStorage, sessionStorage
	    s.setDomStorageEnabled(true);
		
		requestFocus();
		setHardwareAccelerated(((Activity)context).getWindow());
		setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		setWebViewClient(new WebViewClient());
		setWebChromeClient(new WebChromeClient());
		setDownloadListener(new WebViewDownloadListener());
	}
	
	public void addViewWithProgress(Context context) {
		mProgressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal); 
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.height = 5;
		mProgressbar.setLayoutParams(lp);
		mContentView.addView(mProgressbar);
	}
	
	public LinearLayout getLayout() {
		return mContentView;
	}
	
	public class WebViewClient extends android.webkit.WebViewClient {
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		// 消耗掉这个事件。就会不会冒泡传递了
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
		
		@Override
		// 跳转到自定义错误页
		public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
			Log.e(TAG, "errorCode:" + errorCode + ",description:" + description + ", failingUrl:" + failingUrl);
			
			try {
				webView.stopLoading();
			} catch (Exception e) {}
			
			try {
				webView.clearView();
			} catch (Exception e) {}
			
			if (webView.canGoBack()) {
				webView.goBack();
		    }
			
			//view.loadUrl("file:///android_asset/path/to/your/missing-page-template.html");
			webView.loadData("Your internet connection may be down? Please restart your app.", "text/html", "UTF-8"); 
			super.onReceivedError(webView, errorCode, description, failingUrl);
		}
		
		@Override
		@SuppressLint("NewApi")
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
	}
	
	public class WebChromeClient extends android.webkit.WebChromeClient {
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressbar.setVisibility(GONE);
			} else {
				mProgressbar.setVisibility(VISIBLE);
				mProgressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	public class WebViewDownloadListener implements android.webkit.DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			StringBuilder sb = new StringBuilder();
			sb.append("onDownloadStart >>> ");
			sb.append("\n url:" + url);
			sb.append("\n userAgent:" + userAgent);
			sb.append("\n contentDisposition:" + contentDisposition);
			sb.append("\n mimetype:" + mimetype);
			sb.append("\n contentLength:" + contentLength);
			Log.d(TAG, sb.toString());
			//调用系统浏览器下载文件, 如果想自定义下载需开启线程处理
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getContext().startActivity(intent);  
		}
	}

	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	/** 3.0以上设备启用硬件加速  */
	public void setHardwareAccelerated(Window window) {
    	if (Build.VERSION.SDK_INT >= 11) {
    		try {
    			final int FLAG_HARDWARE_ACCELERATED = 0x01000000; 
    		    Method method = Window.class.getMethod("setFlags", int.class, int.class);
		        method.invoke(window, FLAG_HARDWARE_ACCELERATED, FLAG_HARDWARE_ACCELERATED);
		    } catch (Exception e) {
		        Log.e(TAG, "Hardware Acceleration not supported on API " + Build.VERSION.SDK_INT, e);
		    }
    	}
    }
	
	public void callWebViewMethod(String name){
    	// credits: http://stackoverflow.com/questions/3431351/
		//how-do-i-pause-flash-content-in-an-android-webview-when-my-activity-isnt-visible
		try {
            Method method = WebView.class.getMethod(name);
            method.invoke(this);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "No such method: " + name + e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal Access: " + name + e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Invocation Target Exception: " + name + e);
        }
    }
}
