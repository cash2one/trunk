package com.shandagames.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.shandagames.android.R;

/**
 * @file GoogleMapActivity.java
 * @create 2012-8-20 下午6:19:08
 * @author lilong
 * @description TODO
 */
public class PlacesMapActivity extends MapActivity implements LocationListener {

	private MapView mMapView;
	
	private LocationManager locationManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_google_layout);

		mMapView = (MapView) findViewById(R.id.mapview);
		// Setting Zoom Controls
		mMapView.setBuiltInZoomControls(true);
		// Show the location in the Google Map
        showLocation((int) (39.915 * 1E6), (int) (116.404 * 1E6));   
        
		// Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        // Show the location in the Google Map
        if(location!=null){
        	showLocation((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
        }
        locationManager.requestLocationUpdates(provider, 5000, 0, this);
	}

	// Handles Taps on the Google Map
	private Handler mHandler = new Handler(){
		// Invoked by the method onTap() 
		// in the class CurrentLocationOverlay
		@Override
		public void handleMessage(Message msg) {	
			Bundle data = msg.getData();
			// Getting the Latitude of the location
			int latitude = data.getInt("latitude");
			// Getting the Longitude of the location
			int longitude = data.getInt("longitude");	
			// Show the location in the Google Map
			showLocation(latitude,longitude);	
		}
	};
	
	private void showLocation(int latitude, int longitude){    
		// Getting the MapController
    	MapController mc = mMapView.getController();        
    	// Creating an instance of GeoPoint, to display in Google Map
    	GeoPoint p = new GeoPoint(latitude, longitude);
    	// Locating the point in the Google Map
    	mc.animateTo(p);     
		// Applying a zoom
		mc.setZoom(15);
		
		// Getting Overlays of the map
    	List<Overlay> overlays = mMapView.getOverlays();
    	// Getting Drawable object corresponding to a resource image
    	Drawable marker = getResources().getDrawable(R.drawable.bubble_maker_selector);
    	// define offset of the maker
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());   
    	// Creating an ItemizedOverlay
    	TouchedLocationOverlay locationOverlay = new TouchedLocationOverlay(marker,mHandler);        
    	// Creating an OverlayItem to mark the point
    	OverlayItem overlayItem = new OverlayItem(p, "Current Location", "Latitude : " + latitude + ", Longitude:" + longitude);
    	// Adding the OverlayItem in the LocationOverlay
    	locationOverlay.addOverlay(overlayItem);
    	// Clearing the overlays
    	overlays.clear();
    	// Adding locationOverlay to the overlay
    	overlays.add(locationOverlay);
    	// Redraws the map
    	mMapView.invalidate();
    	
    	// Executing ReverseGeocodingTask to get Address
        new ReverseGeocodingTask(this).execute(latitude/1E6, longitude/1E6);
        // Getting current address from latitude and longitude
		//new GetAddressFromLocation().execute(latitude/1E6, longitude/1E6);
    }
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			locationManager.removeUpdates(this);
			showLocation((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
		}
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
	
	private class GetAddressFromLocation extends AsyncTask<Double, Integer, String> {
		@Override
		protected String doInBackground(Double... params) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append("纬度:" + params[0]/1E6 + "," + "经度:" + params[1]/1E6+"\r\n");
			String url = String.format(Locale.getDefault(), 
					"http://maps.google.com/maps/api/geocode/json?latlng=%1$s,%2$s&sensor=true&language=zh_CN", 
					String.valueOf(params[0]), String.valueOf(params[1]));
			try {
				InputStream is = new URL(url).openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder input = new StringBuilder();
				String line = null;
				while((line = br.readLine())!=null) {
					input.append(line);
				}
				br.close();
				is.close();
				
				JSONObject json = new JSONObject(input.toString());
				JSONArray mJsonArray = json.getJSONArray("results");
				if (mJsonArray.length() > 0) {
					JSONObject mJsonObject = mJsonArray.optJSONObject(0);
					if (mJsonObject!=JSONObject.NULL) {
						sb.append(mJsonObject.getString("formatted_address"));
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
            return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				Toast.makeText(PlacesMapActivity.this, result, Toast.LENGTH_SHORT).show();
			} 
		}
	}
	
	private class ReverseGeocodingTask extends AsyncTask<Double, Void, String>{
        Context mContext;
 
        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }
 
        @Override
        protected String doInBackground(Double... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].doubleValue();
            double longitude = params[1].doubleValue();
 
            List<Address> addresses = null;
            StringBuilder sb = new StringBuilder();
 
            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
 
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
                for(int i=0;i<address.getMaxAddressLineIndex();i++){
                	sb.append(address.getAddressLine(i));
                }
            }
 
            return sb.toString();
        }
 
        @Override
        protected void onPostExecute(String addressText) {
            // Setting address of the touched Position
        	Toast.makeText(PlacesMapActivity.this, addressText, Toast.LENGTH_SHORT).show();
        }
    }
	
	private class TouchedLocationOverlay extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();	
		private Handler handler;

		public TouchedLocationOverlay(Drawable defaultMarker,Handler h) {
			super(boundCenterBottom(defaultMarker));	
			
			// Handler object instantiated in the class MainActivity
			this.handler = h;
		}

		// Executed, when populate() method is called
		@Override
		protected OverlayItem createItem(int arg0) {
			return mOverlays.get(arg0);		
		}	

		@Override
		public int size() {		
			return mOverlays.size();
		}	
		
		public void addOverlay(OverlayItem overlay){
			mOverlays.add(overlay);
			populate(); // Invokes the method createItem()
		}
		
		// This method is invoked, when user tap on the map
		@Override
		public boolean onTap(GeoPoint p, MapView map) {		
		    // Creating a Message object to send to Handler
		    Message message = new Message();
		    // Creating a Bundle object ot set in Message object
		    Bundle data = new Bundle();
		    // Setting latitude in Bundle object
		    data.putInt("latitude", p.getLatitudeE6());
		    // Setting longitude in the Bundle object
		    data.putInt("longitude", p.getLongitudeE6());
		    // Setting the Bundle object in the Message object
		    message.setData(data);
		    // Sending Message object to handler
		    handler.sendMessage(message);		
			
			return super.onTap(p, map);
		}	
	}

}
