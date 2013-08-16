package com.shandagames.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.shandagames.android.app.FragmentManagerActivity;
import com.shandagames.android.fragment.HomeFragment;
import com.shandagames.android.fragment.PlanetFragment;
import com.shandagames.android.fragment.WebFlotr2Fragment;
import com.shandagames.android.fragment.WidgetFragment;

public class HomeActivity extends FragmentManagerActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	private ActionBar actionBar;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        actionBar = getSupportActionBar();
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.simple_menu_list);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new ListItemClickListener());
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggleImpl(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close); 
        mDrawerLayout.setDrawerListener(mDrawerToggle);
      
        if (savedInstanceState == null) {
        	selectItem(0);
        }
	}

	private class ListItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	selectItem(position);
	    }
	}
	
	private class ActionBarDrawerToggleImpl extends ActionBarDrawerToggle {

		public ActionBarDrawerToggleImpl(Activity activity, DrawerLayout drawerLayout, 
				int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			super.onDrawerClosed(drawerView);
			actionBar.setTitle(mTitle);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			actionBar.setTitle(mDrawerTitle);
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			super.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			super.onDrawerStateChanged(newState);
		}
	}

	
	private void selectItem(int position) {
        // update the main content by replacing fragments
        onItemChanged(position);
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
	
	private void onItemChanged(int position) {
		int resId = R.id.content_frame;
		switch (position) {
			case 0:
				showFragment(resId, new HomeFragment());
				break;
			case 1:
				startActivity(new Intent(this, ActionTabActivity.class));
				break;
			case 2:
				showFragment(resId, new WidgetFragment());
				break;
			case 3:
				showFragment(resId, new WebFlotr2Fragment());
				break;
			case 4:
				startActivity(new Intent(this, FragmentTabHostActivtity.class));
				break;
			default:
				showFragment(resId, PlanetFragment.newInstance(mPlanetTitles[position]));
				break;
		}
	}
	
	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }
	
	public void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == android.R.id.home) {
			toggleDrawer();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
