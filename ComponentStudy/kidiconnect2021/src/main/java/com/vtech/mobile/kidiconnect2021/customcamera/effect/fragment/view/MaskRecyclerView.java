package com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MaskRecyclerView extends RecyclerView {

    private static final String TAG = "MaskRecyclerView";
    private volatile boolean isUserControl = false;
    private volatile int pageSize = 6;
    private volatile int pageMarginSize = 2;
    private OnItemTriggerListener triggerListener = null;

    private View borderView;
    private boolean matched;
    private boolean isSliding;

    public MaskRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public MaskRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaskRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecycleView();
    }

    private void initRecycleView() {
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                 // 正在滑动
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    isSliding = true;
                }
                // 滑动停止时
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    isSliding = false;
                    // 校准位置，使Mask对准圈圈， flag == false,说明此时不需要校准
                    boolean flag = autoScrollToPosition();
                    // 不需要校准，则出发时间，打开当前Mask特效
                    if (!flag){
                        // 滑动已经停止，开启特效或者下载
                        // 延迟一下，滑动停稳在设置Mask，避免错误
                        long delayed = 300L;
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (isSliding) return;
                                maskTrigger();
                                // 调用maskTrigger打开Mask特效，后面200ms又要再调用maskTrigger一次，避免因为卡顿导致mask图标与特效不一致
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSliding){
                                            return;
                                        }
                                        maskTrigger();
                                    }
                                },200L);
                            }
                        },delayed);
                    }
                }
            }
        });
    }

    /**
     * 滑动列表时，自动对准圆圈
     * @return
     */
    private boolean autoScrollToPosition() {
        isUserControl = true;
        boolean exec = false;
        View view = this.getChildAt(0);

        return false;
    }

    private void maskTrigger() {

    }

    public static interface OnItemTriggerListener {
        void onTriggerAfterSlide(View mainItemView);

        void updateListShow();
    }

    public void setMaxFlingVelocityX(int velocity) {
        int MAX_VELOCITY_X = velocity;
        final MaskRecyclerView maskRecyclerView = this;
        this.setOnFlingListener(new RecyclerView.OnFlingListener() {

            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if (Math.abs(velocityX) > MAX_VELOCITY_X) {
                    velocityX = MAX_VELOCITY_X * (int) Math.signum((double) velocityX);
                    maskRecyclerView.fling(velocityX, velocityY);
                    return true;
                }
                return false;
            }
        });
    }

}
