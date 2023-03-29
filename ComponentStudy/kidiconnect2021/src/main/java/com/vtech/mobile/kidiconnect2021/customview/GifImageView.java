package com.vtech.mobile.kidiconnect2021.customview;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import androidx.annotation.Nullable;

public class GifImageView extends AppCompatImageView {
    public GifImageView(Context context) {
        this(context,null);
    }

    public GifImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GifImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
