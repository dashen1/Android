package com.example.opengl.customcamera.filters;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.opengl.R;

public class CameraFilter extends AbstractFboFilter {

    private final String TAG = "CameraFilter";
    private int vMatrix;

    public CameraFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        return super.onDraw(texture, filterChain);
    }

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, filterContext.cameraMtx, 0);
        Log.d(TAG,"beforeDraw");
    }
}