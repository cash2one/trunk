/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_shandagames_android_jni_NativeSample01 */

#ifndef _Included_com_shandagames_android_jni_NativeSample01
#define _Included_com_shandagames_android_jni_NativeSample01
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_shandagames_android_jni_NativeSample01
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_shandagames_android_jni_NativeSample01_add
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_shandagames_android_jni_NativeSample01
 * Method:    sayHelloInC
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_shandagames_android_jni_NativeSample01_sayHelloInC
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_shandagames_android_jni_NativeSample01
 * Method:    intMethod
 * Signature: ([I)[I
 */
JNIEXPORT jintArray JNICALL Java_com_shandagames_android_jni_NativeSample01_intMethod
  (JNIEnv *, jobject, jintArray);

#ifdef __cplusplus
}
#endif
#endif
