package com.shandagames.android.plugin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file PluginFeatureMethod.java
 * @create 2013-3-25 下午02:52:38
 * @author Jacky.Lee
 * @description TODO 插件方法描述
 */
public class PluginFeatureMethod implements Parcelable {
	private String methodName;
	private String description;
	private boolean needContext;

	public PluginFeatureMethod() {
	}

	private PluginFeatureMethod(Parcel in) {
		methodName = in.readString();
		description = in.readString();
		needContext = in.readByte() == 1 ? true : false;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean needContext() {
		return this.needContext;
	}

	public void setNeedContext(boolean needContext) {
		this.needContext = needContext;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static final Parcelable.Creator<PluginFeatureMethod> CREATOR = new Parcelable.Creator<PluginFeatureMethod>() {
		public PluginFeatureMethod createFromParcel(Parcel in) {
			return new PluginFeatureMethod(in);
		}

		public PluginFeatureMethod[] newArray(int size) {
			return new PluginFeatureMethod[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(methodName);
		dest.writeString(description);
		dest.writeByte(needContext ? (byte) 1 : (byte) 0);
	}
}