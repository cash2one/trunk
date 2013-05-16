package com.shandagames.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @file LetterView.java
 * @create 2013-2-19 下午12:02:38
 * @author lilong
 * @description TODO 字母列表导航
 */
public class LetterView extends View {
	
	private static final char[] data = { '!','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z','#'};
	                            
	private Paint paint;
	
	private OnTouchChangedListener onTouchChangedListener;;
	
	public LetterView(Context context) {
		super(context);
		init();
	}
	
	public LetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setTextSize(14);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		float singleHeight = getMeasuredHeight() / data.length;
		for (int i=0;i<data.length;i++) {
			float xPos = (getMeasuredWidth() - paint.measureText(String.valueOf(data[i]))) / 2;
			float yPos = singleHeight * (i + 1);
			canvas.drawText(String.valueOf(data[i]), xPos, yPos, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int startY = (int) event.getY();
		int idx = startY / (getMeasuredHeight() / data.length);
		if (idx >= data.length - 1) {
			idx = data.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				setBackgroundColor(Color.LTGRAY);
				if (onTouchChangedListener!=null) {
					onTouchChangedListener.onTouchLetterChanged(String.valueOf(data[idx]));
				}
				break;
			case MotionEvent.ACTION_UP:
				setBackgroundDrawable(new ColorDrawable(0x00000000));
				if (onTouchChangedListener!=null) {
					onTouchChangedListener.onTouchLetterCancel();
				}
				break;
		}
		return true;
	}

	public void setOnTouchChangedListener(OnTouchChangedListener onTouchChangedListener) {
		this.onTouchChangedListener = onTouchChangedListener;
	}

	public interface OnTouchChangedListener{
		
		public void onTouchLetterChanged(String str);
		
		public void onTouchLetterCancel();
	}
	
}
