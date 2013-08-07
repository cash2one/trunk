package com.shandagames.android.fragment.callback;

import android.view.View;
import android.widget.ProgressBar;

public class InvalidateProgressBarRunnable implements Runnable {

	private final View view;

	public InvalidateProgressBarRunnable(final View view) {
		this.view = view;
	}

	@Override
	public void run() {
		if (!(view instanceof ProgressBar)) return;
		if (((ProgressBar) view).isIndeterminate()) {
			view.invalidate();
		}
		view.postDelayed(this, 16);
	}

	
}
