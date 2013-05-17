package com.shandagames.android;

import com.shandagames.android.base.AndroidApplication;
import com.shandagames.android.base.BaseActivity;
import com.shandagames.android.cache.lib.ImageFetcher;
import com.shandagames.android.cache.lib.Images;
import com.shandagames.android.fragment.ImageDetailFragment;
import com.shandagames.android.log.Log;
import com.shandagames.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * @file StartActivity.java
 * @create 2013-5-7 下午03:58:36
 * @author lilong
 * @description TODO
 */
public class StartActivity extends BaseActivity  implements OnClickListener {

	private ViewPager viewPager;
	private ImageFetcher mImageFetcher;
	private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	
	@Override
	protected void _onCreate(Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_user_guide);
		
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mImageFetcher = ((AndroidApplication)getApplication()).getImageFetcher(this);
		mImageFetcher.setImageFadeIn(false);
		mImageFetcher.setImageSize(dm.widthPixels, dm.heightPixels);
		mImageFetcher.setLoadingImage(R.drawable.bg_default_pic);
        
        viewPager = (ViewPager)findViewById(R.id.pager);
		StartFragmentAdapter mAdapter = new StartFragmentAdapter(getSupportFragmentManager(), Images.imageUrls.length);
		viewPager.setAdapter(mAdapter);
	}
	
	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}
	
	@Override
    public void onPause() {
		super.onPause();
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registeredFragments.clear();
		registeredFragments=null;
        mImageFetcher.closeCache();
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment fragment = registeredFragments.get(viewPager.getCurrentItem());
		Log.d("onClick >>> " + ((ImageDetailFragment)fragment).getImageUrl());
		finish();
	}
	
	class StartFragmentAdapter extends FragmentStatePagerAdapter {
		private final int mSize;
		
	    public StartFragmentAdapter(FragmentManager fm, int size) {
	        super(fm);
	        this.mSize = size;
	    }

	    @Override
	    public int getCount() {
	    	return mSize; //代表页数
	    }
	    
	    @Override
	    public Fragment getItem(int position) {
	    	Fragment fragment = ImageDetailFragment.newInstance(position);
	    	registeredFragments.put(position, fragment);
	    	return fragment;
	    }

	    @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            final ImageDetailFragment fragment = (ImageDetailFragment) object;
            // As the item gets destroyed we try and cancel any existing work.
            fragment.cancelWork();
            //移除集合元素
            registeredFragments.remove(position);
            //注意这里destroy的是Fragment的视图层次，并不是destroy Fragment对象;
            super.destroyItem(container, position, object);
        } 
	}

}
