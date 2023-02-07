package com.example.rtmp_push;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CameraHelper implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "CameraHelper";
    private Activity mActivity;
    private int mHeight;
    private int mWidth;
    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private SurfaceHolder mSurfaceHolder;


    private Camera.PreviewCallback mPreviewCallback;
    private int mRotation;

    private OnChangedSizeListener mOnChangedSizeListener;

    public CameraHelper(Activity mActivity, int mHeight, int mWidth, int mCameraId) {
        this.mActivity = mActivity;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
        this.mCameraId = mCameraId;
    }

    /** YUV 420 mWidth * mHeight * 3 / 2
     * 4 * 4
     * y y y y
     * y y y y
     * y y y y
     * y y y y
     * u u u u
     * v v v v
     *
     * */

    private void startPreview() {
        try {
            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            setPreviewSize(parameters);
            setPreviewOrientation(parameters);
            mCamera.setParameters(parameters);
            buffer = new byte[mWidth * mHeight * 3 / 2];//为什么？
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            if (mOnChangedSizeListener != null) {
                mOnChangedSizeListener.onChanged(mWidth, mHeight);
            }
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        mSurfaceHolder.addCallback(this);
    }


    private void setPreviewOrientation(Camera.Parameters parameters) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        mRotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (mRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90: // 横屏 左边是头部(home键在右边)
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:// 横屏 头部在右边
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        // 设置角度, 参考源码注释，从源码里面copy出来的，Google给出旋转的解释
        mCamera.setDisplayOrientation(result);
    }

    private void setPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size size = supportedPreviewSizes.get(0);
        // 选择一个与设置的差距最小的支持分辨率
        int m = Math.abs(size.height * size.width - mWidth * mHeight);
        supportedPreviewSizes.remove(0);
        Iterator<Camera.Size> iterator = supportedPreviewSizes.iterator();
        while (iterator.hasNext()) {
            Camera.Size next = iterator.next();
            int n = Math.abs(next.width * next.height - mWidth * mHeight);
            if (n < m) {
                m = n;
                size = next;
            }
        }
        mWidth = size.width;
        mHeight = size.height;
        parameters.setPreviewSize(mWidth, mHeight);
    }

    private void stopPreview() {
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }



    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //TODO 数据没有做旋转处理
        if (mPreviewCallback != null) {
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        camera.addCallbackBuffer(buffer);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    /**
     * . If the preview has already started, applications should stop the preview first before changing preview size
     * @param holder
     * @param format
     * @param width
     * @param height
     */

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stopPreview();
    }

    public void switchCamera() {
    }

    public void setPreviewCallback(Camera.PreviewCallback mPreviewCallback) {
        this.mPreviewCallback = mPreviewCallback;
    }
    public void setOnChangedSizeListener(OnChangedSizeListener mOnChangedSizeListener) {
        this.mOnChangedSizeListener = mOnChangedSizeListener;
    }


    public interface OnChangedSizeListener {
        void onChanged(int width, int height);
    }
}
