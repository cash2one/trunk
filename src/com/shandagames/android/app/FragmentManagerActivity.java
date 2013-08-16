package com.shandagames.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import com.shandagames.android.fragment.callback.FragmentCallback;

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
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setProgressBarIndeterminateVisibility(false);
		fragmentManager = getSupportFragmentManager();
		fragmentStack = FragmentStack.getInstance();
		forceShowActionBarOverflowMenu();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
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
		ft.replace(contentViewID, fragment);
		ft.commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			if (currentFragment != null) {
				if (((FragmentCallback) currentFragment).onBackPressProcess()) {
					return true;
				} 
				if (fragmentManager.getBackStackEntryCount() > 0) {
					// 对Fragment进行弹栈处理
					fragmentStack.popFragment(); 
					fragmentManager.popBackStackImmediate();
					currentFragment = fragmentStack.peekFragment();
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
