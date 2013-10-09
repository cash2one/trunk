package com.shandagames.android.fragment;

import com.shandagames.android.NativeMapActivity;
import com.shandagames.android.app.ActivityHostFragment;
import com.shandagames.android.MapViewerActivity.MapInterface;

public class NativeMapFragment extends ActivityHostFragment<NativeMapActivity> implements MapInterface {

	@Override
	public void center() {
		final NativeMapActivity a = getAttachedActivity();
		if (a == null) return;
		a.center();
	}

	@Override
	protected Class<NativeMapActivity> getActivityClass() {
		return NativeMapActivity.class;
	}

}
