package com.example.advance_ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ExViewPager extends ViewPager {

    public ExViewPager(@NonNull Context context) {
        this(context,null);
    }

    public ExViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("ExViewPager","getParent() = "+getParent());
        return super.dispatchTouchEvent(ev);
    }
}
