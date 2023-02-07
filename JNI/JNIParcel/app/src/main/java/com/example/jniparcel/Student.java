package com.example.jniparcel;

import android.os.Parcel;
import android.os.Parcelable;

// parcelable 和 serializable哪个性能高？
// parcelable 性能高
// 因为Serializable 使用IO流完成（）Parcelable C++ 对象指针 共享内存 通过指针挪动存储数据

public class Student implements Parcelable {

    protected Student(Parcel in) {
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
