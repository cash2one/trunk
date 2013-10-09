package com.shandagames.android.common;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
* Detects when user is close to the end of the current page and starts loading the next page
* so the user will not have to wait (that much) for the next entries.
* From: http://benjii.me/2010/08/endless-scrolling-listview-in-android/
*/
public class EndlessScrollListener implements OnScrollListener {
	// how many entries earlier to start loading next page
	private int visibleThreshold = 5;
	private int currentPage = 0;
	private int previousTotal = 0;
	private boolean loading = true;
	private OnEndReachedListener listener;

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public void setOnEndReachedListener(OnEndReachedListener listener) {
		this.listener = listener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			if (listener != null) {
				listener.onEndReached(currentPage);
			}
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	public int getCurrentPage() {
        return currentPage;
    }
	
	public interface OnEndReachedListener {
		
		public void onEndReached(int pageNumber);
	}
}