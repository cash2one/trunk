package com.shandagames.android.support;

import java.io.IOException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;


@TargetApi(5)
public class BackwardSupport {

	/**
	 * 读取Exif方向参数的
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("BackwardSupportUtil", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}

			}
		}
		return degree;
	}
}
