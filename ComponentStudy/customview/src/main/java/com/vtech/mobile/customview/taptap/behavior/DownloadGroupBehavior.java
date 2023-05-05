package com.vtech.mobile.customview.taptap.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.vtech.mobile.customview.R;

public class DownloadGroupBehavior extends CoordinatorLayout.Behavior<View> {


    private static final String TAG = "DownloadGroupBehavior";
    private float contentY = 0;

    public DownloadGroupBehavior() {
    }

    public DownloadGroupBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency.getId() == R.id.list;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        // 获取原始距离
        if (contentY == 0){
            contentY = dependency.getY();
        }
        // 获取移动距离
        float moveOffset = contentY-dependency.getY();
        // 根据距离计算出占自身整个高度的比例
        float alpha = moveOffset / child.getHeight();
        // 1-当前的比例等于要设置的透明度
        child.setAlpha(1-alpha);
        // 设置偏移，向上移动超出了child的y值的0点坐标，所以应该设置为负值
        child.setTranslationY(-moveOffset);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
