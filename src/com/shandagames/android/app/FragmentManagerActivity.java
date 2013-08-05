package com.shandagames.android.app;

import com.shandagames.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;

/**
 * @file FragmentManagerActivity.java
 * @create 2013-8-5 下午05:01:09
 * @author lilong
 * @description TODO Activity基类，封装Fragment栈管理
 */
public abstract class FragmentManagerActivity extends BaseActivity {

	// Fragment入栈的栈名
	private static final String STACK_NAME = "LevelStack"; 
	// 记录当前栈顶fragment
	private Fragment currentFragment;
	// 自定义栈存储Fragment
	private FragmentStack fragmentStack;
	// Fragment管理容器FragmentManger
	private FragmentManager fragmentManager;
	// ActionBar视图
	public ActionBar actionBar;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		fragmentStack = FragmentStack.getInstance();
		
		forceShowActionBarOverflowMenu();
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));
	}
	
	public final void showFragment(final int contentViewID, final Fragment fragment) {
		final FragmentTransaction ft = fragmentManager.beginTransaction();
		if (((FragmentCallback) fragment).isCleanStack()) {
			fragmentStack.clearFragments();  // 清空栈中存储的Fragment
			fragmentManager.popBackStack(STACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		} 
		if (((FragmentCallback) fragment).isBackStack()) {
			ft.addToBackStack(STACK_NAME);
			fragmentStack.pushFragment(fragment);
		}
		this.currentFragment = fragment;
		ft.replace(contentViewID, fragment, fragment.getClass().getSimpleName());
		ft.commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			if (currentFragment != null) {
				if (((FragmentCallback) currentFragment).onBackPressProcess()) {
					return true;
				} else {
					// 对Fragment进行弹栈处理
					if (fragmentManager.getBackStackEntryCount() >= 1) {
						fragmentStack.popFragment(); 
						fragmentManager.popBackStackImmediate();
						currentFragment = fragmentStack.peekFragment();
						return true;
					} 
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
