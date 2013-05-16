package com.shandagames.android.widget;

import com.shandagames.android.log.LogUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author  lilong
 * @version 2012-7-13 下午6:22:30
 *
 */
public class QLinearLayout extends LinearLayout {

	private static final String TAG = "QLinearLayout";
	
	public QLinearLayout(Context context) {
		super(context);
	}
	
	public QLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/* View会调用此方法来确认自己及所有子对象的大小 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		/*final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight); //保存view大小
        
        for (int i=0;i<getChildCount();i++) {
			View view = getChildAt(i);
			view.measure(widthMeasureSpec, heightMeasureSpec);
		}*/
	}

	/* 为所有子对象分配位置和大小 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onLayout");
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
	}

	/* View大小改变调用此方法  */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onSizeChanged");
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/** 根据你提供的大小和模式，返回你想要的大小值 */
	public static int resolveSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        switch (specMode) {
	        case MeasureSpec.UNSPECIFIED: //未指定尺寸,父控件一般是AdapterView,通过measure方法传入的模式
	            result = size;
	            break;
	        case MeasureSpec.AT_MOST: //最大尺寸:wrap_content 大小随内容变化,不超过父控件最大尺寸即可
	            result = Math.min(size, specSize);
	            break;
	        case MeasureSpec.EXACTLY: //精确尺寸:fille_parent,50dip 大小确定
	            result = specSize;
	            break;
        }
        return result;
    }

	
	
	//用来分发TouchEvent
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		LogUtils.i(TAG, "dispatchTouchEvent");
		return super.dispatchTouchEvent(ev);
	}
	
	//用来拦截TouchEvent
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		LogUtils.i(TAG, "onInterceptTouchEvent");
		return false;
	}
	
	//用来处理TouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//如果最终需要处理事件的view的onTouchEvent()返回了false，
		//那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
		//如果最终需要处理事件的view 的onTouchEvent()返回了true，
		//那么后续事件将可以继续传递给该view的onTouchEvent()处理。
		LogUtils.i(TAG, "onTouchEvent");
		return super.onTouchEvent(event);
	}

}
