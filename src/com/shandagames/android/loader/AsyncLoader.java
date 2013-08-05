package com.shandagames.android.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Loader which extends AsyncTaskLoaders and handles caveats as pointed out in
 * http://code.google.com/p/android/issues/detail?id=14944.
 * 
 * Based on CursorLoader.java in the Fragment compatibility package.
 * 
 * @author Alexander Blom (me@alexanderblom.se)
 * 
 * @param <D> data type
 */
public abstract class AsyncLoader<T> extends AsyncTaskLoader<T> {

	private T data;

	public AsyncLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(T data) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			return;
		}

		this.data = data;

		super.deliverResult(data);
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
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		data = null;
	}

	protected T getData() {
		return data;
	}
}
