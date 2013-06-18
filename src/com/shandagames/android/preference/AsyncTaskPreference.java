package com.shandagames.android.preference;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;

public abstract class AsyncTaskPreference extends Preference implements OnPreferenceClickListener {

	private Task mTask;

	public AsyncTaskPreference(final Context context) {
		this(context, null);
	}

	public AsyncTaskPreference(final Context context, final AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
	}

	public AsyncTaskPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		setOnPreferenceClickListener(this);
	}

	@Override
	public final boolean onPreferenceClick(final Preference preference) {
		if (mTask == null || mTask.getStatus() != Status.RUNNING) {
			mTask = new Task(this);
			mTask.execute();
		}
		return true;
	}

	protected abstract void doInBackground();

	private static class Task extends AsyncTask<Void, Void, Void> {

		private final AsyncTaskPreference mPreference;
		private final Context mContext;
		private final ProgressDialog mProgress;

		public Task(final AsyncTaskPreference preference) {
			mPreference = preference;
			mContext = preference.getContext();
			mProgress = new ProgressDialog(mContext);
		}

		@Override
		protected Void doInBackground(final Void... args) {
			mPreference.doInBackground();
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			if (mProgress == null) return;
			if (mProgress.isShowing()) {
				mProgress.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			if (mProgress == null) return;
			if (mProgress.isShowing()) {
				mProgress.dismiss();
			}
			mProgress.setMessage("Please wait.");
			mProgress.setCancelable(false);
			mProgress.show();
		}

	}

}
