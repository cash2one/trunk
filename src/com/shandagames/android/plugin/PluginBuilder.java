package com.shandagames.android.plugin;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.shandagames.android.plugin.bean.Plugin;

/**
 * @file PluginBuilder.java
 * @create 2013-3-25 下午02:00:05
 * @author Jacky.Lee
 * @description TODO 构建插件组成部分
 */
public final class PluginBuilder {

	private Context context;
	private String xmlName = "plugins";

	public PluginBuilder(Context context) {
		this.context = context;
	}
	
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	
	public List<Plugin> buildPluginsDescrition(List<Plugin> plugins) {
		List<Plugin> richPlugin = new ArrayList<Plugin>();

		for (Plugin plugin : plugins) {
			richPlugin.add(loadPluginFeatureByXML(plugin));
		}
		return richPlugin;
	}

	private Plugin loadPluginFeatureByXML(Plugin plugin) {
		try {
			String packageName = plugin.getPkgInfo().packageName;
			Context targetContext = this.context.createPackageContext(packageName, Context.CONTEXT_INCLUDE_CODE);

			int resId = targetContext.getResources().getIdentifier(this.xmlName, "xml", packageName);
			if (resId == 0) 
				throw new IllegalArgumentException("ERROR: plugin.xml is missing.  Add res/xml/plugins.xml to your project.");
			
			XmlPullParser parser = targetContext.getResources().getXml(resId);
			Plugin pluginFinal = PluginManager.XMLParse(parser);
			pluginFinal.setPkgInfo(plugin.getPkgInfo());
			pluginFinal.setPluginLable(plugin.getPluginLable());
			pluginFinal.setContext(targetContext);
			return pluginFinal;
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	
}
