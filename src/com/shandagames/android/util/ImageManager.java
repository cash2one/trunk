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
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.widget.ImageView;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @file ImageManager.java
 * @create 2013-4-19 下午02:25:07
 * @author Jacky.Lee
 * @description TODO 封装简单的图片下载处理
 */
public class ImageManager {

	public interface ImageListener {
		public void onImageLoaded(String url, Bitmap bitmap);

		public void onImageLoadFailed(String url);
	}


	private FileCache fileCache;
	private static ImageManager instance;
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mCache;

	public static void initInstance(Context context) {
		instance = new ImageManager(context);
	}

	private ImageManager(Context context) {
		this.fileCache = new FileCache(context);
		this.mCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	}

	public static ImageManager getInstance() {
		return instance;
	}
	
	public  boolean containsImage(String url) {
		return mCache.containsKey(url);
	}

	public  boolean loadImage(String url, final ImageView imageView) {
		return loadImage(url, imageView, 0);
	}
	
	public  boolean loadImage(String url, final ImageView imageView, final int resId) {
		final ImageListener listener = new ImageListener() {
			public void onImageLoaded(String url, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}

			public void onImageLoadFailed(String url) {
				if (resId==0) return;
				imageView.setImageResource(resId);
			}
		};

		return loadImage(url, listener);
	}

	public  boolean loadImage(String url, ImageListener listener) {
		if (containsImage(url)) {
			if (listener != null) {
				SoftReference<Bitmap> mReference = mCache.get(url);
				if (mReference==null) {
					mCache.remove(url);
				} else {
					listener.onImageLoaded(url, mReference.get());
					return true;
				}
			}
		} 

		new ImageDownloadTask(listener).execute(url);
		
		return false;
	}

	public void clearCache() {
		mCache.clear();
		fileCache.clear();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void copyStream(InputStream is, OutputStream os) {
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
	
	private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
		private String mUrl;
		private ImageListener mImageListener;
		
		public ImageDownloadTask(ImageListener imageListener) {
			mImageListener = imageListener;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			this.mUrl = params[0];
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			
			File f = fileCache.getFile(mUrl);
			// from SD cache
			Bitmap b = decodeFile(f);
			if (b != null)
				return b;
			
			try {
				// from web
				Bitmap bitmap = null;
				URL imageUrl = new URL(mUrl);
				HttpURLConnection mConn = (HttpURLConnection) imageUrl.openConnection();
				mConn.setDoInput(true);
				mConn.setInstanceFollowRedirects(true);
				mConn.connect();
				InputStream is = mConn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				copyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
				return bitmap;
			} catch (MalformedURLException e) {
				Log.w("Malformed image url.", e);
				return null;
			} catch (FileNotFoundException e) {
				Log.w("", "Ignoring url because it could not be found: " + mUrl);
				return null;
			} catch (IOException e) {
				Log.w("", "Could not fetch image, could not load.", e);
				return null;
			} catch (OutOfMemoryError e) {
				Log.e("", "Could not fetch image, ran out of memory.", e);
				mCache.clear();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap == null) {
				if (mImageListener != null) {
					mImageListener.onImageLoadFailed(mUrl);
				}
			} else {
				mCache.put(mUrl, new SoftReference<Bitmap>(bitmap));
				if (mImageListener != null) {
					mImageListener.onImageLoaded(mUrl, bitmap);
				}
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			if (mImageListener != null) {
				mImageListener.onImageLoadFailed(mUrl);
			}
		}
	}
	
	public class FileCache {

		private File cacheDir;

		public FileCache(Context context) {
			// Find the dir to save cached images
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				cacheDir = new File(Environment.getExternalStorageDirectory(), "Images");
			else
				cacheDir = context.getCacheDir();
			
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}

		public File getFile(String url) {
			// I identify images by hashcode. Not a perfect solution, good for the demo.
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
}