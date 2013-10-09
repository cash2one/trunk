package com.shandagames.android.volley.images;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Basic LRU Memory cache.
 * 
 * @author Trey Robinson
 *
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache{
	
	private final String TAG = this.getClass().getSimpleName();
	
	private static int getDefaultLruCacheSize() {
		// Get max available VM memory, exceeding this amount will throw an
	    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
	    // int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		return cacheSize;
	}
	
	public BitmapLruImageCache() {
		this(getDefaultLruCacheSize());
	}
	
	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		// The cache size will be measured in kilobytes rather than number of items.
		return value.getRowBytes() * value.getHeight() / 1024;
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		Log.d(TAG, "Retrieved item from Mem Cache");
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		Log.d(TAG, "Added item to Mem Cache");
		if (get(url) == null) {
			put(url, bitmap);
		}
	}
	
	public void clearCache() {
		Log.d(TAG, "Memory cache cleared");
		evictAll();
	}
	
	@Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        if (!oldValue.isRecycled()) {
            oldValue.recycle();
            oldValue = null;
        }
    }
}
