package com.shandagames.android.jni;

/**
 * @file NativeSample02.java
 * @create 2013-5-31 下午03:07:17
 * @author lilong
 * @description TODO C调用Java进行数据传递
 */
public class NativeSample02 {

	static {
		System.loadLibrary("NativeSample02");
	}
	
	public native void callCcode();

	public native void callCcode1();

	public native void callCcode2();

	// /C调用java中的空方法
	public void helloFromJava() {
		System.out.println("hello from java ");
	}

	// C调用java中的带两个int参数的方法
	public int add(int x, int y) {
		System.out.println("相加的结果为" + (x + y));
		return x + y;
	}

	// C调用java中参数为string的方法
	public void printString(String s) {
		System.out.println("in java code " + s);
	}

}
