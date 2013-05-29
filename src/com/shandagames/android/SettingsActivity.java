/**
 * 
 */
package com.shandagames.android;

import com.shandagames.android.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @file SettingsActivity.java
 * @create 2012-8-27 下午4:39:13
 * @author lilong
 * @description TODO
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 加载XML设定文件(XML preferences file)
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 每当键发生变化注册一个监听事件
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// 注销onResume()方法中设置的监听.
		// 注销监听最好是当你的程序不使用它们以减少系统不必要的开销， 你可以在onPause()方法中做这件事
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	// 当用户改变设置的选项，onSharedPreferenceChanged()重新启动主activity作为一个新任务，
	// 设置refreshDisplay为true以表明主activity应该更新它的显示，
	// 主activity查询PreferenceManager得到最新的设置.
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// 设置refreshDisplay为true，这样当用户返回主activity，显示会根据新设置进行刷新.
		System.out.println("onSharedPreferenceChanged::"+key);
	}
}
