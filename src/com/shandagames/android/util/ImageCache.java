package com.shandagames.android.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.shandagames.android.log.LogUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

/**
 * @file ImageCache.java
 * @create 2012-8-17 下午12:44:54
 * @author lilong
 * @description The ImageCache class can be used to download images and store
 *              them in the cache directory of the device. Multiple requests to
 *              the same URL will result in a single download, until the cache
 *              timeout has passed.
 */
public class ImageCache {

	/**
	 * The ImageCallback interface defines a single method used to pass an image
	 * back to the calling object when it has been loaded.
	 */
	public static interface ImageCallback {
		/**
		 * The onImageLoaded method is called by the ImageCache when an image has been loaded.
		 * 
		 * @param image The requested image in the form of a Drawable object.
		 * @param url The originally requested URL
		 */
		void onImageLoaded(Drawable image, String url);
	}

	private static final long DAY_TIME = 24 * 60 * 60 * 1000;
	private static final long CACHE_TIMEOUT = 5 * DAY_TIME;
	private static final String CACHE_DIR = "images";
	private final Object _lock = new Object();
	private HashMap<String, SoftReference<Drawable>> _cache;
	private HashMap<String, List<ImageCallback>> _callbacks;
	private static ImageCache _instance = null;

	/**
	 * Gets the singleton instance of the ImageCache.
	 * 
	 * @return The ImageCache.
	 */
	public synchronized static ImageCache getInstance() {
		if (_instance == null) {
			_instance = new ImageCache();
		}
		return _instance;
	}

	private ImageCache() {
		_cache = new HashMap<String, SoftReference<Drawable>>();
		_callbacks = new HashMap<String, List<ImageCallback>>();
	}

	private String getHash(String url) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(url.getBytes());
			return new BigInteger(digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			// this should never happen, but just to make sure return the url
			return url;
		}
	}

	private Drawable drawableFromCache(String url, String hash) {
		Drawable d = null;
		synchronized (_lock) {
			if (_cache.containsKey(hash)) {
				SoftReference<Drawable> ref = _cache.get(hash);
				if (ref != null) {
					d = ref.get();
					if (d == null) {
						_cache.remove(hash);
					}
				}
			}
		}
		return d;
	}

	private File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
	
	public static String createFilePath(File cacheDir, String fileName) {
		try {
			return cacheDir.getAbsolutePath() + File.separator
					+ URLEncoder.encode(fileName.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.w("createFilePath", "Couldn't retrieve ApplicationFilePath for : " + e);
		}
		return null;
	}
	
	public File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				|| !Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) ? 
				getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
		return new File(cachePath + File.separator + uniqueName);
	}
	
	private Drawable loadSync(String url, String hash, Context context) {
		Drawable d = null;
		try {
			d = drawableFromCache(url, hash);

			File file = new File(getDiskCacheDir(context, CACHE_DIR), hash);
			boolean timeout = file.lastModified() + CACHE_TIMEOUT < new Date().getTime();
			if (d == null || timeout) {
				if (timeout) {
					file.delete();
				}
				if (!file.exists()) {
					InputStream is = new URL(url + "?rand="
							+ new Random().nextInt()).openConnection()
							.getInputStream();
					if (file.createNewFile()) {
						FileOutputStream fos = new FileOutputStream(file);
						BufferedOutputStream bos = new BufferedOutputStream(fos, 8 * 1024);
						byte[] buffer = new byte[4 * 1024];
						int size;
						while ((size = is.read(buffer)) > 0) {
							bos.write(buffer, 0, size);
						}
						bos.flush();
						bos.close();
					}
				}
				d = Drawable.createFromPath(file.getAbsolutePath());

				synchronized (_lock) {
					_cache.put(hash, new SoftReference<Drawable>(d));
				}
			}
		} catch (Exception ex) {
			LogUtils.d(getClass().getName(), ex.getMessage());
		}
		return d;
	}

	/**
	 * Loads an image from the passed URL and calls the callback method when the
	 * image is done loading.
	 * 
	 * @param url The URL of the target image.
	 * @param callback A ImageCallback object to pass the loaded image. If null, the
	 *            	   image will only be pre-loaded into the cache.
	 * @param context  The context of the new Drawable image.
	 */
	public void loadAsync(final Context context, final String url, final ImageCallback callback) {
		final String hash = getHash(url);

		synchronized (_lock) {
			List<ImageCallback> callbacks = _callbacks.get(hash);
			if (callbacks != null) {
				if (callback != null)
					callbacks.add(callback);
				return;
			}

			callbacks = new ArrayList<ImageCallback>();
			if (callback != null)
				callbacks.add(callback);
			_callbacks.put(hash, callbacks);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Drawable d = loadSync(url, hash, context);
				List<ImageCallback> callbacks;

				synchronized (_lock) {
					callbacks = _callbacks.remove(hash);
				}

				for (ImageCallback item : callbacks) {
					item.onImageLoaded(d, url);
				}
			}
		}, "ImageCache loader: " + url).start();
	}
}
