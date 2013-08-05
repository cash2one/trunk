package com.shandagames.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

public abstract class FragmentManagerActivity extends BaseActivity {

	// Fragment入栈的栈名
	private static final String STACK_NAME = "LevelStack"; 
	// 记录当前栈顶fragment
	private Fragment currentFragment;
	// 自定义栈存储Fragment
	private FragmentStack fragmentStack;
	// Fragment管理容器FragmentManger
	private FragmentManager fragmentManager;
	
	private boolean isExit = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		fragmentStack = FragmentStack.getInstance();
	}
	
	public final void showFragment(final int contentViewID, final Fragment fragment) {
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (((FragmentCallback) fragment).isCleanStack()) {
			// 清空栈中存储的Fragment
			fragmentStack.clearFragments(); 
			fragmentManager.popBackStack(STACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		} 
		if (((FragmentCallback) fragment).isBackStack()) {
			ft.addToBackStack(STACK_NAME);
			fragmentStack.pushFragment(fragment);
		}
		this.currentFragment = fragment;
		ft.replace(contentViewID, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
					} else { // 提示退出消息
						if (!isExit) {
							isExit = true;
							Toast.makeText(this, "再按一次退出当前应用", Toast.LENGTH_SHORT).show();
							return true;
						} 
					}
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
