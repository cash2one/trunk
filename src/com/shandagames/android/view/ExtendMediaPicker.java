/**
 * 
 */
package com.shandagames.android.view;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

/**
 * @file ImagePicker.java
 * @create 2012-10-16 上午11:42:21
 * @author lilong
 * @description TODO
 */
public class ExtendMediaPicker {
	private static final String TAG = "ExtendImagePicker";
	
	private static final int ACTIVITY_REQUEST_CODE_PICTURE_LIBRARY = 0x2001;
	private static final int ACTIVITY_REQUEST_CODE_TAKE_PHOTO = 0x2002;
	private static final int ACTIVITY_REQUEST_CODE_VIDEO_LIBRARY = 0x2003;
	private static final int ACTIVITY_REQUEST_CODE_TAKE_VIDEO = 0x2004;
	private static final int ACTIVITY_REQUEST_CODE_CROP_PICTURE = 0x2005;
	
	private Activity act;
	private Uri imageUri;
	
	private OnMediaPickerListener mMediaPickerListener;
	
	public ExtendMediaPicker(Activity activity) {
		this.act = activity;
	}

	public void showImagePicker() {
		new AlertDialog.Builder(act)
		.setItems(new String[]{"使用相机拍照", "从手机相册选择"}, 
				new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0:
					launchPictureCamera();
					break;
				case 1:
					launchPictureLibrary();
					break;
				}
			}
		}).create().show();
	}
	
	public void showVideoPicker() {
		new AlertDialog.Builder(act)
		.setItems(new String[]{"拍摄视频", "选择视频文件"}, 
				new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0:
					launchVideoCamera();
					break;
				case 1:
					launchVideoLibrary();
					break;
				}
			}
		}).create().show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK){//result is not correct
			Log.e(TAG, "requestCode = " + requestCode);
			Log.e(TAG, "resultCode = " + resultCode);
			Log.e(TAG, "data = " + data);
			return;
		} else {
			switch (requestCode) {
				case ACTIVITY_REQUEST_CODE_PICTURE_LIBRARY:
					imageUri = data.getData();
					Log.v(TAG, "CHOOSE_PICTURE: uri = " + imageUri);
					cropImageUri(imageUri, 120, 120, ACTIVITY_REQUEST_CODE_CROP_PICTURE);
					break;
				case ACTIVITY_REQUEST_CODE_TAKE_PHOTO:
					Log.v(TAG, "TAKE_PICTURE: uri = " + imageUri);
					cropImageUri(imageUri, 120, 120, ACTIVITY_REQUEST_CODE_CROP_PICTURE);
					break;
				case ACTIVITY_REQUEST_CODE_CROP_PICTURE:
					Log.v(TAG, "CROP_PICTURE: uri = " + imageUri);//it seems to be null
					if(imageUri != null && mMediaPickerListener!=null){
						String imagePath = getRealPathFromURI(imageUri, MediaStore.Images.Media.DATA);
						mMediaPickerListener.onSelectedMediaChanged(imagePath);
					}
					break;
				case ACTIVITY_REQUEST_CODE_TAKE_VIDEO:
				case ACTIVITY_REQUEST_CODE_VIDEO_LIBRARY:
					Log.v(TAG, "CHOOSE_VIDEO: data = " + data);
		            if (data!=null && mMediaPickerListener!=null) {
		            	String videoPath = getRealPathFromURI(data.getData(), MediaStore.Video.Media.DATA);
		            	mMediaPickerListener.onSelectedMediaChanged(videoPath);
		            }
					break;
			}
		}
	}
	
	private void launchPictureLibrary() {
		//Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		Intent photoPickerIntent= new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		act.startActivityForResult(photoPickerIntent, ACTIVITY_REQUEST_CODE_PICTURE_LIBRARY);
	}
	
	@SuppressLint("NewApi")
	private void launchPictureCamera() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dirPath=new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");	
			imageUri = Uri.fromFile(dirPath);
			
			//capture a big bitmap and store it in Uri
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			act.startActivityForResult(intent, ACTIVITY_REQUEST_CODE_TAKE_PHOTO);
		} else {
			Toast.makeText(act, "Before you take photos please insert SD card", Toast.LENGTH_LONG).show();	
		}
    }
	
	private void launchVideoLibrary() {
        //Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
		Intent videoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        videoPickerIntent.setType("video/*");
        act.startActivityForResult(videoPickerIntent, ACTIVITY_REQUEST_CODE_VIDEO_LIBRARY);
    }
	
	private void launchVideoCamera() {
        act.startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE), ACTIVITY_REQUEST_CODE_TAKE_VIDEO);
    }

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
		try {
			//android1.6以后只能传图库中图片
			//http://www.linuxidc.com/Linux/2012-11/73940.htm
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("scale", true);
			//拍摄的照片像素较大，建议直接保存URI，否则内存溢出，较小图片可以直接返回Bitmap
			/*Bundle extras = data.getExtras();
			if (extras != null) {	
				Bitmap bitmap = extras.getParcelable("data");
		    }*/
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("return-data", false);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); // no face detection
			act.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(act, "Can not find image crop app", Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getRealPathFromURI(Uri contentUri, String columnName) {
        String [] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = act.managedQuery(contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
	}
	
	
	public void setOnMediaPickerListener(OnMediaPickerListener listener) {
		this.mMediaPickerListener = listener;
	}
	
	public interface OnMediaPickerListener {
		
		void onSelectedMediaChanged(String mediaUri);
	}
}
