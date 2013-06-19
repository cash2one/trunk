/**
 * 
 */
package com.shandagames.android;

import java.util.Arrays;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.app.BaseGroupAdapter;
import com.shandagames.android.fragment.DetailFragment;
import com.shandagames.android.R;
import com.shandagames.android.slidingmenu.lib.SlidingMenu;
import com.shandagames.android.support.DisplaySupport;

/**
 * @file SlideMenuActivity.java
 * @create 2012-9-4 下午5:07:35
 * @author lilong
 * @description TODO
 */
public class SlideMenuActivity extends BaseActivity implements View.OnClickListener {

	private SlidingMenu mSlideMenu;
	private ListView listView;
	private FragmentManager mFragmentManager;
	private FragmentTransaction trans;
	private String[] mResData;

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mSlideMenu = new SlidingMenu(this);
		setContentView(mSlideMenu);
		
		mSlideMenu.setBehindScrollScale(0.0f);
		mSlideMenu.setBehindWidth(DisplaySupport.dip2px(this, 200));
		mSlideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlideMenu.setContent(R.layout.nav_slide_content);
		mSlideMenu.setMenu(R.layout.nav_slide_menu);

		mFragmentManager = getSupportFragmentManager();
		listView = (ListView) findViewById(android.R.id.list);
		mResData = getResources().getStringArray(R.array.simple_menu_list);
		
		NavAdapter mAdapter = new NavAdapter(this);
		mAdapter.addAll(Arrays.asList(mResData), false);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	private void selectItem(int position) {
		trans = mFragmentManager.beginTransaction();
		trans.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		trans.replace(R.id.placeholder, DetailFragment.newInstance(mResData[position]));
		trans.commit();
		
		listView.setItemChecked(position, true);
		mSlideMenu.toggle(true);
	}
	
	@Override
	protected void addNavagationAndStatus() {
		((TextView)findViewById(R.id.title)).setText(getTitle());
		findViewById(R.id.title_btn_home).setOnClickListener(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlideMenu.isMenuShowing()) {
			mSlideMenu.showContent();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_btn_home:
			mSlideMenu.toggle();
			break;
		}
	}

	private class NavAdapter extends BaseGroupAdapter<String> {
		private int index;
		
		public NavAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View newView(LayoutInflater inflater, View v, int position) {
			// TODO Auto-generated method stub
			index = position;
			v = inflater.inflate(android.R.layout.simple_list_item_1, null);
			v.setBackgroundResource(R.drawable.nav_slide_tab_bg);
			v.setPadding(50, 0, 0, 0);
			return v;
		}
		
		@Override
		public void bindView(View v, String item) {
			// TODO Auto-generated method stub
			if (index == 0) {
				v.setSelected(true);
			}
			((TextView)v).setText(item);
		}
	}
	
}
