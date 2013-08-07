package com.shandagames.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.shandagames.android.app.FragmentManagerActivity;
import com.shandagames.android.fragment.ExitTipsDialogFragment;
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
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mPlanetTitles = getResources().getStringArray(R.array.simple_menu_list);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				selectItem(position);
			}
		});
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        toggleDrawer();
        
        if (savedInstanceState == null) {
        	selectItem(0);
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
			default:
				showFragment(resId, PlanetFragment.newInstance(mPlanetTitles[position]));
				break;
		}
	}
	
	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
	
	public void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	
	/**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			if (fragmentManager.getBackStackEntryCount() == 0) {
				ExitTipsDialogFragment dialogFragment = new ExitTipsDialogFragment();
				dialogFragment.show(fragmentManager, dialogFragment.getClass().getName());
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
