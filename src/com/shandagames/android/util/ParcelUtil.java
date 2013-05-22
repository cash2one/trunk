package com.shandagames.android.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file ParcelUtil.java
 * @create 2013-5-22 下午02:02:58
 * @author lilong
 * @description TODO dreamxsky@gmail.com
 */
public class ParcelUtil {

	public static void writeStringToParcel(Parcel out, String str) {
		if (str != null) {
			out.writeInt(1);
			out.writeString(str);
		} else {
			out.writeInt(0);
		}
	}

	public static String readStringFromParcel(Parcel in) {
		int flag = in.readInt();
		if (flag == 1) {
			return in.readString();
		} else {
			return null;
		}
	}

	public static void writeBooleanToParcel(Parcel out, boolean value) {
		if (value) {
			out.writeByte((byte) 1);
		} else {
			out.writeByte((byte) 0);
		}
	}

	public static boolean readBooleanFromParcel(Parcel in) {
		byte value = in.readByte();
		if (value > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void writeIntToParcel(Parcel out, int value) {
		out.writeInt(value);
	}

	public static int readIntFromParcel(Parcel in) {
		return in.readInt();
	}

	public static void writeLongToParcel(Parcel out, long value) {
		out.writeLong(value);
	}

	public static long readLongFromParcel(Parcel in) {
		return in.readLong();
	}

	public static void writeFloatToParcel(Parcel out, float value) {
		out.writeFloat(value);
	}

	public static float readFloatFromParcel(Parcel in) {
		return in.readFloat();
	}

	public static void writeDoubleToParcel(Parcel out, double value) {
		out.writeDouble(value);
	}

	public static Double readDoubleFromParcel(Parcel in) {
		return in.readDouble();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Parcelable> T readParcelableFromParcel(Parcel in,
			ClassLoader loader) {
		return (T) in.readParcelable(loader);
	}

	public static void writeParcelableToParcel(Parcel out, Parcelable value,
			int parcelableFlags) {
		out.writeParcelable(value, parcelableFlags);
	}
}