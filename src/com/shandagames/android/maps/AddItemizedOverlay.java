/**
 * 
 */
package com.shandagames.android.maps;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * @file AddItemizedOverlay.java
 * @create 2012-8-31 上午10:34:55
 * @author lilong
 * @description TODO
 */
public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context context;

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

	public AddItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public AddItemizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		Toast.makeText(context, "Tap Performed", Toast.LENGTH_LONG).show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// TODO Auto-generated method stub
		if (event.getAction() == 1) {
			GeoPoint geopoint = mapView.getProjection().fromPixels(
					(int) event.getX(), (int) event.getY());
			// latitude
			double lat = geopoint.getLatitudeE6() / 1E6;
			// longitude
			double lon = geopoint.getLongitudeE6() / 1E6;
			Toast.makeText(context, "Lat: " + lat + ", Lon: " + lon, Toast.LENGTH_SHORT).show();
		}
		return false;
	}

}