package com.shandagames.android.plugin.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Plugin implements Parcelable {
	private Context context;
	private String pluginLable;
	private PackageInfo pkgInfo;
	private List<PluginFeature> features = new ArrayList<PluginFeature>();

	public Plugin() {
	}

	private Plugin(Parcel in) {
		pluginLable = in.readString();
		pkgInfo = in.readParcelable(PackageInfo.class.getClassLoader());
		in.readTypedList(features, PluginFeature.CREATOR);
	}

	public String getPluginLable() {
		return this.pluginLable;
	}

	public void setPluginLable(String pluginLable) {
		this.pluginLable = pluginLable;
	}

	public PackageInfo getPkgInfo() {
		return this.pkgInfo;
	}

	public void setPkgInfo(PackageInfo pkgInfo) {
		this.pkgInfo = pkgInfo;
	}

	public List<PluginFeature> getFeatures() {
		return this.features;
	}

	public void addFeature(PluginFeature feature) {
		this.features.add(feature);
	}

	public void setFeatures(List<PluginFeature> features) {
		this.features = features;
	}

	public Context getContext() {
		return this.context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static final Parcelable.Creator<Plugin> CREATOR = new Parcelable.Creator<Plugin>() {
		public Plugin createFromParcel(Parcel in) {
			return new Plugin(in);
		}

		public Plugin[] newArray(int size) {
			return new Plugin[size];
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
		dest.writeString(pluginLable);
		dest.writeParcelable(pkgInfo, flags);
		dest.writeTypedList(features);
	}
}