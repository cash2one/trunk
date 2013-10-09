package com.shandagames.android.location;

import android.content.Context;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;

/**
 * @file LbsHelper.java
 * @create 2013-2-1 上午10:55:07
 * @author lilong
 * @description 启用百度定位获取当前地理位置
 */
public class LbsHelper implements LocationListener {

	private BMapManager mBMapMan;
	private LocationResult locationResult;
	
	public LbsHelper(Context ctx, LocationResult result) {
		if (result == null) return;
		locationResult = result;
		
		mBMapMan = new BMapManager(ctx);
		mBMapMan.init("2F79DCAEA7D103B89059B98008D0957B048C57F9", null);
		mBMapMan.getLocationManager().requestLocationUpdates(this);
		mBMapMan.start();
	}
	
	@Override
	public void onLocationChanged(android.location.Location location) {
		onBestLocationListener(location);
		mBMapMan.getLocationManager().removeUpdates(this);
		mBMapMan.stop();
	}

	private void onBestLocationListener(android.location.Location mLocation) {
		Location mLastLocation = null;
		if (mLocation != null) {
			mLastLocation = new Location();
			mLastLocation.setGeolat(mLocation.getLatitude());
			mLastLocation.setGeolng(mLocation.getLongitude());
			mLastLocation.setAcc(mLocation.getAccuracy());
		} 
		if (locationResult != null) {
			locationResult.getLocation(mLastLocation);
		}
	}
}
