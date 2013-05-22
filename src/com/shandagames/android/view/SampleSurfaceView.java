package com.shandagames.android.view;

import com.shandagames.android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SampleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	private static final String TAG = "SampleSurfaceView";
	
	//标识中断线程
	private boolean isFlag = true; 

	private int mWidth;
	private int mHeight;
	
	private Paint paint;
	private Thread thread;
	private Canvas canvas;
	private Bitmap bitmap;
	private SurfaceHolder mHolder;
	
	public SampleSurfaceView(Context context) {
		super(context);
		Log.d(TAG, "initialize");

		mHolder = getHolder();
		mHolder.addCallback(this);
		setFocusable(true); //获取焦点，执行按键操作
		setFocusableInTouchMode(true);
		setKeepScreenOn(true);// 屏幕常亮
		
		paint = new Paint();
		paint.setTextSize(18);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gender_boy_selected);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		
		mWidth = getWidth();
		mHeight = getHeight(); 
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed");
		// 终止线程操作
		isFlag = false;
		thread = null;
		
		paint.reset();
		paint = null;
		mHolder.removeCallback(this);
	}

	@Override
	public void run() {
		while (isFlag) {
			try {
				draw();
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void draw() {
		try {
			canvas = mHolder.lockCanvas();
			
			//canvas.save();
			canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
			canvas.drawBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), paint);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (canvas != null) {
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

}