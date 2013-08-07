package com.shandagames.android.app;

import com.shandagames.android.fragment.callback.FragmentCallback;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @file BaseFragment.java
 * @create 2012-8-20 上午11:23:16
 * @author lilong dreamxsky@gmail.com
 * @description Fragment基类，对Fragment栈的管理
 */
public class BaseFragment extends Fragment implements FragmentCallback {

	public String TAG = "BaseFragment.class";
	
	/**
	 * 必须有此构造方法，Layout中的静态Fragment会调用,旋转屏幕也会调用 ;
	 */
	public BaseFragment() {
		TAG = getClass().getSimpleName();
    }

	public ActionBar getActionBar() {
		final Activity activity = getActivity();
		if (activity != null) return ((ActionBarActivity) activity).getSupportActionBar();
		return null;
	}
	
    public AndroidApplication getApplication() {
    	final Activity activity = getActivity();
		if (activity != null) return (AndroidApplication) activity.getApplication();
		return null;
    }
    
    public ContentResolver getContentResolver() {
		final Activity activity = getActivity();
		if (activity != null) return activity.getContentResolver();
		return null;
	}
    
    public SharedPreferences getSharedPreferences(final String name, final int mode) {
		final Activity activity = getActivity();
		if (activity != null) return activity.getSharedPreferences(name, mode);
		return null;
	}
    
    public Object getSystemService(final String name) {
		final Activity activity = getActivity();
		if (activity != null) return activity.getSystemService(name);
		return null;
	}
    
    public void registerReceiver(final BroadcastReceiver receiver, final IntentFilter filter) {
		final Activity activity = getActivity();
		if (activity == null) return;
		activity.registerReceiver(receiver, filter);
	}
    
    public void unregisterReceiver(final BroadcastReceiver receiver) {
		final Activity activity = getActivity();
		if (activity == null) return;
		activity.unregisterReceiver(receiver);
	}
    
    public void setProgressBarIndeterminateVisibility(final boolean visible) {
		final Activity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).setProgressBarIndeterminateVisibility(visible);
		}
	}

    @Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
    	Log.v(TAG, "onInflate");
    	super.onInflate(activity, attrs, savedInstanceState);
    }
    
	@Override
	public void onAttach(Activity activity) {
		Log.v(TAG, "onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.v(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.v(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		Log.v(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		Log.v(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.v(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.v(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		Log.v(TAG, "onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.v(TAG, "onDetach");
		super.onDetach();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
		Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 获取栈的数量
	 */
	public int getStackEntryCount() {
		return getFragmentManager().getBackStackEntryCount();
	}
	
	
	public boolean isBackStack() {
		return true;
	}
	
	public boolean isCleanStack() {
		return false;
	}
	
	/**
	 * 子类back键处理方法，如需特殊处理，请覆写该方法
	 */
	public boolean onBackPressProcess() {
		return false;
	}
}
