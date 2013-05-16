package com.shandagames.android.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class DownloadService extends Service {

	public static final String DOWNLOAD_TYPE 	=	"DOWNLOAD_TYPE";
	public static final String DOWNLOAD_URL		= 	"DOWNLOAD_URL";
	
	public static final int DOWNLOAD_TYPE_START = 		0;
	public static final int DOWNLOAD_TYPE_ADD 	= 		1;
	public static final int DOWNLOAD_TYPE_PAUSE = 		2;
	public static final int DOWNLOAD_TYPE_STOP 	= 		3;
	public static final int DOWNLOAD_TYPE_CONTINUE = 	4;

	private DownloadManager mDownloadManager;

	@Override
	public IBinder onBind(Intent intent) {
		return new DownloadServiceImpl();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDownloadManager = new DownloadManager(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		int type = intent.getIntExtra(DOWNLOAD_TYPE, 0);
		String url = intent.getStringExtra(DOWNLOAD_URL);
		
		switch (type) {
			case DOWNLOAD_TYPE_START:
				mDownloadManager.startManage();
				break;
			case DOWNLOAD_TYPE_ADD:
				if (url!=null&&url.length()>0) {
					mDownloadManager.addTask(url);
				}
				break;
			case DOWNLOAD_TYPE_PAUSE:
				mDownloadManager.pauseTask(url);
				break;
			case DOWNLOAD_TYPE_STOP:
				mDownloadManager.close();
				break;
			case DOWNLOAD_TYPE_CONTINUE:
				if (url!=null&&url.length()>0) {
					mDownloadManager.continueTask(url);
				}
				break;
			default:
				break;
		}
		
		return START_REDELIVER_INTENT;
	}

	private class DownloadServiceImpl extends IDownloadService.Stub {

		@Override
		public void startManage() throws RemoteException {
			mDownloadManager.startManage();
		}

		@Override
		public void addTask(String url) throws RemoteException {
			mDownloadManager.addTask(url);
		}

		@Override
		public void pauseTask(String url) throws RemoteException {
			mDownloadManager.pauseTask(url);
		}

		@Override
		public void deleteTask(String url) throws RemoteException {
			mDownloadManager.deleteTask(url);
		}

		@Override
		public void continueTask(String url) throws RemoteException {
			mDownloadManager.continueTask(url);
		}

	}
}
