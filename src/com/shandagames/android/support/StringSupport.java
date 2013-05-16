package com.shandagames.android.support;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.text.TextUtils;

/** 字符串处理相关的工具类  */
public class StringSupport {

	private StringSupport() {
	}
	
	
	 /**
     * 检查字符串是否存在值
     * 
     * @param str 待检验的字符串
     * @return 当 str 不为 null 或 "" 就返回 true
     */
    public static boolean isNullOrEmpty(CharSequence str) {
        return (str == null || str.equals("") || str.equals("null") || str.equals("NULL"));
    }
	
    /** 是否包含空格  */
    public static boolean isSpace(String str) {
		for(int i=0;i<str.length();i++){
			int chr = (int)str.charAt(i);
			if(chr!=32)
				return false;
		}
		return true;
	}
    
    /** 是否为Url */
    public static boolean isUrl(String str) {
		if (isNullOrEmpty(str))
			return false;
		return str.matches("^http://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	}
    
    /** 是否为科学计数法显示  */
    public static boolean isENum(String str) {
    	if (isNullOrEmpty(str))
			return false;
    	return str.matches("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
    }
    
    /** 检测字符串中只能包含：中文、数字、下划线(_)、横线(-) */
    public static boolean checkNickname(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    } 
    
    /** 显示为中文  */
    public static boolean isChinese(char c) {
		return (int) c >= 0x4E00 && (int) c <= 0x9FA5;
	}
    
    /** 字符串转换布尔型  */
    public static boolean str2Boolean(String str, boolean defaultV) {
		if (isNullOrEmpty(str))
			return defaultV;
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
    
    /**
     * 对参数进行UTF-8编码，并替换特殊字符
     * 
     * @param paramDecString 待编码的参数字符串
     * @return 完成编码转换的字符串
     */
    public static String paramEncode(String paramDecString) {
        if (isNullOrEmpty(paramDecString)) {
            return "";
        }
        try {
            return URLEncoder.encode(paramDecString, "UTF-8").replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~")
                    .replace("#", "%23");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将 %XX 换为原符号，并进行UTF-8反编码
     * 
     * @param paramEncString 待反编码的参数字符串
     * @return 未进行UTF-8编码和字符替换的字符串
     */
    public static String paramDecode(String paramEncString) {
        int nCount = 0;
        for (int i = 0; i < paramEncString.length(); i++) {
            if (paramEncString.charAt(i) == '%') {
                i += 2;
            }
            nCount++;
        }

        byte[] sb = new byte[nCount];

        for (int i = 0, index = 0; i < paramEncString.length(); i++) {
            if (paramEncString.charAt(i) != '%') {
                sb[index++] = (byte) paramEncString.charAt(i);
            } else {
                StringBuilder sChar = new StringBuilder();
                sChar.append(paramEncString.charAt(i + 1));
                sChar.append(paramEncString.charAt(i + 2));
                sb[index++] = Integer.valueOf(sChar.toString(), 16).byteValue();
                i += 2;
            }
        }
        String decode = "";
        try {
            decode = new String(sb, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 解析queryString，取得Bundle格式存储的参数队列
     * 
     * @param queryString 查询字符串
     * @return 以Bundle格式存储的参数队列.
     */
    public static Bundle parseUrl(String queryString) {
        try {
            URL url = new URL(queryString);
            Bundle params = new Bundle();
            if (url.getQuery() != null) {
                String array[] = url.getQuery().split("&");
                for (String parameter : array) {
                    String v[] = parameter.split("=");
                    params.putString(URLDecoder.decode(v[0]), paramDecode(v[1]));
                }
            }
            return params;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }
    
    /**
     * 根据Bundle格式存储的参数队列，生成queryString
     * @param QueryParamsList
     * @return 不包括？的queryString
     */
    public static String getQueryString(Bundle bundle) {
    	if (bundle != null && !bundle.isEmpty()) {
	    	StringBuilder queryString=new StringBuilder();
	    	Iterator<String> params = bundle.keySet().iterator();
	    	while (params.hasNext()) {
	    		String key = params.next();
	    		queryString.append('&');
	            queryString.append(key);
	            queryString.append('=');
	            queryString.append(paramEncode(bundle.getString(key)));
	    	}
	    	//去掉第一个&号
	        return queryString.substring(1);
    	}
    	return null;
    }
    
    /**
     * 分割queryString，取得List<NameValuePair>格式存储的参数队列.
     * 
     * @param queryString 查询字符串
     * @return 以List<NameValuePair>格式存储的参数队列.
     */
    public static List<NameValuePair> getQueryParamsList(String queryString) {
        if (queryString.startsWith("?")) {
            queryString = queryString.substring(1);
        }

        List<NameValuePair> result = new ArrayList<NameValuePair>();

        if (queryString != null && !queryString.equals("")) {
            String[] p = queryString.split("&");
            for (String s : p) {
                if (s != null && !s.equals("")) {
                    if (s.indexOf('=') > -1) {
                        String[] temp = s.split("=");
                        if (temp.length > 1) {
                            result.add(new BasicNameValuePair(temp[0], paramDecode(temp[1])));
                        }
                    }
                }
            }
        }
        return result;
    }  
    
    /**
     * 根据List<NameValuePair>格式存储的参数队列，生成queryString
     * @param QueryParamsList
     * @return 不包括？的queryString
     */
    public static String getQueryString(List<NameValuePair> QueryParamsList){
    	if (QueryParamsList != null && !QueryParamsList.isEmpty()) {
	        StringBuilder queryString=new StringBuilder();
	        for(NameValuePair param : QueryParamsList){
	            queryString.append('&');
	            queryString.append(param.getName());
	            queryString.append('=');
	            queryString.append(paramEncode(param.getValue()));
	        }
	        //去掉第一个&号
	        return queryString.substring(1);
    	}
    	return null;
    }
    
    /**
	 * 将中文字符串转换十六进制Unicode编码字符串
	 * @param s 转换字符串
	 * @return Unicode编码字符串
	 */
	public static String str2Unicode(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if (ch > 255) {
				str += "\\u" + Integer.toHexString(ch);
			} else {
				str += "\\" + Integer.toHexString(ch);
			}
		}
		return str;
	}

	/**
	 * 将十六进制Unicode编码字符串转换中文字符串
	 * 
	 * @param str Unicode编码字符串
	 * @return 中文字符串
	 */
	public static String unicode2Str(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), String.valueOf(ch));
		}
		return str;
	}
	
	
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
    
    /**
     * Turns a camel case string into an underscored one, e.g. "HelloWorld"
     * becomes "hello_world".
     * 
     * @param camelCaseString
     *        the string to underscore
     * @return the underscored string
     */
    public static String underscore(String camelCaseString) {
        String[] words = splitByCharacterTypeCamelCase(camelCaseString);
        return TextUtils.join("_", words).toLowerCase();
    }

    /**
     * <p>
     * Splits a String by Character type as returned by
     * <code>java.lang.Character.getType(char)</code>. Groups of contiguous
     * characters of the same type are returned as complete tokens, with the
     * following exception: the character of type
     * <code>Character.UPPERCASE_LETTER</code>, if any, immediately preceding a
     * token of type <code>Character.LOWERCASE_LETTER</code> will belong to the
     * following token rather than to the preceding, if any,
     * <code>Character.UPPERCASE_LETTER</code> token.
     * 
     * <pre>
     * StringUtils.splitByCharacterTypeCamelCase(null)         = null
     * StringUtils.splitByCharacterTypeCamelCase("")           = []
     * StringUtils.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     * StringUtils.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     * StringUtils.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     * StringUtils.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
     * StringUtils.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
     * StringUtils.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
     * StringUtils.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
     * </pre>
     * 
     * @param str
     *        the String to split, may be <code>null</code>
     * @return an array of parsed Strings, <code>null</code> if null String
     *         input
     * @since 2.4
     */
    public static String[] splitByCharacterTypeCamelCase(String str) {
        return splitByCharacterType(str, true);
    }

    /**
     * <p>
     * Splits a String by Character type as returned by
     * <code>java.lang.Character.getType(char)</code>. Groups of contiguous
     * characters of the same type are returned as complete tokens, with the
     * following exception: if <code>camelCase</code> is <code>true</code>, the
     * character of type <code>Character.UPPERCASE_LETTER</code>, if any,
     * immediately preceding a token of type
     * <code>Character.LOWERCASE_LETTER</code> will belong to the following
     * token rather than to the preceding, if any,
     * <code>Character.UPPERCASE_LETTER</code> token.
     * 
     * @param str
     *        the String to split, may be <code>null</code>
     * @param camelCase
     *        whether to use so-called "camel-case" for letter types
     * @return an array of parsed Strings, <code>null</code> if null String
     *         input
     * @since 2.4
     */
    private static String[] splitByCharacterType(String str, boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new String[0];
        }
        char[] c = str.toCharArray();
        ArrayList<String> list = new ArrayList<String>();
        int tokenStart = 0;
        int currentType = Character.getType(c[tokenStart]);
        for (int pos = tokenStart + 1; pos < c.length; pos++) {
            int type = Character.getType(c[pos]);
            if (type == currentType) {
                continue;
            }
            if (camelCase && type == Character.LOWERCASE_LETTER
                    && currentType == Character.UPPERCASE_LETTER) {
                int newTokenStart = pos - 1;
                if (newTokenStart != tokenStart) {
                    list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                    tokenStart = newTokenStart;
                }
            } else {
                list.add(new String(c, tokenStart, pos - tokenStart));
                tokenStart = pos;
            }
            currentType = type;
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return (String[]) list.toArray(new String[list.size()]);
    }

}
