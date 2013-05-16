package com.shandagames.android.plugin;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.shandagames.android.plugin.bean.Plugin;

/**
 * @file PluginSearch.java
 * @create 2013-3-25 上午11:55:57
 * @author lilong
 * @description TODO 查找插件
 */
public class PluginSearch {

	public List<Plugin> getPlugins(Context context) {
		List<Plugin> plugins = new ArrayList<Plugin>();
		try {
			PackageManager pkgManager = context.getPackageManager();
			String pkgName = context.getPackageName();
			String sharedUID = pkgManager.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES).sharedUserId;

			List<PackageInfo> pkgs = pkgManager
					.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			for (PackageInfo pkg : pkgs) {
				if ((!sharedUID.equals(pkg.sharedUserId))
						|| (pkgName.equals(pkg.packageName))) {
					continue;
				}
				Plugin plugin = new Plugin();
				plugin.setPkgInfo(pkg);
				plugin.setPluginLable(pkgManager.getApplicationLabel(
						pkg.applicationInfo).toString());
				plugins.add(plugin);
			}
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
		return plugins;
	}
}
