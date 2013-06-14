package com.shandagames.android.app;

import com.shandagames.android.log.Log;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @file BaseFragment.java
 * @create 2012-8-20 上午11:23:16
 * @author lilong
 * @description 自定义Fragment基类
 */
public abstract class BaseFragment extends Fragment {

	private String TAG = "BaseFragment.class";
	

	public BaseFragment() {
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
	public void onAttach(Activity activity) {
		TAG = this.getClass().getSimpleName();
		Log.d(TAG, "onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public abstract View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
	
	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "onDetach");
		super.onDetach();
	}

}
