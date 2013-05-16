package com.shandagames.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.shandagames.android.parser.ResultType;

/**
 * @file Place.java
 * @create 2012-10-9 下午1:39:54
 * @author lilong
 * @description Implement this class from "Serializable" So that you can pass
 *              this class Object to another using Intents Otherwise you can't
 *              pass to another actitivy
 */
public class Place implements ResultType, Parcelable {
	private String id;
	private String name;
	private String reference;
	private String icon;
	private String vicinity;
	private Geometry geometry;
	private String formatted_address;
	private String formatted_phone_number;

	public Place() {
	}

	private Place(Parcel in) {
		id = in.readString();
		name = in.readString();
		reference = in.readString();
		icon = in.readString();
		vicinity = in.readString();
		geometry = in.readParcelable(Geometry.class.getClassLoader());
		formatted_address = in.readString();
		formatted_phone_number = in.readString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}

	public void setFormatted_phone_number(String formatted_phone_number) {
		this.formatted_phone_number = formatted_phone_number;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
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
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(reference);
		dest.writeString(icon);
		dest.writeString(vicinity);
		dest.writeParcelable(geometry, flags);
		dest.writeString(formatted_address);
		dest.writeString(formatted_phone_number);
	}

	public static class Geometry implements ResultType, Parcelable {
		private Location location;

		public Geometry() {
		}

		private Geometry(Parcel in) {
			location = in.readParcelable(Location.class.getClassLoader());
		}
		
		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public static final Parcelable.Creator<Geometry> CREATOR = new Parcelable.Creator<Geometry>() {
			public Geometry createFromParcel(Parcel in) {
				return new Geometry(in);
			}

			public Geometry[] newArray(int size) {
				return new Geometry[size];
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
			dest.writeParcelable(location, flags);
		}
	}

	public static class Location implements ResultType, Parcelable {
		private double lat;
		private double lng;

		public Location() {
		}

		private Location(Parcel in) {
			lat = in.readDouble();
			lng = in.readDouble();
		}
		
		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
			public Location createFromParcel(Parcel in) {
				return new Location(in);
			}

			public Location[] newArray(int size) {
				return new Location[size];
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
			dest.writeDouble(lat);
			dest.writeDouble(lng);
		}
	}

}
