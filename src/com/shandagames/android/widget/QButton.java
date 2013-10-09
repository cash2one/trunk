/**
 * 
 */
package com.shandagames.android.widget;

import com.shandagames.android.log.LogUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * @file QButton.java
 * @create 2012-9-21 上午11:11:47
 * @author lilong
 * @description TODO
 */
public class QButton extends Button {

	private static final String TAG = "QButton";
	
	/**
	 * @param context
	 */
	public QButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public QButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				LogUtils.i(TAG, "ACTION_DOWN");
				break;
			case MotionEvent.ACTION_MOVE:
				LogUtils.i(TAG, "ACTION_MOVE");
				break;
			case MotionEvent.ACTION_UP:
				LogUtils.i(TAG, "ACTION_UP");
				break;
		}
		return super.onTouchEvent(event);
	}
	
}
