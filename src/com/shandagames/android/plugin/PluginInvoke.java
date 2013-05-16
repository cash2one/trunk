package com.shandagames.android.plugin;

import java.lang.reflect.Method;
import com.shandagames.android.plugin.bean.Plugin;
import com.shandagames.android.plugin.bean.PluginFeature;
import com.shandagames.android.plugin.bean.PluginFeatureMethod;
import android.content.Context;

public class PluginInvoke {
	private Context context;

	public PluginInvoke(Context context) {
		this.context = context;
	}

	public void invoke(Plugin plugin, PluginFeature feature,
			PluginFeatureMethod method) {
		try {
			Context targetContext = this.context.createPackageContext(
					plugin.getPkgInfo().packageName,
					Context.CONTEXT_INCLUDE_CODE);

			Class<?> clazz = targetContext.getClassLoader().loadClass(
					feature.getFeatureName());
			Object pluginActivity = clazz.newInstance();

			if (method.needContext()) {
				Method target = clazz.getMethod(method.getMethodName(),
						new Class[] { Context.class });
				target.invoke(pluginActivity, new Object[] { this.context });
			} else {
				Method target = clazz.getMethod(method.getMethodName(),
						new Class[0]);
				target.invoke(pluginActivity, new Object[0]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
