#include <jni.h>
#include <string>
#include "com_example_jniparcel_DParcel.h"

// 全局变量和静态一样 就不需要返回地址 opencv parcel都是这样
static Parcel * parcel = 0;

extern "C" JNIEXPORT jlong JNICALL Java_com_example_jniparcel_DParcel_nativeCreate
        (JNIEnv *, jobject) {
    Parcel * parcel1 = new Parcel();
    return reinterpret_cast<jlong>(parcel1);
}

/*
 * Class:     com_example_jniparcel_DParcel
 * Method:    nativeSetDataPosition
 * Signature: (JI)V
 */
extern "C" JNIEXPORT void JNICALL Java_com_example_jniparcel_DParcel_nativeSetDataPosition
        (JNIEnv *, jobject, jlong native_ptr, jint mDataPos) {
    Parcel * parcel1 = reinterpret_cast<Parcel *>(native_ptr);
    parcel1->setDataPosition(mDataPos);
}
/*
 * Class:     com_example_jniparcel_DParcel
 * Method:    nativeWriteInt
 * Signature: (I)V
 */
extern "C" JNIEXPORT void JNICALL Java_com_example_jniparcel_DParcel_nativeWriteInt
        (JNIEnv *, jobject, jlong native_ptr, jint val) {
    Parcel * parcel1 = reinterpret_cast<Parcel *>(native_ptr);
    parcel1->writeInt(val);
}

/*
 * Class:     com_example_jniparcel_DParcel
 * Method:    nativeReadInt
 * Signature: (J)I
 */
extern "C" JNIEXPORT jint JNICALL Java_com_example_jniparcel_DParcel_nativeReadInt
        (JNIEnv *, jobject, jlong native_ptr) {
    Parcel * parcel1 = reinterpret_cast<Parcel *>(native_ptr);
    return parcel1->readInt();
}
