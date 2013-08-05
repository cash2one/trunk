package com.shandagames.android.support.accessor;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;

public class AnimationAccessor {

	@TargetApi(Build.VERSION_CODES.FROYO)
	public static void cancelAnimation(final View view, final Animation animation) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			if (view != null) {
				view.setAnimation(null);
			} else {
				animation.cancel();
			}
		} 
	}
	
}
