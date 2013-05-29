package com.shandagames.android.constant;

/**
 * @file Config.java
 * @create 2013-3-15 上午10:25:28
 * @author lilong
 * @description TODO 环境变量配置
 */
public final class Config {

	private Config() {
	}
	
	/** 是否需要自动登录 */
	public static boolean needAutoLogin = true;

	/** 是否需要推送通知 */
	public static boolean needPushNotification = true;

	/** 是否发送异常日志 */
	public static final boolean sendReportWhenCrash = true;

	
}
