package com.shandagames.android.download;

public interface DownloadTaskListener {
	
	public void updateProcess(DownloadTask task); // 更新进度

	public void finishDownload(DownloadTask task); // 完成下载

	public void preDownload(DownloadTask task); // 准备下载

	public void errorDownload(DownloadTask task, int error); // 下载错误
}
