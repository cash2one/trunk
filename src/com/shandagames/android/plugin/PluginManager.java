package com.shandagames.android.plugin;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.shandagames.android.plugin.bean.Plugin;
import com.shandagames.android.plugin.bean.PluginDescription;
import com.shandagames.android.plugin.bean.PluginFeature;
import com.shandagames.android.plugin.bean.PluginFeatureMethod;
import android.util.Xml;

/**
 * @file PluginManager.java
 * @create 2013-3-25 上午10:55:57
 * @author Jacky.Lee
 * @description TODO 插件管理封装类
 */
public final class PluginManager {

	private static final String _FEATURE = "feature";
	private static final String _METHOD = "method";
	private static final String _DESCRIPTION = "description";
	private static final String _ICON = "icon";
	private static final String _TITLE = "title";
 	private static final String _NEEDCTX = "if-need-context";
 	private static final String _NAME = "name";
	
	/** 从配置文件中解析插件资源  */
	static public Plugin XMLParse(InputStream is) throws XmlPullParserException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		return XMLParse(parser);
	}
	
	/** 解析插件配置文件  */
	static public Plugin XMLParse(XmlPullParser parser) {
		Plugin plugin = new Plugin();
		PluginFeature feature = null;
		PluginDescription description = null;
		PluginFeatureMethod method = null;
		
		try {
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String strNode = parser.getName();
				if (eventType == XmlPullParser.START_TAG) {
					 if (strNode.equals(_FEATURE))  {
						feature = new PluginFeature();
						feature.setFeatureName(parser.getAttributeValue(null, _NAME));
					} else if (strNode.equals(_DESCRIPTION)) {
						description = new PluginDescription();
						description.setIcon(parser.getAttributeValue(null, _ICON));
						description.setTitle(parser.getAttributeValue(null, _TITLE));
						description.setDescription(parser.nextText());
					} else if (strNode.equals(_METHOD)) {
						method = new PluginFeatureMethod();
						method.setMethodName(parser.getAttributeValue(null, _NAME));
						String ifNeedContext = parser.getAttributeValue(null, _NEEDCTX).trim();
						if (Boolean.parseBoolean(ifNeedContext)) {
							method.setNeedContext(true);
						} else {
							method.setNeedContext(false);
						}
						method.setDescription(parser.nextText());
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (strNode.equals(_METHOD)) {
						feature.addMethod(method);
					} else if (strNode.equals(_DESCRIPTION)) {
						feature.setPluginDesc(description);
					} else if (strNode.equals(_FEATURE)) {
						plugin.addFeature(feature);
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return plugin;
	}
	
}
