package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.base.BaseActivity;
import com.shandagames.android.util.ImageHelper;
import com.shandagames.android.widget.TouchImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author  lilong
 * @version 2012-6-15 下午2:49:59
 */
public class SplashActivity extends BaseActivity {

	private Bitmap bitmap = null;
	private Handler mHandler = new Handler();
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageView ivSplash = new TouchImageView(this);
		ivSplash.setScaleType(ScaleType.FIT_XY);
		
		bitmap = ImageHelper.readBitmap(getResources(), R.drawable.splash);
		ivSplash.setImageBitmap(bitmap);
		setContentView(ivSplash);
		
		mHandler.postDelayed(mRunnable, 3000);
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap!=null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		mHandler.removeCallbacks(mRunnable);
	}
}
