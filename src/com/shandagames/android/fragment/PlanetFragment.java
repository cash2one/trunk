package com.shandagames.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shandagames.android.R;
import com.shandagames.android.app.BaseFragment;

public class PlanetFragment extends BaseFragment {

	public static final String ARG_PLANET_EXTRAS = "planet";
	
	public static PlanetFragment newInstance(String title) {
		PlanetFragment fragment = new PlanetFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_PLANET_EXTRAS, title);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.planet_list_item, null);
		TextView textView = (TextView) rootView.findViewById(android.R.id.text1);
		String message = "";
		if (getArguments() != null) {
			message = getArguments().getString(ARG_PLANET_EXTRAS);
		}
		textView.setText(message);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
