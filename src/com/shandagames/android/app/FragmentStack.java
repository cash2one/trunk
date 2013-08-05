package com.shandagames.android.app;

import java.util.Stack;
import android.support.v4.app.Fragment;

public class FragmentStack {

	// 定义一个私有的静态全局变量来保存该类的唯一实例
	private static FragmentStack instance;
	
	// 自定义同步栈记录当前栈
	private Stack<Fragment> fragmentStack = new Stack<Fragment>();

	/**
	 * 构造函数必须是私有的 
	 * 这样在外部便无法使用 new 来创建该类的实例 
	 */
	private FragmentStack() {
	}
	
	public synchronized static FragmentStack getInstance() {
		if (instance == null) {
			//这里可以保证只实例化一次 
	        //即在第一次调用时实例化 
	        //以后调用便不会再实例化 
			instance = new FragmentStack();
		}
		return instance;
	}
	
	/**
	 * 将当前Fragment推入栈中
	 * 
	 * @param fragment 
	 */
	public void pushFragment(Fragment fragment) {
		if (fragment != null) {
			fragmentStack.add(fragment);
		}
	}
	
	/**
	 * 获取当前栈顶的Fragment并移除它
	 */
	public Fragment popFragment() {
		if (!fragmentStack.isEmpty()) {
			return fragmentStack.pop();
		}
		return null;
	}
	
	/**
	 * 获取当前栈顶的Fragment 
	 */
	public Fragment peekFragment() {
		if (!fragmentStack.isEmpty()) {
			return fragmentStack.peek();
		}
		return null;
	}
	
	/**
	 * 清空栈中存储的Fragment
	 */
	public void clearFragments() {
		fragmentStack.clear();
	}
	
	/**
	 * 栈中存储Fragment的数目 
	 */
	public int getStackSize() {
		return fragmentStack.size();
	}
}
