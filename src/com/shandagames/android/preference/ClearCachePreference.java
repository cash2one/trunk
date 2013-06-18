package com.shandagames.android.preference;

import java.io.File;
import java.io.FileFilter;
import com.shandagames.android.support.accessor.EnvironmentAccessor;
import android.content.Context;
import android.util.AttributeSet;

public class ClearCachePreference extends AsyncTaskPreference {

	public ClearCachePreference(final Context context) {
		this(context, null);
	}

	public ClearCachePreference(final Context context, final AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
	}

	public ClearCachePreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void doInBackground() {
		final Context context = getContext();
		if (context == null) return;
		final File external_cache_dir = EnvironmentAccessor.getExternalCacheDir(context);

		if (external_cache_dir != null) {
			for (final File file : external_cache_dir.listFiles((FileFilter) null)) {
				deleteRecursive(file);
			}
		}
		final File internal_cache_dir = context.getCacheDir();
		if (internal_cache_dir != null) {
			for (final File file : internal_cache_dir.listFiles((FileFilter) null)) {
				deleteRecursive(file);
			}
		}
	}

	private static void deleteRecursive(final File f) {
		if (f.isDirectory()) {
			for (final File c : f.listFiles()) {
				deleteRecursive(c);
			}
		}
		f.delete();
	}

}
