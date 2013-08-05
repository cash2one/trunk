package com.shandagames.android;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import com.shandagames.android.fragment.DetailFragment;

public class ActionActivity extends ActionBarActivity {

	private static final int resId = android.R.id.content;
	
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	private FragmentTransaction trans;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout container = new FrameLayout(this);
		container.setId(resId);
		container.setBackgroundColor(getResources().getColor(android.R.color.white));
		setContentView(container);
		
		forceShowActionBarOverflowMenu();
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setNavigationModeTab();
		//setNavigationModeList();
	}

	private void setNavigationModeTab() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("Tab1").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab1"))), true);
		actionBar.addTab(actionBar.newTab().setText("Tab2").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab2"))));
		actionBar.addTab(actionBar.newTab().setText("Tab3").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab3"))));
		actionBar.addTab(actionBar.newTab().setText("Tab4").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab4"))));
		actionBar.addTab(actionBar.newTab().setText("Tab5").setTabListener(new FragmentTabListener(DetailFragment.newInstance("Tab5"))));
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
	
	/** 在有 menu按键的手机上面，ActionBar 上的 overflow menu 默认不会出现，只有当点击了 menu按键时才会显示 */
	private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub = menu.addSubMenu("Theme");
        sub.add(0, R.style.Theme_AppCompat, 0, "Default");
        sub.add(0, R.style.Theme_AppCompat_Light, 0, "Light");
        sub.add(0, R.style.Theme_AppCompat_Light_DarkActionBar, 0, "Light (Dark Action Bar)");
        // setShowAsAction：设置ActionBar中menu的显示方式
        sub.getItem().setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
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
