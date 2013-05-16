package com.shandagames.android.media;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;

public final class MediaScannerNotifier implements MediaScannerConnectionClient {
	private static final boolean V = false;
	private static final String TAG = "MediaScannerNotifier";
    
    private Context mContext;
    private Handler mCallback;
    private String path;
    private String mimeType;
    private MediaScannerConnection mConnection;

    public MediaScannerNotifier(Context context, Handler handler) {
        mContext = context;
        mCallback = handler;
        mConnection = new MediaScannerConnection(mContext, this);
        if (V) Log.v(TAG, "Connecting to MediaScannerConnection");
    }

    public void onMediaScannerConnected() {
        if (V) Log.v(TAG, "MediaScannerConnection onMediaScannerConnected");
        mConnection.scanFile(path, mimeType);
    }

    public void onScanCompleted(String path, Uri uri) {
        try {
            if (V) {
                Log.v(TAG, "MediaScannerConnection onScanCompleted");
                Log.v(TAG, "MediaScannerConnection path is " + path);
                Log.v(TAG, "MediaScannerConnection Uri is " + uri);
            }
            if (uri != null) {
                Message msg = Message.obtain();
                msg.setTarget(mCallback);
                msg.obj = uri;
                msg.sendToTarget();
            } 
        } catch (Exception ex) {
            Log.v(TAG, "MediaScannerConnection exception: " + ex);
        } finally {
            if (V) Log.v(TAG, "MediaScannerConnection disconnect");
            mConnection.disconnect();
        }
    }
    
    /**
     * 扫描文件标签信息
     * 
     * @param filePath 文件路径 eg:/sdcard/MediaPlayer/dahai.mp3
     * @param fileType 文件类型 eg: audio/mp3 media /* application/ogg
     * */
    public void scanFile(String filepath, String fileType) {
           this.path = filepath;
           this.mimeType = fileType;
           // 连接之后调用onMediaScannerConnected()方法
           mConnection.connect();
    }
}