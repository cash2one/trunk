package com.shandagames.android.cache.core;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.BaseAdapter;
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
    private static final boolean DEBUG = true;

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
    
    public boolean exists(Uri uri) {
        return mDiskCache.exists(Uri.encode(uri.toString()));
    }

    /**
     * If IOException is thrown, we don't have the resource available.
     */
    public File getFile(Uri uri) {
        if (DEBUG) Log.d(TAG, "getInputStream(): " + uri);
        return mDiskCache.getFile(Uri.encode(uri.toString()));
    }

    /**
     * If IOException is thrown, we don't have the resource available.
     */
    public InputStream getInputStream(Uri uri) throws IOException {
        if (DEBUG) Log.d(TAG, "getInputStream(): " + uri);
        return mDiskCache.getInputStream(Uri.encode(uri.toString()));
    }

    /**
     * Request a resource be downloaded. Useful to call after a IOException from getInputStream.
     */
    public void request(Uri uri) {
        if (DEBUG) Log.d(TAG, "request(): " + uri);
        mRemoteResourceFetcher.fetch(uri, Uri.encode(uri.toString()));
    }
    
    /**
     * Explicitly expire an individual item.
     */
    public void invalidate(Uri uri) {
        mDiskCache.invalidate(Uri.encode(uri.toString()));
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

        private Uri mRequestUri;

        abstract public void requestReceived(Observable observable, Uri uri);

        public ResourceRequestObserver(Uri requestUri) {
            mRequestUri = requestUri;
        }

        @Override
        public void update(Observable observable, Object data) {
            if (DEBUG) Log.d(TAG, "Recieved update: " + data);
            Uri dataUri = (Uri)data;
            if (dataUri == mRequestUri) {
                if (DEBUG) Log.d(TAG, "requestReceived: " + dataUri);
                requestReceived(observable, dataUri);
            }
        }
    }

    public static class RemoteResourceManagerObserver implements Observer {

    	private Handler mHandler;
    	private BaseAdapter mListAdapter;
    	
    	public RemoteResourceManagerObserver(BaseAdapter mAdapter) {
    		mListAdapter = mAdapter;
    		mHandler = new Handler(Looper.getMainLooper());
    	}
    	
		@Override
		public void update(Observable observable, Object data) {
			if (DEBUG) Log.d(TAG, "Recieved update: " + data);
			mHandler.post(mUpdatePhotos);
		}
    	
		public void removeCallbacks() {
			mHandler.removeCallbacks(mUpdatePhotos);
		}
		
		private Runnable mUpdatePhotos = new Runnable() {
	        @Override
	        public void run() {
	        	mListAdapter.notifyDataSetChanged();
	        }
	    };
    }
}
