package com.shandagames.android;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.fragment.NativeMapV2Fragment;
import com.shandagames.android.fragment.WebMapFragment;

/**
 * 谷歌地图使用，详细说明参考官方文档
 * @file MapViewerActivity.java
 * @create 2013-9-27 下午03:39:49
 * @author lilong [dreamxsky@gmail.com]
 * @description TODO
 */
public class MapViewerActivity extends FragmentActivity implements Constants, OnClickListener {
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_viewer);
		final Uri uri = getIntent().getData();
		if (uri == null || !AUTHORITY_MAP.equals(uri.getAuthority())) {
			finish();
			return;
		}
		final Bundle bundle = new Bundle();
		final String param_lat = uri.getQueryParameter(QUERY_PARAM_LAT);
		final String param_lng = uri.getQueryParameter(QUERY_PARAM_LNG);
		if (param_lat == null || param_lng == null) {
			finish();
			return;
		}
		try {
			bundle.putDouble(INTENT_KEY_LATITUDE, Double.valueOf(param_lat));
			bundle.putDouble(INTENT_KEY_LONGITUDE, Double.valueOf(param_lng));
		} catch (final NumberFormatException e) {
			finish();
			return;
		}
		final Fragment fragment = isNativeMapSupported() ? new NativeMapV2Fragment() : new WebMapFragment();
		fragment.setArguments(bundle);
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.map_frame, fragment).commit();
	}
	
	private boolean isNativeMapSupported() {
		// 获取谷歌服务的状态
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// 判断谷歌服务状态是否有效
		if (status != ConnectionResult.SUCCESS) {
			Toast.makeText(this, "Google Play service are not available", Toast.LENGTH_LONG).show();
			//GooglePlayServicesUtil.getErrorDialog(status, this, GOOGLE_PLAY_SERVICE_REQUEST_CODE).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(final View view) {
		switch (view.getId()) {
			case R.id.close: {
				onBackPressed();
				break;
			}
			case R.id.center: {
				final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.map_frame);
				if (!(fragment instanceof MapInterface)) {
					break;
				}
				((MapInterface) fragment).center();
				break;
			}
		}
	}
	
	public static interface MapInterface {

		public void center();
	}
}
