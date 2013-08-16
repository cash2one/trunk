package com.shandagames.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.shandagames.android.app.FragmentManagerActivity;
import com.shandagames.android.widget.SlidingUpPanelLayout;
import com.shandagames.android.widget.SlidingUpPanelLayout.PanelSlideListener;

public class SlidingUpPanelActivity extends FragmentManagerActivity {
	private static final String TAG = "SlidingUpPanelActivity";

	private SlidingUpPanelLayout mSlidingUpPanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding_up_pane_layout);
		
		mSlidingUpPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mSlidingUpPanel.setShadowDrawable(getResources().getDrawable(R.drawable.drawer_shadow));
		mSlidingUpPanel.setPanelSlideListener(new SliderListener());
	}

	private class SliderListener implements PanelSlideListener {

		@Override
		public void onPanelSlide(View panel, float slideOffset) {
			Log.d(TAG, "onPanelSlide");
		}

		@Override
		public void onPanelCollapsed(View panel) {
			Log.d(TAG, "onPanelCollapsed");
		}

		@Override
		public void onPanelExpanded(View panel) {
			Log.d(TAG, "onPanelExpanded");
		}
		
	}
}
