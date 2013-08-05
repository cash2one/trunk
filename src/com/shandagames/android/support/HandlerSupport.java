package com.shandagames.android.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/**
 * @file HandlerSupport.java
 * @create 2013-4-9 下午02:23:25
 * @author Jacky.Lee
 * @description TODO 封装常用的Handler消息处理方式
 */
public final class HandlerSupport {

	// 默认主线程中创建Handler
	private static Handler mHandler = new Handler(Looper.getMainLooper());

	/** 工厂方法创建实例对象 */
	public static HandlerSupport newFactory(Handler handler) {
		return new HandlerSupport(handler);
	}

	private HandlerSupport(Handler handler) {
		mHandler = handler;
	}

	public static void post(Runnable mRunnable) {
		mHandler.post(mRunnable);
	}

	public static void postDelayed(Runnable mRunnable, long delayMillis) {
		mHandler.postDelayed(mRunnable, delayMillis);
	}

	public static void postAtTime(Runnable mRunnable, long uptimeMillis) {
		mHandler.postAtTime(mRunnable, uptimeMillis);
	}

	public static void removeCallbacks(Runnable mRunnable) {
		mHandler.removeCallbacks(mRunnable);
	}

	
	// #############################################################################################################################
	

	public void sendMessage(int what) {
		mHandler.sendMessage(mHandler.obtainMessage(what));
	}

	public void sendMessage(int what, int arg1, int arg2) {
		mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
	}

	public void sendMessageDelayed(int what, long delayMillis) {
		mHandler.sendMessageDelayed(mHandler.obtainMessage(what), delayMillis);
	}

	public void sendMessageDelayed(int what, int arg1, int arg2,
			long delayMillis) {
		mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2),
				delayMillis);
	}

	public void sendMessage(int what, Object obj) {
		mHandler.sendMessage(mHandler.obtainMessage(what, obj));
	}

	public void sendMessage(int what, Bundle bundle) {
		mHandler.sendMessage(mHandler.obtainMessage(what, bundle));
	}

	public void removeMessages(int what) {
		mHandler.removeMessages(what);
	}

}
