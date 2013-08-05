package com.shandagames.android;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.shandagames.android.app.AndroidApplication;
import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.cache.core.RemoteResourceManager;
import com.shandagames.android.cache.lib.Images;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.fragment.ImageFragment;
import com.shandagames.android.photoview.PhotoView;
import com.shandagames.android.util.ImageHelper;

/**
 * @file PhotoViewerActivity.java
 * @create 2013-5-20 上午11:11:11
 * @author lilong
 * @description TODO
 */
public class PhotoViewerActivity extends BaseActivity implements View.OnClickListener {
	
	public static final String PHOTO_VIEWER_EXTRAS = "PHOTO_VIEWER_EXTRAS";
	
	private List<String> imageUrls;
	private RemoteResourceManager mRrm;
	private Set<String> mLaunchedPhotoFetches;
	private SparseArray<ImageFragment> registeredFragments;
	
	private TextView mPageShow;
	private ViewPager mViewPager;
	private ProgressBar progressBar;
	private ZoomControls mZoomButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_viewer);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		
		ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
		backBtn.setOnClickListener(this);
		Button saveBtn = (Button) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		mPageShow = (TextView) findViewById(R.id.tv_page);

		ImageButton leftRotateBtn=(ImageButton)findViewById(R.id.left_rotate_btn);
		leftRotateBtn.setOnClickListener(this);
		ImageButton rightRotateBtn=(ImageButton)findViewById(R.id.right_rotate_btn);
		rightRotateBtn.setOnClickListener(this);
		
		mZoomButtons = (ZoomControls) findViewById(R.id.zoomButtons);
		mZoomButtons.setZoomSpeed(100);
		mZoomButtons.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCurrentImageView().zoomIn();
				updateZoomButtonsEnabled();
			}
		});
		mZoomButtons.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCurrentImageView().zoomOut();
				updateZoomButtonsEnabled();
			}
		});
		
		if (getIntent().getStringArrayListExtra(PHOTO_VIEWER_EXTRAS) != null) {
			imageUrls = getIntent().getStringArrayListExtra(PHOTO_VIEWER_EXTRAS);
		} else {
			imageUrls = Arrays.asList(Images.imageUrls);
		}
		
		mLaunchedPhotoFetches = new HashSet<String>();
		registeredFragments = new SparseArray<ImageFragment>();
		mRrm = ((AndroidApplication)getApplication()).getRemoteResourceManager();
		ImagePagerAdapter imageAdapter = new ImagePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(imageAdapter);
		mZoomButtons.setIsZoomOutEnabled(false);
	}

	private void updateZoomButtonsEnabled() {
		PhotoView imageView = getCurrentImageView();
		if (imageView != null) {
			mZoomButtons.setIsZoomInEnabled(imageView.IsZoomInEnabled());
			mZoomButtons.setIsZoomOutEnabled(imageView.IsZoomOutEnabled());
		}
	}
	
	private PhotoView getCurrentImageView() {
		ImageFragment fragment = registeredFragments.get(mViewPager.getCurrentItem());
		return (PhotoView)fragment.getImageView();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
		case R.id.save_btn:
			String fileName = System.currentTimeMillis() +".jpg";
			File storeDir = Constants.CAMERA_IMAGE_BUCKET_DIR;
			File destFile = new File(storeDir, fileName);
			boolean success = true;
			if (!storeDir.exists()) {
				success = storeDir.mkdirs();
			}
			if (success) {
				PhotoView imageView = getCurrentImageView();
				Bitmap bitmap = ImageHelper.drawable2Bitmap(imageView.getDrawable());
				ImageHelper.saveBitmapAs(bitmap, destFile.getAbsolutePath());
				
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(destFile);
				mediaScanIntent.setData(contentUri);
				sendBroadcast(mediaScanIntent);
				Toast.makeText(getBaseContext(), "照片成功保存到系统相册", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.left_rotate_btn:
			PhotoView imageView = getCurrentImageView();
			Bitmap bitmap = ImageHelper.drawable2Bitmap(imageView.getDrawable());
			if(bitmap!=null){
				Bitmap result=ImageHelper.rotateBitmap(bitmap, -90);
				imageView.setImageBitmap(result);
				imageView.update();
				updateZoomButtonsEnabled();
			}
			break;
		case R.id.right_rotate_btn:
			imageView = getCurrentImageView();
			bitmap = ImageHelper.drawable2Bitmap(imageView.getDrawable());
			if(bitmap!=null){
				Bitmap result=ImageHelper.rotateBitmap(bitmap, 90);
				imageView.setImageBitmap(result);
				imageView.update();
				updateZoomButtonsEnabled();
			}
			break;
		}
	}

	public void updateShow(int index) {
		String str = "%d/%d";
		mPageShow.setText(String.format(str, index, imageUrls.size()));
	}
	
	public void updatePhoto(String url) {
		if (!mLaunchedPhotoFetches.contains(url)) {
			mLaunchedPhotoFetches.add(url);
		}
	}
	
	public void removePhoto(String url) {
		if (mLaunchedPhotoFetches.contains(url)) {
			mLaunchedPhotoFetches.remove(url);
		}
	}
	
	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}
	
	public RemoteResourceManager getResourceManager() {
		return mRrm;
	}
	
	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			updateShow(position);
			return ImageFragment.newInstance(imageUrls.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageFragment fragment = (ImageFragment)super.instantiateItem(container, position);
			registeredFragments.put(position, fragment);
			return fragment;
		}
		
		@Override
		public int getCount() {
			if (imageUrls==null) {
				return 0;
			} else {
				return imageUrls.size();
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			registeredFragments.remove(position);
		}
	}
}
