package com.vtech.mobile.customview.activity.coordinate;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtech.mobile.customview.R;
import com.vtech.mobile.customview.activity.coordinate.adapter.CustomRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CoordinateActivity extends AppCompatActivity {

    @BindView(R.id.rv_child)
    RecyclerView rv_child;

    private List<String> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_child.setLayoutManager(layoutManager);
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(datas);
        rv_child.setAdapter(adapter);
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("data : "+i);
        }
    }
}
