package com.shandagames.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

	private int id;
	// 姓名
	private String name;
	// 手机号码
	private String phone;
	// 电话号码
	private String tel;
	// 电子邮件
	private String email;
	// 地址
	private String address;
	// 备注
	private String backContent;

	public Person() {
	}

	public Person(int id, String name, String phone, String tel, String email,
			String address, String backContent) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.tel = tel;
		this.email = email;
		this.address = address;
		this.backContent = backContent;
	}

	public Person(Parcel in) {
		id = in.readInt();
		name = in.readString();
		phone = in.readString();
		tel = in.readString();
		email = in.readString();
		address = in.readString();
		backContent = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBackContent() {
		return backContent;
	}

	public void setBackContent(String backContent) {
		this.backContent = backContent;
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		public Person createFromParcel(Parcel in) {
			return new Person(in);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeInt(id);
		out.writeString(name);
		out.writeString(phone);
		out.writeString(tel);
		out.writeString(email);
		out.writeString(address);
		out.writeString(backContent);
	}

}
