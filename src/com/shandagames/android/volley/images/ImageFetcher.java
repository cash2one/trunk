package com.shandagames.android.volley.images;

import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageFetcher implements ImageCache {
	
	private static final String TAG = "ImageFetcher";

    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	
	private DiskLruImageCache mDiskLruCache;
	private BitmapLruImageCache mMemoryCache;
	
	public ImageFetcher(Context context, String uniqueName) {
		this(context, uniqueName, 
				DEFAULT_MEM_CACHE_ENABLED, 
				DEFAULT_DISK_CACHE_ENABLED);
	}
	
	public ImageFetcher(Context context, String uniqueName, 
			boolean memoryCacheEnabled, boolean diskCacheEnabled) {
		if (memoryCacheEnabled) {
			mMemoryCache = new BitmapLruImageCache();
		}
		
		if (diskCacheEnabled) {
			mDiskLruCache = new DiskLruImageCache(context, uniqueName);
		}
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		if (url == null) {
			return null;
		}
		
		Bitmap bitmap = null;
		String key = createKey(url);
		if (mMemoryCache != null) {
			bitmap = mMemoryCache.getBitmap(key);
		}
		
		if (mDiskLruCache != null && bitmap == null) {
			bitmap = mDiskLruCache.getBitmap(key);
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if (url == null || bitmap == null) {
			return;
		}
		
		String key = createKey(url);
		if (mMemoryCache != null) {
			mMemoryCache.putBitmap(key, bitmap);
		}
		
		if (mDiskLruCache != null) {
			mDiskLruCache.putBitmap(key, bitmap);
		}
	}

	/**
	 * Creates a unique cache key based on a url value
	 * @param url
	 * 		url to be used in key creation
	 * @return
	 * 		cache key value
	 */
	private String createKey(String url){
		return String.valueOf(url.hashCode());
	}
}
