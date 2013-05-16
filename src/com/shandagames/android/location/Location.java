package com.shandagames.android.location;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file Location.java
 * @create 2013-2-1 上午11:03:46
 * @author lilong
 * @description TODO
 */
public class Location implements Parcelable {

	private double geolat;
	private double geolng;
	private String address="";
	private float acc=-1;
	
	public Location() {
	}

	private Location(Parcel in) {
		geolat = in.readDouble();
		geolng = in.readDouble();
		address = in.readString();
		acc = in.readFloat();
	}

	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
		public Location createFromParcel(Parcel in) {
			return new Location(in);
		}

		public Location[] newArray(int size) {
			return new Location[size];
		}
	};

	public String getGeolatString() {
		return Double.toString(geolat);
	}

	public void setGeolat(double geolat) {
		this.geolat = geolat;
	}

	public String getGeolngString() {
		return Double.toString(geolng);
	}

	public void setGeolng(double geolng) {
		this.geolng = geolng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getGeolat() {
		return geolat;
	}

	public double getGeolng() {
		return geolng;
	}

	public float getAcc() {
		return acc;
	}

	public void setAcc(float acc) {
		this.acc = acc;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(geolat);
		out.writeDouble(geolng);
		out.writeString(address);
		out.writeFloat(acc);
	}

	@Override
	public String toString() {
		return "Location [geolat=" + geolat + ", geolng=" + geolng
				+ ", address=" + address + ", acc=" + acc + "]";
	}

}
