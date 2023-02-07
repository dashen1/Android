package com.example.architecture.mvvm;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;

import com.example.architecture.BR;
import com.example.architecture.R;
import com.example.architecture.databinding.ActivityMvvmBinding;

import java.util.Random;

public class MVVMActivity extends AppCompatActivity {

    private static final String TAG = "MVVMActivity";

    private User user;

    private Goods goods;

    private ObservableMap<String,String> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mvvm);

        ActivityMvvmBinding activityMvvmBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        user = new User("coder","123");
        activityMvvmBinding.setUserInfo(user);

        goods = new Goods("code","hello",21);
        activityMvvmBinding.setGoods(goods);

        activityMvvmBinding.setHandler(new GoodsHandler());

        map = new ObservableArrayMap<>();
        map.put("name","student");
        map.put("age","24");
        activityMvvmBinding.setMap(map);

        ObservableList<String> list = new ObservableArrayList<>();
        list.add("one");
        list.add("two");
        activityMvvmBinding.setList(list);
        activityMvvmBinding.setIndex(0);
        activityMvvmBinding.setKey("name");

        // 双向数据绑定

        // 事件绑定


        goods.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId== com.example.architecture.BR.name){
                    Log.i(TAG,"BR.name");
                }else if (propertyId == com.example.architecture.BR.details) {
                    Log.e(TAG, "BR.details");
                } else if (propertyId == com.example.architecture.BR._all) {
                    Log.e(TAG, "BR._all");
                } else {
                    Log.e(TAG, "未知");
                }

            }
        });
    }

    public void changeData(View view) {
        map.put("name","student,hi"+new Random().nextInt(100));
    }

    public class GoodsHandler{
        public void changeGoodsName(){
            goods.setName("code"+new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }

        public void changeGoodsDetails(){
            goods.setDetails("hello"+new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }


    }
}
