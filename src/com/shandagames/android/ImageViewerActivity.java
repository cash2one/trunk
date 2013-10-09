package com.shandagames.android;

import java.io.File;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.shandagames.android.constant.Constants;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageViewerActivity extends FragmentActivity implements Constants, 
	OnClickListener, LoaderCallbacks<Bitmap> {

	private ImageViewTouch mImageView;
	private View mProgress;
	private ImageButton mRefreshStopSaveButton;
	private boolean mImageLoaded;
	private File mImageFile;
	
	@Override
	protected void onCreate(final Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.image_viewer);
		loadImage(true);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mImageView = (ImageViewTouch) findViewById(R.id.image_viewer);
		mRefreshStopSaveButton = (ImageButton) findViewById(R.id.refresh_stop_save);
		mProgress = findViewById(R.id.progress);
	}
	
	private void loadImage(final boolean init) {
		getSupportLoaderManager().destroyLoader(0);
		final Uri uri = getIntent().getData();
		if (uri == null) {
			finish();
			return;
		}
		mImageView.setImageBitmap(null);
		final Bundle args = new Bundle();
		args.putParcelable(INTENT_KEY_URI, uri);
		if (init) {
			getSupportLoaderManager().initLoader(0, args, this);
		} else {
			getSupportLoaderManager().restartLoader(0, args, this);
		}
	}
	

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> loader, Bitmap args) {
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> loader) {
	}
	
	
	
}
