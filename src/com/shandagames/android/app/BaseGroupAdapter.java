/**
 * 
 */
package com.shandagames.android.app;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @file ListAdapter.java
 * @create 2012-9-4 下午12:30:56
 * @author Jacky.Lee
 * @description TODO 封装常用数据适配操作
 */
public abstract class BaseGroupAdapter<T> extends BaseAdapter {

	private LayoutInflater inflater;

	private List<T> data = new ArrayList<T>();

	public BaseGroupAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int size = 0;
		if (data != null) {
			size += data.size();
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return data != null && !data.isEmpty();
	}

	@Override
	public T getItem(int position) {
		if (data == null) {
			return null;
		}
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public final View getView(int position, View v, ViewGroup parent) {
		if (v == null) {
			v = newView(inflater, v, position);
		}

		bindView(v, data.get(position));

		return v;
	}

	public List<T> getItems() {
		return data;
	}

	public void addAll(List<T> items) {
		data.addAll(items);
		notifyDataSetChanged();
	}

	public void addAll(List<T> items, boolean redrawList) {
		data.addAll(items);
		if (redrawList) {
			notifyDataSetChanged();
		}
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

	public void remove(int position) {
		data.remove(position);
		notifyDataSetChanged();
	}

	public abstract View newView(LayoutInflater inflater, View v, int position);

	public abstract void bindView(View v, T item);

}
