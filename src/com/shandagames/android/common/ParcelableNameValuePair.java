/**
 * 
 */
package com.shandagames.android.common;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file ParcelableNameValuePair.java
 * @create 2012-11-14 上午10:51:07
 * @author lilong
 * @description If you need to send parameters with the request you can add a list of
 * 				parcelableNameValuePair directly to the intent with the IntentRequestBuilder.
 */
public class ParcelableNameValuePair extends BasicNameValuePair implements
		Parcelable, NameValuePair {

	private ParcelableNameValuePair(Parcel parcel) {
		this(parcel.readString(), parcel.readString());
	}

	/**
	 * @param name
	 * @param value
	 */
	public ParcelableNameValuePair(String name, String value) {
		super(name, value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getName());
		dest.writeString(getValue());
	}

	public static final Parcelable.Creator<ParcelableNameValuePair> CREATOR = new Parcelable.Creator<ParcelableNameValuePair>() {
		@Override
		public ParcelableNameValuePair createFromParcel(Parcel parcel) {
			return new ParcelableNameValuePair(parcel);
		}

		@Override
		public ParcelableNameValuePair[] newArray(int size) {
			return new ParcelableNameValuePair[size];
		}
	};

}
