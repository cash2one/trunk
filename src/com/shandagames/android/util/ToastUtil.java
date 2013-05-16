package com.shandagames.android.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

/**
 * @file ToastUtil.java
 * @create 2012-8-31 上午10:11:29
 * @author lilong
 * @description 通用Toast消息封装类
 */
public final class ToastUtil {

	private static Handler handler = new Handler(Looper.getMainLooper());

	private ToastUtil() {
	}

	public static void showMessage(Context act, String msg) {
		showMessage(act, msg, Toast.LENGTH_LONG);
	}

	public static void showMessage(Context act, int msg) {
		showMessage(act, act.getString(msg), Toast.LENGTH_LONG);
	}

	public static void showMessage(Context act, View view) {
		showMessage(act, view, Toast.LENGTH_LONG);
	}

	public static void showMessage(final Context act, final String msg,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, msg, len).show();
			}
		});
	}

	public static void showMessage(final Context act, final View view,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = new Toast(act);
				toast.setView(view);
				toast.setDuration(len);
				// 设置文本显示位置,默认Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM
				// toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0,  0);
				// 使用setMargin对位置大幅度调整,参数为：横向和纵向的百分比
				// toast.setMargin(0f, 0.5f);//屏幕纵向正中间的上方显示
				toast.show();
			}
		});
	}
}
