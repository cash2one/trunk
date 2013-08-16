package com.shandagames.android.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * @file AbstractTaskLoader.java
 * @create 2013-8-9 下午12:06:03
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO Loader which extends AsyncTaskLoaders and handles caveats as pointed out in
 */
public abstract class AbstractTaskLoader<T> extends AsyncTaskLoader<T> {

	private T data;

	public AbstractTaskLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(T result) {
		if (isReset()) {
			return;
		}

		this.data = result;

		super.deliverResult(result);
	}

	@Override
	protected void onStartLoading() {
		if (data != null) {
			deliverResult(data);
		}

		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(T result) {
		super.onCanceled(result);

		onRelease(result);
	}

	@Override
	protected void onReset() {
		super.onReset();

		onStopLoading();

		if (data != null) {
			onRelease(data);
			this.data = null;
		}
	}

	/**
	 * 释放资源
	 */
	protected void onRelease(T result) {
	}

	protected T getData() {
		return data;
	}
}
