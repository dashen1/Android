#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_jnikotlintest01_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

 int compare(const jint * number1, const jint * number2) {
    return *number1 - *number2;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity_sort(JNIEnv *env, jobject thiz, jintArray arr) {
    // TODO: implement sort()
    jint * intArray = env->GetIntArrayElements(arr, nullptr);

    int length = env->GetArrayLength(arr);

    /**
     * 参数1：void * 数组的首地址
     * 参数2：数组的大小长度
     * 参数3：元素的大小
     * 参数4：对比的方法指针
     * */
    // qsort(void* __base, size_t __nmemb, size_t __size, int (*__comparator)(const void* __lhs, const void* __rhs))
    qsort(intArray, length, sizeof(int), reinterpret_cast<int (*)(const void *, const void *)>(compare));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity2_loadCache(JNIEnv *env, jclass thiz, jstring name) {
    // TODO: implement loadCache()
    // Opencv, webRtc大量使用静态缓存

    jfieldID f_id = nullptr;
    if (f_id == nullptr) {
        f_id = env->GetStaticFieldID(thiz, "name1", "Ljava/lang.String;");
    } else {

    }
    env->SetStaticObjectField(thiz, );
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity2_initStaticCache(JNIEnv *env, jclass clazz) {
    // TODO: implement initStaticCache()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity2_clearStaticCache(JNIEnv *env, jclass clazz) {
    // TODO: implement clearStaticCache()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity3_exception(JNIEnv *env, jclass clazz) {
    // TODO: implement exception()
    // 假设现在想操作 name999,但目前是没有的 就会在native层崩溃
    jfieldID f_id = env->GetStaticFieldID(clazz, "name999","Ljava/lang/String;");
    // 崩溃的两种解决方案
    // 1.补救
    jthrowable throwable = env->ExceptionOccurred();// 检测本次执行是否有异常
    if (throwable) {
        env->ExceptionClear();
        jfieldID f_id = env->GetStaticFieldID(clazz, "name1","Ljava/lang/String;");
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity3_exception2(JNIEnv *env, jclass clazz) {
    // TODO: implement exception2()
    jfieldID f_id = env->GetStaticFieldID(clazz, "name999","Ljava/lang/String;");
    jthrowable throwable = env->ExceptionOccurred();// 检测本次执行是否有异常
    if (throwable) {
        env->ExceptionClear();
        // Throw 拋一個java对象
        jclass clz = env->FindClass("java/lang/NoSuchFieldException");
        env->ThrowNew(clz, "exception occurred!");
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnikotlintest01_MainActivity3_exception3(JNIEnv *env, jclass clazz) {// JNIEnv *env 二级指针
    // TODO: implement exception3()

    jmethodID showID = env->GetStaticMethodID(clazz, "show", "()V");
    env->CallStaticVoidMethod(clazz, showID);

    //不是马上崩溃，所以有时间可以检测

    // 这样也会崩溃
    env->NewStringUTF("AAA");

    // 所以异常检测要紧挨可能发生异常的代码
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();// 输出描述信息
        env->ExceptionClear();
    }
}

// c是没有对象的，所以要传env