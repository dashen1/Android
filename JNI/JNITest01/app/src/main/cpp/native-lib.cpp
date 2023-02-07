#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_jnitest01_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest01_MainActivity_testArrayAction(JNIEnv *env, jobject thiz, jint count,
                                                        jstring text_info, jintArray ints,
                                                        jobjectArray strs) {
    int countInt = count;

    const char * textInfo = env->GetStringUTFChars(text_info, NULL);

    int * jintArray = env->GetIntArrayElements(ints, NULL);

    jsize size = env->GetArrayLength(ints);

    for (int i = 0; i < size; ++i) {
        *(jintArray + i) += 100; // c++修改 不影响java层
    }
    /**
     * 0 刷新java数组， 并释放c++层数组
     * JNI_COMMIT 只提交 只刷新java数组， 不释放c++层数组
     * JNI_ABORT 只释放c++层数组
     * */
    env->ReleaseIntArrayElements(ints, jintArray, 0);
    // jobjectArray: 代表是java引用类型数组， 不一样
    jsize strSize = env->GetArrayLength(strs);
    for (int i = 0; i < strSize; ++i) {
        jstring jobj = static_cast<jstring>(env->GetObjectArrayElement(strs, i));
        const char * jobjCharp = env->GetStringUTFChars(jobj, NULL);

        //释放jstring
        env->ReleaseStringUTFChars(jobj, jobjCharp);
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest01_MainActivity_putObject(JNIEnv *env, jobject thiz, jobject student,
                                                  jstring str) {
    // TODO: implement putObject()
    jclass  studentClass = env->GetObjectClass(student);
    // 2. Student 类里面的函数规则 签名
    jmethodID setName = env->GetMethodID(studentClass, "setName", "(Ljava/lang/String;)V");
    jmethodID getName = env->GetMethodID(studentClass, "getName", "()Ljava/lang/String;");
    jmethodID showInfo = env->GetStaticMethodID(studentClass, "showInfo", "(Ljava/lang/String;)V");

    // 3. 调用setName
    jstring value = env->NewStringUTF("AAAA");
    env->CallVoidMethod(student, setName, value);

    //4.调用getName
    jstring getNameResult = static_cast<jstring>(env->CallObjectMethod(student, getName));
    const char * getNameValue = env->GetStringUTFChars(getNameResult, NULL);

    //5.调用静态showInfo
    jstring jstringValue =env->NewStringUTF("hello static method, I am c++");
    env->CallStaticVoidMethod(studentClass, showInfo, jstringValue);

}

// JNI函数 全局引用 局部引用

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest01_MainActivity_insertObject(JNIEnv *env, jobject thiz) {
    // TODO: implement insertObject()
    //1. 通过报名+类名的方式 拿到Student class
    const char * studentStr = "com/example/jnitest01/Student";
    jclass studentClass = env->FindClass(studentStr);

    // 2.通过student的class 实例化Student对象 C++ new Student
    jobject studentObject = env->AllocObject(studentClass);// AllocObject 只实例化对象，不会调用对象的构造函数

    //方法的规则定义
    jmethodID setName = env->GetMethodID(studentClass, "setName", "(Ljava/lang/String;)V");
    jmethodID setAge = env->GetMethodID(studentClass, "setAge", "(I)V");

    jstring strValue = env->NewStringUTF("Hello");
    env->CallVoidMethod(studentObject, setName, strValue);
    env->CallVoidMethod(studentObject, setAge, 99);

    //4.通过报名+类名的方式 拿到Student class
    const char * personStr = "com/example/jnitest01/Person";
    jclass personClass = env->FindClass(personStr);

    jobject personObject = env->AllocObject(personClass);

    jmethodID setStudent = env->GetMethodID(personClass, "setStudent", "(Lcom/example/jnitest01/Student;)V");
    env->CallVoidMethod(personObject, setStudent, studentObject);

    //养成好习惯 不用就及时释放

}

jclass dogClass;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest01_MainActivity_testQuote(JNIEnv *env, jobject thiz) {
    // TODO: implement testQuote()
    if (NULL == dogClass) {
//        const char * dogStr = "com/example/jnitest01/Dog";
//        dogClass = env->FindClass(dogStr);
        // 升级为全局引用 JNI函数结束也不会释放，需要手动释放 相当于c++ 的new、手动delete
        const char * dogStr = "com/example/jnitest01/Dog";
        jclass temp = env->FindClass(dogStr);
        dogClass = static_cast<jclass>(env->NewGlobalRef(temp));
    }
    // <init> V 是不会变的
    jmethodID init = env->GetMethodID(dogClass, "<init>", "()V");
    jobject  dogObject = env->NewObject(dogClass, init);

    jmethodID init1 = env->GetMethodID(dogClass, "<init>", "(I)V");
    jobject  dogObject1 = env->NewObject(dogClass, init1,10);

    jmethodID init2 = env->GetMethodID(dogClass, "<init>", "(II)V");
    jobject  dogObject2 = env->NewObject(dogClass, init2, 10, 20);

    jmethodID init3 = env->GetMethodID(dogClass, "<init>", "(III)V");
    jobject  dogObject3 = env->NewObject(dogClass, init3, 10, 20, 30);
    env->DeleteLocalRef(dogClass);
    // JNI 函数结束， 会释放局部引用 dogClass虽然被释放，但还是不等于NULL, 只是一个悬空指针，所以第二次进不来IF,会导致崩溃
}

extern int age;
extern void show(); //声明show函数

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest01_MainActivity_deleteQuote(JNIEnv *env, jobject thiz) {
    // TODO: implement deleteQuote()
    if (dogClass != NULL) {
        env->DeleteGlobalRef(dogClass);
        dogClass = NULL;
    }
    show();
}