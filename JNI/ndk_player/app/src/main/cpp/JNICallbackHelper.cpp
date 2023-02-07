#include "JNICallbackHelper.h"

JNICallbackHelper::JNICallbackHelper(JavaVM *vm, JNIEnv *env, jobject job) {
    this->vm = vm;
    this->env = env;
    this->job = env->NewGlobalRef(job);//升级为全局变量

    jclass clazz = env->GetObjectClass(job);
    jmd_prepared = env->GetMethodID(clazz, "onPrepared", "()V");
    jmd_onError = env->GetMethodID(clazz, "onError", "(I)V");
}

JNICallbackHelper::~JNICallbackHelper() {
    vm = 0;
    env->DeleteGlobalRef(job);
    job = 0;
    env = 0;
}

void JNICallbackHelper::onPrepared(int thread_mode) {
    if (thread_mode == THREAD_MAIN) {
        env->CallVoidMethod(job, jmd_prepared);
    } else if (thread_mode == THREAD_CHILD) {
        JNIEnv *env_child;
        vm->AttachCurrentThread(&env_child, 0);//AttachCurrentThread 类似 java runOnMainThread() 在子线程中执行主线程方法
        env_child->CallVoidMethod(job, jmd_prepared);
        vm->DetachCurrentThread();
    }
}

void JNICallbackHelper::onError(int thread_mode, int errorCode) {
    if (thread_mode == THREAD_MAIN) {
        env->CallVoidMethod(job, jmd_onError);
    } else if (thread_mode == THREAD_CHILD) {
        JNIEnv *env_child;
        vm->AttachCurrentThread(&env_child, 0);//AttachCurrentThread 类似 java runOnMainThread() 在子线程中执行主线程方法
        env_child->CallVoidMethod(job, jmd_onError, errorCode);
        vm->DetachCurrentThread();
    }
}
