package com.vtech.mobile.customview.taptap.behavior;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.vtech.mobile.customview.R;

public class NavigationBarBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "NavigationBarBehavior";
    private RelativeLayout navigationBar;
    private int translationY = 0;
    private int barColor = Color.rgb(3, 218, 197);

    public NavigationBarBehavior() {
    }

    public NavigationBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 依赖于RecyclerView
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (navigationBar == null) {
            ViewGroup rootView = (ViewGroup) parent.getParent().getParent();
            navigationBar = rootView.findViewById(R.id.rlNavigationBar);
            Log.d(TAG, "layoutDependsOn: " + navigationBar);
        }
        return dependency.getId() == R.id.list;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        // 获取原始距离 recyclerView的总高度
        if (translationY == 0) {
            translationY = (int) (dependency.getY() - child.getHeight());
            Log.d(TAG, "translationY: " + translationY);
        }
        // 获得当前距离content列表的距离
        float depY = dependency.getY() - child.getHeight();
        // 除以2用于区分滑动到上半部还是下半部
        int ban = translationY / 2;
        float offset = 0;
        int color = barColor;
        Log.e(TAG,"child.getHeight() : "+child.getHeight());
        // 处于原始距离的下半部移动，减去10是为了修复移动到屏幕外后还有几像素的露白
        if (depY > (ban - 10)) {
            // depY / translationY -> RecyclerView移动的百分比*当前View的高度
            // offset都是从 0 -》 -160
            offset = (depY / translationY * child.getHeight() - child.getHeight()) * 2;
            color = Color.TRANSPARENT;
            Log.e(TAG,"下半部移动 : "+offset);
        } else {
            // 处于原始距离的上半部移动
            // 当滑动到上半部时直接使用一半header的高度计算出占child的比重
            // offset都是从 -160 -》 0
            offset = depY / ban * child.getHeight();
            offset = offset > 0 ? -offset : 0;
            Log.e(TAG,"上半部移动 ："+offset);
        }

        child.setBackgroundColor(color);
        child.setTranslationY(offset);
        navigationBar.setTranslationY(offset);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
