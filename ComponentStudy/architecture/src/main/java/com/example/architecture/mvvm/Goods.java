package com.example.architecture.mvvm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.architecture.BR;

public class Goods extends BaseObservable {

    // 如果是public修饰符，则可以直接在成员变量上加 @Bindable注解
    @Bindable
    public String name;

    //如果是private修饰符，则需要在成员变量的get()方法上添加 @Bindable注解
    private String details;

    private float price;

    public Goods(String name, String details, float price) {
        this.name = name;
        this.details = details;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // 更新本字段
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        // 更新所有字段
        notifyChange();
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
