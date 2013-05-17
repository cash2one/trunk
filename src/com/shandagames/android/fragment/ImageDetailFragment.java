package com.shandagames.android.fragment;

import com.shandagames.android.StartActivity;
import com.shandagames.android.base.BaseFragment;
import com.shandagames.android.cache.lib.ImageFetcher;
import com.shandagames.android.cache.lib.Images;
import com.shandagames.android.photoview.PhotoView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @file ImageDetailFragment.java
 * @create 2013-5-7 下午04:02:28
 * @author lilong
 * @description TODO
 */
public class ImageDetailFragment extends BaseFragment {
	
	public static final String KEY_CONTENT = "android:key";
	
    private int mImageNum;
    
    private String imageUrl;
    
    private ImageView mImageView;
    
    public static ImageDetailFragment newInstance(int imageNum) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putInt(KEY_CONTENT, imageNum);
        f.setArguments(args);

        return f;
    }
    
    public ImageDetailFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	mImageNum = savedInstanceState.getInt(KEY_CONTENT);
        }
        mImageNum = getArguments()!=null ? getArguments().getInt(KEY_CONTENT) : -1;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mImageView = new PhotoView(getActivity());
		mImageView.setScaleType(ScaleType.FIT_XY);
		return mImageView;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageUrl = Images.imageUrls[mImageNum];
        
        if (StartActivity.class.isInstance(getActivity())) {
        	ImageFetcher mImageFetcher = ((StartActivity)getActivity()).getImageFetcher();
        	mImageFetcher.loadImage(Images.imageUrls[mImageNum], mImageView);
        }
        // Pass clicks on the ImageView to the parent activity to handle
        if (OnClickListener.class.isInstance(getActivity())) {
            mImageView.setOnClickListener((OnClickListener) getActivity());
        }
    }
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, mImageNum);
    }
	
	public void cancelWork() {
		ImageFetcher.cancelWork(mImageView);
        mImageView.setImageDrawable(null);
        mImageView = null;
    }
}
