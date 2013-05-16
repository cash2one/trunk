package com.shandagames.android.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.shandagames.android.log.LogUtils;

/**
 * @file CacheStorage.java
 * @create 2012-10-9 上午10:24:41
 * @author lilong
 * @description Cache storage class. This class can store or restore cache info.
 */
public class CacheStorage {

	public synchronized static boolean storeCache(File file) {
		if (!file.getParentFile().exists()) {
			boolean success = file.getParentFile().mkdirs();
			if (!success) {
				return false;
			}
		}

		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(CacheManager.getCacheMap());
			out.writeObject(CacheManager.getCacheConfigMap());
		} catch (FileNotFoundException e) {
			LogUtils.out(e.getMessage());
			return false;
		} catch (IOException e) {
			LogUtils.out(e.getMessage());
			return false;
		}  catch (Exception e) {
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public synchronized static boolean restoreCache(File file) {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));

			Map<String, String> cacheMap = (Map<String, String>) in.readObject();
			Map<String, CacheConfig> cacheConfigMap = (Map<String, CacheConfig>) in.readObject();

			CacheManager.clearCache();
			Iterator<Entry<String, String>> it = cacheMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				CacheConfig config = cacheConfigMap.get(key);
				if (config != null) {
					CacheManager.addCache(key, value, config);
				}
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		}  catch (Exception e) {
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return true;
	}
}
