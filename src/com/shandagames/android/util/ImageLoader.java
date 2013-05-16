/**
 * https://github.com/thest1/LazyList
 */
package com.shandagames.android.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.shandagames.android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * @file ImageLoader.java
 * @create 2012-8-31 上午10:11:29
 * @author lilong
 * @description Android Loading Image from URL
 */
public class ImageLoader {

	private MemoryCache memoryCache;
	private FileCache fileCache;
	private Map<ImageView, String> imageViews;
	private ExecutorService executorService;
	private int stub_id;

	public ImageLoader(Context context) {
		memoryCache = new MemoryCache();
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
		stub_id = R.drawable.bg_default_pic;
	}

	public void displayImage(String url, int loader, ImageView imageView) {
		stub_id = loader;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(loader);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			copyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError) 
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1=new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2=new FileInputStream(f);
			Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Task for the queue

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Activity a = (Activity) photoToLoad.imageView.getContext();
				a.runOnUiThread(bd);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public class FileCache {

		private File cacheDir;

		public FileCache(Context context) {
			// Find the dir to save cached images
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED))
				cacheDir = new File(
						android.os.Environment.getExternalStorageDirectory(),
						"TempImages");
			else
				cacheDir = context.getCacheDir();
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}

		public File getFile(String url) {
			// I identify images by hashcode. Not a perfect solution, good for
			// the demo.
			String filename = String.valueOf(url.hashCode());
			// Another possible solution (thanks to grantland)
			// String filename = URLEncoder.encode(url);
			File f = new File(cacheDir, filename);
			return f;

		}

		public void clear() {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return;
			for (File f : files)
				f.delete();
		}

	}

	public class MemoryCache {
		private static final String TAG = "MemoryCache";
		private Map<String, SoftReference<Bitmap>> cache = Collections.synchronizedMap(
				new LinkedHashMap<String, SoftReference<Bitmap>>(10, 1.5f, true)); // Last argument true for LRU ordering
		private long size = 0;// current allocated size
		private long limit = 1000000;// max memory in bytes

		public MemoryCache() {
			// use 25% of available heap size
			setLimit(Runtime.getRuntime().maxMemory() / 4);
		}

		public void setLimit(long new_limit) {
			limit = new_limit;
			Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024.+ "MB");
		}

		public Bitmap get(String id) {
			try {
				if (!cache.containsKey(id))
					return null;
				// NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78
				SoftReference<Bitmap> ref = cache.get(id);
				return ref.get();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public void put(String id, Bitmap bitmap) {
			try {
				if (cache.containsKey(id))
					size -= getSizeInBytes(cache.get(id).get());
				cache.put(id, new SoftReference<Bitmap>(bitmap));
				size += getSizeInBytes(bitmap);
				checkSize();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}

		private void checkSize() {
			Log.i(TAG, "cache size=" + size + " length=" + cache.size());
			if (size > limit) {
				// least recently accessed item will be the first one iterated
				Iterator<Entry<String, SoftReference<Bitmap>>> iter = cache.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, SoftReference<Bitmap>> entry = iter.next();
					size -= getSizeInBytes(entry.getValue().get());
					iter.remove();
					if (size <= limit)
						break;
				}
				Log.i(TAG, "Clean cache. New size " + cache.size());
			}
		}

		public void clear() {
			try {
				// NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78
				cache.clear();
				size = 0;
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
		}

		private long getSizeInBytes(Bitmap bitmap) {
			if (bitmap == null)
				return 0;
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
}
