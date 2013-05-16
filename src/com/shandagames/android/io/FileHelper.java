package com.shandagames.android.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @file FileHelper.java
 * @create 2013-4-10 上午11:14:57
 * @author Jacky.Lee
 * @description TODO
 */
public final class FileHelper {

	private FileHelper() {
	}
	
	/** 检查文件是否存在，不存在就创建 */
    public static boolean createIfNoExists(String path) {
    	File file = new File(path);
		boolean isSuccess = false;
		if (!file.exists()) {
			isSuccess = file.mkdirs();
		}
		return isSuccess;
    }
	
    
    /** 压缩数据 */
    public static byte[] gzip(String content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream zipOut;
        try {
            zipOut = new GZIPOutputStream(out);
            zipOut.write(content.getBytes());
            zipOut.finish();
            zipOut.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    
    /** 写入文件内容 */
    public static boolean writeFile(String content, String path, boolean gzip) {
        if (!createIfNoExists(path)) {
        	return false;
        }
        try {
	        OutputStream output = new FileOutputStream(new File(path));
	        if (gzip) output = new GZIPOutputStream(output);
	        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
	        writer.write(content);
	        writer.close();
	        output.close();
	        return true;
        } catch (IOException ex) {
        	ex.printStackTrace();
        } 
        return false;
    }
    
    /** 读取文件内容  */
    public static String readFile(String path, boolean gzip) {
    	File file = new File(path);
    	if (!file.exists()) {
    		return null;
    	}
    	try {
	    	InputStream input = new FileInputStream(file);
	    	if (gzip) input = new GZIPInputStream(input);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
	    	StringBuilder sb = new StringBuilder();
	    	String s;
	        while ((s = reader.readLine()) != null) {
	        	sb.append(s);
	        }
	        reader.close();
	        input.close();
	        return sb.toString();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    
    /** 递归文件列表  */
    public static List<File> recursiveFileFind(File[] files){
	    int index = 0;
	    List<File> arrFile = new ArrayList<File>();
        if(files!=null){
	        while(index!=files.length){
	        	if(files[index].isDirectory()){
	        		File file[] = files[index].listFiles();
	                recursiveFileFind(file);
	        	}
	        	index++;
	            arrFile.add(files[index]);
	        }
        }
        return arrFile;
    }
    
    /** 获取扩展存储空间的缓存目录  */
	static public File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
	
	/** 获取缓存目录  */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				|| !Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) ? 
				getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/** 判断指定的文件是否存在并创建文件 */
	public static String createFilePath(File cacheDir, String fileName) {
		try {
			return cacheDir.getAbsolutePath() + File.separator
					+ URLEncoder.encode(fileName.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.w("createFilePath", "Couldn't retrieve ApplicationFilePath for : " + e);
		}
		return null;
	}
}
