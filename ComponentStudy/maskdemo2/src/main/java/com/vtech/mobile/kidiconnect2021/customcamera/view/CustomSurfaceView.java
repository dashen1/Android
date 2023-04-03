package com.vtech.mobile.kidiconnect2021.customcamera.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.vtech.mobile.kidiconnect2021.customcamera.drawer.VideoDrawer;
import com.vtech.mobile.kidiconnect2021.customcamera.service.ICameraService;
import com.vtech.mobile.kidiconnect2021.customcamera.service.impl.CameraService;

import java.util.Timer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.tillusory.sdk.TiSDKManager;

/**
 * 该控件用于相机预览，视频录像
 */
public class CustomSurfaceView extends GLSurfaceView
    implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

  private final String TAG = "CustomSurfaceView";
  /**
   * 用于针对相机的操作，如打开，关闭
   */
  private ICameraService cameraService;
  /**
   * 前面的cameraService用于操作相机
   * 这个mCameraDrawer用于渲染
   */
  private VideoDrawer mCameraDrawer;

  private int dataWidth = 0, dataHeight = 0;

  private boolean isSetParm = false;
  /**
   * 默认使用前置摄像头
   * console只有一个后置摄像头，需要额外处理
   */
  private static int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
  /**
   * 录制完成后执行的任务，由外部设置。
   */
  private static Runnable recordComplete;
  /**
   * 真正开始录制时的执行的任务
   */
  private static Runnable realStartRecord;
  /**
   * 相机是否打开
   */
  private static boolean isCameraOpenStatus = false;

  private Activity activity;
  /**
   * 定时器，每隔一段时间对焦一下
   */
  private Timer autoFocusTimer;

  public CustomSurfaceView(Context context) {
    this(context, null);
  }

  public CustomSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * 初始化工作
   */
  private void init() {

    setEGLContextClientVersion(2);
    setRenderer(this);
    setRenderMode(RENDERMODE_WHEN_DIRTY);


    setPreserveEGLContextOnPause(true);

    setCameraDistance(100);
    // 初始化相机绘制类
    mCameraDrawer = new VideoDrawer(getResources());
    // 相机管理服务
    cameraService = CameraService.getInstance();
  }

  /**
   * 具体打开相机的方法
   *
   * @param cameraId  相机ID
   * @param openFocus 是否开启对焦
   */
  private void openCameraAndPreview(int cameraId, boolean openFocus) {
    try {

      // 使用相机管理类操作打开底层相机
      cameraService.open(cameraId, activity);
      // 调用这个：每次进入页面开始相机预览，会自动跳掉15帧之后，才使用TISDK来渲染，避免白屏问题
      mCameraDrawer.resetTiSkipFrameCount();
      // 向CameraDrawer传递相机ID
      mCameraDrawer.setCameraId(cameraId);
      // 获取相机预览尺寸
      final Point previewSize = cameraService.getPreviewSize();
      if(previewSize != null) {
        dataWidth = previewSize.x;
        dataHeight = previewSize.y;
      }
      SurfaceTexture texture = mCameraDrawer.getTexture();
      texture.setOnFrameAvailableListener(this);
      cameraService.setPreviewTexture(texture);
      // 开启相机的预览
      cameraService.startPreview();
      // 如果要开启对焦，则调用openAutoFocus，该方法会创建一个定时器，每个一段时间的对焦一下
      TiSDKManager.getInstance().setMask("CatR");
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    }
  }

  /**
   * 判断相机是否打开
   *
   * @return true 、false
   */
  public boolean isOpen() {
    return cameraService.isOpen();
  }


  /**
   * 纹理创建与否
   */
  private volatile boolean isSurfaceCreated = false;

  /**
   * 这个方法会在创建纹理时调用
   * 所以页面使用CustomSurfaceView控件，然后自动创建纹理，再调用这个方法，我们
   * 在方法体中打开相机并预览
   *
   * @param gl
   * @param config
   */
  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    mCameraDrawer.onSurfaceCreated(gl, config);
    if (!isSetParm) {
      // 自动打开相机
      openCameraAndPreview(cameraId, true);
      stickerInit();
    }
    mCameraDrawer.setPreviewSize(dataWidth, dataHeight);
    isSurfaceCreated = true;
    mCameraDrawer.setPreviewSize(getWidth(), getHeight());
  }

  private int surfaceWidth = 0;
  private int surfaceHeight = 0;

  /**
   * 页面变化时会调用
   *
   * @param gl
   * @param width
   * @param height
   */
  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {

    mCameraDrawer.resetTiSkipFrameCount();


    if (isSurfaceCreated && surfaceWidth == width && surfaceHeight == height) {
      return;
    }
    surfaceWidth = width;
    surfaceHeight = height;
    mCameraDrawer.onSurfaceChanged(gl, width, height);
  }

  /**
   * 在Draw画面时，会循环调用该方法
   * 我们在这个方法中，调用了mCameraDrawer.onDrawFrame(gl);完成Ti的渲染
   *
   * @param gl
   */
  @Override
  public void onDrawFrame(GL10 gl) {
    if (isSetParm) {
      if (renderBlackFrame || skipBlackFrameCount > 0) {
        skipBlackFrameCount--;
        mCameraDrawer.drawBlackFrame(gl);
      } else {
        mCameraDrawer.onDrawFrame(gl);
      }
    }
  }


  private boolean renderBlackFrame = false;
  private int skipBlackFrameCount = 0;

  public static void SetIsCameraOpenStatus(boolean status) {
    isCameraOpenStatus = status;
  }

  /**
   * 用于onResume时，重新打开相机
   * 如在Home页面调用onResume方法时，调用了本控件的onResume方法，进行相机重新打开
   */
  @Override
  public void onResume() {
    super.onResume();

    post(new Runnable() {
      @Override
      public void run() {
        if (isSetParm) {
          // 重新打开相机
          openCameraAndPreview(cameraId, true);
        }
        mCameraDrawer.resetTiSkipFrameCount();
      }
    });
    mCameraDrawer.resetTiSkipFrameCount();
    // Since the view is always re-created on smart phone, No need to open again.
  }


  /**
   * 每次跳转到其它页面 都会调用该方法
   * 此时需要关闭相机，取消对焦，
   */
  @Override
  public void onPause() {
    super.onPause();
      if (cameraService != null) {
        try {
          cameraService.close();
          mCameraDrawer.resetTiSkipFrameCount();
          Log.d(TAG, "reset count " + mCameraDrawer.getCount());
        } catch (Exception e) {
          Log.e(TAG, e.getMessage());
        }
      }

  }

  public void cameraServiceStop() {

    if (cameraService != null) {
      try {
        cameraService.close();
      } catch (Exception e) {
        Log.e(TAG, e.getMessage());
      }
    }
  }

  /**
   * 开始视频录制
   */
  public void startRecord() {
    // mCameraDrawer.resetRecordConfig();
    queueEvent(new Runnable() {
      @Override
      public void run() {
        mCameraDrawer.startRecord();
      }
    });
  }

  /**
   * 停止视频录制
   */
  public void stopRecord() {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        mCameraDrawer.stopRecord();
      }
    });
  }

  /**
   * 录制完成后要执行的任务
   * 如录制完成后，调转到其它页面，此时可以在录制前设置
   *
   * @param runnable
   */
  public static void onRecordComplete(Runnable runnable) {
    recordComplete = runnable;
  }

  /**
   * 点击录制后，真正开启录制时要执行的任务
   *
   * @param runnable
   */
  public static void onRealStartRecord(Runnable runnable) {
    realStartRecord = runnable;
  }

  /**
   * 强制关闭录制，如录制一半退出页面，此时强制停止录制
   */
  public void forceStopRecord() {
    recordComplete = null;
    mCameraDrawer.forceStopRecord();
  }

  /**
   * 设置录制视频保存路径
   *
   * @param path 保存路径
   */
  public void setSavePath(String path) {
    mCameraDrawer.setSavePath(path);
  }

  /**
   * 获知视频保存录制
   *
   * @return 保存路径
   */
  public String getSavePath() {
    return mCameraDrawer.getSavePath();
  }

  public void resume(final boolean auto) {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        mCameraDrawer.onResume(auto);
      }
    });
  }

  public void pause(final boolean auto) {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        mCameraDrawer.onPause(auto);
      }
    });
  }

//  public void onTouch(final MotionEvent event) {
//    queueEvent(new Runnable() {
//      @Override
//      public void run() {
//        mCameraDrawer.onTouch(event);
//      }
//    });
//  }

//  public void setOnFilterChangeListener(SlideGpuFilterGroup.OnFilterChangeListener listener) {
//    mCameraDrawer.setOnFilterChangeListener(listener);
//  }

  private void stickerInit() {
    if (!isSetParm && dataWidth > 0 && dataHeight > 0) {
      isSetParm = true;
    }
  }

  @Override
  public void onFrameAvailable(SurfaceTexture surfaceTexture) {
    this.requestRender();
  }

  public static Runnable getRealStartRecordTask() {
    return realStartRecord;
  }

  public static Runnable getRecordCompleteTask() {
    return recordComplete;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }
}
