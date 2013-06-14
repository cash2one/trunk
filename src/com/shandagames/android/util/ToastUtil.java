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

	private static Toast toast;
	
	private static Handler handler;

	static {
		handler = new Handler(Looper.getMainLooper());
	}

	public static void showMessage(Context ctx, String msg) {
		showMessage(ctx, msg, Toast.LENGTH_SHORT);
	}

	public static void showMessage(Context ctx, int msg) {
		showMessage(ctx, ctx.getString(msg), Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context ctx, final String text,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(ctx, text, len);
				} else {
					toast.setText(text);
					toast.setDuration(len);
				}
				toast.show();
			}
		});
	}

	public static void showMessage(final Context ctx, final View view,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = new Toast(ctx);
				} else {
					toast.setDuration(len);
					toast.setView(view);
				}
				// 设置文本显示位置,默认Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM
				// toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0,  0);
				// 使用setMargin对位置大幅度调整,参数为：横向和纵向的百分比
				// toast.setMargin(0f, 0.5f);//屏幕纵向正中间的上方显示
				toast.show();
			}
		});
	}
}
