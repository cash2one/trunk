/**
 * 
 */
package com.shandagames.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * @file MyCustomLocationOverlay.java
 * @create 2012-8-27 上午10:25:45
 * @author lilong
 * @description google地图的圆点变为带箭头的图标并随方向转动 
 */
public class MyCustomLocationOverlay extends MyLocationOverlay {

	private Context context;
	private float mOrientation;
	private Bitmap arrowBitmap;

	/**
	 * @param context
	 * @param mapView
	 */
	public MyCustomLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void drawMyLocation(Canvas canvas, MapView mapView,
			Location lastFix, GeoPoint myLocation, long when) {
		// TODO Auto-generated method stub
		// super.drawMyLocation(canvas, mapView, lastFix, myLocation, when);
		// translate the GeoPoint to screen pixels
		Point screenPts = mapView.getProjection().toPixels(myLocation, null);
		// create a rotated copy of the marker
		// Bitmap arrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_green);
		Matrix matrix = new Matrix();
		matrix.postRotate(mOrientation);
		Bitmap rotatedBmp = Bitmap.createBitmap(arrowBitmap, 0, 0,
				arrowBitmap.getWidth(), arrowBitmap.getHeight(), matrix, true);
		// add the rotated marker to the canvas
		canvas.drawBitmap(rotatedBmp, screenPts.x - (rotatedBmp.getWidth() / 2), 
				screenPts.y - (rotatedBmp.getHeight() / 2), null);
	}

	public void setOrientation(float newOrientation) {
		mOrientation = newOrientation;
	}

}
