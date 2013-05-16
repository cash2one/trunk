package com.shandagames.android.location;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import com.shandagames.android.base.AndroidApplication;

/**
 * @file LocationCallBack.java
 * @create 2013-2-1 上午10:59:17
 * @author lilong
 * @description TODO
 */
public abstract class LocationCallBack implements LocationResult {

	private static final long TIME_OUT = 20 * 1000;

	private boolean isTimeOut = true;
	private Handler mHandler = new Handler();

	private Runnable timeOutTask = new Runnable() {
		@Override
		public void run() {
			if (isTimeOut) {
				onTimeOut(getLocation());
			}
		}
	};

	public LocationCallBack() {
		mHandler.postDelayed(timeOutTask, TIME_OUT);
	}

	public Location getLocation() {
		AndroidApplication mClient = AndroidApplication.getInstance();
		SharedPreferences preferences = mClient.getSharedPreferences("preferences", 0);
		Location location = new Location();
		location.setGeolat(preferences.getFloat("lastknowgeolat", 0));
		location.setGeolng(preferences.getFloat("lastknowgeolong", 0));
		return location;
	}
	
	@Override
	public void getLocation(Location location) {
		isTimeOut = false;
		mHandler.removeCallbacks(timeOutTask);
		if (location == null) {
			location = getLocation();
		}
		onBestLocation(location);
		AndroidApplication mClient = AndroidApplication.getInstance();
		SharedPreferences preferences = mClient.getSharedPreferences("preferences", 0);
		Editor editor = preferences.edit();
		editor.putFloat("lastknowgeolat", (float) location.getGeolat());
		editor.putFloat("lastknowgeolong", (float) location.getGeolng());
		editor.putLong("lastknowgeotimestamp", System.currentTimeMillis());
		editor.commit();
	}

	public abstract  void onTimeOut(Location location);
	
	public abstract void onBestLocation(Location location);

}
