package com.shandagames.android.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class FontViewGroup extends ViewGroup {

	public FontViewGroup(Context context) {
		super(context);
	}

	@Override
	// 对每个子View进行measure():设置每子View的大小，即实际宽和高  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取该ViewGroup的实际长和宽  涉及到MeasureSpec类的使用  
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		//设置本ViewGroup的宽高 
		setMeasuredDimension(width, height);
		
		for (int i=0;i<getChildCount();i++) {
			View v = getChildAt(i); //获得每个对象的引用  
			v.measure(50, 50); //简单的设置每个子View对象的宽高为 50px , 50px   
			//或者可以调用ViewGroup父类方法measureChild()或者measureChildWithMargins()方法  
            //this.measureChild(child, widthMeasureSpec, heightMeasureSpec) ; 
		}
	}

	@Override
	 // 子View视图进行布局  
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	}

}
