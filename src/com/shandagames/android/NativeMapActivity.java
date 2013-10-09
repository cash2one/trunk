package com.shandagames.android;

import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.shandagames.android.constant.Constants;

public class NativeMapActivity extends MapActivity implements Constants {

	private MapView mMapView;

	public void center() {
		final Bundle extras = getIntent().getExtras();
		if (extras == null || !extras.containsKey(INTENT_KEY_LATITUDE) || !extras.containsKey(INTENT_KEY_LONGITUDE))
			return;
		final double lat = extras.getDouble(INTENT_KEY_LATITUDE, 0.0), lng = extras
				.getDouble(INTENT_KEY_LONGITUDE, 0.0);
		final GeoPoint gp = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		final MapController mc = mMapView.getController();
		mc.animateTo(gp);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onCreate(final Bundle icicle) {
		super.onCreate(icicle);
		final Bundle extras = getIntent().getExtras();
		if (extras == null || !extras.containsKey(INTENT_KEY_LATITUDE) || !extras.containsKey(INTENT_KEY_LONGITUDE)) {
			finish();
			return;
		}
		mMapView = new MapView(this, GOOGLE_MAPS_API_KEY);
		mMapView.setClickable(true);
		final List<Overlay> overlays = mMapView.getOverlays();
		final double lat = extras.getDouble(INTENT_KEY_LATITUDE, 0.0), lng = extras
				.getDouble(INTENT_KEY_LONGITUDE, 0.0);
		final GeoPoint gp = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		final Drawable d = getResources().getDrawable(R.drawable.ic_map_marker);
		final Itemization markers = new Itemization(d);
		final OverlayItem overlayitem = new OverlayItem(gp, "", "");
		markers.addOverlay(overlayitem);
		overlays.add(markers);
		final MapController mc = mMapView.getController();
		mc.setZoom(12);
		mc.animateTo(gp);
		setContentView(mMapView);
	}

	static class Itemization extends ItemizedOverlay<OverlayItem> {

		private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public Itemization(final Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public void addOverlay(final OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		@Override
		protected OverlayItem createItem(final int i) {
			return mOverlays.get(i);
		}

		protected static Drawable boundCenterBottom(final Drawable d) {
			d.setBounds(-d.getIntrinsicWidth() / 2, -d.getIntrinsicHeight(), d.getIntrinsicWidth() / 2, 0);
			return d;
		}
	}
}
