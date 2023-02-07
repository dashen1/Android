#include <jni.h>
#include <string>

// 我先声明int get(), 运行后先去找实现（ 库里面的实现）， 如果找不到会报错
// extern int get();
//
// 日志输出
#include <android/log.h>

#define TAG "Derry"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__);
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG,  __VA_ARGS__);
// 由于库是c编写的，必须采用c的编译方式
extern "C" {
    extern int get();
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndk26_1cmake_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    LOGE("native-lib hello:\n", get());
    return env->NewStringUTF(hello.c_str());
}