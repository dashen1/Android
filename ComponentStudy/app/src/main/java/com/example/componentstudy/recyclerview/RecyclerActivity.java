package com.example.componentstudy.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.R;

public class RecyclerActivity extends AppCompatActivity {

    TextView tv_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        tv_text = findViewById(R.id.tv_text);
        // attachToRoot = false 意思是：我们当前的xml是独立的，和父View是分开的，需要父View的LayoutParams
        // 比如RecyclerView需要独立的xml
        // attachToRoot = true 意思是：当前的View和最终父View是在同一个xml的，此时是不需要最终父View的LayoutParams 之后由 onMeasure来最终确定大小
        // 因为xml没有被添加到最终根root上，所以不会显示
        // LayoutInflater.from(this).inflate(R.layout.activity_recycler,null,true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                tv_text.setText("I was changed in child thread!");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
