package com.shandagames.android.task;

import java.io.File;
import android.os.Environment;
import com.shandagames.android.http.BetterHttpApiV1;
import com.shandagames.android.network.CountingOutputStream.Progress;

/**
 * @file DownloadFileTask.java
 * @create 2013-8-9 下午03:40:48
 * @author lilong
 * @description TODO 网络下载任务
 */
public class DownloadFileTask extends AbstractAsyncTask<String, Integer, Void> {

	public DownloadFileTask(String taskName, ITaskListener taskListener) {
		super(taskName, taskListener);
	}

	@Override
	protected Void doInBackground(String... params) {
		File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".apk");
		BetterHttpApiV1 httpRequest = BetterHttpApiV1.getInstance();
		httpRequest.getRequest().downloadFileToDisk(params[0], file.getAbsolutePath(), new Progress() {
			@Override
			public void transferred(long num, long totalSize) {
				publishProgress((int)((num * 100) / totalSize));
			}
		});
		return null;
	}
	
}
