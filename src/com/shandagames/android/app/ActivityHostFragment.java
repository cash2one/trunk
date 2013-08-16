package com.shandagames.android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerTrojan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

/**
 * This is a fragment that will be used during transition from activities to
 * fragments.
 */
public abstract class ActivityHostFragment<T extends Activity> extends LocalActivityManagerFragment {

	private final static String ACTIVITY_TAG = "hosted";
	private T mAttachedActivity;

	public T getAttachedActivity() {
		return mAttachedActivity;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		if (mAttachedActivity == null) return;
		mAttachedActivity.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final Intent intent = new Intent(getActivity(), getActivityClass());
		final Bundle args = getArguments();
		if (args != null) {
			intent.putExtras(args);
		}

		final Window w = getLocalActivityManager().startActivity(ACTIVITY_TAG, intent);
		mAttachedActivity = null;
		final Context context = w.getContext();
		if (context instanceof Activity) {
			try {
				mAttachedActivity = (T) context;
				if (context instanceof FragmentCallback) {
					((FragmentCallback<T>) context).setCallbackFragment(this);
				}
			} catch (final ClassCastException e) {
				// This should't happen.
				e.printStackTrace();
			}
		}
		final View wd = w != null ? w.getDecorView() : null;

		if (wd != null) {
			final ViewParent parent = wd.getParent();
			if (parent != null) {
				final ViewGroup v = (ViewGroup) parent;
				v.removeView(wd);
			}

			wd.setVisibility(View.VISIBLE);
			wd.setFocusableInTouchMode(true);
			if (wd instanceof ViewGroup) {
				((ViewGroup) wd).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			}
		}
		return wd;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mAttachedActivity == null || !isVisible() || !isAdded()) return false;
		if (!mAttachedActivity.onOptionsItemSelected(item) && mAttachedActivity instanceof FragmentActivity) {
			final FragmentManager fm = ((FragmentActivity) mAttachedActivity).getSupportFragmentManager();
			return FragmentManagerTrojan.dispatchOptionsItemSelected(fm, item);
		}
		return true;
	}

	@Override
	public void onOptionsMenuClosed(final Menu menu) {
		if (mAttachedActivity == null) return;
		mAttachedActivity.onOptionsMenuClosed(menu);
	}

	@Override
	public void onPrepareOptionsMenu(final Menu menu) {
		if (mAttachedActivity == null) return;
		mAttachedActivity.onPrepareOptionsMenu(menu);
	}

	protected abstract Class<T> getActivityClass();

	public static interface FragmentCallback<A extends Activity> {

		void setCallbackFragment(ActivityHostFragment<A> fragment);

	}
}
