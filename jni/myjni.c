#include <jni.h>
#include <string.h>

jstring Java_com_shandagames_android_common_NativeSample_sayHi
  (JNIEnv *env, jobject obj) {
	return (*env)->NewStringUTF(env, "Hello from My-JNI !");
}
