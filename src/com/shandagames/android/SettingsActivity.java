/**
 * 
 */
package com.shandagames.android;

import com.shandagames.android.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @file SettingsActivity.java
 * @create 2012-8-27 下午4:39:13
 * @author lilong
 * @description TODO
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

}
