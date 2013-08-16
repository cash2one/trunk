package com.shandagames.android.task;

public interface ITaskListener {

	// 开始任务
	void onTaskStart(String taskName);

	// 执行任务
	void onTaskLoading(Object value);

	// 完成任务
	void onTaskFinished(String taskName, Object result);

	// 取消任务
	/*void onCancelTask(String taskName);*/
}