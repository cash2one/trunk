package com.shandagames.android.support.accessor;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.InputDevice;
import android.view.MotionEvent;

public class MotionEventAccessor {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static float getAxisValue(final MotionEvent event, final int axis) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return event.getAxisValue(axis);
		return 0;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static int getSource(final MotionEvent event) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return event.getSource();
		return InputDevice.SOURCE_TOUCHSCREEN;
	}
}
