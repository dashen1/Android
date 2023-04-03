package com.vtech.mobile.kidiconnect2021.customcamera.service.impl;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

import com.vtech.mobile.kidiconnect2021.customcamera.service.ICameraService;

import java.io.IOException;

;

/**
 * CameraService
 * <p>
 * 系统相机管理类，用来操作相机，如打开，关闭，设置相机分辨率等
 * <p>
 * Console G3 Camera Support Size:
 * 1920 * 1080 (16:9)
 * 1280 * 960  (12:9)
 * 1280 * 720  (16:9)
 */
public class CameraService implements ICameraService {

  private final static String TAG = "CameraService";
  /**
   * 单例模式
   */
  private static volatile CameraService instance;
  /**
   * 配置类：最小预览宽高与比例
   */
  private ICameraService.Config config;
  /**
   * 系统API：Camera
   */
  private Camera mCamera;
  /**
   * 记录相机参数
   */
  private Camera.Parameters param;
  /**
   * 预览的尺寸
   */
  private Camera.Size previewSize;

  private Point mPreviewSize;
  private Point mPictureSize;
  /**
   * Camera ID
   */
  private int mCameraId = -1;
  /**
   * 相机是否打开
   */
  private boolean isOpen;

  private CameraService() {
    // 默认参数
    this.config = new Config();
    config.minPreviewWidth = 720;
    config.minPictureWidth = 720;
    config.rate = 1.778f;
  }

  /**
   * 获取CameraService实例
   * 单例模式
   *
   * @return
   */
  public static CameraService getInstance() {
    if (instance == null) {
      synchronized (CameraService.class) {
        if (instance == null)
          instance = new CameraService();
      }
    }
    return instance;
  }

  /**
   * 打开硬件相机
   *
   * @param cameraId 相机ID
   * @param activity 活动
   */
  @Override
  public void open(int cameraId, Activity activity) {

    // ================================ 打开相机 ====================================================
    this.mCameraId = cameraId;

    try {
        // Android根据相机ID打开对应相机
        this.mCamera = Camera.open(cameraId);
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
          // 设置相机方向
          setCameraDisplayOrientation(activity, cameraId, mCamera);
      // 标志着相机硬件打开
      isOpen = true;
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
      isOpen = false;
    }

    // ================================ 为相机设置参数 ===============================================


    if (this.mCamera != null) {
      try {

        this.param = this.mCamera.getParameters();

        // 设置闪光灯为关闭状态，当前后置相机时，由于需要调用该方法打开相机，此时就会自动把闪光灯关闭掉
        param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        this.mCamera.setParameters(param);
      } catch (Exception e) {
        Log.e(TAG, e.getMessage());
      }

      // 把相机的预览参数给记录下来
      Camera.Size pre = param.getPreviewSize();
      this.mPreviewSize = new Point(pre.height, pre.width);
      this.mPictureSize = new Point(pre.height, pre.width);
    }
  }

  /**
   * 用于校准相机方向，是固定代码
   *
   * @param activity
   * @param cameraId
   * @param camera
   */
  public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
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

  /**
   * 给相机设置绘制纹理
   *
   * @param texture SurfaceTexture
   */
  @Override
  public void setPreviewTexture(SurfaceTexture texture) {
    try {
      this.mCamera.setPreviewTexture(texture);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
  }

  @Override
  public void setDisplayOrientation(int orientation) throws Exception {
    if (mCamera != null)
      mCamera.setDisplayOrientation(orientation);
    else
      Log.w(TAG,"setDisplayOrientation failed because mCamera is null.");
  }

  @Override
  public void setConfig(Config config) {
    this.config = config;
  }

  @Override
  public void setPreviewCallBack(final IPreviewCallback previewCallBack) {
    if (this.mCamera != null && previewCallBack != null) {
      this.mCamera.setPreviewCallback(new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
          previewCallBack.onPreviewFrame(
              data, mPreviewSize.x, mPreviewSize.y, camera
          );
        }
      });
    }
  }

  /**
   * 相机开始预览
   */
  @Override
  public void startPreview() {
    if (null != this.mCamera) {
      this.mCamera.startPreview();
    }
  }

  /**
   * 相机停止预览
   */
  @Override
  public void stopPreview() {
    if (this.mCamera != null) {
      this.mCamera.stopPreview();
    }
  }

  /**
   * 拍照
   * @param tackPicture 拍照后的回调
   */
  @Override
  public void tackPicture(final ITakePicture tackPicture) {
    if (this.mCamera != null) {
      this.mCamera.takePicture(null, null, new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
          boolean falg = false;
          if (data != null && data.length > 0) {
            falg = true;
          }
          mCamera.startPreview();
          tackPicture.onCapture(falg, data, mCamera);
        }
      });
    }
  }

  @Override
  public Point getPreviewSize() {
    return this.mPreviewSize;
  }

  @Override
  public Point getPictureSize() {
    return this.mPictureSize;
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

  @Override
  public void setFlashlight(int flashMode) {
    switch (flashMode) {
      case FLASH_MODE_AUTO:
        if (param != null) {
          this.param.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
          this.mCamera.setParameters(param);
          this.mCamera.startPreview();
        }
      case FLASH_MODE_ON:
        if (param != null) {
          this.param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
          this.mCamera.setParameters(param);
          this.mCamera.startPreview();
        }
        break;
      case FLASH_MODE_OFF:
        if (param != null) {
          this.param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
          this.mCamera.setParameters(param);
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

}
