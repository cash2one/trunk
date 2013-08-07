package com.shandagames.android.fragment.callback;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {

	private String mTag;
	private Class<T> mClass;
	private Fragment mFragment;
	private Activity mActivity;

	/** Constructor used each time a new tab is created. 
     * @param activity  The host Activity, used to instantiate the fragment 
     * @param tag  The identifier tag for the fragment 
     * @param clz  The fragment's Class, used to instantiate the fragment 
     */  
	public TabListener(Activity activity, String tag, Class<T> clz) {
		this.mTag = tag;
		this.mClass = clz;
		this.mActivity = activity;
	}

	/* The following are each of the ActionBar.TabListener callbacks */ 
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		 // Check if the fragment is already initialized  
        if (mFragment == null) {  
            // If not, instantiate and add it to the activity  
            mFragment = Fragment.instantiate(mActivity, mClass.getName());  
            ft.add(android.R.id.content, mFragment, mTag);  
        } else {  
            // If it exists, simply attach it in order to show it  
            ft.attach(mFragment);  
        }  
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {  
            // Detach the fragment, because another one is being attached  
            ft.detach(mFragment);  
        }  
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}
