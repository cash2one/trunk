package com.shandagames.android.maps;

import java.util.ArrayList;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.shandagames.android.R;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class TouchedLocationOverlay extends ItemizedOverlay<OverlayItem> implements ItemizedOverlay.OnFocusChangeListener {
	
	private Handler handler;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();	
	
	private View mPopView;
	private MapView mapView;

	public TouchedLocationOverlay(Drawable defaultMarker,Handler h, MapView mapView, View popView) {
		super(boundCenterBottom(defaultMarker));	
		// Handler object instantiated in the class MainActivity
		this.handler = h;
		
		this.mapView = mapView;
		this.mPopView = popView;
		
		setOnFocusChangeListener(this);
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
	
	public void removeOverlay(int position) {
		mOverlays.remove(position);
	}
	
	// This method is invoked, when user tap on the map
	@Override
	public boolean onTap(GeoPoint p, MapView map) {		
	    // Creating a Message object to send to Handler
//	    Message message = new Message();
//	    // Creating a Bundle object ot set in Message object
//	    Bundle data = new Bundle();
//	    // Setting latitude in Bundle object
//	    data.putInt("latitude", p.getLatitudeE6());
//	    // Setting longitude in the Bundle object
//	    data.putInt("longitude", p.getLongitudeE6());
//	    // Setting the Bundle object in the Message object
//	    message.setData(data);
//	    // Sending Message object to handler
//	    handler.sendMessage(message);		
		
		return super.onTap(p, map);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
		// TODO Auto-generated method stub
		if (newFocus != null) {
			MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
			params.x = -5;//X轴偏移
			params.y = -60;//Y轴偏移
			params.point = newFocus.getPoint();
			mapView.updateViewLayout(mPopView, params);
			mPopView.setVisibility(View.VISIBLE);
			mPopView.findViewById(R.id.balloon_progress).setVisibility(View.VISIBLE);
			mPopView.findViewById(R.id.balloon_bubbleText).setVisibility(View.GONE);
		}
	}	
}
