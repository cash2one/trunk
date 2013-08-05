package com.shandagames.android.util;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @file FileUtils.java
 * @create 2013-7-17 下午05:19:08
 * @author lilong
 * @description TODO 封转常用的文件操作
 */
public class FileUtils {
	/**
	 * 获取文件夹或者文件大小
	 * 
	 * @param String path 路径或者文件
	 * @return String 文件的大小，以BKMG来计量
	 */
	public static String getPathSize(String path) {
		String flieSizesString = "";
		File file = new File(path.trim());
		long fileSizes = 0;
		if (null != file && file.exists()) {
			if (file.isDirectory()) { // 如果路径是文件夹的时候
				fileSizes = getFileFolderTotalSize(file);
			} else if (file.isFile()) {
				fileSizes = file.length();
			}
		}
		flieSizesString = formatFileSizeToString(fileSizes);
		return flieSizesString;
	}

	/**
	 * 删除除指定文件之外的所有文件
	 * 
	 * @param appCachePath 被删除的文件夹目录
	 * @param excludedFileName 文件夹第一级目录下，不想被删除的文件的名称
	 */
	public static void cleanCacheFile(final String appCachePath,
			final String excludedFileName) {
		try {
			File file = new File(appCachePath.trim());
			if (null != file && file.isDirectory()) {
				for (File file1 : file.listFiles()) {
					if (file1.isDirectory()) {
						deleteAllFile(file1.getAbsolutePath());
					} else {
						if (!excludedFileName.equals(file1.getName())) {
							file1.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除所有文件
	 */
	public static void deleteAllFile(String path) {
		File file = new File(path);
		if (null != file && file.exists()) {
			if (file.isDirectory()) {
				File[] fileList = file.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					String filePath = fileList[i].getPath();
					deleteAllFile(filePath);
				}
			} else if (file.isFile()) {
				file.delete();
			}
		}
	}

	private static long getFileFolderTotalSize(File fileDir) {
		long totalSize = 0;
		File fileList[] = fileDir.listFiles();
		for (int fileIndex = 0; fileIndex < fileList.length; fileIndex++) {
			if (fileList[fileIndex].isDirectory()) {
				totalSize = totalSize
						+ getFileFolderTotalSize(fileList[fileIndex]);
			} else {
				totalSize = totalSize + fileList[fileIndex].length();
			}
		}
		return totalSize;
	}

	/**
	 * 格式化文件长度大小
	 */
	private static String formatFileSizeToString(long fileSize) {// 转换文件大小
		String fileSizeString = "";
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		if (fileSize == 0) {
			fileSizeString = "0B";
		} else if (fileSize < 1024) {
			fileSizeString = decimalFormat.format((double) fileSize) + "B";
		} else if (fileSize < (1 * 1024 * 1024)) {
			fileSizeString = decimalFormat.format((double) fileSize / 1024)
					+ "KB";
		} else if (fileSize < (1 * 1024 * 1024 * 1024)) {
			fileSizeString = decimalFormat.format((double) fileSize
					/ (1 * 1024 * 1024))
					+ "MB";
		} else {
			fileSizeString = decimalFormat.format((double) fileSize
					/ (1 * 1024 * 1024 * 1024))
					+ "GB";
		}
		return fileSizeString;
	}

}
