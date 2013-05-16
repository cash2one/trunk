/**
 * 
 */
package com.shandagames.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @file PlaceMarketActivity.java
 * @create 2012-10-29 下午4:08:26
 * @author lilong
 * @description TODO
 */
public class WebViewClientActivity extends Activity {

	private final static int FILECHOOSER_RESULTCODE=0x0001;  
	   
	private WebView webView;

	private ValueCallback<Uri> mUploadMessage; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		webView = new WebView(this);
		webView.setWebViewClient(new ViewClient());
		webView.setWebChromeClient(new ViewChromeClient());
		
		setContentView(webView);
	}

	private class ViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
		
		@Override
    	public void onPageFinished(WebView view, String url) {
    		// TODO Auto-generated method stub
    		super.onPageFinished(view, url);
    	}
	}

	public class ViewChromeClient extends WebChromeClient {
		//The undocumented magic method override  
        //Eclipse will swear at you if you try to put @Override here  
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  
	         mUploadMessage = uploadMsg;  
	         Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	         i.addCategory(Intent.CATEGORY_OPENABLE);  
	         i.setType("image/*");  
	         startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  
        }  
	}
	
	@Override  
	 protected void onActivityResult(int requestCode, int resultCode,  Intent intent) {
		if(requestCode==FILECHOOSER_RESULTCODE)  {
			if (null == mUploadMessage) return;  
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();  
            mUploadMessage.onReceiveValue(result);  
            mUploadMessage = null;  
		}
	 }  
	
	// To handle "Back" key press event for WebView to go back to previous screen.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
