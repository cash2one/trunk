/**
 * 
 */
package com.shandagames.android.cache.lib;

import java.lang.reflect.Field;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @file BitmapDecoder.java
 * @create 2012-11-12 下午1:58:29
 * @author lilong
 * @description TODO
 */
public class ImageDecoder {

	private static final int DEFAULT_MAX_IMAGE_HEIGHT = 0;
	private static final int DEFAULT_MAX_IMAGE_WIDTH = 0;
	
	private ImageSize imageSize;
	
	public ImageDecoder() {
		setImageSize(new ImageSize(DEFAULT_MAX_IMAGE_HEIGHT, DEFAULT_MAX_IMAGE_WIDTH));
	}
	
	public void setImageSize(ImageSize targetSize) {
		this.imageSize = targetSize;
	}
	
	/**
	 * Defines image size for loading at memory (for memory economy) by {@link ImageView} parameters.<br />
	 * Size computing algorithm:<br />
	 * 1) Get <b>layout_width</b> and <b>layout_height</b>. If both of them haven't exact value then go to step #2.</br>
	 * 2) Get <b>maxWidth</b> and <b>maxHeight</b>. If both of them are not set then go to step #3.<br />
	 * 3) Get device screen dimensions.
	 */
	public ImageSize getImageSizeScaleTo(ImageView imageView) {
		LayoutParams params = imageView.getLayoutParams();
		Context context = imageView.getContext();
		DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
		
		int width = params.width; // Get layout width parameter
		if (width <= 0) width = getFieldValue(imageView, "mMaxWidth"); // Check maxWidth parameter
		if (width <= 0) width = imageSize.width;
		if (width <= 0) width = outMetrics.widthPixels;

		int height = params.height; // Get layout height parameter
		if (height <= 0) height = getFieldValue(imageView, "mMaxHeight"); // Check maxHeight parameter
		if (height <= 0) height = imageSize.height;
		if (height <= 0) height = outMetrics.heightPixels;
		
		// Consider device screen orientation
		int screenOrientation = imageView.getContext().getResources().getConfiguration().orientation;
		if ((screenOrientation == Configuration.ORIENTATION_PORTRAIT && width > height)
				|| (screenOrientation == Configuration.ORIENTATION_LANDSCAPE && width < height)) {
			int tmp = width;
			width = height;
			height = tmp;
		}

		return new ImageSize(width, height);
	}
	
	private int getFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}

	public static class ImageSize {
		private static final String TO_STRING_PATTERN = "%sx%s";
	
		private final int width;
		private final int height;
	
		public ImageSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	
		public int getWidth() {
			return width;
		}
	
		public int getHeight() {
			return height;
		}
	
		@Override
		public String toString() {
			return String.format(TO_STRING_PATTERN, width, height);
		}
	}
}
