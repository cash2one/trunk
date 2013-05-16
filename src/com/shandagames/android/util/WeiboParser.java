package com.shandagames.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.View;

public class WeiboParser {

	/**
	 * 将text中@某人、#某主题的字体加亮，匹配的表情文字以表情显示
	 * 
	 * @param text
	 * @param context
	 * @return SpannableString 设置setMovementMethod(LinkMovementMethod.getInstance());
	 */
	public static SpannableString formatContent(CharSequence text, final OnSpannableClickListener onSpannableListener) {
		SpannableString spannableString = new SpannableString(text);
		/*
		 * @[^\\s:：]+[:：\\s] 匹配@某人 #([^\\#|.]+)# 匹配#某主题 
		 */
		Pattern pattern = Pattern.compile("@([^\\[\\]\\s:：]+)|#([^\\#|.]+)#");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.startsWith("@")) { // @某人，加亮字体
				spannableString.setSpan(new HackyClickableSpan() {
					// 在onClick方法中可以编写单击链接时要执行的动作
					@Override
					public void onClick(View widget) {
						if (onSpannableListener != null) {
							String username = match;
							username = username.replace("@", "");
							username = username.replace(":", "");
							username = username.trim();
							onSpannableListener.onLinkedUser(username);
						}
					}
				}, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("#")) { // #某主题
				spannableString.setSpan(new HackyClickableSpan() {
					// 在onClick方法中可以编写单击链接时要执行的动作
					@Override
					public void onClick(View widget) {
						if (onSpannableListener != null) {
							String theme = match;
							theme = theme.replace("#", "");
							theme = theme.trim();
							onSpannableListener.onLinkedTheme(theme);
						}
					}
				}, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} 
		}
		// add web,url,tel,email link
		Linkify.addLinks(spannableString, Linkify.ALL);

		return spannableString;
	}
	
	public static interface OnSpannableClickListener {
		/** '@'到某个人 */
		public void onLinkedUser(String content);
		
		/** 链接到某主题 */
		public void onLinkedTheme(String content);
	}
	
	
	private static class HackyClickableSpan extends ClickableSpan {

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			// TODO Auto-generated method stub
			ds.bgColor=Color.BLACK;
			ds.setColor(ds.linkColor);
			ds.setUnderlineText(false);
		}
	}
}
