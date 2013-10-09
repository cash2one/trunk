package com.shandagames.android.app;

import java.lang.reflect.Field;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import com.shandagames.android.debug.ViewServer;
import android.content.res.Configuration;

/**
 * @file BaseActivity.java
 * @create 2012-8-20 上午11:23:16
 * @author lilong dreamxsky@gmail.com
 * @description Activity基类
 */
public class BaseActivity extends ActionBarActivity {

	public String TAG = "BaseActivity.class";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TAG = getClass().getSimpleName();
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		// 开启fragment的debug模式
		FragmentManager.enableDebugLogging(false);
		// 开启视图层次优化调试
		ViewServer.get(this).addWindow(this);
		// 开始trace性能调试
		//android.os.Debug.startMethodTracing(TAG);
	}

	@Override
	protected void onRestart() {
		Log.v(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		Log.v(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "onResume");
		super.onResume();
		
		// 视图层次优化：onResume
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	protected void onPause() {
		Log.v(TAG, "onPause");
		super.onPause();
		
		// 终止性能分析,调试代码会生成trace文件
		//android.os.Debug.stopMethodTracing();
	}

	@Override
	protected void onStop() {
		Log.v(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
		
		// 视图层次优化：onDestroy
		ViewServer.get(this).removeWindow(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, "onConfigurationChanged");
	    super.onConfigurationChanged(newConfig);
	    
	    // 在Manifest中声明android:configChanges="orientation|keyboard|keyboardHidden"之后
	    // 在改变屏幕方向、弹出软件盘和隐藏软键盘时，不再去执行onCreate()方法，而是直接执行onConfigurationChanged()
	}
	
	@Override
	public void onBackPressed() {
		Log.v(TAG, "onBackPressed");
		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.v(TAG, "onKeyDown");
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.v(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		
		//应用遇到意外情况（如：内存不足、用户直接按Home键）由系统销毁一个Activity时，此方法会被调用;
		//通常只适合于保存一些临时性的状态，而onPause()适合用于数据的持久化保存
	}

	@Override
	protected final void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.v(TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
		
		// Note: onSaveInstanceState方法和onRestoreInstanceState方法不一定是成对的被调用;
		// onRestoreInstanceState被调用的前提是，activity确实被系统销毁了;
		// 如果仅仅是停留在有这种可能性的情况下，则该方法不会被调用;
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		Log.v(TAG, "onRetainCustomNonConfigurationInstance");
		return super.onRetainCustomNonConfigurationInstance();
		
		// 通常在onSaveInstanceState()之后被调用，调用顺序同样介于onStop()和 onDestroy()之间，
		// 横竖屏切换保存数据，可以返回一个包含有状态信息的Object，其中甚至可以包含Activity本身
	}

	@Override
	public Object getLastCustomNonConfigurationInstance() {
		Log.v(TAG, "getLastNonConfigurationInstance");
		return super.getLastCustomNonConfigurationInstance();
		
		// 通常与onRetainCustomNonConfigurationInstance函数一起使用
		// 恢复窗口时，可以直接使用getLastCustomNonConfigurationInstance()返回数据
	}
	
	public final void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }
    }
}
