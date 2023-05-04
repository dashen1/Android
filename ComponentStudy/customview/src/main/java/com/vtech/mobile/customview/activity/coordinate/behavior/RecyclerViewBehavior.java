package com.vtech.mobile.customview.activity.coordinate.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<RecyclerView> {

    private final String TAG = "RecyclerViewBehavior";

    float contentY = 0;

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull RecyclerView child, @NonNull View dependency) {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull RecyclerView child, @NonNull View dependency) {
        // 在这里进行时间消费，只关心竖向滑动
        if (contentY==0){
            contentY = child.getY();
        }
        float scroll = contentY+dependency.getY();
        Log.e(TAG,"scroll : "+scroll);
        child.setY(scroll);
        return true;
    }

    private int getMaxScroll(TextView child) {
        return child.getHeight();
    }
}
