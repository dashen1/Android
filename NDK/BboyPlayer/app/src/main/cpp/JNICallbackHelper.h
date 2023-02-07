//
// Created by se0891 on 2022/4/7.
//

#ifndef BBOYPLAYER_JNICALLBACKHELPER_H
#define BBOYPLAYER_JNICALLBACKHELPER_H

#include <jni.h>
#include "util.h"

class JNICallbackHelper{
private:
    JavaVM *vm = 0;
    JNIEnv *env=0;
    jobject job;
    jmethodID jmd_prepared;
    jmethodID jmd_onError;
    jmethodID jmd_onProgress;

public:
    JNICallbackHelper(JavaVM *vm, JNIEnv *env,jobject job);
    ~JNICallbackHelper();

    void onPrepared(int thread_mode);

    void onError(int thread_mode, int error_code);

    void onProgress(int thread_mode, int audio_time);
};

#endif //BBOYPLAYER_JNICALLBACKHELPER_H
