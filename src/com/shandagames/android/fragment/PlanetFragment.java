package com.shandagames.android.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shandagames.android.R;
import com.shandagames.android.app.BaseFragment;

public class PlanetFragment extends BaseFragment {

	public static final String ARG_PLANET_EXTRAS = "planet";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.planet_list_item, null);
		TextView textView = (TextView) rootView.findViewById(android.R.id.text1);
		textView.setText(getArguments().getString(ARG_PLANET_EXTRAS));
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// 日志级别输出
		Log.v(TAG, Log.isLoggable(TAG, Log.VERBOSE)+"");
		Log.d(TAG, Log.isLoggable(TAG, Log.DEBUG)+"");
		Log.i(TAG, Log.isLoggable(TAG, Log.INFO)+"");
		Log.w(TAG, Log.isLoggable(TAG, Log.WARN)+"");
		Log.e(TAG, Log.isLoggable(TAG, Log.ERROR)+"");
	}

	public boolean isBackStack() {
		return false;
	}
}
