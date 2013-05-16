package com.shandagames.android.util;

import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;

/**
 * @author  lilong
 * @version 2012-5-31 下午12:19:03
 *
 */
public class SpannableUtils {

	/** 文字高亮显示  */
	public static CharSequence highlight(CharSequence text,int start,int end,Parcel src){  
        SpannableStringBuilder spannable=new SpannableStringBuilder(text.toString());//用于可变字符串  
        CharacterStyle  span=new ForegroundColorSpan(src);  //字体颜色
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        return spannable;
    } 
	
	/** 文字添加下划线  */
    public static CharSequence underline(CharSequence text,int start,int end){  
        SpannableStringBuilder spannable=new SpannableStringBuilder(text.toString());  
        CharacterStyle span=new UnderlineSpan();  
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        return spannable;
    }  
    
    /** 文字添加删除线  */
    public static CharSequence strike(CharSequence text,int start,int end){  
        SpannableStringBuilder spannable=new SpannableStringBuilder(text.toString());  
        CharacterStyle span=new StrikethroughSpan();  
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        return spannable;
    }  
}
