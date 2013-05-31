#include <string.h>
#include <android/log.h>
#include "NativeSample02.h"

#define LOG_TAG "logfromc"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//1.调用java中的无参helloFromJava方法
JNIEXPORT void JNICALL Java_com_shandagames_android_jni_NativeSample02_callCcode
(JNIEnv *env, jobject obj) {
	// 获取到DataProvider对象
	char* classname = "com/shandagames/android/jni/NativeSample02";
	jclass dpclazz = (*env)->FindClass(env,classname);
	if (dpclazz == 0) {
		LOGI("not find class!");
	} else {
		LOGI("find class");
	}
	//第三个参数 和第四个参数 是方法的签名,第三个参数是方法名  , 第四个参数是根据返回值和参数生成的
	//获取到NativeSample02要调用的方法
	jmethodID methodID = (*env)->GetMethodID(env,dpclazz,"helloFromJava","()V");
	if (methodID == 0) {
		LOGI("not find method!");
	} else {
		LOGI("find method");
	}
	//调用这个方法
	(*env)->CallVoidMethod(env, obj,methodID);
}

// 2.调用java中的printString方法传递一个字符串
JNIEXPORT void JNICALL Java_com_shandagames_android_jni_NativeSample02_callCcode1
(JNIEnv *env, jobject obj) {
	LOGI("in code");
	// 获取到DataProvider对象
	char* classname = "com/shandagames/android/jni/NativeSample02";
	jclass dpclazz = (*env)->FindClass(env,classname);
	if (dpclazz == 0) {
		LOGI("not find class!");
	} else {
		LOGI("find class");
	}
	// 获取到要调用的method
	jmethodID methodID = (*env)->GetMethodID(env,dpclazz,"printString","(Ljava/lang/String;)V");
	if (methodID == 0) {
		LOGI("not find method!");
	} else {
		LOGI("find method");
	}
	//调用这个方法
	(*env)->CallVoidMethod(env, obj,methodID,(*env)->NewStringUTF(env,"Hello"));
}

// 3. 调用java中的add方法 , 传递两个参数 jint x,y
JNIEXPORT void JNICALL Java_com_shandagames_android_jni_NativeSample02_callCcode2
(JNIEnv *env, jobject obj) {
	char* classname = "com/shandagames/android/jni/NativeSample02";
	jclass dpclazz = (*env)->FindClass(env,classname);
	jmethodID methodID = (*env)->GetMethodID(env,dpclazz,"add","(II)I");
	(*env)->CallIntMethod(env, obj,methodID,3l,4l);
}
