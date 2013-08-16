package com.shandagames.android;

import android.os.Bundle;
import android.view.View;
import com.shandagames.android.app.FragmentManagerActivity;
import android.support.v4.widget.SlidingPaneLayout;

public class SlidingPanelActivity extends FragmentManagerActivity {

	private SlidingPaneLayout mSlidingLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding_pane_layout);
		mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
		mSlidingLayout.setPanelSlideListener(new SliderListener());
		mSlidingLayout.openPane();
	}
	
	private class SliderListener extends SlidingPaneLayout.SimplePanelSlideListener {
		
		@Override
        public void onPanelOpened(View panel) {
        }

        @Override
        public void onPanelClosed(View panel) {
        }
        
        @Override
		public void onPanelSlide(View panel, float slideOffset) {
		}
	}
}
