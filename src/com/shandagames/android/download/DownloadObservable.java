package com.shandagames.android.download;

import java.util.Observable;

public class DownloadObservable extends Observable {

	public static final String NOTIFICATIONI_PREPARE_DOWNLOAD = "NOTIFICATIONI_PREPARE_DOWNLOAD";
	public static final String NOTIFICATIONI_UPDATE_PROGRESS = "NOTIFICATIONI_UPDATE_PROGRESS";
	public static final String NOTIFICATIONI_FINISHED_DOWNLOAD = "NOTIFICATIONI_FINISHED_DOWNLOAD";
	public static final String NOTIFICATIONI_ERROR_DOWNLOAD = "NOTIFICATIONI_ERROR_DOWNLOAD";
	
	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	public static class ObservableData {

		private String key;
		private DownloadTask downloadTask;

		public ObservableData() {
		}
		
		public ObservableData(String _key, DownloadTask task) {
			this.key = _key;
			this.downloadTask = task;
		}
		
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public DownloadTask getDownloadTask() {
			return downloadTask;
		}

		public void setDownloadTask(DownloadTask downloadTask) {
			this.downloadTask = downloadTask;
		}

		@Override
		public String toString() {
			return this.key;
		}
	}
}
