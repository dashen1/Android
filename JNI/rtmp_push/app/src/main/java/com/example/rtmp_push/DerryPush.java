package com.example.rtmp_push;

import android.app.Activity;
import android.view.SurfaceHolder;

public class DerryPush {

    static {
        System.loadLibrary("rtmp_push");
    }

    private CameraHelper cameraHelper;
    private VideoChannel videoChannel;

    public DerryPush(Activity activity, int width, int height, int cameraId, int fps, int bitrate) {
        native_init();
        videoChannel = new VideoChannel();
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        cameraHelper.setPreviewDisplay(surfaceHolder);
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }

    public void startLive() {
        videoChannel.startLive();
    }

    public void stopLive() {
        videoChannel.stopLive();
    }

    public void release() {
        videoChannel.release();
    }

    private native void native_init();
    private native void native_start(String path);
    private native void native_stop();
    private native void native_release();

    private native void native_initVideoEncoder(int width, int  height, int mFps, int bitrate);
    private native void native_pushVideo(byte[] data);
}
