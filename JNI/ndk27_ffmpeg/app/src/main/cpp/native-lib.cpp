#include <jni.h>
#include <string>

// ffmpeg 是纯C的 必须加进 extern
extern "C" {
    #include <libavutil/avutil.h>;
    #include <libavformat/avformat.h>;
    #include <libavcodec/avcodec.h>;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndk27_1ffmpeg_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "FFmpeg 当前的版本是：";
    hello.append(av_version_info());
    return env->NewStringUTF(hello.c_str());
}