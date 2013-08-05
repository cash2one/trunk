package com.shandagames.android.support.accessor;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

public class ViewAccessor {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void setLayerType(final View view, final int layerType, final Paint paint) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) return;
		view.setLayerType(layerType, paint);
	}
}
