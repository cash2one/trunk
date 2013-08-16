package com.shandagames.android.fragment;

import com.shandagames.android.InternalSettingsActivity;
import com.shandagames.android.app.ActivityHostFragment;

public class InternalSettingsFragment extends ActivityHostFragment<InternalSettingsActivity> {

	@Override
	protected Class<InternalSettingsActivity> getActivityClass() {
		return InternalSettingsActivity.class;
	}

}
