package com.shandagames.android.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import com.shandagames.android.bean.User;
import com.shandagames.android.cache.core.NullDiskCache;
import com.shandagames.android.cache.core.RemoteResourceManager;
import com.shandagames.android.cache.lib.ImageFetcher;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.constant.PreferenceSettings;
import com.shandagames.android.http.BetterHttpApiV1;
import com.shandagames.android.location.BestLocationListener;
import com.shandagames.android.location.LocationException;
import com.shandagames.android.log.Log;
import com.shandagames.android.log.LoggingHandler;
import com.shandagames.android.receiver.LoggedInOutBroadcastReceiver;
import com.shandagames.android.receiver.MediaCardStateBroadcastReceiver;
import com.shandagames.android.util.ImageCache;
import com.shandagames.android.util.SmileyParser;

/**
 * 
 * @file AndroidApplication.java
 * @create 2012-8-15 下午5:08:28
 * @author lilong
 * @description TODO Application基类，存储全局数据
 */
public class AndroidApplication extends Application implements
		MediaCardStateBroadcastReceiver.OnMediaCardAvailableListener,
		LoggedInOutBroadcastReceiver.OnLoggedInOutStateListener {

	private static final String TAG = "AndroidApplication";
	private static final boolean DEBUG = Constants.DEVELOPER_MODE;
	
	public static int mVersion;
	public static String mToken;
	public static SharedPreferences mPrefs;
	public static SharedPreferences mUserPrefs;
	private static AndroidApplication instance;
	
	private User self;
	private boolean ready = true;
	private TaskHandler mTaskHandler;
	private HandlerThread mTaskThread;
	
	private BestLocationListener mBestLocationListener;
	private RemoteResourceManager mRemoteResourceManager;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Log.setLevel(DEBUG);

        inital();
        register();
        sendCrashReports();
        
        // 应用需要后台执行数据处理，开启新的线程处理
        mTaskThread = new HandlerThread(TAG + "-AsyncThread");
        mTaskThread.start();
        mTaskHandler = new TaskHandler(mTaskThread.getLooper());
        
        mBestLocationListener = new BestLocationListener();
	}

	private void inital() {
		ImageCache.initInstance("project");
		loadResourceManagers();
		mVersion = getVersionCode(this);
		SmileyParser.init(getApplicationContext()); 
		BetterHttpApiV1.init(getApplicationContext(), null, false);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		self = PreferenceSettings.getUser(this);
		String token = PreferenceSettings.getToken(this);

		if (token.length()!=0 && self.getId()!=0) {
			// 1. 注册推送服务
			// 2. 存储用户信息
			// 3. 自动清除缓存
			mToken = token;
			setReady(true); 
			setUserPreferences(self.getId());
		}
		
		// FIXME: StrictMode类在1.6以下的版本中没有，会导致类加载失败。因此将这些代码设成关闭状态，仅在做性能调试时才打开。
		// NOTE: StrictMode模式需要2.3+ API支持。
		/*if (DEBUG) { 
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build()); 
		}*/
	}
	
	private void register() {
		LoggedInOutBroadcastReceiver mLoggedInOutReceiver = new LoggedInOutBroadcastReceiver();
        mLoggedInOutReceiver.register(getApplicationContext());
        mLoggedInOutReceiver.setOnLoggedInOutListener(this);
        
        MediaCardStateBroadcastReceiver mMediaStateReceiver = new MediaCardStateBroadcastReceiver();
        mMediaStateReceiver.register(getApplicationContext());
        mMediaStateReceiver.setOnMediaCardAvailableListener(this);
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
		String PACKAGE_NAME = getPackageName();
		Logger.getLogger(PACKAGE_NAME).addHandler(new LoggingHandler());
        Logger.getLogger(PACKAGE_NAME).setLevel(Level.ALL);
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public User getUser() {
		return self;
	}

	public void setUser(User user) {
		this.self = user;
	}
	
	public boolean hasToken() {
    	return (mToken != null);
	}
	
	/** 登陆成功重新调用此方法更新对象  */
	public void setUserPreferences(long userId) {
		String PREFERENCE_NAME = userId + "_preferences";
		mUserPrefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
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
        try {
            Log.d("Attempting to load RemoteResourceManager(cache)");
            mRemoteResourceManager = new RemoteResourceManager(Constants.APP_NAME, Constants.PHOTO_DIR);
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

	@Override
	public void onMediaCardAvailable() {
		// TODO Auto-generated method stub
		Log.d("Media state changed, reloading resource managers");
		loadResourceManagers();
		mTaskHandler.sendEmptyMessage(TaskHandler.MESSAGE_INIT_DIR);
	}

	@Override
	public void onMediaCardUnavailable() {
		// TODO Auto-generated method stub
		getRemoteResourceManager().shutdown();
        loadResourceManagers();
	}

	static class TaskHandler extends Handler {
		private static final int MESSAGE_INIT_DIR = 0;
        private static final int MESSAGE_UPDATE_USER = 1;
        private static final int MESSAGE_START_SERVICE = 2;

        
        public TaskHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (DEBUG) Log.d(TAG, "handleMessage: " + msg.what);
            
            switch (msg.what) {
	            case MESSAGE_INIT_DIR:
	            	setupDefaults();
	            	break;
	            case MESSAGE_UPDATE_USER:
	            	break;
	            case MESSAGE_START_SERVICE:
	            	break;
            }
        }
	}
	
	private static void setupDefaults() {
		try { //创建默认文件目录
			File root=new File(Environment.getExternalStorageDirectory(), Constants.APP_NAME);
			String[] dirs = { Constants.TEMP_DIR, Constants.CACHE_DIR, Constants.AUDIO_DIR, 
							  Constants.AVATAR_DIR, Constants.DATA_DIR, Constants.PHOTO_DIR };
			for(String dir : dirs){
				File directory=new File(root, dir);
				if(!directory.exists()){
					directory.mkdirs();
				}
				File file=new File(directory, ".nomedia");
				if(!file.exists()){
					file.createNewFile();
				}
			}
		} catch (IOException e) {
		}
	}
	
	 private int getVersionCode(Context context) {
	        try {
	            PackageManager pm = context.getPackageManager();
	            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
	            return pi.versionCode;
	        } catch (NameNotFoundException e) {
	            if (DEBUG) Log.d(TAG, "Could not retrieve package info", e);
	            throw new RuntimeException(e);
	        }
    }
	
	private void sendCrashReports() {
		if (!DEBUG) { // 非调试状态，收集异常日志 
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					try {
						ByteArrayOutputStream bos=new ByteArrayOutputStream(1024);
						PrintWriter writer=new PrintWriter(bos);
						e.printStackTrace(writer);
						writer.flush();
						bos.flush();
						String error=new String(bos.toByteArray());
						bos.close();
						writer.close();
					} catch (Exception ex) {
						Log.e(ex.getMessage());
					}
					System.exit(-1);
				}
			});
		}
	}

	@Override
	public void onLoggedIn() {
		// TODO 登陆成功
		 mTaskHandler.sendEmptyMessage(TaskHandler.MESSAGE_UPDATE_USER);
	}

	@Override
	public void onLoggedOut() {
		// TODO 登出成功
		PreferenceSettings.clearAll(this);
	}
}
