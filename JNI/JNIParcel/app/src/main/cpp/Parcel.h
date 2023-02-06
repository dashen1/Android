
#ifndef DERRYPARCEL_JAVA_PARCEL_H
#define DERRYPARCEL_JAVA_PARCEL_H

#include <jni.h>
#include <malloc.h>

class Parcel {
private:
    char * mData = 0;// DParcel 对象共享内存地址
    int mDataPOS= 0;
    void changePos(int val);
    jint len = 0;

public:
    Parcel();

    virtual ~Parcel();

    void writeInt(int val);

    void setDataPosition(int mDataPos);

    jint readInt();
};

#endif