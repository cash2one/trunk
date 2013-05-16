package com.shandagames.android.plugin;

import java.util.List;
import com.shandagames.android.plugin.bean.Plugin;
import android.content.Context;

public class PluginOperation {

	static public void performSync(Context context, String xmlName) {
		// 查找插件
		PluginSearch psearch = new PluginSearch();
		// 第一次获得的是简要的插件描述
		List<Plugin> plugins = psearch.getPlugins(context);
		// 然后将插件再组装一下
		PluginBuilder pbuilder = new PluginBuilder(context);
		if (xmlName != null) pbuilder.setXmlName(xmlName);
		// 将用户所定义插件描述融合进去
		plugins = pbuilder.buildPluginsDescrition(plugins);

	}

}
