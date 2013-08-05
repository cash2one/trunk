package com.shandagames.android.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * @file BitmapLruCache.java
 * @create 2013-7-23 下午01:45:40
 * @author lilong
 * @description TODO Helper Class for Volley Bitmap ImageCache, 
 *					 since it need LruCache Memory Cache Class 						
 */
public class VolleyBitmapLruCache extends LruCache<String, Bitmap> implements
		ImageCache {

	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		return cacheSize;
	}

	public VolleyBitmapLruCache() {
		this(getDefaultLruCacheSize());
	}

	public VolleyBitmapLruCache(int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}

	@Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        if (!oldValue.isRecycled()) {
            oldValue.recycle();
            oldValue = null;
        }
    }
	
	@Override
	public Bitmap getBitmap(String key) {
		return get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap bitmap) {
		put(key, bitmap);
	}
}