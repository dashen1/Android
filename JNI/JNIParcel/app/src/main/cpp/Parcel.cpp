#include "Parcel.h"

Parcel::Parcel() {
    this->mData = static_cast<char *>(malloc(1024));//堆区
}

Parcel::~Parcel() {
    if (this->mData) {
        free(this->mData);
    }

    if (this->mDataPOS) {
        this->mDataPOS = NULL;
    }
}

int Parcel::readInt() {
    jint result = *reinterpret_cast<int *>(this->mData + this->mDataPOS);
    changePos(sizeof (int ))
    return result;
}

void Parcel::writeInt(int val) {
    // 先转为int 指针地址，然后把val存放到该地址对应的空间
    *reinterpret_cast<int *>(this->mData + this->mDataPOS) = val;
    changePos(sizeof(int))
}

void Parcel::setDataPosition(int mDataPos) {
    this->mDataPOS = mDataPos;
}

void Parcel::changePos(int pos) {
    this->mDataPOS += pos;
}