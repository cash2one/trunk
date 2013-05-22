package com.shandagames.android.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.shandagames.android.util.ParcelUtil;

public class User implements Parcelable {

	private long id;
	private int age;
	private String nickName;
	private String userName;
	private String homeCity;
	private String photo;
	private String gender;
	private String signature;
	private String birthday;

	public User() {
	}

	private User(Parcel in) {
		id = ParcelUtil.readLongFromParcel(in);
		age = ParcelUtil.readIntFromParcel(in);
		nickName = ParcelUtil.readStringFromParcel(in);
		userName= ParcelUtil.readStringFromParcel(in);
		homeCity=ParcelUtil.readStringFromParcel(in);
		photo=ParcelUtil.readStringFromParcel(in);
		gender=ParcelUtil.readStringFromParcel(in);
		signature=ParcelUtil.readStringFromParcel(in);
		birthday=ParcelUtil.readStringFromParcel(in);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdString(){
		return Long.toString(id);
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean hasAvatar() {
		return (photo != null && photo.length() > 0);
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isMale(){
		if(gender!=null && gender.equals("male")){
			return true;
		}
		return false;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtil.writeLongToParcel(out, id);
		ParcelUtil.writeIntToParcel(out, age);
		ParcelUtil.writeStringToParcel(out, nickName);
		ParcelUtil.writeStringToParcel(out, userName);
		ParcelUtil.writeStringToParcel(out, homeCity);
		ParcelUtil.writeStringToParcel(out, photo);
		ParcelUtil.writeStringToParcel(out, gender);
		ParcelUtil.writeStringToParcel(out, signature);
		ParcelUtil.writeStringToParcel(out, birthday);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof User){
			if(((User) o).id==this.id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
