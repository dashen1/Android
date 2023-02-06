#ifndef DERRYPLAYER_JNICALLBACKHELPER_H
#define DERRYPLAYER_JNICALLBACKHELPER_H

#include <jni.h>
#include "util.h"
#include <iostream>

class JNICallbackHelper {
private:
    JavaVM *vm = 0;
    JNIEnv *env = 0;
    jobject job;
    jmethodID jmd_prepared;
    jmethodID jmd_onError;

public:
    JNICallbackHelper(JavaVM *vm, JNIEnv *env, jobject job);

    JNICallbackHelper();

    virtual ~JNICallbackHelper();

    void onPrepared(int thread_mode);

    void onError(int thread_mode, int errorCode);
};
#endif
