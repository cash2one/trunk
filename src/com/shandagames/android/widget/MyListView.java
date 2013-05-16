/**
 * 
 */
package com.shandagames.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @file MyListView.java
 * @create 2012-9-21 上午10:35:29
 * @author lilong
 * @description TODO
 */
public class MyListView extends ListView {

	/**
	 * @param context
	 */
	public MyListView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/****
	 * 拦截触摸事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			//映射到列表中的一个坐标，根据这个坐标可以确定点击的是哪一个item
			//返回这个位置包含的指定点，如果这2个点不相交返回INVALID_POSITION 
			int itemnum = pointToPosition(x, y);
			if (itemnum == AdapterView.INVALID_POSITION)
				break;
			else {
				if (itemnum == 0) {
					if (itemnum == (getAdapter().getCount() - 1)) {
						// 只有一项
						// setSelector(R.drawable.list_round);
					} else {
						// 第一项
						// setSelector(R.drawable.list_top_round);
					}
				} else if (itemnum == (getAdapter().getCount() - 1)) {
					// 最后一项
					// setSelector(R.drawable.list_bottom_round);
				} else {
					// 中间项
					// setSelector(R.drawable.list_center_round);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	
	/***
	 * 动态设置listview的高度，解决ScrollView冲突问题
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// params.height += 5;// if without this statement,the listview will be a little short
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

}
