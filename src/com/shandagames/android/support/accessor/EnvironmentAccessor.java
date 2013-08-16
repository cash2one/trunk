package com.shandagames.android.support.accessor;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

public final class EnvironmentAccessor {

	@TargetApi(Build.VERSION_CODES.FROYO)
	public static File getExternalCacheDir(final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
			return context.getExternalCacheDir();
		final File ext_storage_dir = Environment.getExternalStorageDirectory();
		if (ext_storage_dir != null && ext_storage_dir.isDirectory()) {
			final String ext_cache_path = ext_storage_dir.getAbsolutePath() + "/Android/data/"
					+ context.getPackageName() + "/cache/";
			final File ext_cache_dir = new File(ext_cache_path);
			if (ext_cache_dir.isDirectory() || ext_cache_dir.mkdirs()) return ext_cache_dir;
		}
		return null;
	}
}
