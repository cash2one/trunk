package com.shandagames.android.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSupport {
	/**
	 * 检查字符串是否存在值
	 * 
	 * @param str
	 *            待检验的字符串
	 * @return 当 str 不为 null 或 "" 就返回 true
	 */
	public static boolean isNullOrEmpty(CharSequence str) {
		return (str == null || str.equals("") || 
				str.equals("null") || str.equals("NULL"));
	}

	/** 是否包含空格 */
	public static boolean isSpace(String str) {
		for (int i = 0; i < str.length(); i++) {
			int chr = (int) str.charAt(i);
			if (chr != 32) {
				return false;
			}
		}
		return true;
	}

	/** 是否为Url */
	public static boolean isUrl(String str) {
		if (isNullOrEmpty(str))
			return false;
		return str.matches("^http://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	}

	/** 是否为科学计数法显示 */
	public static boolean isENum(String str) {
		if (isNullOrEmpty(str))
			return false;
		return str.matches("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
	}

	/** 检测字符串中只能包含：中文、数字、下划线(_)、横线(-) */
	public static boolean checkStr(String sequence) {
		final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		return !matcher.find();
	}

	/** 显示为中文 */
	public static boolean isChinese(char c) {
		return (int) c >= 0x4E00 && (int) c <= 0x9FA5;
	}

	/** 字符串转换布尔型 */
	public static boolean str2Boolean(String str, boolean defaultV) {
		if (isNullOrEmpty(str))
			return defaultV;
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
}
