package com.shandagames.android.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @file Parser.java
 * @create 2012-9-24 下午4:22:16
 * @author lilong
 * @description TODO
 */
public interface Parser<T extends ResultType> {

	public T parse(JSONObject json) throws JSONException;

	public Group<?> parse(JSONArray array) throws JSONException;
}
