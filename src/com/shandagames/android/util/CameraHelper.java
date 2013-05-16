/**
 * 
 */
package com.shandagames.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.hardware.Camera;

/**
 * @file CameraHelper.java
 * @create 2012-8-28 下午5:39:32
 * @author lilong
 * @description 解决Camera低版本getSupportedPreviewSizes
 * 和getSupportedPictureSizes函数问题 
 */
public class CameraHelper {

	private static final String KEY_PREVIEW_SIZE = "preview-size";
	private static final String KEY_PICTURE_SIZE = "picture-size";
	private static final String SUPPORTED_VALUES_SUFFIX = "-values";
	private Camera.Parameters mParms;

	public CameraHelper(Camera.Parameters mParms) {
		this.mParms = mParms;
	}

	public List<Size> getSupportedPreviewSizes() {
		String str = mParms.get(KEY_PREVIEW_SIZE + SUPPORTED_VALUES_SUFFIX);
		return splitSize(str);
	}

	public List<Size> getSupportedPictureSizes() {
		String str = mParms.get(KEY_PICTURE_SIZE + SUPPORTED_VALUES_SUFFIX);	
		return splitSize(str);
	}	
	
	private ArrayList<Size> splitSize(String str) {
		if (str == null)
			return null;
		StringTokenizer tokenizer = new StringTokenizer(str, ",");
		ArrayList<Size> sizeList = new ArrayList<Size>();
		while (tokenizer.hasMoreElements()) {
			Size size = strToSize(tokenizer.nextToken());
			if (size != null)
				sizeList.add(size);
		}
		if (sizeList.size() == 0)
			return null;
		return sizeList;
	}

	private Size strToSize(String str) {
		if (str == null)
			return null;
		int pos = str.indexOf('x');
		if (pos != -1) {
			String width = str.substring(0, pos);
			String height = str.substring(pos + 1);
			return new Size(Integer.parseInt(width), Integer.parseInt(height));
		}
		return null;
	}

	public class Size {

		/**
		 * Sets the dimensions for pictures.
		 * 
		 * @param w the photo width (pixels)
		 * @param h the photo height (pixels)
		 */
	    public Size(int w, int h) {
	        width = w;
	        height = h;
	    }

	    /**
	     * Compares {@code obj} to this size.
	     */
	    @Override
	    public boolean equals(Object obj) {
	        if (!(obj instanceof Size)) {
	            return false;
	        }
	        Size s = (Size) obj;
	        return width == s.width && height == s.height;
	    }

	    @Override
	    public int hashCode() {
	        return width * 32713 + height;
	    }

	    /*** width of the picture */
	    public int width;

	    /*** height of the picture */
	    public int height;
	}
	
	
}
