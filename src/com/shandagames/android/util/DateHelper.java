package com.shandagames.android.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @file DateHelper.java
 * @create 2013-4-10 下午12:00:42
 * @author Jacky.Lee
 * @description TODO
 */
@SuppressLint("SimpleDateFormat")
public final class DateHelper {

	private DateHelper() {
	}

	/** 格式化日期  */
	public static String formatString(long milliseconds) {
		return formatString(milliseconds, "yyyy-MM-dd HH:mm:ss");
	}
	
	/** 指定模式格式化日期  */
	public static String formatString(long milliseconds, String pattern) {
    	try {
    		Date date = new Date(milliseconds);
    		DateFormat dateFormat = new SimpleDateFormat(pattern);
	    	return dateFormat.format(date);
    	} catch (IllegalArgumentException ex) {
    		throw new IllegalArgumentException("the pattern is not considered to be usable by this formatter. " + ex.getMessage());
    	}
    }
	
	/** 计算当前年龄  */
    public static int calculateAge (int _year, int _month, int _day) {
		GregorianCalendar cal = new GregorianCalendar();
		int y, m, d, a;		

		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(_year, _month, _day);
		a = y - cal.get(Calendar.YEAR);
		if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH))
				&& (d < cal.get(Calendar.DAY_OF_MONTH)))) {
			--a;
		}
		if(a < 0)
			throw new IllegalArgumentException("Age < 0");
		return a;
	}
    
    /**
     * Convert time to a string
     *
     * @param millis e.g.time/length from file
     * @return formated string (hh:)mm:ss
     */
    public static String millisToString(long millis) {
        boolean negative=millis<0;
        millis=Math.abs(millis);

        millis/=1000;
        int sec=(int) (millis%60);
        millis/=60;
        int min=(int) (millis%60);
        millis/=60;
        int hours=(int) millis;

        String time;
        DecimalFormat format=(DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (millis>0) {
            time=(negative ? "-" : "")+hours+":"+format.format(min)+":"+format.format(sec);
        } else {
            time=(negative ? "-" : "")+min+":"+format.format(sec);
        }
        return time;
    }
    
    /**
     * 根据 timestamp 生成各类时间状态串
     * 
     * @param timestamp 距1970 00:00:00 GMT的秒数
     * @return 时间状态串(如：刚刚5分钟前)
     */
    @SuppressLint("SimpleDateFormat")
	public static String getTimeState(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        try {
            long _timestamp = Long.parseLong(timestamp) * 1000;
            if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
                return "刚刚";
            } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
                return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
                        + "分钟前";
            } else {
                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(_timestamp);
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(c.getTime());
                }
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(c.getTime());
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy年M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
