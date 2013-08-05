package com.shandagames.android;

import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import com.shandagames.android.app.FragmentManagerActivity;
import com.shandagames.android.fragment.PlanetFragment;

public class ActionTabActivity extends FragmentManagerActivity {

	private String[] mTitles = {"首页", "类别", "热门免费", "免费新品"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout container = new FrameLayout(this);
		container.setId(android.R.id.content);
		setContentView(container);
	}

	private void setNavigationModeTab() {
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String itemTab : mTitles) {
			Tab tab = actionBar.newTab().setText(itemTab)
					  .setTabListener(new FragmentTabListener(itemTab));
			actionBar.addTab(tab);
		}
		actionBar.getTabAt(0).select();
	}
	
	private void setNavigationModeList() {
		List<String> data = Arrays.asList(mTitles);
		// 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// 生成一个SpinnerAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
		// 为ActionBar设置下拉菜单和监听器
		actionBar.setListNavigationCallbacks(adapter, new FragmentListListener(data));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_tab_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mode_standard:
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			break;
		case R.id.mode_list:
			setNavigationModeList();
			break;
		case R.id.mode_tabs:
			setNavigationModeTab();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class FragmentTabListener implements ActionBar.TabListener {

		private String title;
		private Fragment fragment;
		
		public FragmentTabListener(String title) {
			this.title = title;
			this.fragment = PlanetFragment.newInstance(title);
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(android.R.id.content, fragment, title);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}
	
	class FragmentListListener implements ActionBar.OnNavigationListener {
		private List<String> data;
		
		public FragmentListListener(List<String> items) {
			this.data = items;
		}
		
		@Override
		public boolean onNavigationItemSelected(int position, long itemId) {
			showFragment(android.R.id.content, PlanetFragment.newInstance(data.get(position)));
			return true;
		}
		
	}
}
