package com.shandagames.android.support;

import java.io.File;
import java.util.List;
import java.util.Locale;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * @file IntentSupport.java
 * @create 2013-8-2 上午11:55:46
 * @author lilong
 * @description TODO 封装常用的Intent操作 
 */
public class IntentSupport {

    public static final String MIME_TYPE_EMAIL = "message/rfc822";
    public static final String MIME_TYPE_TEXT = "text/*";

    /**
     * Checks whether there are applications installed which are able to handle the given
     * action/data.
     * 
     * @param context
     *            the current context
     * @param action
     *            the action to check
     * @param uri
     *            that data URI to check (may be null)
     * @param mimeType
     *            the MIME type of the content (may be null)
     * @return true if there are apps which will respond to this action/data
     */
    public static boolean isIntentAvailable(Context context, String action, Uri uri, String mimeType) {
        final Intent intent = (uri != null) ? new Intent(action, uri) : new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Checks whether there are applications installed which are able to handle the given
     * action/type.
     * 
     * @param context
     *            the current context
     * @param action
     *            the action to check
     * @param mimeType
     *            the MIME type of the content (may be null)
     * @return true if there are apps which will respond to this action/type
     */
    public static boolean isIntentAvailable(Context context, String action, String mimeType) {
        final Intent intent = new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Checks whether there are applications installed which are able to handle the given intent.
     * 
     * @param context
     *            the current context
     * @param intent
     *            the intent to check
     * @return true if there are apps which will respond to this intent
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    public static Intent newEmailIntent(Context context, String address, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.setType(MIME_TYPE_EMAIL);

        return intent;
    }

    /** 快速回馈信息  */
    public static Intent newShareIntent(Context context, String subject, String message,
            String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND); //启动分享发送的属性  
        shareIntent.putExtra(Intent.EXTRA_TEXT, message); //分享的内容  
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject); //分享的主题
        shareIntent.setType(MIME_TYPE_TEXT); //分享发送的数据类型  
        //shareIntent.putExtra(Intent.EXTRA_STREAM, ""); //分享流媒体
        return Intent.createChooser(shareIntent, chooserDialogTitle); //目标应用选择对话框的标题 
    }

    public static Intent newMapsIntent(String address, String placeTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append("geo:0,0?q=");

        String addressEncoded = Uri.encode(address);
        sb.append(addressEncoded);
        // pass text for the info window
        String titleEncoded = Uri.encode("(" + placeTitle + ")");
        sb.append(titleEncoded);
        // set locale; probably not required for the maps app?
        sb.append("&hl=" + Locale.getDefault().getLanguage());

        return new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
    }

    /**
     * Creates an intent which when fired, will launch the camera to take a picture that's saved to
     * a temporary file so you can use it directly without going through the gallery.
     * 
     * @param tempFile
     *            the file that should be used to temporarily store the picture
     * @return the intent
     */
    public static Intent newTakePictureIntent(File tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        return intent;
    }

    /**
     * Creates an intent which when fired, will launch the phone's picture gallery to select a
     * picture from it.
     * 
     * @return the intent
     */
    public static Intent newSelectPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * Creates an intent which when fired, will launch the phone's video gallery to select a
     * video from it.
     * 
     * @return the intent
     */
    public static Intent newSelectVideoIntent() {
    	 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("video/*");
         return intent;
    }
    
    /**
     * Creates an intent which when fired, will launch the phone's audio gallery to select a
     * audio from it.
     * 
     * @return the intent
     */
    public static Intent newSelectAudioIntent() {
    	 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("audio/*");
         return intent;
    }
    
    /**
     * Creates an intent that will open the phone app and enter the given number. Unlike
     * {@link #newCallNumberIntent(String)}, this does not actually dispatch the call, so it gives
     * the user a chance to review and edit the number.
     * 
     * @param phoneNumber
     *            the number to dial
     * @return the intent
     */
    public static Intent newDialNumberIntent(String phoneNumber) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber.replace(" ", "")));
    }

    /**
     * Creates an intent that will immediately dispatch a call to the given number. NOTE that unlike
     * {@link #newDialNumberIntent(String)}, this intent requires the
     * {@link android.Manifest.permission#CALL_PHONE} permission to be set.
     * 
     * @param phoneNumber
     *            the number to call
     * @return the intent
     */
    public static Intent newCallNumberIntent(String phoneNumber) {
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.replace(" ", "")));
    }

    /** 拍摄视频   */
    public static Intent newTakeVideoIntent() {
    	return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }
    
    /** 发送短信  */
    public static Intent newSmsIntent(String phoneNumber, String message) {
    	Uri uri = Uri.parse("smsto:" + phoneNumber);
    	Intent intent = new Intent(Intent.ACTION_SENDTO);
    	intent.setData(uri);
    	intent.putExtra("sms_body", message);
    	intent.setType("vnd.android-dir/mms-sms");
    	return intent;
    }
    
    /** 从market市场安装应用app */
	public static Intent newMarketIntent(String packageName) {
		Intent installIntent = new Intent(Intent.ACTION_VIEW);  
		// market://details?id=com.adobe.flashplayer
        installIntent.setData(Uri.parse("market://details?id=" + packageName));  
        return installIntent;
	}
	
	/** 打开浏览器访问Url */
	public static Intent newWebViewIntent(String uri) {
		 return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
	}
	
	/** 应用中实现Google搜索  */
	public static Intent newGoogleSearchIntent(String term) {
		 Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		 intent.putExtra(SearchManager.QUERY, term);
		 return intent;
	}
	
	/** 打开WiFi设置 */
	public static Intent newWiFiSettingIntent() {
		return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	}
	
	/** 打开GPS设置  */
	public static Intent newGpsSettingsIntent() {
		return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	}
	
	/** 打开视频录制  */
	public static Intent newVideoIntent(File tempFile) {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); 
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
	    return intent;
	}
	
	/** 截取图像部分区域 ;魅族的机器没有返回data字段，但是返回了filePath */
	public static Intent newCropImageUri(Uri uri, int outputX, int outputY){
		//android1.6以后只能传图库中图片
		//http://www.linuxidc.com/Linux/2012-11/73940.htm
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true"); //发送裁剪信号
		intent.putExtra("aspectX", 1); 	//X方向上的比例
		intent.putExtra("aspectY", 1); 	//Y方向上的比例
		intent.putExtra("outputX", outputX); //裁剪区的宽
		intent.putExtra("outputY", outputY); //裁剪区的高
		intent.putExtra("scale", true);//是否保留比例
		//拍摄的照片像素较大，建议直接保存URI，否则内存溢出，较小图片可以直接返回Bitmap
		/*Bundle extras = data.getExtras();
		if (extras != null) {	
			Bitmap bitmap = extras.getParcelable("data");
	    }*/
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);  //是否返回数据     
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 图片格式
		intent.putExtra("noFaceDetection", true); //关闭人脸检测
		return intent;
	}
	
	/** 系统原生的管理应用程序界面 */
	public static Intent newManageApplicationIntent() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
		return intent;
	}
	
	/** 安装应用: 指定安装应用路径 */
	public static Intent newInstallApkIntent(File apkFile) {
		Intent intent=new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://"+apkFile.toString()), "application/vnd.android.package-archive");
		return intent;
	}
	
	/** 卸载应用APK: 指定卸载应用包名*/
	public static Intent newUnInstallApkIntent(Uri data) {
		return new Intent(Intent.ACTION_DELETE, data);
	}
	
	/** 扫描指定文件  */
	public static Intent newScanFileIntent(Uri data) {
		return new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data);
	}
}
