package com.shandagames.android.util;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;

public final class ClipboardUtils {

	public static CharSequence getText(final Context context) {
		return ((android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).getText();
	}

	public static boolean setText(final Context context, final CharSequence text) {
		if (context == null) return false;
		((android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(text);
		return true;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ClipData getData(Context context) {
		return ((android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean setData(Context context, final ClipData clipData) {
		if (context == null) return false;
		((android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clipData);
		return true;
	}
}
