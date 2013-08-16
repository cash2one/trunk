package com.shandagames.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import com.shandagames.android.app.FragmentManagerActivity;
import com.shandagames.android.fragment.PlanetFragment;

public class FragmentTabHostActivtity extends FragmentManagerActivity {

	private FragmentTabHost mTabHost;
	private String[] mTitles = {"首页", "类别", "广场", "更多"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		for (int i=0;i<4;i++) {
			mTabHost.addTab(mTabHost.newTabSpec("Tab"+i).setIndicator(mTitles[i]),PlanetFragment.class, null);
			mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
		}
		
		mTabHost.setCurrentTab(0);
	}
}
