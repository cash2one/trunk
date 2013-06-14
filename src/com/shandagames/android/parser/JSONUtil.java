/**
 * 
 */
package com.shandagames.android.parser;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.shandagames.android.parser.Result.Meta;

/**
 * @file JsonHelper.java
 * @create 2012-9-20 下午2:32:26
 * @author lilong
 * @description TODO
 */
public final class JSONUtil {
	
	/**
	 * Takes a parser, a json string, and returns a result.
	 * 
	 * @param parser   the parser object
	 * @param content  The content of the resolution
	 * @param DEBUG    if as a debug version, it will print error log.
	 */
	@SuppressWarnings("unchecked")
	public static Result consume(Parser<? extends ResultType> parser, String content, boolean DEBUG) {
		Result result = new Result();
		Meta meta = new Meta();
		try {
			JSONObject json = new JSONObject(content);
			if (json.has("notification")) {
				JSONObject obj = json.getJSONObject("notification");
				meta.setNotification(obj.optString("message"));
			}
			if (json.has("meta")) {
				JSONObject obj = json.getJSONObject("meta");
				meta.setCode(obj.optInt("code"));
				meta.setErrorType(obj.optString("errorType"));
				meta.setErrorDetail(obj.optString("errorDetail"));
			}
			result.setMeta(meta);

			if (json.has("results")) {
				Object element = json.get("results");
				if (element instanceof JSONArray) {
					result.setResult(parser.parse((JSONArray) element));
				} else {
					JSONObject jsonObject = (JSONObject) element;
					Iterator<String> iterator = (Iterator<String>) jsonObject.keys();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						Object obj = json.get(key);
						ResultType resultType = null;
						if (obj instanceof JSONArray) {
							resultType = parser.parse((JSONArray) obj);
						} else {
							resultType = parser.parse((JSONObject) obj);
						}
						result.setResult(resultType);
					}
				}
			}
		} catch (JSONException ex) {
			result.setException(ex);
			if (DEBUG) {
				throw new RuntimeException("Error parsing JSON response: " + ex.getMessage());
			}
		} catch (Exception ex) {
			result.setException(ex);
			if (DEBUG) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}