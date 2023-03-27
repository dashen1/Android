package com.example.opengl.customcamera.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.opengl.customcamera.filters.AbstractFilter;
import com.example.opengl.customcamera.filters.CameraFilter;
import com.example.opengl.customcamera.filters.FilterChain;
import com.example.opengl.customcamera.filters.FilterContext;
import com.example.opengl.customcamera.filters.ScreenFilter;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRenderImpl implements GLSurfaceView.Renderer {

    private final String TAG = "CameraRender";
    private CameraSurfaceView cameraView;
    private SurfaceTexture mSurfaceTexture;
    private int textureID;

    private ScreenFilter screenFilter;
    private CameraFilter cameraFilter;

    private float[] mtx = new float[16];

    private FilterChain filterChain;
    /**
     * 是否在录制
     */
    private boolean recordingEnabled;
    /**
     * 录制的几个状态
     */
    private int recordingStatus;
    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;
    private static final int RECORDING_PAUSE = 3;
    private static final int RECORDING_RESUME = 4;
    private static final int RECORDING_PAUSED = 5;


    public CameraRenderImpl(CameraSurfaceView cameraView) {
        this.cameraView = cameraView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textureID = createTextureID();
        mSurfaceTexture = new SurfaceTexture(textureID);

        Context context = cameraView.getContext();
        List<AbstractFilter> filters = new ArrayList<>();
        filters.add(new CameraFilter(context));
        filters.add(new ScreenFilter(context));
        filterChain = new FilterChain(new FilterContext(), filters, 0);
        if (recordingEnabled) {
            recordingStatus = RECORDING_RESUMED;
        } else {
            recordingStatus = RECORDING_OFF;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        filterChain.setSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //更新纹理 因为摄像头数据和纹理是绑定在一起的了，但是需要通知更新纹理，纹理才会拿到最新一帧的的数据
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
        filterChain.setTransformMatrix(mtx);
        filterChain.setTransformMatrix(mtx);
        int id = filterChain.process(textureID);
    }

    private int createTextureID() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }
}
