package com.shandagames.android.constant;

import java.util.UUID;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.shandagames.android.bean.User;
import com.shandagames.android.preferences.SettingUtils;

/**
 * @file PreferenceSettings.java
 * @create 2013-3-26 下午02:35:48
 * @author lilong
 * @description TODO 数据配置存储
 */
public final class PreferenceSettings {

	private static final String PREFERENCE_IS_FIRST_RUN = "is_first_run";
	private static final String PREFERENCE_UNIQUE_IDENTIFIER = "unique";
	private static final String PREFERENCE_CREDENTIAL_TOKEN = "token";

	public static final String PREFERENCE_USER_ID = "userId";
	public static final String PREFERENCE_USER_NAME = "userName";
	public static final String PREFERENCE_NICK_NAME = "nickName";
	public static final String PREFERENCE_USER_AVATAR = "photo";
	public static final String PREFERENCE_USER_GENDER = "gender";
	
	
	private PreferenceSettings() {
	}
	
	public static boolean isFisrtRun(Context context) {
		return SettingUtils.contains(context, PREFERENCE_IS_FIRST_RUN);
	}

	public static void storeIsFirstRun(Context context) {
		SettingUtils.set(context, PREFERENCE_IS_FIRST_RUN, true);
	}

	public static String createUniqueId(Context context) {
		String uniqueId = SettingUtils.get(context, PREFERENCE_UNIQUE_IDENTIFIER, null);
		if (uniqueId == null) {
			uniqueId = UUID.randomUUID().toString();
			SettingUtils.set(context, PREFERENCE_UNIQUE_IDENTIFIER, uniqueId);
		}
		return uniqueId;
	}

	public static void storeToken(Context context, String mToken) {
		SettingUtils.set(context, PREFERENCE_CREDENTIAL_TOKEN, mToken);
	}

	public static String getToken(Context context) {
		return SettingUtils.get(context, PREFERENCE_CREDENTIAL_TOKEN, null);
	}
	
	public static void storeUser(Context context, User user) {
		Editor editor = SettingUtils.getEditor(context);
		editor.putLong(PREFERENCE_USER_ID, user.getId());
		editor.putString(PREFERENCE_USER_NAME, user.getUserName());
		editor.putString(PREFERENCE_NICK_NAME, user.getNickName());
		editor.putString(PREFERENCE_USER_AVATAR, user.getPhoto());
		editor.putString(PREFERENCE_USER_GENDER, user.getGender());
		SettingUtils.commitOrApply(editor);
	}
	
	public static User getUser(Context context) {
		User user = new User();
		user.setId(SettingUtils.get(context, PREFERENCE_USER_ID, 0L));
		user.setUserName(SettingUtils.get(context, PREFERENCE_USER_NAME, null));
		user.setNickName(SettingUtils.get(context, PREFERENCE_NICK_NAME, null));
		user.setPhoto(SettingUtils.get(context, PREFERENCE_USER_AVATAR, null));
		user.setGender(SettingUtils.get(context, PREFERENCE_USER_GENDER, null));
		return user;
	}
	
	/** 清空所有存储值   */
	public static void clearAll(Context context) {
		Editor editor = SettingUtils.getEditor(context);
		editor.clear();
		SettingUtils.commitOrApply(editor);
	}
}
