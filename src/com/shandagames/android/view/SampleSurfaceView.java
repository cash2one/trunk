package com.shandagames.android.view;

import com.shandagames.android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SampleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable, GestureDetector.OnGestureListener {
	private static final String TAG = "SampleSurfaceView";
	
	private int DIRECTION = 0;
	private boolean isFlag = true; //标识中断线程
	private int bitmapX = 150;
	private int bitmapY = 150;
	
	private int mWidth;
	private int mHeight;
	
	private Paint paint;
	private Thread thread;
	private Canvas canvas;
	private Bitmap bitmap;
	private SurfaceHolder mHolder;
	private GestureDetector mDetector;
	
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
		
		mDetector = new GestureDetector(context, this);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gender_boy_selected);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		bitmapX = getWidth() / 2;
		bitmapY = getHeight() / 2;
		
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
		Log.d(TAG, "draw");
		
		try {
			canvas = mHolder.lockCanvas();
			canvas.drawColor(Color.WHITE);
			
			if (bitmapX >= (mWidth-20)) {
				bitmapX = 0;
			} else if (bitmapX <= 0) {
				bitmapX = (mWidth-20);
			}
			
			switch (DIRECTION) {
			case 1:
				canvas.drawBitmap(bitmap, bitmapX-=5, bitmapY, paint);
				break;
			case 2:
				canvas.drawBitmap(bitmap, bitmapX+=5, bitmapY, paint);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (canvas != null) {
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() > e2.getX() && Math.abs(velocityX) > 0) {
			// 向左滑动
			DIRECTION = 1;
		} else if (e2.getX() > e1.getX() && Math.abs(velocityX) > 0) {
			// 向右滑动
			DIRECTION = 2;
		} 
		return true;
	}
	
}