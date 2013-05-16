package com.shandagames.android.support;

import java.io.File;
import android.os.Environment;
import android.os.StatFs;

/**
 * 
  * 项目名称：IresearchAndroid   
  * 类名称：MemoryStatus   
  * 类描述：  获取手机内存状态
  * 创建人：Selience   
  * 创建时间：2011-7-20 下午07:10:09   
  * @version
 */
public class MemoryStatus {

	static final int ERROR = -1;
	
	static final String TAG = "MemoryStatus";
	
	/** 获取SDCard的状态,检验SDCard是否可用具有可读可写权限 */
	static public boolean isMemoryAvailable() {
		/*
		 * Environment.MEDIA_UNMOUNTED 用户手动到手机设置中卸载sd卡之后的状态
		 * Environment.MEDIA_REMOVED 用户手动卸载,然后将sd卡从手机取出之后的状态
		 * Environment.MEDIA_BAD_REMOVAL 用户未到手机设置中手动卸载sd卡,直接拨出之后的状态
		 * Environment.MEDIA_SHARED 手机直接连接到电脑作为U盘使用之后的状态
		 * Environment.MEDIA_CHECKINGS 手机正在扫描sd卡过程中的状态
		 */
	    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || 
	    		!Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED);
	}
	
	public static boolean isExternalStorageMounted() {
        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);

        return !(!canRead || onlyRead || unMounted);
    }
	
	public static String getSdCardPath() {
		return (isExternalStorageMounted()?Environment.getExternalStorageDirectory().getAbsolutePath():"");
    }
	
	/** 获取设备可使用的内存空间大小  */
	static public long getAvailableInternalMemorySize() {
		return getUsableSpace(Environment.getDataDirectory());
	}
	
	/** 获取设备总内存空间大小  */
	static public long getTotalInternalMemorySize() {
		return getTotalSpace(Environment.getDataDirectory());
	}
	
	/** 获取可使用的SDCard大小  */
	static public long getAvailableExternalMemorySize() {
		return getUsableSpace(Environment.getExternalStorageDirectory());
	}
	
	/** 获取SDCard总内存大小  */
	static public long getTotalExternalMemorySize() {
		return getTotalSpace(Environment.getExternalStorageDirectory());
	}
	
	/** 格式化显示存储大小  */
	static public String formatSize(long size) {
		String suffix = null;
	
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
	
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	/** 检验指定路径有效的存储空间 */
	static public long getUsableSpace(File path) {
		try {
	        final StatFs stats = new StatFs(path.getPath());
	        long blockSize = stats.getBlockSize();
			long availableBlocks = stats.getAvailableBlocks();
			return availableBlocks * blockSize;
		} catch (Exception ex) {
			return ERROR;
		}
    }
	
	/** 检验指定路径总的存储空间 */
	static public long getTotalSpace(File path) {
		try {
			final StatFs stats = new StatFs(path.getPath());
	        long blockSize = stats.getBlockSize();
			long totalBlocks = stats.getBlockCount();
			return totalBlocks * blockSize;
		} catch (Exception ex) {
			return ERROR;
		}
	}
	
}