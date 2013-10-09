package com.shandagames.android.volley.images;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.shandagames.android.cache.lib.DiskLruCache;
import com.shandagames.android.constant.Config;

/**
 * Implementation of DiskLruCache by Jake Wharton
 * modified from http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method
 */
public class DiskLruImageCache implements ImageCache  {

	private static final boolean DEBUG = Config.DEBUG;
	private static final String TAG = "DiskLruImageCache";
	
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8*1024;
    private static final int DEFAULT_COMPRESS_QUALITY = 75;
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;

    private DiskLruCache mDiskLruCache;
    
    public DiskLruImageCache(Context context,String uniqueName) {
        try {
            final File diskCacheDir = getDiskCacheDir(context, uniqueName );
            if (diskCacheDir != null) {
            	if (!diskCacheDir.exists()) {
            		diskCacheDir.mkdirs();
            	}
            	if (getUsableSpace(diskCacheDir) > DEFAULT_DISK_CACHE_SIZE) {
            		mDiskLruCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, DEFAULT_DISK_CACHE_SIZE );
            		if (DEBUG) Log.d(TAG, "Disk cache initialized");
            	}
            }
        } catch (IOException e) {
        	Log.e(TAG, "initDiskCache - " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
    	if (mDiskLruCache != null) {
    		try {
    			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
    			if (snapshot == null) {
    				final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
    				if (writeBitmapToFile(bitmap, editor)) {
    					mDiskLruCache.flush();
    					editor.commit();
    					if (DEBUG) Log.d(TAG, "image put on disk cache" + key);
    				} else {
    					editor.abort();
    					if (DEBUG) Log.d(TAG, "ERROR on: image put on disk cache " + key);
    				}
    			} else {
    				snapshot.getInputStream(DISK_CACHE_INDEX).close();
    			}
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	}
    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
    		throws IOException, FileNotFoundException {
	    OutputStream out = null;
	    try {
	        out = new BufferedOutputStream(editor.newOutputStream(DISK_CACHE_INDEX), IO_BUFFER_SIZE);
	        return bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, out);
	    } finally {
	        if (out != null ) {
	            out.close();
	        }
	    }
	}
    
    @Override
    public Bitmap getBitmap(String key) {
    	if (mDiskLruCache != null) {
    		InputStream inputStream = null;
    	    DiskLruCache.Snapshot snapshot = null;
	        try {
	            snapshot = mDiskLruCache.get(key);
	            if (snapshot != null) {
	            	inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
	            	if (inputStream != null) {
	            		return BitmapFactory.decodeStream(new BufferedInputStream(inputStream, IO_BUFFER_SIZE));
	            	}
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	        	try {
		        	if (inputStream != null) {
		        		inputStream.close();
		        	}
		        	if (snapshot != null) {
		                snapshot.close();
		            }
	        	} catch (IOException e) {}
	        }
    	}
        return null;
    }

    public File getFile(String key) {
    	if (mDiskLruCache != null) {
    		return new File(mDiskLruCache.getDirectory(), key);
    	}
    	return null;
    }
    
    public boolean exists(String key) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(key);
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null ) {
                snapshot.close();
            }
        }
        return contained;
    }

    public void clearCache() {
        try {
        	mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
        	final String cachePath = getExternalCacheDir(context).getPath();
        	return new File(cachePath + File.separator + uniqueName);
        }
        return null;
    }
    
    @TargetApi(Build.VERSION_CODES.FROYO)
    private File getExternalCacheDir(Context context) {
    	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalCacheDir();
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
    
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
    	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
