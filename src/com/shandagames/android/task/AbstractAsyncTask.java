package com.shandagames.android.task;

import android.os.AsyncTask;
import android.os.Build;
import java.util.concurrent.Executor;
import android.annotation.TargetApi;

/**
 * @file AbstractAsyncTask.java
 * @create 2013-8-9 下午03:19:44
 * @author lilong
 * @description TODO 自定义异步任务类，封装任务处理
 */
public abstract class AbstractAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	// 任务名称
	private String mTaskName;
	// 监听事件
	private ITaskListener mTaskListener;
	
	// 默认构造函数
	AbstractAsyncTask(String taskName, ITaskListener taskListener) {
		this.mTaskName = taskName;
		this.mTaskListener = taskListener;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mTaskListener != null) {
			mTaskListener.onTaskStart(mTaskName);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (mTaskListener != null) {
			mTaskListener.onTaskFinished(mTaskName, result);
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		if (mTaskListener != null) {
			mTaskListener.onTaskLoading(values[0]);
		} 
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public void cancelTask(boolean isCancelable) {
		if (isCancelable && getStatus()==Status.RUNNING) {
			cancel(true);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public final void execute(Executor aExecutor, Params... aParams) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			super.execute(aParams);
		} else {
			super.executeOnExecutor(aExecutor, aParams);
		}
	}
}
