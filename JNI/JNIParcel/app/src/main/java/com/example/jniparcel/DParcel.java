package com.example.jniparcel;

public class DParcel {

    private long mNativePtr = 0;

    static {
        System.loadLibrary("jniparcel");
    }

    private static class MyObject {
        private static DParcel DPARCEL = new DParcel(0);
    }

    private DParcel(long mNativePtr) {
        this.mNativePtr = mNativePtr;
        this.mNativePtr = nativeCreate();
    }

    public static DParcel obtain() {
        return MyObject.DPARCEL;
    }

    public final void writeInt(int val) {
        nativeWriteInt(mNativePtr, val);
    }

    public final void nativeSetDataPosition(int pos) {
        nativeSetDataPosition(mNativePtr, pos);
    }

    public final int readInt() {
      return nativeReadInt(mNativePtr);
    }

    private native long nativeCreate();// 在native层构建Parcel.cpp对象指针地址
    public native void nativeSetDataPosition(long mNativePtr, int pos);// 为了下次从0位置开始
    public native void nativeWriteInt(long mNativePtr, int val);
    public native int nativeReadInt(long mNativePtr);
}
