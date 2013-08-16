package com.shandagames.android.support.accessor;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;

public final class ActivityAccessor {

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void onBackPressed(final Activity activity) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) return;
		activity.onBackPressed();
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void overridePendingTransition(final Activity activity, final int enter_anim, final int exit_anim) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) return;
		activity.overridePendingTransition(enter_anim, exit_anim);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static void setHomeButtonEnabled(final Activity activity, final boolean enabled) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) return;
		final ActionBar action_bar = activity.getActionBar();
		action_bar.setHomeButtonEnabled(enabled);
	}
}
