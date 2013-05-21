package com.shandagames.android.cache.core;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Jacky Lee (dreamxsky@gmail.com)
 */
public class RemoteResourceManager {
    private static final String TAG = "RemoteResourceManager";
    private static final boolean DEBUG = false;

    private DiskCache mDiskCache;
    private RemoteResourceFetcher mRemoteResourceFetcher;

    public RemoteResourceManager(String dirPath, String cacheName) {
        this(new BaseDiskCache(dirPath, cacheName));
    }

    public RemoteResourceManager(DiskCache cache) {
        mDiskCache = cache;
        mRemoteResourceFetcher = new RemoteResourceFetcher(mDiskCache);
    }

    public void addObserver(Observer observer) {
    	mRemoteResourceFetcher.addObserver(observer);
    }
    
    public void deleteObserver(Observer observer) {
    	mRemoteResourceFetcher.deleteObserver(observer);
    }
    
    public boolean exists(String uri) {
        return mDiskCache.exists(Uri.encode(uri));
    }

    /**
     * If IOException is thrown, we don't have the resource available.
     */
    public File getFile(String uri) {
        return mDiskCache.getFile(Uri.encode(uri));
    }

    /**
     * If IOException is thrown, we don't have the resource available.
     */
    public InputStream getInputStream(String uri) {
    	try {
	        return mDiskCache.getInputStream(Uri.encode(uri));
    	} catch (IOException ex) {
    		ex.printStackTrace();
    		return null;
    	}
    }

    /**
     * Request a resource be downloaded. Useful to call after a IOException from getInputStream.
     */
    public void request(String uri) {
        if (DEBUG) Log.d(TAG, "request(): " + uri);
        mRemoteResourceFetcher.fetch(uri, Uri.encode(uri));
    }
    
    /**
     * Explicitly expire an individual item.
     */
    public void invalidate(String uri) {
        mDiskCache.invalidate(Uri.encode(uri));
    }

    public void shutdown() {
        mRemoteResourceFetcher.shutdown();
        mDiskCache.cleanup();
    }

    public void clear() {
        mRemoteResourceFetcher.shutdown();
        mDiskCache.clear();
    }

    public static abstract class ResourceRequestObserver implements Observer {

        private String mRequestUri;

        abstract public void requestReceived(Observable observable, String uri);

        public ResourceRequestObserver(String requestUri) {
            mRequestUri = requestUri;
        }

        @Override
        public void update(Observable observable, Object data) {
            if (DEBUG) Log.d(TAG, "Recieved update: " + data);
            String dataUri = data.toString();
            if (dataUri.equals(mRequestUri)) {
                requestReceived(observable, dataUri);
            }
        }
    }

    public static class RemoteResourceManagerObserver implements Observer {

    	private Handler mHandler;
    	private Runnable mRunnable;
    	
    	public RemoteResourceManagerObserver(Runnable mUpdatePhotos) {
    		mHandler = new Handler(Looper.getMainLooper());
    		mRunnable = mUpdatePhotos;
    	}
    	
		@Override
		public void update(Observable observable, Object data) {
			if (DEBUG) Log.d(TAG, "Recieved update: " + data);
			mHandler.post(mRunnable);
		}
    	
		public void removeCallbacks() {
			mHandler.removeCallbacks(mRunnable);
		}
    }
}
