package com.shandagames.android.app;

import android.app.LocalActivityManager;
import android.os.Bundle;

/**
 * This is a fragment that will be used during transition from activities to
 * fragments.
 */
@SuppressWarnings("deprecation")
public class LocalActivityManagerActivity extends BaseActivity {

	private static final String KEY_STATE_BUNDLE = "LocalActivityManagerState";

	private LocalActivityManager mLocalActivityManager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle state = null;
		if (savedInstanceState != null) {
			state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
		}

		mLocalActivityManager = new LocalActivityManager(this, true);
		mLocalActivityManager.dispatchCreate(state);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocalActivityManager.dispatchDestroy(this.isFinishing());
	}

	@Override
	public void onPause() {
		super.onPause();
		mLocalActivityManager.dispatchPause(this.isFinishing());
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocalActivityManager.dispatchResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
	}

	@Override
	public void onStop() {
		super.onStop();
		mLocalActivityManager.dispatchStop();
	}

	protected LocalActivityManager getLocalActivityManager() {
		return mLocalActivityManager;
	}
}
