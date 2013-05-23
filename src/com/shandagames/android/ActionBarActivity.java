package com.shandagames.android;

import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.shandagames.android.fragment.DetailFragment;

public class ActionBarActivity extends SherlockFragmentActivity {

	private static final int resId = android.R.id.content;
	
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	private FragmentTransaction trans;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		
		FrameLayout container = new FrameLayout(this);
		container.setId(resId);
		container.setBackgroundColor(getResources().getColor(android.R.color.white));
		setContentView(container);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//setNavigationModeTab();
		setNavigationModeList();
	}

	private void setNavigationModeTab() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("Tab1").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab1"))), true);
		actionBar.addTab(actionBar.newTab().setText("Tab2").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab2"))));
	}
	
	private void setNavigationModeList() {
		List<String> data = Arrays.asList(getResources().getStringArray(R.array.simple_menu_list));
		// 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// 生成一个SpinnerAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
		// 为ActionBar设置下拉菜单和监听器
		actionBar.setListNavigationCallbacks(adapter, new FragmentListListener(data));
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		SubMenu sub = menu.addSubMenu("Theme");
        sub.add(0, R.style.Theme_Sherlock, 0, "Default");
        sub.add(0, R.style.Theme_Sherlock_Light, 0, "Light");
        sub.add(0, R.style.Theme_Sherlock_Light_DarkActionBar, 0, "Light (Dark Action Bar)");
        // setShowAsAction：设置ActionBar中menu的显示方式
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==android.R.id.home) {
			finish();
			return false;
		}
		setTheme(item.getItemId());
		return true;
	}
	
	class FragmentTabListener implements ActionBar.TabListener {

		private Fragment fragment;
		
		public FragmentTabListener(Fragment fragment) {
			this.fragment = fragment;
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.add(android.R.id.content, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.remove(fragment);
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}
		
	}
	
	class FragmentListListener implements ActionBar.OnNavigationListener {
		private List<String> data;
		
		public FragmentListListener(List<String> items) {
			this.data = items;
		}
		
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// TODO Auto-generated method stub
			fragmentManager = getSupportFragmentManager();
			trans = fragmentManager.beginTransaction();
			// 添加一种切换动画
			trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			//trans.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
			trans.replace(android.R.id.content, DetailFragment.newInstance(data.get(itemPosition)));
			trans.addToBackStack(null);
			trans.commit();
			
			return true;
		}
		
	}
}
