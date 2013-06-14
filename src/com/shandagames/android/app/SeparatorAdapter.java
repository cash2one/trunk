package com.shandagames.android.app;

import java.util.TreeSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @file SeparatorAdapter.java
 * @create 2013-3-19 下午01:32:44
 * @author Jacky.Lee
 * @description TODO 包含分隔符适配器
 */
public abstract class SeparatorAdapter<T> extends BaseGroupAdapter<T> {
	protected static final int TYPE_ITEM = 0;
	protected static final int TYPE_SEPARATOR = 1;
	protected static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

	private TreeSet<Object> mSeparatorsSet = new TreeSet<Object>();

	public SeparatorAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void addSeparatorItem(int... args) {
        // save separator position
		for (int i=0;i<args.length;i++) {
			mSeparatorsSet.add(i);
		}
	}
	
	@Override
	public boolean isEnabled(int position) {
		return !mSeparatorsSet.contains(position);
	}

	@Override
	// return correct view type id by position
	public int getItemViewType(int position) {
		if (mSeparatorsSet.contains(position)) {
			return TYPE_SEPARATOR;
		}
		return TYPE_ITEM;
	}

	@Override
	// return how many different view layouts you have
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public View newView(LayoutInflater inflater, View v, int position) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
	    return newSeparatorView(inflater, v, position, type);
	}

	public abstract View newSeparatorView(LayoutInflater inflater, View v, int position, int itemType);
	
}
