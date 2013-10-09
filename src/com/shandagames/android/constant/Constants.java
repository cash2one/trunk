package com.shandagames.android.constant;

import java.io.File;
import android.os.Environment;

/**
 * @file Constants.java
 * @create 2012-10-9 上午9:54:22
 * @author lilong
 * @description TODO 应用存储常量
 */
public interface Constants {

	public static final String APP_NAME = "project";
	
	public static final String LOGTAG = APP_NAME;
	
	// google map constants
	public static final String INTENT_KEY_URI = "uri";
	public static final String AUTHORITY_MAP = "map";
	public static final String QUERY_PARAM_LAT = "lat";
	public static final String QUERY_PARAM_LNG = "lng";
	public static final String INTENT_KEY_LATITUDE = "latitude";
	public static final String INTENT_KEY_LONGITUDE = "longitude";
	public static final String GOOGLE_MAPS_API_KEY_DEBUG = "AIzaSyBUqVVSlsAlJwwQ0yJnxHD8o_S_oIHxTUg";
	public static final String GOOGLE_MAPS_API_KEY_RELEASE = "AIzaSyAzEWKI7wwyp_PgZ0LW9RlESZb3qWxq_QY";
	public static final String GOOGLE_MAPS_API_KEY = Config.DEBUG ? GOOGLE_MAPS_API_KEY_DEBUG : GOOGLE_MAPS_API_KEY_RELEASE;
	
	// debug or release server constants
	public static final String DEBUG_SERVER = "www.gameplus.com/test";
	public static final String RELEASE_SERVER = "www.gameplus.com/release";
	
	// dir cache constants
	public static final File SDCARD_ROOT = Environment.getExternalStorageDirectory();
	public static final File CAMERA_IMAGE_BUCKET_DIR = new File(SDCARD_ROOT, "DCIM/Camera");
	public static final File BASE_DIR = new File(SDCARD_ROOT, APP_NAME); 
	
	public static final String TEMP_DIR 	=	"temp";
	public static final String CACHE_DIR 	= 	"cache";
	public static final String DATA_DIR 	= 	"cache/data";
	public static final String AUDIO_DIR 	= 	"cache/audio";
	public static final String AVATAR_DIR	= 	"cache/avatar";
	public static final String PHOTO_DIR 	= 	"cache/photo";
	
}
