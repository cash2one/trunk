package com.shandagames.android.util;

import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * 分装常用的快捷方式操作
 * @file ShortCutUtils.java
 * @create 2013-9-18 下午04:22:43
 * @author lilong
 * @description TODO
 */
public class ShortcutUtils {

	private ShortcutUtils() {
	}

	/**
	 * 添加快捷方式
	 * @param context 上下文对象
	 * @param appName 应用名称
	 * @param recourceId 资源图标
	 * @param intent 目标intent
	 */
	public static void addShortcut(Context context, String appName, int recourceId, Class<?> cls) {
		if (!hasShortCut(context, appName)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClass(context, cls);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			
			Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			// Shortcut name
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
			shortcut.putExtra("duplicate", false); // Just create once
			// Setup current activity shoud be shortcut object
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			// Set shortcut icon
			ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, recourceId);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			// Set shortcut name
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
			// Send broadcast
			context.sendBroadcast(shortcut);
		}
	}

	/**
	 * 判断是否存在快捷方式
	 * @param context 上下文对象
	 * @param appName 应用名称
	 * @return
	 */
	private static boolean hasShortCut(Context context, String appName) {
		try {
			String authority = getAuthorityFromPermission(context, "READ_SETTINGS");
			if (authority == null) {
				authority = getAuthorityFromPermission(context, "WRITE_SETTINGS");
			}
			
			Log.e("hasShortCut", "authority:" + authority);
			if (authority == null) return true;
			
			final Uri CONTENT_URI = Uri.parse("content://" + authority + "/favorites?notify=true");
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(CONTENT_URI, null, "title=?",
					new String[] { appName }, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/*	注意：不同的ROM对应不同的权限，注意设备适配问题
	 	<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
	    <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
	    <uses-permission android:name="com.android.launcher.permission.WRITE_SETTINGS" />
	    <uses-permission android:name="com.huawei.launcher3.permission.READ_SETTINGS" />
	    <uses-permission android:name="com.huawei.launcher3.permission.WRITE_SETTINGS" />
	    <uses-permission android:name="com.htc.launcher.permission.READ_SETTINGS"/>
	    <uses-permission android:name="com.htc.launcher.permission.WRITE_SETTINGS"/>
	    <uses-permission android:name="com.fede.launcher.permission.READ_SETTINGS" />
	    <uses-permission android:name="com.fede.launcher.permission.WRITE_SETTINGS" />
	*/
	private static String getAuthorityFromPermission(Context context, String permission){
		if (permission == null) return null;  
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);   
		if (packs != null) {  
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers; 
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (provider.readPermission != null) {
							if ((provider.readPermission).contains(permission)){
								return provider.authority;
							}
						}
						if (provider.writePermission != null) {
							if ((provider.writePermission).contains(permission)){
								return provider.authority;
							}
						}
					}					
				}
			}
		}
		return null;
	}
}
