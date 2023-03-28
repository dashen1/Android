package com.vtech.mobile.kidiconnect2021.customcamera.service;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;


public class CameraService implements ICameraService {

    private final static String TAG = "CameraServiceImpl";

    private static volatile CameraService instance;

    private Config mConfig;

    private Camera mCamera;

    private Camera.Parameters mParameters;

    private Camera.Size mPreviewSize;

    private Point mPreviewPoint;
    private Point mPicturePoint;

    private int mCameraId = -1;

    private boolean isOpen;

    public CameraService() {
        this.mConfig = new Config();
        mConfig.minPreviewWidth = 720;
        mConfig.minPictureWidth = 720;
        mConfig.rate = 1.778f;
    }

    public static CameraService getInstance(){
        if (instance==null){
            synchronized (CameraService.class){
                if (instance==null){
                    instance = new CameraService();
                }
            }
        }
        return instance;
    }

    @Override
    public void open(int cameraId, Activity activity) {
        Log.d(TAG,"open");
        this.mCameraId = cameraId;
        this.mCamera = Camera.open(cameraId);
        try {
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
                setCameraDisplayOrientation(activity, cameraId, mCamera);
            isOpen = true;
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
            isOpen = false;
        }
        if (this.mCamera !=null){
            try {
                // 如果是console可重新设置
                this.mParameters = this.mCamera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                this.mCamera.setParameters(mParameters);
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
            Camera.Size pre = mParameters.getPreviewSize();
            this.mPreviewPoint = new Point(pre.height, pre.width);
            this.mPicturePoint = new Point(pre.height, pre.width);
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Log.d(TAG,"setCameraDisplayOrientation");
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) {
        Log.d(TAG,"setPreviewTexture");
        try {
            this.mCamera.setPreviewTexture(texture);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void setDisplayOrientation(int orientation) throws Exception {
        Log.d(TAG,"setDisplayOrientation");
        if (mCamera != null)
            mCamera.setDisplayOrientation(orientation);
        else
            Log.w(TAG,"setDisplayOrientation failed because mCamera is null.");
    }

    @Override
    public void setConfig(Config config) {
        Log.d(TAG,"setConfig");
        this.mConfig = config;
    }

    @Override
    public void setPreviewCallBack(IPreviewCallback previewCallBack) {
        Log.d(TAG,"setPreviewCallBack");
        if (this.mCamera != null && previewCallBack != null) {
            this.mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    previewCallBack.onPreviewFrame(
                            data, mPreviewPoint.x, mPreviewPoint.y, camera
                    );
                }
            });
        }
    }

    @Override
    public void startPreview() {
        if (null != this.mCamera) {
            Log.d(TAG,"startPreview");
            this.mCamera.startPreview();
        }
    }

    @Override
    public void stopPreview() {
        if (this.mCamera != null) {
            Log.d(TAG,"stopPreview");
            this.mCamera.stopPreview();
        }
    }

    @Override
    public void tackPicture(ITakePicture tackPicture) {
        if (this.mCamera!=null){
            this.mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    boolean flag = false;
                    if (data!=null&&data.length>0){
                        flag =true;
                    }
                    mCamera.startPreview();
                    tackPicture.onCapture(flag, data, mCamera);
                }
            });
        }
    }

    @Override
    public void setFlashlight(int flashMode) {
        switch (flashMode) {
            case FLASH_MODE_AUTO:
                if (mParameters != null) {
                    this.mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    this.mCamera.setParameters(mParameters);
                    this.mCamera.startPreview();
                }
            case FLASH_MODE_ON:
                if (mParameters != null) {
                    this.mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    this.mCamera.setParameters(mParameters);
                    this.mCamera.startPreview();
                }
                break;
            case FLASH_MODE_OFF:
                if (mParameters != null) {
                    this.mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    this.mCamera.setParameters(mParameters);
                    this.mCamera.startPreview();
                }
                break;
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void setAutoFocus(Camera.AutoFocusCallback autoFocus) {
        if (mCamera != null)
            mCamera.autoFocus(autoFocus);
    }

    @Override
    public int getCameraId() {
        return this.mCameraId;
    }

    @Override
    public Point getPreviewPoint() {
        return this.mPreviewPoint;
    }

    @Override
    public Point getPicturePoint() {
        return this.mPicturePoint;
    }

    @Override
    public Camera getCamera() {
        return mCamera;
    }

    @Override
    public void close() throws Exception {
        if(mCamera!=null) {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewCallback(null);
            this.mCamera.release();
            this.mCamera = null;
            isOpen = false;
        } else{
            Log.w(TAG,"close camera failed because mCamera is null.");
        }
    }
}
