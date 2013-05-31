//导入日志头文件
#include <android/log.h>

//引入头文件
#include <string.h>
#include "NativeSample01.h"

//修改日志tag中的值
#define LOG_TAG "logfromc"

//日志显示的等级
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// 其中 JENEnv* 代表的是java环境 , 通过这个环境可以调用java的方法，jobject 表示哪个对象调用了 这个c语言的方法, thiz就表示的是当前的对象

// java中的jstring, 转化为c的一个字符数组
char* Jstring2CStr(JNIEnv* env, jstring jstr) {
	char* rtn = NULL;
	jclass clsstring = (*env)->FindClass(env, "java/lang/String");
	jstring strencode = (*env)->NewStringUTF(env, "GB2312");
	jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray)(*env)->CallObjectMethod(env, jstr, mid,
			strencode); // String.getByte("GB2312");
	jsize alen = (*env)->GetArrayLength(env, barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1); //new char[alen+1]; "\0"
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0); //释放内存

	return rtn;
}

//处理整形相加
JNIEXPORT jint JNICALL Java_com_shandagames_android_jni_NativeSample01_add(
		JNIEnv *env, jobject obj, jint x, jint y) {
	//打印 java 传递过来的 jstring ;
	LOGI("log from c code ");
	LOGI("x= %ld",x);
	LOGD("y= %ld",y);
	return x + y;
}

//处理字符串追加
JNIEXPORT jstring JNICALL Java_com_shandagames_android_jni_NativeSample01_sayHelloInC(
		JNIEnv *env, jobject obj, jstring str) {
	char* p = Jstring2CStr(env, str);
	LOGI("%s",p);
	char* newstr = "append string";

	//strcat(dest, sorce) 把sorce字符串添加到dest字符串的后面
	LOGI("END");
	return (*env)->NewStringUTF(env, strcat(p, newstr));
}

//处理数组中的每一个元素
JNIEXPORT jintArray JNICALL Java_com_shandagames_android_jni_NativeSample01_intMethod(
		JNIEnv *env, jobject obj, jintArray arr) {
	// 1.获取到 arr的大小
	int len = (*env)->GetArrayLength(env, arr);
	LOGI("len=%d", len);

	if (len == 0) {
		return arr;
	}

	//取出数组中第一个元素的内存地址
	jint* p = (*env)->GetIntArrayElements(env, arr, 0);
	int i = 0;
	for (; i < len; i++) {
		LOGI("len=%ld", *(p+i));//取出的每个元素
		*(p + i) += 5; //取出的每个元素加五
	}
	return arr;
}
