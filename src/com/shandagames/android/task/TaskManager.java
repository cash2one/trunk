/**
 * 
 */
package com.shandagames.android.task;

import java.util.Observable;
import java.util.Observer;

/**
 * @file ActivityObserable.java
 * @create 2012-8-30 上午11:49:42
 * @author Jacky.Lee
 * @description TODO
 */
public class TaskManager extends Observable {

	private static TaskManager instance;
	
	private TaskManager() {
	}

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}
	
	
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	public void addTask(Observer observer) {
		super.addObserver(observer);
	}

	public void deleteTask(Observer observer) {
		super.deleteObserver(observer);
	}

	
	public static class Entry {

		private String _key;
		private Object _value;

		public Entry() {
		}

		public Entry(String _key, Object _value) {
			super();
			this._key = _key;
			this._value = _value;
		}

		public String getKey() {
			return _key;
		}

		public void setKey(String _key) {
			this._key = _key;
		}

		public Object getValue() {
			return _value;
		}

		public void setValue(Object _value) {
			this._value = _value;
		}

	}

}
