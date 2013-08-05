package com.shandagames.android.fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shandagames.android.R;
import com.shandagames.android.app.BaseFragment;

public class HomeFragment extends BaseFragment {

	private ViewPager viewPager;
	private PagerTabStrip strip;

	private String[] titles = {"首页", "类别", "热门免费", "免费新品"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_main, null);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		strip = (PagerTabStrip) view.findViewById(R.id.strip);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		strip.setDrawFullUnderline(false);
		strip.setTabIndicatorColor(Color.DKGRAY);
		strip.setBackgroundColor(Color.GRAY);
		strip.setNonPrimaryAlpha(0.5f);
		strip.setTextSpacing(15);
		strip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		
		// Note: 此处使用ViewPager，只能使用getChildFragmentManager()，否则弹栈将出现问题
		// http://stackoverflow.com/questions/7338823/viewpager-recursive-entry-to-executependingtransactions
		viewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
		viewPager.setOnPageChangeListener(new HomePageChangeListener());
		viewPager.setPageTransformer(false, new HomePageTransformer());
	}

	@Override
	public boolean isBackStack() {
		return false;
	}
	
	class HomePagerAdapter extends FragmentStatePagerAdapter {
		private FragmentManager fm;
		
		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		@Override
		public Fragment getItem(int position) {
			PlanetFragment fragment = new PlanetFragment();
			Bundle bundle = new Bundle();
			bundle.putString(PlanetFragment.ARG_PLANET_EXTRAS, titles[position]);
			fragment.setArguments(bundle);
			fm.beginTransaction().commitAllowingStateLoss();
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}
	
	class HomePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int position) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
		}
		
	}
	
	class HomePageTransformer implements PageTransformer {

		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public void transformPage(View page, float position) {
			 page.setRotationY(position * -30);
			 //final float normalizedposition = Math.abs(Math.abs(position) - 1);
			 //page.setAlpha(normalizedposition);
		}
		
	}
}
