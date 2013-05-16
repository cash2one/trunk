package com.shandagames.android.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.Queue;

import com.shandagames.android.download.DownloadObservable.ObservableData;

public class DownloadManager extends Thread {
	public static final String SDCARD_ROOT = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	
	private static final int MAX_TASK_COUNT = 10;
	private static final int MAX_DOWNLOAD_THREAD_COUNT = 3;

	private Context mContext;
	private final String mFileRoot;
	private final TaskQueue mTaskQueue;
	private final List<DownloadTask> mDownloadingTasks;
	private final List<DownloadTask> mPausingTasks;
	
	private boolean isRunning = false;
	
	private DownloadObservable mObservable;
	
	public DownloadManager(Context context) {
		mContext = context;
		mTaskQueue = new TaskQueue();
		mDownloadingTasks = new ArrayList<DownloadTask>();
		mPausingTasks = new ArrayList<DownloadTask>();
		mFileRoot = SDCARD_ROOT + "";
		mObservable = new DownloadObservable();
	}
	
	public void startManage() {
		if (!isRunning) {
			isRunning = true;
			this.start();
		}
	}

	public void close() {
		isRunning = false;
		pauseAllTask();
	}

	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void run() {
		super.run();
		while (isRunning) {
			DownloadTask task = mTaskQueue.poll();
			mDownloadingTasks.add(task);
			task.execute();
		}
	}

	public void addTask(String url) {
		if (getTotalTaskCount() >= MAX_TASK_COUNT) {
			Toast.makeText(mContext, "任务列表已满", Toast.LENGTH_LONG).show();
			return;
		}

		try {
			addTask(newDownloadTask(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	private void addTask(DownloadTask task) {
		mTaskQueue.offer(task);

		if (!this.isAlive()) {
			this.start();
		}
	}

	public boolean hasTask(String url) {
		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task.getUrl().equals(url)) {
				return true;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
		}
		return false;
	}

	public DownloadTask getTask(int position) {
		if (position >= mDownloadingTasks.size()) {
			return mTaskQueue.get(position - mDownloadingTasks.size());
		} else {
			return mDownloadingTasks.get(position);
		}
	}

	public int getQueueTaskCount() {
		return mTaskQueue.size();
	}

	public int getDownloadingTaskCount() {
		return mDownloadingTasks.size();
	}

	public int getPausingTaskCount() {
		return mPausingTasks.size();
	}

	public int getTotalTaskCount() {
		return getQueueTaskCount() + getDownloadingTaskCount() + getPausingTaskCount();
	}

	public void pauseTask(DownloadTask task) {
		if (task != null) {
			task.onCancelled();

			// move to pausing list
			String url = task.getUrl();
			try {
				mDownloadingTasks.remove(task);
				task = newDownloadTask(url);
				mPausingTasks.add(task);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pauseTask(String url) {
		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				pauseTask(task);
			}
		}
	}

	public void pauseAllTask() {
		DownloadTask task;
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			mTaskQueue.remove(task);
			mPausingTasks.add(task);
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null) {
				pauseTask(task);
			}
		}
	}

	public void deleteTask(String url) {
		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				// 删除文件
				task.onCancelled();
				completeTask(task);
				return;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mTaskQueue.remove(task);
			}
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mPausingTasks.remove(task);
			}
		}
	}

	public void continueTask(String url) {
		DownloadTask task;
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				continueTask(task);
			}
		}
	}

	public void continueTask(DownloadTask task) {
		if (task != null) {
			mPausingTasks.remove(task);
			mTaskQueue.offer(task);
		}
	}

	public void completeTask(DownloadTask task) {
		if (mDownloadingTasks.contains(task)) {
			mDownloadingTasks.remove(task);
		}
	}

	public void addObserver(Observer observer) {
		if (mObservable != null) {
			mObservable.addObserver(observer);
		}
	}
	
	public void deleteObserver(Observer observer) {
		if (mObservable != null) {
			mObservable.deleteObserver(observer);
		}
	}
	
	/**
	 * Create a new download task with default config
	 */
	private DownloadTask newDownloadTask(String in)
			throws MalformedURLException {
		File root = new File(mFileRoot);
		if (root.exists()) {
			delete(root);
		}

		if (!root.mkdirs()) {
			Log.e(null, "Failed to make directories: " + root.getAbsolutePath());
			return null;
		}
		
		return new DownloadTask(mContext, in, mFileRoot, downloadTaskListener);
	}

	public static boolean delete(File path) {
		boolean result = true;
		if (path.exists()) {
			if (path.isDirectory()) {
				for (File child : path.listFiles()) {
					result &= delete(child);
				}
				result &= path.delete(); // Delete empty directory.
			}
			if (path.isFile()) {
				result &= path.delete();
			}
			if (!result) {
				Log.e(null, "Delete failed;");
			}
			return result;
		} else {
			Log.e(null, "File does not exist.");
			return false;
		}
	}
	
	private DownloadTaskListener downloadTaskListener = new DownloadTaskListener() {

		@Override
		public void updateProcess(DownloadTask task) {
			ObservableData data = new ObservableData();
			data.setKey(DownloadObservable.NOTIFICATIONI_UPDATE_PROGRESS);
			data.setDownloadTask(task);
			mObservable.notifyObservers(data);
		}

		@Override
		public void finishDownload(DownloadTask task) {
			completeTask(task);
			
			ObservableData data = new ObservableData();
			data.setKey(DownloadObservable.NOTIFICATIONI_FINISHED_DOWNLOAD);
			data.setDownloadTask(task);
			mObservable.notifyObservers(data);
		}

		@Override
		public void preDownload() {
			ObservableData data = new ObservableData();
			data.setKey(DownloadObservable.NOTIFICATIONI_PREPARE_DOWNLOAD);
			mObservable.notifyObservers(data);
		}

		@Override
		public void errorDownload(int error) {
			ObservableData data = new ObservableData();
			data.setKey(DownloadObservable.NOTIFICATIONI_ERROR_DOWNLOAD);
			mObservable.notifyObservers(data);
		}
		
	};
	
	private final class TaskQueue {
		
		private Queue<DownloadTask> taskQueue;

		public TaskQueue() {
			taskQueue = new LinkedList<DownloadTask>();
		}

		public void offer(DownloadTask task) {
			taskQueue.offer(task);
		}

		public DownloadTask poll() {
			DownloadTask task = null;
			while (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT
					|| (task = taskQueue.poll()) == null) {
				try {
					Thread.sleep(1000); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return task;
		}

		public DownloadTask get(int position) {
			if (position >= size()) {
				return null;
			}
			return ((LinkedList<DownloadTask>) taskQueue).get(position);
		}

		public int size() {
			return taskQueue.size();
		}

		@SuppressWarnings("unused")
		public boolean remove(int position) {
			return taskQueue.remove(get(position));
		}

		public boolean remove(DownloadTask task) {
			return taskQueue.remove(task);
		}
	}

}
