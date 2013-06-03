package com.shandagames.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shandagames.android.R;

public class DetailFragment extends Fragment {

	public static final String TAG = "AFragment";

	private TextView textViewRamSetting;

	public static DetailFragment newInstance(String status) {
		DetailFragment fragment = new DetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("status", status);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	//创建Fragment
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle bundle) {
		View view = inflater.inflate(R.layout.frag_content_view, viewGroup, false);
		textViewRamSetting = (TextView) view.findViewById(R.id.textViewRamSetting);
		return view;
	}

	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		Bundle b = getArguments();
		String str = null;
		if (b != null && b.getString("status")!=null) {
			str = b.getString("status");
		}
		textViewRamSetting.setText("onActivityCreated >> " + (str==null?this:str));
	}

	//Fragment变为可见时
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	//Fragment进入后台模式时
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	//当Fragment销毁
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}