package com.shandagames.android;

import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.utils.QHttpClient;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * OAuth Version 1.0授权示例（使用WebView方式获取授权码）
 */
public class QWeiBoActivity extends Activity implements View.OnClickListener {

	private static String TAG="QWeiBoActivity";

	private static final int mRequestCode = 1;
	
    /*
     * 申请APP KEY的具体介绍，可参见 
     * http://wiki.open.t.qq.com/index.php/应用接入指引
     */
	
    //认证成功后浏览器会被重定向到这个url中   本例子中不需改动
	private String oauthCallback = "null"; 
    //应用申请到的APP KEY
	private String oauthConsumeKey = "801179720"; 
    //应用申请到的APP SECRET
	private String oauthConsumerSecret="0417dc3b61da4d892bc3ba1dd3f46ec0";
	//Oauth鉴权所需及所得信息的封装存储单元
	private OAuthV1 oAuth; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Button btn = new Button(this);
		btn.setText("腾讯微博");
		btn.setOnClickListener(this);
		//设置界面元素，并添加对各按钮的监听
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1,-2);
		setContentView(btn, lp);
		
        oAuth=new OAuthV1(oauthCallback);
        oAuth.setOauthConsumerKey(oauthConsumeKey);
        oAuth.setOauthConsumerSecret(oauthConsumerSecret);

        //关闭OAuthV1Client中的默认开启的QHttpClient。
		OAuthV1Client.getQHttpClient().shutdownConnection();
		//为OAuthV1Client配置自己定义QHttpClient。
		OAuthV1Client.setQHttpClient(new QHttpClient());
    }

	@Override
	public void onClick(View v) {
		try {
			//向腾讯微博开放平台请求获得未授权的Request_Token
			Log.i(TAG, "---------Step1: Get requestToken--------");
			oAuth=OAuthV1Client.requestToken(oAuth);
			 
			Log.i(TAG, "---------Step2: authorization--------");
			//创建Intent,使用WebView让用户授权
	        Intent intent = new Intent(this, OAuthV1AuthorizeWebView.class);
	        intent.putExtra("oauth", oAuth);
			startActivityForResult(intent,mRequestCode);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		//按下的如果是BACK，同时没有重复
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	    	//关闭OAuthV1Client中的自定义的QHttpClient。
	        OAuthV1Client.getQHttpClient().shutdownConnection();
	        finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/*
	 * 通过读取OAuthV1AuthorizeWebView返回的Intent，获取用户授权后的验证码
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)	{
		if (requestCode==mRequestCode)	{
			if (resultCode==OAuthV1AuthorizeWebView.RESULT_CODE)	{
				try {
					//从返回的Intent中获取验证码
					oAuth=(OAuthV1) data.getExtras().getSerializable("oauth");
					
					/*
					 * 注意：此时oauth中的Oauth_token和Oauth_token_secret将发生变化，用新获取到的
					 * 已授权的access_token和access_token_secret替换之前存储的未授权的request_token
					 * 和request_token_secret.
					 */
					Log.i(TAG, "---------Step3: getAccessToken--------");
					oAuth=OAuthV1Client.accessToken(oAuth);
					
					Log.i(TAG, "---------Step4: Test API V1--------");
					//微博信息封装存储单元
					UserAPI userAPI = new UserAPI(OAuthConstants.OAUTH_VERSION_1);
					String response = userAPI.info(oAuth, "json");
					userAPI.shutdownConnection();
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
