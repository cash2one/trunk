package com.shandagames.android.widget;

import java.util.HashMap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class TabManager implements OnTabChangeListener {

	private int containerId;
	private TabHost tabhost = null;
	private TabInfo currentTab = null;
	private FragmentActivity activity = null;
	private final HashMap<String, TabInfo> tabInfoMap = new HashMap<String, TabInfo>();

	public TabManager(FragmentActivity activity, TabHost tabhost,
			int containerId) {
		this.activity = activity;
		this.tabhost = tabhost;
		this.containerId = containerId;
		this.tabhost.setOnTabChangedListener(this);
	}

	public void addTab(TabSpec tabspec, Class<?> klass, Bundle args) {
		String tag = tabspec.getTag();
		TabInfo tabInfo = new TabInfo(tag, klass, args);

		// check is the tab already exist.
		// If so deactivate it bcoz the initiate state is not show.
		tabInfo.fragment = this.activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction transaction = this.activity
					.getSupportFragmentManager().beginTransaction();
			transaction.detach(tabInfo.fragment).commit();
		}
		this.tabInfoMap.put(tag, tabInfo);
		this.tabhost.addTab(tabspec);
	}

	@Override
	public void onTabChanged(String tag) {
		TabInfo newTab = this.tabInfoMap.get(tag);
		if (this.currentTab == newTab) {
			return;
		}
		FragmentTransaction transaction = this.activity
				.getSupportFragmentManager().beginTransaction();
		// detach current tab
		if (this.currentTab != null && this.currentTab.fragment != null) {
			transaction.detach(this.currentTab.fragment);
		}
		if (newTab != null) {
			if (newTab.fragment == null) {
				newTab.fragment = Fragment.instantiate(this.activity,
						newTab.klass.getName(), newTab.args);
				transaction.add(this.containerId, newTab.fragment, newTab.tag);
			} else {
				transaction.attach(newTab.fragment);
			}
		}
		this.currentTab = newTab;
		transaction.commit();
		this.activity.getSupportFragmentManager().executePendingTransactions();
	}

	
	class TabInfo {
		private final String tag;
		private final Class<?> klass;
		private final Bundle args;
		private Fragment fragment = null;

		TabInfo(String tag, Class<?> klass, Bundle args) {
			this.tag = tag;
			this.klass = klass;
			this.args = args;
		}
	}

}
