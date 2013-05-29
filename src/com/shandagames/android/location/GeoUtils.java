/**
 * Copyright 2010 Mark Wyszomierski
 */

package com.shandagames.android.location;

import com.google.android.maps.GeoPoint;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @file GeoUtils.java
 * @create 2012-9-20 下午12:45:45
 * @author lilong
 * @description TODO
 */
public class GeoUtils {

    /**
     * To be used if you just want a one-shot best last location, iterates over
     * all providers and returns the most accurate result.
     */
    public static Location getBestLastGeolocation(Context context) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = manager.getAllProviders();
         
        Location bestLocation = null;
        for (String it : providers) {
            Location location = manager.getLastKnownLocation(it);
            if (location != null) {
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
        }
        
        return bestLocation;
    }
    
    public static GeoPoint locationToGeoPoint(Location location) {
        if (location != null) {
            GeoPoint pt = new GeoPoint(
                (int)(location.getLatitude() * 1E6 + 0.5),
                (int)(location.getLongitude() * 1E6 + 0.5));
            return pt;
        } else {
            return null;
        }
    }
    
    public static GeoPoint stringLocationToGeoPoint(String strlat, String strlon) {
        try {
            double lat = Double.parseDouble(strlat);
            double lon = Double.parseDouble(strlon);
            GeoPoint pt = new GeoPoint(
                    (int)(lat * 1E6 + 0.5),
                    (int)(lon * 1E6 + 0.5));
            return pt;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {     
        float[] results=new float[1];     
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);     
        return results[0];     
    }  
	
	// This is an approximation function, which does not take the 
	public static double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
	    float pk = (float) (180/3.14169);
	    float a1 = lat_a / pk;
	    float a2 = lng_a / pk;
	    float b1 = lat_b / pk;
	    float b2 = lng_b / pk;

	    float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1) * FloatMath.cos(b2);
	    float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1) * FloatMath.sin(b2);
	    float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);

	    return 6366000*tt;
	}
	
	@SuppressLint("NewApi")
	public void onLocationChanged(Context ctx, Location location, Handler h) {
	    // Bypass reverse-geocoding if the Geocoder service is not available on the
	    // device. The isPresent() convenient method is only available on Gingerbread or above.
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
	        // Since the geocoding API is synchronous and may take a while.  You don't want to lock
	        // up the UI thread.  Invoking reverse geocoding in an AsyncTask.
	    	ReverseGeocodingTask(ctx, location, h);
	    }
	}
	
	public static void ReverseGeocodingTask(final Context ctx, final Location location, 
			final Handler h) {
		new Thread(new Runnable() {
			@Override
			public void run() {
	            StringBuilder sb = null;
	            try {
	            	Geocoder geocoder = new Geocoder(ctx, Locale.getDefault()); 
	                List<Address> list = geocoder.getFromLocation(
	                        location.getLatitude(), location.getLongitude(), 1);
	                if (list != null && list.size() > 0) {
	                	sb = new StringBuilder();
	                    Address address = list.get(0);
	                    // sending back first address line and locality
	                    for(int i=0;i<address.getMaxAddressLineIndex();i++){
	                    	sb.append(address.getAddressLine(i));
	                    }
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            } finally {
	                Message msg = Message.obtain();
	                msg.setTarget(h);
	                if (sb != null) {
	                    msg.what = 0x1000;
	                    Bundle bundle = new Bundle();
	                    bundle.putString("address", sb.toString());
	                    msg.setData(bundle);
	                } 
	                msg.sendToTarget();
	            }
			}
		}).start();
	}
	
	
}