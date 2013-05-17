package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.base.BaseActivity;
import com.shandagames.android.log.Log;
import com.shandagames.android.photoview.PhotoViewAttacher;
import com.shandagames.android.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import com.shandagames.android.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.shandagames.android.util.ImageHelper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

/**
 * @author  lilong
 * @version 2012-6-15 下午2:49:59
 */
public class SplashActivity extends BaseActivity {
	static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %%";
	
	private Bitmap bitmap = null;
	private Handler mHandler = new Handler();
	private PhotoViewAttacher mAttacher;
	private Toast mCurrentToast;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageView ivSplash = new ImageView(this);
		bitmap = ImageHelper.readBitmap(getResources(), R.drawable.splash);
		ivSplash.setImageBitmap(bitmap);
		setContentView(ivSplash);
		
		mAttacher = new PhotoViewAttacher(ivSplash);
		mAttacher.setScaleType(ScaleType.FIT_XY);
		
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
		
		mHandler.postDelayed(mRunnable, 3000);
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem zoomToggle = menu.findItem(R.id.menu_zoom_toggle);
		zoomToggle.setTitle(mAttacher.canZoom() ? "Enable Zoom" : "Disable Zoom");

		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_zoom_toggle:
				mAttacher.setZoomable(!mAttacher.canZoom());
				return true;

			case R.id.menu_scale_fit_center:
				mAttacher.setScaleType(ScaleType.FIT_CENTER);
				return true;

			case R.id.menu_scale_fit_start:
				mAttacher.setScaleType(ScaleType.FIT_START);
				return true;

			case R.id.menu_scale_fit_end:
				mAttacher.setScaleType(ScaleType.FIT_END);
				return true;

			case R.id.menu_scale_fit_xy:
				mAttacher.setScaleType(ScaleType.FIT_XY);
				return true;

			case R.id.menu_scale_scale_center:
				mAttacher.setScaleType(ScaleType.CENTER);
				return true;

			case R.id.menu_scale_scale_center_crop:
				mAttacher.setScaleType(ScaleType.CENTER_CROP);
				return true;

			case R.id.menu_scale_scale_center_inside:
				mAttacher.setScaleType(ScaleType.CENTER_INSIDE);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap!=null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		mAttacher.cleanup(); //  Need to call clean-up
		mHandler.removeCallbacks(mRunnable);
	}
	
	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
			float xPercentage = x * 100f;
			float yPercentage = y * 100f;

			if (null != mCurrentToast) {
				mCurrentToast.cancel();
			}

			mCurrentToast = Toast.makeText(SplashActivity.this,
					String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage), Toast.LENGTH_SHORT);
			mCurrentToast.show();
		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {
			Log.v("onMatrixChanged:"+rect.toString());
		}
	}
}
