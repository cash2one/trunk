/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_shandagames_android_jni_NativeSample01 */
/* http://blog.csdn.net/xyz_lmn/article/details/6959545  */

#ifndef _Included_com_shandagames_android_jni_NativeSample01
#define _Included_com_shandagames_android_jni_NativeSample01
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_shandagames_android_jni_NativeSample01  表示Native方法的类名称
 * Method:    add	表示方法名称
 * Signature: (II)I	是方法的标识，它是一个标识符，主要供我们在JNI操作java对象的方法使用的;
 * Signature一般是两部分构成，一个方法的参数，另一个是返回类型。方法参数在括号里面，返回类型在后面;
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
