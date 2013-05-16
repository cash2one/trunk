package com.shandagames.android.base;

import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import com.shandagames.android.cache.core.NullDiskCache;
import com.shandagames.android.cache.core.RemoteResourceManager;
import com.shandagames.android.cache.lib.ImageFetcher;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.constant.PreferenceSettings;
import com.shandagames.android.crash.CrashHandler;
import com.shandagames.android.http.BetterHttpApiV1;
import com.shandagames.android.location.BestLocationListener;
import com.shandagames.android.location.LocationException;
import com.shandagames.android.log.Log;
import com.shandagames.android.log.LoggingHandler;
import com.shandagames.android.receiver.LoggedInOutBroadcastReceiver;
import com.shandagames.android.receiver.MediaCardStateBroadcastReceiver;
import com.shandagames.android.support.ManifestSupport;
import com.shandagames.android.util.SmileyParser;

/**
 * 
 * @file AndroidApplication.java
 * @create 2012-8-15 下午5:08:28
 * @author lilong
 * @description TODO
 */
public class AndroidApplication extends Application implements
		MediaCardStateBroadcastReceiver.OnMediaCardAvailableListener,
		LoggedInOutBroadcastReceiver.OnLoggedInOutStateListener {

	private static final boolean DEBUG = Constants.DEVELOPER_MODE;
	
	public static int mVersion;
	public static String mPackageName;
	public static SharedPreferences mPrefs;

	private boolean isReady = false;
	private static AndroidApplication instance;
	private RemoteResourceManager mRemoteResourceManager;
	private BestLocationListener mBestLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		isReady = true;
		Log.setLevel(DEBUG);
		
		mPackageName = getPackageName();
		mVersion = ManifestSupport.getApplicationForVersionCode(this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mBestLocationListener = new BestLocationListener();
        
		// 收集异常日志
		if (!DEBUG) {
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(this);
		}
		
		// FIXME: StrictMode类在1.6以下的版本中没有，会导致类加载失败。因此将这些代码设成关闭状态，仅在做性能调试时才打开。
		// NOTE: StrictMode模式需要2.3+ API支持。
		/*if (DEBUG) { 
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build()); 
		}*/

		// Set up storage cache.
		loadResourceManagers();
		// Set up emoticons
		SmileyParser.init(getApplicationContext());
		 // Set up http request operation
        BetterHttpApiV1.init(getApplicationContext(), null, false);
		// Catch sdcard state changes
		new MediaCardStateBroadcastReceiver().bind(this);
		 // Catch logins or logouts.
        new LoggedInOutBroadcastReceiver().bind(this);
	}

	@Override
	public void onTerminate() {
		// FIXME: 根据android文档，onTerminate不会在真实机器上被执行到
		// 因此这些清理动作需要再找合适的地方放置，以确保执行。
		instance = null;
		super.onTerminate();
	}

	public void buildPrintTrace() {
		/** initiaize java logging */
		Logger.getLogger(mPackageName).addHandler(new LoggingHandler());
        Logger.getLogger(mPackageName).setLevel(Level.ALL);
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public static AndroidApplication getInstance() {
		return instance;
	}
	
	public RemoteResourceManager getRemoteResourceManager() {
        return mRemoteResourceManager;
    }
	
	public ImageFetcher getImageFetcher(final FragmentActivity activity) {
        // The ImageFetcher takes care of loading remote images into our ImageView
        ImageFetcher fetcher = new ImageFetcher(activity);
        fetcher.addImageCache(activity);
        return fetcher;
    }
	
	private void loadResourceManagers() {
        // We probably don't have SD card access if we get an
        // IllegalStateException. If it did, lets
        // at least have some sort of disk cache so that things don't npe when
        // trying to access the
        // resource managers.
        try {
            Log.d("Attempting to load RemoteResourceManager(cache)");
            mRemoteResourceManager = new RemoteResourceManager("project", "cache/images");
        } catch (IllegalStateException e) {
            Log.d("Falling back to NullDiskCache for RemoteResourceManager");
            mRemoteResourceManager = new RemoteResourceManager(new NullDiskCache());
        }
    }
	
	public void requestLocationUpdates(boolean gps) {
		mBestLocationListener.register((LocationManager) getSystemService(Context.LOCATION_SERVICE), gps);
	}

	public void requestLocationUpdates(Observer observer) {
		mBestLocationListener.addObserver(observer);
		mBestLocationListener.register((LocationManager) getSystemService(Context.LOCATION_SERVICE), true);
	}

	public void updateLocation() {
		mBestLocationListener.updateLastKnownLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
	}
	
	public void updateLocation(Location location) {
		mBestLocationListener.updateLocation(location);
	}
	
	public void removeLocationUpdates() {
		mBestLocationListener.unregister((LocationManager) getSystemService(Context.LOCATION_SERVICE));
	}

	public void removeLocationUpdates(Observer observer) {
		mBestLocationListener.deleteObserver(observer);
		this.removeLocationUpdates();
	}

	public Location getLastKnownLocation() {
		return mBestLocationListener.getLastKnownLocation();
	}

	public Location getLastKnownLocationOrThrow() throws LocationException {
		Location location = mBestLocationListener.getLastKnownLocation();
		if (location == null) {
			throw new LocationException();
		}
		return location;
	}

	public void clearLastKnownLocation() {
		mBestLocationListener.clearLastKnownLocation();
	}

	
	private void loadCredentials() {
		// Try logging in and setting up server oauth, then user credentials.
        String userName = mPrefs.getString(PreferenceSettings.PREFERENCE_USERNAME, null);
        String password = mPrefs.getString(PreferenceSettings.PREFERENCE_PASSWORD, null);
        BetterHttpApiV1.getInstance().setCredentials(userName, password);
        if (BetterHttpApiV1.getInstance().hasCredentials()) {
        	 sendBroadcast(new Intent(LoggedInOutBroadcastReceiver.INTENT_ACTION_LOGGED_IN));
        } else {
        	 sendBroadcast(new Intent(LoggedInOutBroadcastReceiver.INTENT_ACTION_LOGGED_OUT));
        }
	}
	
	
	@Override
	public void onMediaCardAvailable() {
		// TODO Auto-generated method stub
		Log.d("Media state changed, reloading resource managers");
		loadResourceManagers();
	}

	@Override
	public void onMediaCardUnavailable() {
		// TODO Auto-generated method stub
		getRemoteResourceManager().shutdown();
        loadResourceManagers();
	}

	@Override
	public void onLoggedIn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoggedOut() {
		// TODO Auto-generated method stub
	}

}
