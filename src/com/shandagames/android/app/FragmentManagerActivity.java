package com.shandagames.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import com.shandagames.android.fragment.FragmentCallback;

/**
 * @file FragmentManagerActivity.java
 * @create 2013-8-5 下午05:01:09
 * @author lilong
 * @description TODO Activity基类，封装Fragment栈管理
 */
public abstract class FragmentManagerActivity extends BaseActivity {

	// 记录当前栈顶fragment
	private Fragment currentFragment;
	// Fragment管理容器FragmentManger
	private FragmentManager fragmentManager;
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setProgressBarIndeterminateVisibility(false);
		FragmentManager.enableDebugLogging(false);
		fragmentManager = getSupportFragmentManager();
		forceShowActionBarOverflowMenu();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	public final void showFragment(final int resViewId, final Fragment fragment) {
		final FragmentTransaction ft = fragmentManager.beginTransaction();
		if (((FragmentCallback) fragment).isCleanStack()) {
			clearFragment();
		} 
		String tag = fragment.getClass().getSimpleName();
		if (((FragmentCallback) fragment).isBackStack()) {
			ft.addToBackStack(tag);
		}
		this.currentFragment = fragment;
		ft.replace(resViewId, fragment, tag);
		ft.commit();
	}
	
	private Fragment peekFragment() {
		int backStackCount = fragmentManager.getBackStackEntryCount();
		if (backStackCount > 0) {
			BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount-1);
			return fragmentManager.findFragmentByTag(backEntry.getName());
		}
		return null;
	}
	
	private void clearFragment() {
		int backStackCount = fragmentManager.getBackStackEntryCount();
		for (int i=0; i < backStackCount; i++) {
			fragmentManager.popBackStackImmediate();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			if (currentFragment != null) {
				if (((FragmentCallback) currentFragment).onBackPressProcess()) {
					return true;
				} 
				if (fragmentManager.getBackStackEntryCount() > 0) {
					fragmentManager.popBackStackImmediate();
					this.currentFragment = peekFragment();
					return true;
				} 
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}
