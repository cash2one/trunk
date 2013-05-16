package com.shandagames.android.local;

import java.io.Serializable;
import java.util.Date;

/**
 * @file CacheConfig.java
 * @create 2012-10-9 上午10:13:59
 * @author lilong
 * @description Cache configuration class
 */
public class CacheConfig implements Serializable {

	private static final long serialVersionUID = 5478101787287713635L;

	/**
	 * default durable time: 1 minute.
	 */
	public static final long ONE_MINUTE_DURABLE_TIME = 1000 * 60;

	public static final long HALF_HOUR_DURABLE_TIME = ONE_MINUTE_DURABLE_TIME * 30;

	public static final long ONE_HOUR_DURABLE_TIME = HALF_HOUR_DURABLE_TIME * 2;

	public static final long HALF_DAY_DURABLE_TIME = ONE_HOUR_DURABLE_TIME * 12;

	public static final long ONE_DAY_DURABLE_TIME = HALF_DAY_DURABLE_TIME * 2;

	public static final long HALF_MONTH_DURABLE_TIME = ONE_DAY_DURABLE_TIME * 15;

	public static final long ONE_MONTH_DURABLE_TIME = HALF_MONTH_DURABLE_TIME * 2;

	public static final long ONE_YEAR_DURABLE_TIME = ONE_DAY_DURABLE_TIME * 365;

	private long beginTime;

	private boolean isForever = false;

	private long durableTime;

	private long lastAccessTime;

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public boolean isForever() {
		return isForever;
	}

	public void setForever(boolean isForever) {
		this.isForever = isForever;
	}

	public long getDurableTime() {
		return durableTime;
	}

	public void setDurableTime(int durableTime) {
		this.durableTime = durableTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public static CacheConfig getDefaultConfig() {
		CacheConfig config = new CacheConfig();
		config.beginTime = (new Date()).getTime();
		config.isForever = false;
		config.durableTime = ONE_MINUTE_DURABLE_TIME;
		config.lastAccessTime = config.beginTime;
		return config;
	}

	public static CacheConfig getForeverConfig() {
		CacheConfig config = new CacheConfig();
		config.beginTime = (new Date()).getTime();
		config.isForever = true;
		config.durableTime = Long.MAX_VALUE;
		config.lastAccessTime = config.beginTime;
		return config;
	}

	public static CacheConfig getConfigByDuration(long duration) {
		CacheConfig config = new CacheConfig();
		config.beginTime = (new Date()).getTime();
		config.isForever = false;
		config.durableTime = duration;
		config.lastAccessTime = config.beginTime;
		return config;
	}
}
