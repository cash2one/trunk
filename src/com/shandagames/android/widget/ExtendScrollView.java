package com.shandagames.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 兼容ViewPager的ScrollView
 * 解决了ViewPager在ScrollView中的滑动反弹问题
 * 
 * @author  lilong
 * @version 2012-8-2 下午5:05:45
 *
 */
public class ExtendScrollView extends ScrollView {

	// 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public ExtendScrollView(Context context) {
    	super(context);
    }
    
    public ExtendScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if(xDistance > yDistance){
                    return false;
                }  
        }

        return super.onInterceptTouchEvent(ev);
    }
	
}
