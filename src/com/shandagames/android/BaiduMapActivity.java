package com.shandagames.android;

import java.util.List;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.shandagames.android.R;
import com.shandagames.android.log.LogUtils;
import com.shandagames.android.maps.LongPressOverlay;
import com.shandagames.android.maps.TouchedLocationOverlay;

/**
 * @file BaiduMapActivity.java
 * @create 2012-8-20 上午11:31:05
 * @author lilong
 * @description TODO
 * http://dev.baidu.com/wiki/geolocation/index.php?title=AndroidAPI%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%972.6
 * http://dev.baidu.com/wiki/geolocation/index.php?title=AndroidAPI%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A32.6
 */
public class BaiduMapActivity extends MapActivity implements LocationListener, MKGeneralListener, MKSearchListener {

	private BMapManager mapManager;
	private MapController mc;
	private MKLocationManager mLocationManager;
	
	private MapView mMapView = null;	// 地图View
	private MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	private View mPopView = null;
	
	private List<Overlay> overlays;
	private TouchedLocationOverlay locationOverlay;
	private MyLocationOverlay myLocationOverlay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_baidu_layout);
		// Creating an instance of BMapManager 
		mapManager = new BMapManager(getApplication());
		// init arguments of BMapManager object
		mapManager.init("2F79DCAEA7D103B89059B98008D0957B048C57F9", this);
		// imit MapManager object
		super.initMapActivity(mapManager);
		
		// Getting mapView object from layout
		mMapView = (MapView) findViewById(R.id.mapview);
		// Setting Zoom Controls
    	mMapView.setBuiltInZoomControls(true);
    	// Setting mapView could be click
    	mMapView.setClickable(true);
    	
    	// Init popView layout
    	initPopView();
    	
    	// Getting the MapController
    	mc = mMapView.getController(); 
    	// Getting Overlays of the map
    	overlays = mMapView.getOverlays();
    	// Adding longPressOverlay to the overlay
    	overlays.add(new LongPressOverlay(mHandler, mMapView));

    	myLocationOverlay = new MyLocationOverlay(this, mMapView);
    	myLocationOverlay.enableMyLocation();
    	myLocationOverlay.enableCompass();
    	overlays.add(myLocationOverlay);

    	// Getting Drawable object corresponding to a resource image
    	Drawable marker = getResources().getDrawable(R.drawable.bubble_maker_selector);
    	// define offset of the maker
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());   
		// Creating an ItemizedOverlay
    	locationOverlay = new TouchedLocationOverlay(marker,mHandler,mMapView,mPopView);   
		
		// 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(mapManager, this);
        // Getting LocationManager object from System Service
		mLocationManager = mapManager.getLocationManager();
        
		// Show the location in the Google Map
        //showLocation(myLocationOverlay.getMyLocation());
		showLocation(new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6)));        
	}
	
    private void initPopView(){
    	if(null == mPopView){
    		mPopView = getLayoutInflater().inflate(R.layout.bubble_overlay_popup, null);
    		mMapView.addView(mPopView, new MapView.LayoutParams(
					MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT, null,
					MapView.LayoutParams.BOTTOM_CENTER));
			mPopView.setVisibility(View.GONE);
    	}
    }
	
	// Handles Taps on the Google Map
	private Handler mHandler = new Handler(){
		// Invoked by the method onTap() 
		// in the class CurrentLocationOverlay
		@Override
		public void handleMessage(Message msg) {	
			if (msg.obj == null) return;
			showLocation((GeoPoint) msg.obj);
		}
	};
	
	private void showLocation(final GeoPoint p){    
		mSearch.reverseGeocode(p);
		// Creating an OverlayItem to mark the point
    	OverlayItem overlayItem = new OverlayItem(p, "Current Location", "Loading...");
    	// Adding the OverlayItem in the LocationOverlay
    	locationOverlay.addOverlay(overlayItem);
    	// Getting focus of the overlayItem
    	locationOverlay.setFocus(overlayItem);
    	// Clearing the overlays
    	if (overlays.size() > 0) {
    		locationOverlay.removeOverlay(0);
    	}
    	// Adding locationOverlay to the overlay
    	overlays.add(locationOverlay);
    	// Applying a zoom
		mc.setZoom(18);
    	// Locating the point in the Map
    	mc.animateTo(p);
    	// Redraws the map
    	mMapView.invalidate();
    	
//    	// Showing the popView
//    	Point point = new Point();
//    	mMapView.getProjection().toPixels(p, point);
//    	TranslateAnimation translate = new TranslateAnimation(0, 0, -point.y, 0);
//    	translate.setDuration(500L);
//    	translate.setInterpolator(new LinearInterpolator());
//    	translate.setFillAfter(true);
//    	mPopView.startAnimation(translate);
//    	mPopView.setVisibility(View.VISIBLE);
    }
	
	@Override
	protected void onResume() {
		if (mapManager != null) {
			mLocationManager.requestLocationUpdates(this);
			//通过enableProvider和enableProvider方法，选择定位的Provider
			mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
			mLocationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
			mapManager.start();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if (mapManager != null) {
			mLocationManager.removeUpdates(this);
			//通过enableProvider和enableProvider方法，选择定位的Provider
			mLocationManager.disableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
			mLocationManager.disableProvider(MKLocationManager.MK_GPS_PROVIDER);
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			myLocationOverlay.disableCompass();
			myLocationOverlay.disableMyLocation();
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 当位置发生变化时触发此方法
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			showLocation(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)));
		}
	}
	
	@Override
	public void onGetNetworkState(int iError) {
		// TODO Auto-generated method stub
		LogUtils.d("MKGeneralListener", "onGetNetworkState error is "+ iError);
		Toast.makeText(this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetPermissionState(int iError) {
		// TODO Auto-generated method stub
		LogUtils.d("MKGeneralListener", "onGetPermissionState error is "+ iError);
		if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
			// 授权Key错误：
			Toast.makeText(this, "请在BMapApiDemoApp.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onGetAddrResult(final MKAddrInfo res, int error) {
		// TODO Auto-generated method stub
		if (res!=null && !TextUtils.isEmpty(res.strAddr)) {
			mPopView.findViewById(R.id.balloon_progress).setVisibility(View.GONE);
			TextView txtView = (TextView) mPopView.findViewById(R.id.balloon_bubbleText);
			txtView.setVisibility(View.VISIBLE);
			txtView.setText(res.strAddr);
		}
	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}

}
