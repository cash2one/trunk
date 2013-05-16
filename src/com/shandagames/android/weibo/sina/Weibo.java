/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shandagames.android.weibo.sina;

import java.io.IOException;
import java.net.MalformedURLException;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Encapsulation main Weibo APIs, Include: 1. getRquestToken , 2.
 * getAccessToken, 3. url request. Used as a single instance class. Implements a
 * weibo api as a synchronized way.
 * 
 * @author ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class Weibo {

    // public static String SERVER = "http://api.t.sina.com.cn/";
    public static String SERVER = "https://api.weibo.com/2/";
    public static String URL_OAUTH_TOKEN = "http://api.t.sina.com.cn/oauth/request_token";
    public static String URL_AUTHORIZE = "http://api.t.sina.com.cn/oauth/authorize";
    public static String URL_ACCESS_TOKEN = "http://api.t.sina.com.cn/oauth/access_token";
    public static String URL_AUTHENTICATION = "http://api.t.sina.com.cn/oauth/authenticate";

    public static String URL_OAUTH2_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";

    // public static String URL_OAUTH2_ACCESS_AUTHORIZE =
    // "http://t.weibo.com:8093/oauth2/authorize";
    public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";

    private static String APP_KEY = "";
    private static String APP_SECRET = "";

    private static Weibo mWeiboInstance = null;
    private Token mAccessToken = null;
    private RequestToken mRequestToken = null;

    private WeiboAuthListener mWeiboAuthListener;

    public static final String TOKEN = "access_token";
    public static final String EXPIRES = "expires_in";
    public static final String DEFAULT_REDIRECT_URI = "wbconnect://success";// 暂不支持
    public static final String DEFAULT_CANCEL_URI = "wbconnect://cancel";// 暂不支持

    private String mRedirectUrl;

    private Weibo() {
        Utility.setRequestHeader("Accept-Encoding", "gzip");
        Utility.setTokenObject(this.mRequestToken);
        mRedirectUrl = DEFAULT_REDIRECT_URI;
    }

    public synchronized static Weibo getInstance() {
        if (mWeiboInstance == null) {
            mWeiboInstance = new Weibo();
        }
        return mWeiboInstance;
    }

    // 设置accessToken
    public void setAccessToken(AccessToken token) {
        mAccessToken = token;
    }

    public Token getAccessToken() {
        return this.mAccessToken;
    }

    public void setupConsumerConfig(String consumer_key, String consumer_secret) {
        Weibo.APP_KEY = consumer_key;
        Weibo.APP_SECRET = consumer_secret;
    }

    public static String getAppKey() {
        return Weibo.APP_KEY;
    }

    public static String getAppSecret() {
        return Weibo.APP_SECRET;
    }

    public void setRequestToken(RequestToken token) {
        this.mRequestToken = token;
    }

    public static String getSERVER() {
        return SERVER;
    }

    public static void setSERVER(String sERVER) {
        SERVER = sERVER;
    }

    // 设置oauth_verifier
    public void addOauthverifier(String verifier) {
        mRequestToken.setVerifier(verifier);
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String mRedirectUrl) {
        this.mRedirectUrl = mRedirectUrl;
    }

    /**
     * Requst sina weibo open api by get or post
     * 
     * @param url
     *            Openapi request URL.
     * @param params
     *            http get or post parameters . e.g.
     *            gettimeling?max=max_id&min=min_id max and max_id is a pair of
     *            key and value for params, also the min and min_id
     * @param httpMethod
     *            http verb: e.g. "GET", "POST", "DELETE"
     * @throws IOException
     * @throws MalformedURLException
     * @throws WeiboException
     */
    public String request(Context context, String url, WeiboParameters params, String httpMethod,
            Token token) throws WeiboException {
        String rlt = Utility.openUrl(context, url, httpMethod, params, this.mAccessToken);
        return rlt;
    }

    /**/
    public RequestToken getRequestToken(Context context, String key, String secret,
            String callback_url) throws WeiboException {
        Utility.setAuthorization(new RequestTokenHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("oauth_callback", callback_url);
        String rlt;
        rlt = Utility.openUrl(context, Weibo.URL_OAUTH_TOKEN, "POST", postParams, null);
        RequestToken request = new RequestToken(rlt);
        this.mRequestToken = request;
        return request;
    }

    public AccessToken generateAccessToken(Context context, RequestToken requestToken)
            throws WeiboException {
        Utility.setAuthorization(new AccessTokenHeader());
        WeiboParameters authParam = new WeiboParameters();
        authParam.add("oauth_verifier", this.mRequestToken.getVerifier()/* "605835" */);
        authParam.add("source", APP_KEY);
        String rlt = Utility.openUrl(context, Weibo.URL_ACCESS_TOKEN, "POST", authParam,
                this.mRequestToken);
        AccessToken accessToken = new AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    public AccessToken getXauthAccessToken(Context context, String app_key, String app_secret,
            String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new XAuthHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("x_auth_username", usrname);
        postParams.add("x_auth_password", password);
        postParams.add("oauth_consumer_key", APP_KEY);
        String rlt = Utility.openUrl(context, Weibo.URL_ACCESS_TOKEN, "POST", postParams, null);
        AccessToken accessToken = new AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    /**
     * 获取Oauth2.0的accesstoken
     * 
     * https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&
     * client_secret=YOUR_CLIENT_SECRET&grant_type=password&redirect_uri=
     * YOUR_REGISTERED_REDIRECT_URI&username=USER_NAME&pasword=PASSWORD
     * 
     * @param context
     * @param app_key
     * @param app_secret
     * @param usrname
     * @param password
     * @return
     * @throws WeiboException
     */
    public Oauth2AccessToken getOauth2AccessToken(Context context, String app_key,
            String app_secret, String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("username", usrname);
        postParams.add("password", password);
        postParams.add("client_id", app_key);
        postParams.add("client_secret", app_secret);
        postParams.add("grant_type", "password");
        String rlt = Utility.openUrl(context, Weibo.URL_OAUTH2_ACCESS_TOKEN, "POST", postParams,
                null);
        Oauth2AccessToken accessToken = new Oauth2AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    /**
     * User-Agent Flow
     * @param activity
     * @param listener  授权结果监听器
     */
    public void authorize(Activity activity, WebView webView, final WeiboAuthListener listener) {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());
        mWeiboAuthListener = listener;
        startWebViewAuth(activity, webView);
    }

    private void startWebViewAuth(Activity activity, WebView webView) {
    	 WeiboParameters parameters = new WeiboParameters();
    	 CookieSyncManager.createInstance(activity);
    	 webViewAuth(activity, webView, parameters, new WeiboAuthListener() {
			@Override
			public void onWeiboException(WeiboException ex) {
				// TODO Auto-generated method stub
				mWeiboAuthListener.onWeiboException(ex);
			}
			
			@Override
			public void onError(AuthError ex) {
				// TODO Auto-generated method stub
				mWeiboAuthListener.onError(ex);
			}
			
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
                if (null == mAccessToken) {
                    mAccessToken = new Token();
                }
                mAccessToken.setToken(values.getString(TOKEN));
                mAccessToken.setExpiresIn(values.getString(EXPIRES));
                if (isSessionValid()) {
                    Log.d("Weibo-authorize",  "Login Success! access_token=" + mAccessToken.getToken() + " expires=" + mAccessToken.getExpiresIn());
                    mWeiboAuthListener.onComplete(values);
                } else {
                    Log.d("Weibo-authorize", "Failed to receive access token");
                    mWeiboAuthListener.onWeiboException(new WeiboException("Failed to receive access token."));
                }
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				mWeiboAuthListener.onCancel();
			}

			@Override
			public void onPageStarted() {
				// TODO Auto-generated method stub
				mWeiboAuthListener.onPageStarted();
			}

			@Override
			public void onPageFinished() {
				// TODO Auto-generated method stub
				mWeiboAuthListener.onPageFinished();
			}
		});
    }

    private void webViewAuth(Context context, WebView mWebView, WeiboParameters parameters, final WeiboAuthListener listener) {
    	 parameters.add("client_id", APP_KEY);
         parameters.add("response_type", "token");
         parameters.add("redirect_uri", mRedirectUrl);
         parameters.add("display", "mobile");
         if (isSessionValid()) {
             parameters.add(TOKEN, mAccessToken.getToken());
         }
         String url = URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);
         if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
             Utility.showAlert(context, "Error", "Application requires permission to access the Internet");
         } else {
        	 mWebView.setVerticalScrollBarEnabled(false);
             mWebView.setHorizontalScrollBarEnabled(false);
             mWebView.getSettings().setJavaScriptEnabled(true);
             mWebView.requestFocus();
             mWebView.loadUrl(url);
             mWebView.setWebViewClient(new WeiboWebViewClient(context, listener));
         }
    }
    
    public boolean isSessionValid() {
        if (mAccessToken != null) {
            return (!TextUtils.isEmpty(mAccessToken.getToken()) && (mAccessToken.getExpiresIn() == 0 || (System
                    .currentTimeMillis() < mAccessToken.getExpiresIn())));
        }
        return false;
    }
    
    private class WeiboWebViewClient extends WebViewClient {
    	private Context mContext;
    	private WeiboAuthListener mListener;
    	
    	public WeiboWebViewClient(Context context,WeiboAuthListener listener){
    		this.mContext=context;
    		this.mListener=listener;
    	}
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 待后台增加对默认重定向地址的支持后修改下面的逻辑
            if (url.startsWith(getRedirectUrl())) {
                handleRedirectUrl(view, url);
                return true;
            }
            // launch non-dialog URLs in a full browser
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(new AuthError(description, errorCode, failingUrl));
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	//mListener.onPageStarted();
            // google issue. shouldOverrideUrlLoading not executed
            if (url.startsWith(getRedirectUrl())) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
            mListener.onPageStarted();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        	//mListener.onPageFinished();
            super.onPageFinished(view, url);
            mListener.onPageFinished();
        }
        
        private void handleRedirectUrl(WebView view, String url) {
            Bundle values = Utility.parseUrl(url);

            String error = values.getString("error");
            String error_code = values.getString("error_code");

            if (error == null && error_code == null) {
                mListener.onComplete(values);
            } else if (error.equals("access_denied")) {
                // 用户或授权服务器拒绝授予数据访问权限
                mListener.onCancel();
            } else {
                mListener.onWeiboException(new WeiboException(error, Integer.parseInt(error_code)));
            }
        }
    }
}
