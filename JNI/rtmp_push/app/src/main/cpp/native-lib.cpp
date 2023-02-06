#include <jni.h>
#include <string>

// #include "librtmp/rtmp.h"
#include <rtmp.h> // 查找系统环境变量
#include <x264.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_rtmp_1push_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    char version[50];
    sprintf(version, "librtmp version:%d", RTMP_LibVersion());

    //============================================= x264


    //============================================= faac
    return env->NewStringUTF(version);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1init(JNIEnv *env, jobject thiz) {
    // TODO: implement native_init()

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1start(JNIEnv *env, jobject thiz, jstring path) {
    // TODO: implement native_start()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1stop(JNIEnv *env, jobject thiz) {
    // TODO: implement native_stop()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1release(JNIEnv *env, jobject thiz) {
    // TODO: implement native_release()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1initVideoEncoder(JNIEnv *env, jobject thiz,
                                                               jint width, jint height, jint m_fps,
                                                               jint bitrate) {
    // TODO: implement native_initVideoEncoder()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmp_1push_DerryPush_native_1pushVideo(JNIEnv *env, jobject thiz,
                                                        jbyteArray data) {
    // TODO: implement native_pushVideo()
}