package com.vtech.mobile.customview.activity.coordinate.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class ScrollBehavior extends CoordinatorLayout.Behavior<TextView> {

    private final String TAG = "ScrollBehavior";
    //相对于y轴滑动的距离
    private int mScrollY = 0;
    //总共滑动的距离
    private int totalScroll = 0;

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull TextView child, int layoutDirection) {
        Log.e(TAG, "onLayoutChild...");
        parent.onLayoutChild(child, layoutDirection);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // 为了dispatch成功
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // 边界处理
        int consumedy = dy;
        Log.e(TAG, "onNestedPreScroll + " + dy);
        int scroll = totalScroll + dy;
        if (Math.abs(scroll) > getMaxScroll(child)) {
            consumedy = getMaxScroll(child) - Math.abs(totalScroll);
        } else if (scroll < 0) {
            consumedy = 0;
        }
        // 在这里进行时间消费，只关心竖向滑动
        ViewCompat.offsetTopAndBottom(child, -consumedy);
        totalScroll += consumedy;
        consumed[1] = consumedy;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    private int getMaxScroll(TextView child) {
        return child.getHeight();
    }
}
