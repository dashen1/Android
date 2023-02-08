package com.example.fragment.viewpager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fragment.R;
import com.example.fragment.fragment.BlankFragment;
import com.example.fragment.fragment.BlankFragment2;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager vp_main;
    private SlidingTabLayout tab_layout;

    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"fragment1","fragment2"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_viewpager);

        vp_main = findViewById(R.id.vp_main);
        tab_layout = findViewById(R.id.tab_layout);

        mFragments = new ArrayList<>();
        mFragments.add(new BlankFragment());
        mFragments.add(new BlankFragment2());

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vp_main.setAdapter(pagerAdapter);
        vp_main.setOffscreenPageLimit(mFragments.size());
        tab_layout.setViewPager(vp_main, mTitles);

    }


}
