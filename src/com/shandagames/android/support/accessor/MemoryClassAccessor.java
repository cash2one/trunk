package com.shandagames.android.support.accessor;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;

public class MemoryClassAccessor {
	
	/**
	 * Get the memory class of this device (approx. per-app memory limit)
	 * 
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static int getMemoryClass(final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
			return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		return (int) (Debug.getNativeHeapSize() / 1024 / 1024);
	}
}
