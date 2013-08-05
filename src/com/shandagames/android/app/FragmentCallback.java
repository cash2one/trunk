package com.shandagames.android.app;

public interface FragmentCallback {

	/**
	 * 是否添加到回退栈 
	 * 
	 * @return true表示添加，false不添加
	 */
	boolean isBackStack();
	
	/**
	 * 是否清空回退栈
	 * 
	 * @return true表示清空栈，false则不清栈
	 */
	boolean isCleanStack();
	
	/**
	 * 返回键处理方法
	 * 
	 * @return true表示对返回键处理，false则不处理
	 */
	boolean onBackPressProcess();
	
}
