package com.shandagames.android.constant;

import java.util.UUID;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @file PreferenceSettings.java
 * @create 2013-3-26 下午02:35:48
 * @author lilong
 * @description TODO 数据配置存储
 */
public final class PreferenceSettings {

	private static final String PREFERENCE_IS_FIRST_RUN = "is_first_run";
	private static final String PREFERENCE_CREDENTIAL_TOKEN = "token";
	private static final String PREFERENCE_UNIQUE_IDENTIFIER = "unique_identifier";
	
	public static final String PREFERENCE_USERNAME = "userName";
	public static final String PREFERENCE_PASSWORD = "password";
	
	
	private PreferenceSettings() {
	}
	
	// Check if this is a new install client.
	public static boolean isFisrtRun(SharedPreferences preferences) {
		return preferences.getBoolean(PREFERENCE_IS_FIRST_RUN, false);
	}

	// Sign current user is first run client.
	public static void storeIsFirstRun(SharedPreferences preferences) {
		Editor editor = preferences.edit();
		editor.putBoolean(PREFERENCE_IS_FIRST_RUN, true);
		editor.commit();
	}

	public static String createUniqueId(SharedPreferences preferences) {
		String uniqueId = preferences.getString(PREFERENCE_UNIQUE_IDENTIFIER,
				null);
		if (uniqueId == null) {
			uniqueId = UUID.randomUUID().toString();
			Editor editor = preferences.edit();
			editor.putString(PREFERENCE_UNIQUE_IDENTIFIER, uniqueId);
			editor.commit();
		}
		return uniqueId;
	}

	public static String getUserToken(SharedPreferences preferences) {
		return preferences.getString(PREFERENCE_CREDENTIAL_TOKEN, null);
	}

	public static void storeUserToken(SharedPreferences preferences,
			String mToken) {
		if (mToken != null) {
			Editor editor = preferences.edit();
			editor.putString(PREFERENCE_CREDENTIAL_TOKEN, mToken);
			editor.commit();
		}
	}

	public static void storeLoginAndPassword(SharedPreferences preferences, 
			String userName, String password) {
		Editor editor = preferences.edit();
		editor.putString(PREFERENCE_USERNAME, userName);
		editor.putString(PREFERENCE_PASSWORD, password);
		editor.commit();
	}
}
