package com.shandagames.android.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * @file GroupParser.java
 * @create 2012-9-20 下午1:35:07
 * @author lilong
 * @description 解析Json字符串
 */
public class GroupParser extends AbstractParser<Group<ResultType>> {

	private Parser<? extends ResultType> mSubParser;

	public GroupParser(Parser<? extends ResultType> subParser) {
		mSubParser = subParser;
	}

	/**
	 * When we encounter a JSONObject in a GroupParser, we expect one attribute
	 * named 'type', and then another JSONArray attribute.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Group<ResultType> parse(JSONObject json) throws JSONException {
		Group<ResultType> group = new Group<ResultType>();
		Iterator<String> it = (Iterator<String>) json.keys();
		while (it.hasNext()) {
			String key = it.next();
			if (key.equals("count")) {
				group.setCount(json.optInt(key));
			} else {
				Object obj = json.get(key);
				if (obj instanceof JSONArray) {
					parse(group, (JSONArray) obj);
				}
			}
		}
		return group;
	}

	/**
	 * Here we are getting a straight JSONArray and do not expect the 'type' attribute.
	 */
	@Override
	public Group<ResultType> parse(JSONArray array) throws JSONException {
		Group<ResultType> group = new Group<ResultType>();
		parse(group, array);
		return group;
	}

	private void parse(Group<ResultType> group, JSONArray array)
			throws JSONException {
		for (int i = 0, m = array.length(); i < m; i++) {
			Object element = array.get(i);
			ResultType item = null;
			if (element instanceof JSONArray) {
				item = mSubParser.parse((JSONArray) element);
			} else {
				item = mSubParser.parse((JSONObject) element);
			}
			if (item != null)  group.add(item);
		}
	}
}
