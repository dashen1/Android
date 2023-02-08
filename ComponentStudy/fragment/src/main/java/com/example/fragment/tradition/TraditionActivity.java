package com.example.fragment.tradition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fragment.R;
import com.example.fragment.fragment.BlankFragment;
import com.example.fragment.fragment.BlankFragment2;

import java.util.ArrayList;

public class TraditionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_fragment1;
    private Button btn_fragment2;

    private int currentIndex = 0;
    private ArrayList<Fragment> fragmentArrayList;

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fragment1 = findViewById(R.id.btn_fragment1);
        btn_fragment1.setOnClickListener(this);

        btn_fragment2 = findViewById(R.id.btn_fragment2);
        btn_fragment2.setOnClickListener(this);

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(BlankFragment.newInstance("hello fragment", "f1"));
        fragmentArrayList.add(BlankFragment2.newInstance("hello fragment2", "f2"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment1:
                changeFragment(0);
                break;
            case R.id.btn_fragment2:
                changeFragment(1);
                break;
        }
    }

    // 方案 2 更好
    // 并且在Fragment切换时，系统会调用 onHiddenChanged(boolean hidden)方法，可以通过重写在该方法中进行一些必要的操作。
    private void changeFragment(int index) {
        currentIndex = index;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null != mCurrentFragment) {
            ft.hide(mCurrentFragment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentArrayList.get(index).getClass().getName());
        if (null == fragment) {
            fragment = fragmentArrayList.get(index);
        }
        mCurrentFragment = fragment;
        // 判断此fragment是否已经添加到FragmentTransaction事务中
        if (!fragment.isAdded()) {
            ft.add(R.id.container, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }

    // 方案1 replace
    // 每次切换，都会重新加载一次Fragment的生命周期
    // 导致重新调用Fragment的 onCreateView(),所以在切换页面时就无法保存Fragment的状态
    private void replaceContainer(int position){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    }
}
