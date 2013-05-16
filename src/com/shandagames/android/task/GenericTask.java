package com.shandagames.android.task;

/**
 * @file GenericTask.java
 * @create 2013-4-9 下午03:10:17
 * @author lilong
 * @description TODO
 */
public abstract class GenericTask<Result> extends AsyncTaskEx<String, Integer, Result> {

	private String taskName;
	private TaskListener mListener = null;

	protected GenericTask(String taskName, TaskListener taskListener) {
		this.taskName = taskName;
		this.mListener = taskListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mListener != null) {
			mListener.onTaskStart(taskName);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (mListener != null) {
			mListener.onTaskFinished(taskName, result);
		}
	}

	public void cancelTask(boolean isCancelable) {
		if (isCancelable && getStatus() == Status.RUNNING) {
			cancel(true);
		}
	}

	@Override
	abstract protected Result doInBackground(String... params);

	
	protected String getTaskName() {
		return this.taskName;
	}
}
