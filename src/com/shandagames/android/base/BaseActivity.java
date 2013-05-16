package com.shandagames.android.base;

import android.os.Bundle;
import com.shandagames.android.log.Log;
import com.shandagames.android.task.TaskListener;
import com.shandagames.android.util.ActivityStack;

import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @file BaseActivity.java
 * @create 2012-8-20 上午11:23:16
 * @author lilong
 * @description 封装自定义Activity基类
 */
public abstract class BaseActivity extends FragmentActivity implements TaskListener {
	private static final String STATES_KEY = "android:states";
	protected LocalActivityManager mLocalActivityManager;

	private String TAG = "BaseActivity.class";
	
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getCanonicalName();
		Log.d(TAG, "onCreate");
		
		Bundle states = savedInstanceState != null ? savedInstanceState
				.getBundle(STATES_KEY) : null;
		mLocalActivityManager = new LocalActivityManager(this, true);
		mLocalActivityManager.dispatchCreate(states);
		
		_onCreate(savedInstanceState);
		
		// 当前活动界面添加栈中
		ActivityStack.getInstance().pushActivity(this);
		// 开始trace性能调试
		//android.os.Debug.startMethodTracing(TAG);
	}

	// 因为onCreate方法无法返回状态，因此无法进行状态判断，
	// 为了能对上层返回的信息进行判断处理，我们使用_onCreate代替真正的
	// onCreate进行工作。onCreate仅在顶层调用_onCreate。
	protected abstract void _onCreate(Bundle savedInstanceState);
	
	
	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected final void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
		addNavagationAndStatus();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		mLocalActivityManager.dispatchResume();
	}

	@Override
	//应用遇到意外情况（如：内存不足、用户直接按Home键）由系统销毁一个Activity时，此方法会被调用;
	//通常onSaveInstanceState()只适合于保存一些临时性的状态，而onPause()适合用于数据的持久化保存
	protected final void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		Bundle state = mLocalActivityManager.saveInstanceState();
		if (state != null) {
			outState.putBundle(STATES_KEY, state);
		}
	}

	/** 可以返回一个包含有状态信息的Object，其中甚至可以包含Activity Instance本身 */
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		// onRetainNonConfigurationInstance()在onSaveInstanceState()之后被调用
		// 调用顺序同样介于onStop()和 onDestroy()之间，横竖屏切换保存数据
		Log.d(TAG, "onRetainCustomNonConfigurationInstance");
		return super.onRetainCustomNonConfigurationInstance();
	}

	/** 恢复窗口时，可以直接使用该方法返回数据 */
	@Override
	public Object getLastCustomNonConfigurationInstance() {
		// TODO Auto-generated method stub
		Log.d(TAG, "getLastNonConfigurationInstance");
		return super.getLastCustomNonConfigurationInstance();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		mLocalActivityManager.dispatchPause(this.isFinishing());
		// 终止性能分析,调试代码会在SDcard中生成追踪文件
		//android.os.Debug.stopMethodTracing();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
		mLocalActivityManager.dispatchStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		ActivityStack.getInstance().removeActivity(this);
		mLocalActivityManager.dispatchDestroy(this.isFinishing());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		Log.d(TAG, "onCreateDialog");
		return super.onCreateDialog(id, args);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		Log.d(TAG, "onPrepareDialog");
		super.onPrepareDialog(id, dialog, args);
	}

	// 添加导航组件
	protected void addNavagationAndStatus() {
	}

	@Override
	public void onTaskStart(String taskName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTaskFinished(String taskName, Object result) {
		// TODO Auto-generated method stub
	}
	
}
