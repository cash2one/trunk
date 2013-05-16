package com.shandagames.android.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;

/**
 * @file DiagnosticSupport.java
 * @create 2013-4-10 上午10:34:45
 * @author Jacky.Lee
 * @description TODO
 */
public class DiagnosticSupport {

    public static final int ANDROID_API_LEVEL;

    static {
        int apiLevel = -1;
        try {
            apiLevel = Build.VERSION.class.getField("SDK_INT").getInt(null);
        } catch (Exception e) {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        }
        ANDROID_API_LEVEL = apiLevel;
    }

    /** 获取设备唯一标识ID */
	public static String generateDeviceId(Context context) {
		UUID uuid = null;
		final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		try {
			//ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
			//它在Android <=2.1 or Android >=2.3的版本是可靠、稳定的，但在2.2的版本并不是100%可靠的
			//在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
			if (androidId != null && !"9774d56d682e549c".equals(androidId)) {
				uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
			} else {
				//根据不同的手机设备返回IMEI，MEID或者ESN码
				//非手机设备就没有这个DEVICE_ID，获取DEVICE_ID需要READ_PHONE_STATE权限
				final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				if (deviceId != null) {
					uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//UUID经常用来标识在某个应用中的唯一ID
		if (uuid == null) {
			uuid = UUID.randomUUID();
		}
		return uuid.toString().replace("-", "");
	}
    
    public static String createDiagnosis(Activity context, Exception error) {
        StringBuilder sb = new StringBuilder();

        sb.append("Application version: v" + ManifestSupport.getApplicationForVersionName(context) + "\n");
        sb.append("Device locale: " + Locale.getDefault().toString() + "\n\n");
        sb.append("Android ID: " + generateDeviceId(context));

        // phone information
        sb.append("PHONE SPECS\n");
        sb.append("model: " + Build.MODEL + "\n");
        sb.append("brand: " + Build.BRAND + "\n");
        sb.append("product: " + Build.PRODUCT + "\n");
        sb.append("device: " + Build.DEVICE + "\n\n");

        // android information
        sb.append("PLATFORM INFO\n");
        sb.append("Android " + Build.VERSION.RELEASE + " " + Build.ID + " (build "
                + Build.VERSION.INCREMENTAL + ")\n");
        sb.append("build tags: " + Build.TAGS + "\n");
        sb.append("build type: " + Build.TYPE + "\n\n");

        // settings
        sb.append("SYSTEM SETTINGS\n");
        String networkMode = null;
        ContentResolver resolver = context.getContentResolver();
        try {
            if (Settings.Secure.getInt(resolver, Settings.Secure.WIFI_ON) == 0) {
                networkMode = "DATA";
            } else {
                networkMode = "WIFI";
            }
            sb.append("network mode: " + networkMode + "\n");
            sb.append("HTTP proxy: "
                    + Settings.Secure.getString(resolver, Settings.Secure.HTTP_PROXY) + "\n\n");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        sb.append("STACK TRACE FOLLOWS\n\n");

        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));

        sb.append(stackTrace.toString());

        return sb.toString();
    }

}
