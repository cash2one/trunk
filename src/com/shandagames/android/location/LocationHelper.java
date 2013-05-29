package com.shandagames.android.location;

import java.util.List;
import java.util.Observable;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * @file LocationHelper.java
 * @create 2013-2-1 上午10:36:14
 * @author Jacky.Lee (dreamxsky@gmail.com)
 * @description 启用谷歌定位当前地理位置
 */
public class LocationHelper extends Observable implements LocationListener {
	private static final boolean DEBUG = true;
	private static final String TAG = "LocationHelper";
	
	private static final int CHECK_INTERVAL = 1000 * 30;
	
	private static final long LOCATION_UPDATE_MIN_TIME = 10 * 1000; // 间隔时间
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 10; // 间隔最小距离
	    
	private Location mLastLocation;
	private LocationManager locationManager;
	
	public LocationHelper() {
		super();
	}
	
	public void onLocationChanged(Location location) {
		if (DEBUG) Log.d(TAG, "onLocationChanged: " + location);
		updateLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}
	
	public void updateLocation(Location location) {
        if (DEBUG) {
            Log.d(TAG, "updateLocation: Old: " + mLastLocation);
            Log.d(TAG, "updateLocation: New: " + location);
        }
        // Cases where we only have one or the other.
        if (location != null && mLastLocation == null) {
            if (DEBUG) Log.d(TAG, "updateLocation: Null last location");
            onBestLocationChanged(location);
            return;

        } else if (location != null && isBetterLocation(location, mLastLocation)) {
        	if (DEBUG) Log.d(TAG, "the best location is " + location);
        	onBestLocationChanged(location);
        	 
        } else if (location == null) {
            if (DEBUG) Log.d(TAG, "updated location is null, doing nothing");
            return;
        } 
	 }
	
	synchronized public void onBestLocationChanged(Location location) {
        if (DEBUG) Log.d(TAG, "onBestLocationChanged: " + location);
        mLastLocation = location;
        setChanged();
        notifyObservers(location);
    }

    synchronized public Location getLastKnownLocation() {
        return mLastLocation;
    }
    
    synchronized public void clearLastKnownLocation() {
        mLastLocation = null;
    }
    
    public void register(Context ctx) {
    	if (DEBUG) Log.d(TAG, "Registering this location listener: " + this.toString());
    	locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    	List<String> providers = locationManager.getProviders(true);
    	for (int i = 0; i < providers.size(); i++) {
    		String providerName = providers.get(i);
    		if (locationManager.isProviderEnabled(providerName)) {
    			updateLocation(locationManager.getLastKnownLocation(providerName));
    			locationManager.requestLocationUpdates(providerName, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
    		}
    	}
    }
    
    public void unregister() {
        if (DEBUG) Log.d(TAG, "Unregistering this location listener: " + this.toString());
        if (locationManager != null) {
        	locationManager.removeUpdates(this);
        }
    }
    
	/**
	 * Determines whether one Location reading is better than the current Location fix
	 * 
	 * @param location The new Location that you want to evaluate
	 * @param currentBestLocation The current Location fix, to which you want to compare the new one
	 */
	public boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether two providers are the same
	 */
	public boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
