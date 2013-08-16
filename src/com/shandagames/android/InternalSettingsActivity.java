/**
 * 
 */
package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.util.ToastUtil;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * @file SettingsActivity.java
 * @create 2012-8-27 下午4:39:13
 * @author lilong
 * @description TODO
 */
@SuppressWarnings("deprecation")
public class InternalSettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {

	public static final String SHARED_PREFERENCES_NAME = "preferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		addPreferencesFromResource(R.xml.settings);
		getPreferenceManager()
				.setSharedPreferencesName(SHARED_PREFERENCES_NAME);

		findPreference("text_preference").setOnPreferenceClickListener(this);
		findPreference("checkbox_preference").setOnPreferenceClickListener(this);
		
		// 1. 先调用onPreferenceClick()方法，如果该方法返回true，则不再调用onPreferenceTreeClick方法 ，否则则继续调用onPreferenceTreeClick方法。
		// 2. onPreferenceChange的方法独立与其他两种方法的运行，也就是说，它总是会运行。点击某个Preference控件后，会先回调onPreferenceChange()方法；
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		ToastUtil.showMessage(this, preference.getTitle());
		return false;
	}

	// 当Preference的值发生改变时触发该事件，true则以新值更新控件的状态，false则do noting
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equals("checkbox_preference")) {
			ToastUtil.showMessage(this, String.valueOf(newValue));
			return false; // 不保存更新值
		}
		return false; // 保存更新后的值
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return false;
	}
}
