package com.vtech.mobile.kidiconnect2021.customcamera.service;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

/**
 * Author: T.L. QIU
 * Date: 2020-11-09 10:04.
 */
public interface ICameraService {

  class Config {
    public float rate = 1.778f;
    public int minPreviewWidth;
    public int minPictureWidth;
  }

  int FLASH_MODE_AUTO = 0;
  int FLASH_MODE_ON = 1;
  int FLASH_MODE_OFF = 2;

  void open(int cameraId, Activity activity);

  void setPreviewTexture(SurfaceTexture texture);

  void setDisplayOrientation(int orientation) throws Exception;

  void setConfig(Config config);

  void setPreviewCallBack(IPreviewCallback previewCallBack);

  void startPreview();

  void stopPreview();

  void tackPicture(ITakePicture tackPicture);

  void setFlashlight(int flashMode);

  boolean isOpen();

  void setAutoFocus(Camera.AutoFocusCallback autoFocus);

  int getCameraId();

  Point getPreviewSize();

  Point getPictureSize();

  Camera getCamera();

  interface IPreviewCallback {
    void onPreviewFrame(byte[] bytes, int width, int height, Camera camera);
  }

  interface ITakePicture {
    public void onCapture(boolean success, byte[] data, Camera camera);
  }

  void close() throws Exception;

}
