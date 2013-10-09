package com.shandagames.android.fragment;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shandagames.android.R;
import com.shandagames.android.MapViewerActivity.MapInterface;
import com.shandagames.android.constant.Constants;

public class NativeMapV2Fragment extends SupportMapFragment implements Constants, MapInterface {

	private double latitude = 39.90960456049752;
	private double longitude = 116.3972282409668;
	
	private GoogleMap mMap;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Bundle bundle = getArguments();
		if (bundle != null) {
			latitude = bundle.getDouble(INTENT_KEY_LATITUDE, latitude);
			longitude = bundle.getDouble(INTENT_KEY_LONGITUDE, longitude);
		}
		
		// Getting GoogleMap object from the fragment
		mMap = getMap();
		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);
		// Showing the current location in Google Map
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		// Zoom in the Google Map
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		// add markers
		mMap.addMarker(new MarkerOptions()
			.position(latLng).title("天安门")
			.icon(BitmapDescriptorFactory
			.fromResource(R.drawable.ic_map_marker)));
	}

	@Override
	public void center() {
		mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
	}
}
