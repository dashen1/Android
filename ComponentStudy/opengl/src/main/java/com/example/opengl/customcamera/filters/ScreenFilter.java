package com.example.opengl.customcamera.filters;

import android.content.Context;

import com.example.opengl.R;

public class ScreenFilter extends AbstractFilter{

    public ScreenFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }
}
