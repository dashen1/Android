#include <jni.h>
#include <string>
#include <pthread.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// 静态注册缺点：
// 1、JNI函数名非常长 2、捆绑 上层 包名+类名
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_staticRegister(JNIEnv *env, jobject thiz) {
    // TODO: implement staticRegister() 下面是动态注册
}

JavaVM * javaVm = nullptr; //如果不赋初始值 默认给系统乱值 c++11后，取代NULL,作用是可以初始化指针赋值

const char * mainActivityClassName = "com/example/jni_dynamicregister/MainActivity";
const char * javaMethod01 = "dynamicJavaMethod01";

//native 真正的函数
void dynamicJavaMethod01() {

}

void dynamicJavaMethod02() {

}

static const JNINativeMethod jniNativeMethod [] = {
        {"dynamicJavaMethod01", "()V", (void*)(dynamicJavaMethod01)},
        {"dynamicJavaMethod02", "(Ljava/lang/String;)I", (int*)(dynamicJavaMethod01)},
};

//默认像java的构造函数 如果你不写构造函数，默认就有构造函数
//JNI
JNIEXPORT jint JNI_OnLoad(JavaVM * javaVm, void *) {
    // this.javaVm = javaVm
    ::javaVm = javaVm;

    //动态注册 全部做完
    JNIEnv * jniEnv = nullptr;
    int result = javaVm->GetEnv(reinterpret_cast<void **>(&jniEnv), JNI_VERSION_1_6);

    //result == 0 就是成功 [ffmpeg 成功就是0]
    if (result != JNI_OK) {
        return -1;
    }
    jclass mainActivityClass = jniEnv->FindClass(mainActivityClassName);
    jniEnv->RegisterNatives(mainActivityClass, jniNativeMethod, sizeof(jniNativeMethod) / sizeof(JNINativeMethod));
}

class MyContext {
private:
    JNIEnv * jniEnv = nullptr;
    JavaVM * javaVm1 = nullptr;
};

MyContext * myContext = static_cast<MyContext *>(pVoid);

void * myThreadTaskAction(void * pVoid) {
    JNIEnv * jniEnv = nullptr;
    jint attachResult = ::javaVm->AttachCurrentThread(&jniEnv, nullptr);
    if (attachResult != JNI_OK) {
        return 0;
    }

    jclass mainActivityClass = jniEnv->GetObjectClass();
    ::javaVm->DetachCurrentThread();
    return nullptr;
}


// JNIEnv *env 不能跨越线程 否则崩溃 但是可以跨越函数
// jobject thiz 不能跨越线程 也不能跨越函数 否则崩溃 谁调用JNI(就是哪个函数调用JNI的方法 函数里面可以是不同线程) 谁的实例就给jobject activity或者new Thread地址不一样
//javaVM 能够跨越线程和函数


// TODO 解决方式 （android进程只有一个javaVM,javaVM是和进程绑定的， 是全局的，跨线程的）

void * run(void *) {
    JNIEnv * newEnv = nullptr;
    ::javaVm->AttachCurrentThread(&newEnv, nullptr);

    ::javaVm->DetachCurrentThread();
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_nativeThread(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeThread()

    pthread_t  pid;
    pthread_create(&pid, nullptr, myThreadTaskAction, nullptr);
    pthread_join(pid, nullptr);

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_nativeFun1(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeFun1()
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_nativeFun2(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeFun2()
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_nativeFun3(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeFun3()
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_1dynamicregister_MainActivity_nativeFun4(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeFun4()
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);
}