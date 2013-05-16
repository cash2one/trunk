package com.shandagames.android.common;

//http://blog.csdn.net/furongkang/article/details/6857610
public class NativeSample {

	static {
		// 静态代码块，先于构造函数执行,在此会对C/C++代码的库即：*.so文件进行加载
		// 注意最终生成的动态库文件后缀名为.so, 加载时候不要加上后缀名，而在Linux开发环境中加载动态库时候需要加上后缀,切记这点
		System.loadLibrary("myjni");
	}
	
	//声明native函数speak(),此函数的实现是在C/C++代码中,通过动态库进行调用
	public native String sayHi();
}
