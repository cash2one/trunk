/**
 * 
 */
package com.shandagames.android.task;

/**
 * @file TaskListener.java
 * @create 2012-10-11 下午5:13:03
 * @author Jacky.Lee
 * @description TODO
 */
public interface TaskListener<T> {

	
	void onTaskStart(String taskName);
	
	void onTaskFinished(String taskName, T result);
}
