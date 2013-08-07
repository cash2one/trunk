package com.shandagames.android.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class BaseDialogFragment extends DialogFragment {

	public String TAG = "BaseDialogFragment.class";
	
	/**
	 * 必须有此构造方法
	 */
	public BaseDialogFragment() {
		TAG = getClass().getSimpleName();
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
		if (activity == null) return;
		activity.setProgressBarIndeterminateVisibility(visible);
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
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		Log.v(TAG, "onCreateDialog");
		return super.onCreateDialog(savedInstanceState);
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
}
