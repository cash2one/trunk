package com.shandagames.android.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @file AbstractParser.java
 * @create 2012-10-11 下午12:18:11
 * @author lilong
 * @description TODO
 */
public abstract class AbstractParser<T extends ResultType> implements Parser<T> {

	/**
	 * All derived parsers must implement parsing a JSONObject instance of themselves.
	 */
	public abstract T parse(JSONObject json) throws JSONException;

	/**
	 * Only the GroupParser needs to implement this.
	 */
	public Group<?> parse(JSONArray array) throws JSONException {
		throw new JSONException("Unexpected JSONArray parse type encountered.");
	}
}