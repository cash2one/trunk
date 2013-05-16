package com.shandagames.android.plugin.bean;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class PluginFeature implements Parcelable {
	private String featureName;
	private PluginDescription pluginDesc = new PluginDescription();
	private List<PluginFeatureMethod> methods = new ArrayList<PluginFeatureMethod>();

	public PluginFeature() {
	}

	private PluginFeature(Parcel in) {
		featureName = in.readString();
		pluginDesc = in.readParcelable(PluginDescription.class.getClassLoader());
		in.readTypedList(methods, PluginFeatureMethod.CREATOR);
	}

	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public PluginDescription getPluginDesc() {
		return pluginDesc;
	}

	public void setPluginDesc(PluginDescription pluginDesc) {
		this.pluginDesc = pluginDesc;
	}

	public List<PluginFeatureMethod> getMethods() {
		return this.methods;
	}

	public void addMethod(PluginFeatureMethod method) {
		this.methods.add(method);
	}

	public static final Parcelable.Creator<PluginFeature> CREATOR = new Parcelable.Creator<PluginFeature>() {
		public PluginFeature createFromParcel(Parcel in) {
			return new PluginFeature(in);
		}

		public PluginFeature[] newArray(int size) {
			return new PluginFeature[size];
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
		dest.writeString(featureName);
		dest.writeParcelable(pluginDesc, flags);
		dest.writeTypedList(methods);
	}
}