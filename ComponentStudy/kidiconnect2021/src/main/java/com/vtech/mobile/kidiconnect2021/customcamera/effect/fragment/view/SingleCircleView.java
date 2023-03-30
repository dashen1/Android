package com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.vtech.mobile.kidiconnect2021.customcamera.utils.UIHelper;

public class SingleCircleView extends View {
    public SingleCircleView(Context context) {
        this(context, null);
    }

    public SingleCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setARGB(255, 235, 245, 96);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(UIHelper.dp2px(this.getContext(), 3));
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2.f, getHeight() / 2.f, getWidth() / 2.1f, paint);
    }
}
