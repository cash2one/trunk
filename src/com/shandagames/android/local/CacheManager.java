package com.shandagames.android.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @file CacheManager.java
 * @create 2012-10-9 上午11:26:17
 * @author lilong
 * @description Manager class for cache.
 */
public class CacheManager {

	private static final int MAX_CAPACITY = 50;

	private static final int DELETE_COUNT = 20;

	private static Map<String, Object> cacheMap = Collections.synchronizedMap(new HashMap<String, Object>());

	private static Map<String, CacheConfig> cacheConfigMap = Collections.synchronizedMap(new HashMap<String, CacheConfig>());

	public synchronized static void addCache(String key, Object value, CacheConfig config) {
		if (cacheMap.size() >= MAX_CAPACITY) {
			updateCache();
		}
		cacheMap.put(key, value);
		if (config == null) {
			cacheConfigMap.put(key, CacheConfig.getDefaultConfig());
		} else {
			cacheConfigMap.put(key, config);
		}
	}

	public synchronized static void removeCache(String key) {
		cacheMap.remove(key);
		cacheConfigMap.remove(key);
	}

	public synchronized static void clearCache() {
		cacheMap.clear();
		cacheConfigMap.clear();
	}

	public synchronized static Object getCacheValue(String key) {
		Object value = cacheMap.get(key);
		CacheConfig config = cacheConfigMap.get(key);

		if (value == null || config == null) {
			return null;
		}

		long now = (new Date()).getTime();
		if (now - config.getBeginTime() > config.getDurableTime()) {
			removeCache(key);
			return null;
		}
		config.setLastAccessTime((new Date()).getTime());

		return value;
	}

	public synchronized static void updateCache() {
		// 清除缓存的策略。
		// 1. 先清除过期的。
		// 2. 如果没有过期的，则清除最旧访问的20条数据。
		// 因为缓存的数据量很少，处理起来很快，所以这里没有用单独的线程处理。
		boolean overdueFlag = false;
		List<String> overdueCache = new ArrayList<String>();
		List<Entry<String, CacheConfig>> entryList = new ArrayList<Entry<String, CacheConfig>>(MAX_CAPACITY);

		Set<Entry<String, CacheConfig>> entries = cacheConfigMap.entrySet();
		Iterator<Entry<String, CacheConfig>> it = entries.iterator();
		long now = (new Date()).getTime();
		while (it.hasNext()) {
			Entry<String, CacheConfig> entry = it.next();
			entryList.add(entry);
			CacheConfig config = entry.getValue();
			if (config.isForever()) {
				continue;
			}
			if (now - config.getBeginTime() > config.getDurableTime()) {
				overdueCache.add(entry.getKey());
				overdueFlag = true;
			}
		}
		if (overdueFlag) {
			for (String key : overdueCache) {
				cacheMap.remove(key);
				cacheConfigMap.remove(key);
			}
			return;
		}

		if (cacheMap.size() <= MAX_CAPACITY) {
			return;
		}

		ComparatorEntry comparator = new ComparatorEntry();
		Collections.sort(entryList, comparator);
		for (int i = 0; i < DELETE_COUNT; i++) {
			String key = entryList.get(i).getKey();
			cacheMap.remove(key);
			cacheConfigMap.remove(key);
		}
	}

	public static Map<String, Object> getCacheMap() {
		return cacheMap;
	}

	public static Map<String, CacheConfig> getCacheConfigMap() {
		return cacheConfigMap;
	}

	private static class ComparatorEntry implements Comparator<Object> {
		@SuppressWarnings("unchecked")
		public int compare(Object arg0, Object arg1) {
			Entry<String, CacheConfig> entry0 = (Entry<String, CacheConfig>) arg0;
			Entry<String, CacheConfig> entry1 = (Entry<String, CacheConfig>) arg1;
			long time0 = entry0.getValue().getLastAccessTime();
			long time1 = entry1.getValue().getLastAccessTime();
			if (time0 < time1) {
				return -1;
			} else if (time0 > time1) {
				return 1;
			}
			return 0;
		}
	}
}
