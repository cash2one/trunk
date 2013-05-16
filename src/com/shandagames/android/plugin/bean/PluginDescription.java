package com.shandagames.android.plugin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file PluginDescription.java
 * @create 2013-3-25 下午03:27:26
 * @author lilong
 * @description TODO 插件的详细描述
 */
public class PluginDescription implements Parcelable {

	private String icon;
	private String title;
	private String description;

	public PluginDescription() {
	}

	private PluginDescription(Parcel in) {
		icon = in.readString();
		title = in.readString();
		description = in.readString();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static final Parcelable.Creator<PluginDescription> CREATOR = new Parcelable.Creator<PluginDescription>() {
		public PluginDescription createFromParcel(Parcel in) {
			return new PluginDescription(in);
		}

		public PluginDescription[] newArray(int size) {
			return new PluginDescription[size];
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
		dest.writeString(icon);
		dest.writeString(title);
		dest.writeString(description);
	}

}
