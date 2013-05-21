package com.shandagames.android.fragment;

import java.util.Observable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.shandagames.android.PhotoViewerActivity;
import com.shandagames.android.R;
import com.shandagames.android.cache.core.RemoteResourceManager;
import com.shandagames.android.cache.core.RemoteResourceManager.ResourceRequestObserver;
import com.shandagames.android.photoview.PhotoView;
import com.shandagames.android.util.ImageHelper;

/**
 * @file ImageFragment.java
 * @create 2013-5-20 上午11:19:49
 * @author lilong
 * @description TODO 图片查看器
 */
public class ImageFragment extends Fragment {
	private static final String FRAGMENT_EXTRA_URL = "FRAGMENT_EXTRA_URL";
	
	private String imageUrl = "";
	private ImageView imageView;
	private RemoteResourceManager mRrm;
	
	public static ImageFragment newInstance(String imageUri) {
		ImageFragment imageFragment = new ImageFragment();
		Bundle bundle = new Bundle();
		bundle.putString(FRAGMENT_EXTRA_URL, imageUri);
		// 参数在创建期间被保留
		imageFragment.setArguments(bundle);
		return imageFragment;
	}
	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FRAGMENT_EXTRA_URL)) {
        	imageUrl = bundle.getString(FRAGMENT_EXTRA_URL);
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imageView = new PhotoView(getActivity());
		return imageView;
	}

	public ImageView getImageView() {
		return imageView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (PhotoViewerActivity.class.isInstance(getActivity())) {
        	final PhotoViewerActivity act = (PhotoViewerActivity) getActivity();
        	//act.showProgress();
        	mRrm = act.getResourceManager();
        	mRrm.addObserver(new ResourceRequestObserver(imageUrl) {
        		@Override
				public void requestReceived(Observable observable, final String uri) {
					observable.deleteObserver(this);
					act.removePhoto(uri.toString());
					act.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (mRrm.exists(uri)) {
					    		Bitmap bitmap = ImageHelper.decodeFile(mRrm.getFile(uri));
					    		imageView.setImageBitmap(bitmap);
							}
						}
					});
        		}
			});
        	setViewPhoto(imageUrl);
        }
	}

	private void setViewPhoto(String imageUri) {
    	if (mRrm.exists(imageUri)) {
    		Bitmap bitmap = ImageHelper.decodeFile(mRrm.getFile(imageUri));
    		imageView.setImageBitmap(bitmap);
    		//((PhotoViewerActivity)getActivity()).hideProgress();
		} else {
			imageView.setImageResource(R.drawable.bg_default_pic);
			((PhotoViewerActivity)getActivity()).updatePhoto(imageUrl);
			mRrm.request(imageUri);
		}
	}
}
