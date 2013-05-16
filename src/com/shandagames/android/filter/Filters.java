package com.shandagames.android.filter;


public class Filters {
	static{
		System.loadLibrary("filters-jni");
	}

	public native static void nashville(int[] pixels, int w, int h);

	public native static void lordkelvin(int[] pixels, int w, int h);
	
	public native static void blurcircle(int[] pixels, int w, int h);

	public native static void hefe(int[] pixels, int w, int h);
	
	public native static void xproII(int[] pixels, int w, int h);

	public native static void highlight(int[] pixels, int w, int h);

	public native static void lomo(int[] pixels, int w, int h);

	public native static void gray(int[] pixels, int w, int h);

	public native static void emboss(int[] pixels, int w, int h);

	public native static void oil(int[] pixels, int w, int h);
	
}
