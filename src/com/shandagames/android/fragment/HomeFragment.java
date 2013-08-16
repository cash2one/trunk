package com.shandagames.android.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.ShareActionProvider;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.shandagames.android.R;
import com.shandagames.android.app.BaseFragment;
import com.shandagames.android.util.ToastUtil;

public class HomeFragment extends BaseFragment {

	private ViewPager viewPager;
	private PagerTabStrip strip;

	private String[] titles = { "首页", "类别", "热门免费", "免费新品" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

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
		viewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
		viewPager.setOnPageChangeListener(new HomePageChangeListener());
		viewPager.setPageTransformer(false, new HomePageTransformer());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.action_bar_main_menu, menu);
		
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
		searchView.setQueryHint("请输入文字");
		searchView.setOnQueryTextListener(new SearchQueryTextListener());
		
		ShareActionProvider shareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_share));
		shareProvider.setShareIntent(new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, "Text I want to share"));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean isBackStack() {
		return false;
	}

	@Override
	public boolean isCleanStack() {
		// 清空栈中Fragment
		return true;
	}
	
	class HomePagerAdapter extends FragmentStatePagerAdapter {

		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PlanetFragment.newInstance(titles[position]);
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
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
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
			// final float normalizedposition = Math.abs(Math.abs(position) - 1);
			// page.setAlpha(normalizedposition);
		}

	}
	
	class SearchQueryTextListener implements OnQueryTextListener {

		@Override
		public boolean onQueryTextChange(String newText) {
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			ToastUtil.showMessage(getActivity(), "Searching for: " + query + "...");
			return true;
		}
		
	}

	public static class SettingsActionProvider extends ActionProvider {
		/** An intent for launching the system settings. */
		private static final Intent sSettingsIntent = new Intent(Settings.ACTION_SETTINGS);
		
		public SettingsActionProvider(Context context) {
			super(context);
		}

		@Override
		public View onCreateActionView() {
			TextView txtView = new TextView(getContext());
			txtView.setText("setting");
			return txtView;
		}
		
		@Override
        public boolean onPerformDefaultAction() {
			getContext().startActivity(sSettingsIntent);
			return true;
		}
	}
}
