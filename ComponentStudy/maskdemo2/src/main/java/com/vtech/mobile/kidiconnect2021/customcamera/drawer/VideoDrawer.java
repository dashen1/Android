package com.vtech.mobile.kidiconnect2021.customcamera.drawer;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.vtech.mobile.kidiconnect2021.R;
import com.vtech.mobile.kidiconnect2021.customcamera.filter.AFilter;
import com.vtech.mobile.kidiconnect2021.customcamera.filter.CameraFilter;
import com.vtech.mobile.kidiconnect2021.customcamera.filter.GroupFilter;
import com.vtech.mobile.kidiconnect2021.customcamera.filter.NoFilter;
import com.vtech.mobile.kidiconnect2021.customcamera.filter.WaterMarkFilter;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.EasyGlUtils;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.MatrixUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;

/**
 * 绘制视频预览，以及录制视频
 * 在CustomSurfaceView中被使用
 * https://blog.csdn.net/qqchenjian318/article/details/77396653
 */
public class VideoDrawer implements GLSurfaceView.Renderer {
  /**
   * 预览过滤器
   */
  private final AFilter showFilter;
  /**
   * 绘制过滤器
   */
  private final AFilter drawFilter;
  /**
   * 绘制水印的过滤器
   */
  private final GroupFilter mBeFilter;
  /**
   * 纹理
   */
  private SurfaceTexture mSurfaceTextrue;
  /**
   * 预览数据的宽高
   */
  private int mPreviewWidth = 0, mPreviewHeight = 0;
  /**
   * 控件的宽高
   */
  private int width = 0, height = 0;

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
  /**
   * 视频保存录制
   */
  private String savePath;
  /**
   * 纹理ID
   */
  private int textureID;
  private int[] fFrame = new int[1];
  private int[] fTexture = new int[1];
  /**
   * 用于显示的变换矩阵
   */
  private float[] SM = new float[16];


  public VideoDrawer(Resources resources) {
    // 初始化Filter
    showFilter = new NoFilter(resources);
    drawFilter = new CameraFilter(resources);
    mBeFilter = new GroupFilter(resources);

    /*
      这里是加录制视频水印
     */
    WaterMarkFilter waterMarkFilter = new WaterMarkFilter(resources);
    waterMarkFilter.setWaterMark(BitmapFactory.decodeResource(resources, R.drawable.default_profile_12));
    waterMarkFilter.setPosition(30, 50, 300, 400);
    addFilter(waterMarkFilter);

    recordingEnabled = false;

    // TODO: 2021/8/23 此处可以修改视频录制的参数
    // 视频宽
    int width = 240;
    // 视频高
    int height = 400;
    // 视频比特率
    int rate = 480 * 1000;

    RecordConfig(width, height, rate);
  }


  private void addFilter(AFilter filter) {
    mBeFilter.addFilter(filter);
  }

  private int mSaveWidth = 720;
  private int mSaveHeight = 1080;
  private int mBitRate = 300000;

  private void setSaveVideoRect(int width, int height) {
    mSaveWidth = width;
    mSaveHeight = height;
  }

  public void RecordConfig(int width, int height, int bitRate) {
    setSaveVideoRect(width, height);
    mBitRate = bitRate;
  }

  private float SaveWHRate() {
    return (float) mSaveWidth / mSaveHeight;
  }

  private float PreviewWHRate() {
    return (float) mPreviewWidth / mPreviewHeight;
  }

  /**
   * 创建视图时进行一些初始化工作
   * 创建纹理ID
   * 创建View 绑定纹理id
   * 创建drawFilter 绑定纹理id
   * 创建
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   * 绑定纹理id
   * 创建mBeFilter
   * <p>
   * Filter是需要和纹理ID进行绑定的
   */
  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    textureID = createTextureID();
    mSurfaceTextrue = new SurfaceTexture(textureID);

    drawFilter.create();
    drawFilter.setTextureId(textureID);

    showFilter.create();
    showFilter.setTextureId(textureID);

    mBeFilter.create();

    if (recordingEnabled) {
      recordingStatus = RECORDING_RESUMED;
    } else {
      recordingStatus = RECORDING_OFF;
    }
  }

  /**
   * 当视图大小发生改变时调用
   * 清除GL
   * 创建缓冲区
   * 创建纹理索引
   * 纹理名称与纹理进行绑定
   * 初始化一个纹理
   * 重置filter大小
   * <p>
   * 更新参数并重新绑定
   */
  @Override
  public void onSurfaceChanged(GL10 gl10, int i, int i1) {
    width = i;
    height = i1;
    /**
     * 清除
     */
    GLES20.glDeleteFramebuffers(1, fFrame, 0);
    GLES20.glDeleteTextures(1, fTexture, 0);
    /**
     * 创建一个帧染缓冲区对象
     */
    GLES20.glGenFramebuffers(1, fFrame, 0);
    /**
     * 根据纹理数量 返回的纹理索引
     */
    GLES20.glGenTextures(1, fTexture, 0);
    /**
     * 将生产的纹理名称和对应纹理进行绑定
     */
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[0]);
    /**
     * 根据指定的参数 生产一个2D的纹理 调用该函数前  必须调用glBindTexture以指定要操作的纹理
     */
    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mPreviewWidth, mPreviewHeight,
        0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
    useTexParameter();
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    mBeFilter.setSize(mPreviewWidth, mPreviewHeight);
    drawFilter.setSize(mPreviewWidth, mPreviewHeight);

    int newPreviewWidth = mPreviewWidth;
    int newPreviewHeight = mPreviewHeight;
    if (SaveWHRate() > PreviewWHRate()) {
      newPreviewHeight = (int) (newPreviewHeight * PreviewWHRate() / SaveWHRate());
    } else {
      newPreviewWidth = (int) (newPreviewWidth * SaveWHRate() / PreviewWHRate());
    }

    MatrixUtils.getShowMatrix(SM, newPreviewWidth, newPreviewHeight, width, height);
    //MatrixUtils.getShowMatrix(SM, mPreviewWidth, mPreviewWidth, width, height);
    showFilter.setMatrix(SM);
  }

  /**
   * 切换摄像头的时候
   * 会出现画面颠倒的情况
   * 通过跳帧来解决
   */
  boolean switchCamera = false;
  int skipFrame;

  public void switchCamera() {
    switchCamera = true;
  }

  /**
   * 通过这个来控制视频方向
   */
  private TiRotation drawTiRotation = TiRotation.CLOCKWISE_ROTATION_180;

  /**
   * 重置跳帧计数
   * 每次开启预览画面时，都跳掉几帧再使用Ti，避免白屏问题
   */
  public void resetTiSkipFrameCount() {
    count = 15;
    useTiSdk = false;
  }

  public int getCount() {
    return count;
  }

  private int count = 15;

  // 倒置显示
  private boolean needInversion = false;

  /**
   * TODO 该方法比较重要，绘制画面是会自动循环调用该方法
   *
   * @param gl10
   */
  @Override
  public void onDrawFrame(GL10 gl10) {
    try {
      mSurfaceTextrue.updateTexImage();

      // 切换相机时，掐掉几帧不显示
      if (switchCamera) {
        skipFrame++;
        if (skipFrame > 1) {
          skipFrame = 0;
          switchCamera = false;
        }
        return;
      }

      // 根据这个变量，控制画面是正常显示还是旋转180度显示
      // 避免某些设备出现画面倒置问题
      // 看看本类的matchOrientation方法
      Log.d("ViewDrawer","needInversion : "+needInversion);
      // 不旋转直接显示
      normalShow();
    } catch (Exception ignored) {

    }
  }


  public void drawBlackFrame(GL10 gl10) {
    try {
      mSurfaceTextrue.updateTexImage();
      GLES20.glViewport(0, 0, width, height);
      showFilter.setTextureId(0);
      showFilter.draw();
      EasyGlUtils.unBindFrameBuffer();
    } catch (Exception ignored) {

    }
  }

  private volatile boolean useTiSdk;

  private void normalShow() {

    int textureId = fTexture[0];

    if (!useTiSdk) {
      if (count-- < 0)
        useTiSdk = true;
    } else {
      // TODO: 2021/8/23 此处使用TISDK进行渲染
      // 可参考Ti的官方文档
//      textureId = TiSDKManager.getInstance().renderTexture2D(
//          fTexture[0],//2D纹理Id
//          mPreviewWidth,//图像宽度
//          mPreviewHeight,//图像高度
//          drawTiRotation,//TiRotation枚举，图像顺时针旋转的角度
//          true//图像是否左右镜像
//      );
    }

    EasyGlUtils.bindFrameTexture(fFrame[0], textureId);

    int newPreviewWidth = mPreviewWidth;
    int newPreviewHeight = mPreviewHeight;
    // newx and newy is to make camera preview in center.
    int newx = 0;
    int newy = 0;
    if (SaveWHRate() > PreviewWHRate()) {
      newPreviewHeight = (int) (newPreviewHeight * SaveWHRate() / PreviewWHRate());
      newy = -Math.abs(mPreviewHeight - newPreviewHeight) / 2;
    } else {
      newPreviewWidth = (int) (newPreviewWidth * PreviewWHRate() / SaveWHRate());
      newx = -Math.abs(mPreviewWidth - newPreviewWidth) / 2;
    }

    GLES20.glViewport(newx, newy, newPreviewWidth, newPreviewHeight);
    drawFilter.setTextureId(textureID);
    drawFilter.draw();
    EasyGlUtils.unBindFrameBuffer();

    /**
     *  添加贴图
     */
    TiSDKManager.getInstance().setMask("CatR");
    TiSDKManager.getInstance().setSticker("");
    textureId = TiSDKManager.getInstance().renderTexture2D(
            textureId,//2D纹理Id
          mPreviewWidth,//图像宽度
          mPreviewHeight,//图像高度
          drawTiRotation,//TiRotation枚举，图像顺时针旋转的角度
          false//图像是否左右镜像
      );
    /*
      绘制水印
     */
    mBeFilter.setTextureId(textureId);
    mBeFilter.draw();


    /*
      绘制显示的filter
     */
    GLES20.glViewport(0, 0, width, height);

    showFilter.setTextureId(mBeFilter.getOutputTexture());
    showFilter.draw();
  }


  /**
   * 设置预览效果的size
   */
  public void setPreviewSize(int width, int height) {
    if (mPreviewWidth != width || mPreviewHeight != height) {
      mPreviewWidth = width;
      mPreviewHeight = height;
    }
  }


  /**
   * 根据摄像头设置纹理映射坐标
   */
  public void setCameraId(int id) {
    drawFilter.setFlag(id);
  }

  public void startRecord() {
    recordingEnabled = true;
  }

  public void stopRecord() {
    recordingEnabled = false;
  }

  public void forceStopRecord() {
    recordingEnabled = false;
  }

  public void setSavePath(String path) {
    this.savePath = path;
  }

  public String getSavePath() {
    return this.savePath;
  }

  public SurfaceTexture getTexture() {
    return mSurfaceTextrue;
  }

  public void onPause(boolean auto) {
    if (auto) {
      if (recordingStatus == RECORDING_ON) {
        recordingStatus = RECORDING_PAUSED;
      }
      return;
    }
    if (recordingStatus == RECORDING_ON) {
      recordingStatus = RECORDING_PAUSE;
    }
  }

  public void onResume(boolean auto) {
    if (auto) {
      if (recordingStatus == RECORDING_PAUSED) {
        recordingStatus = RECORDING_RESUME;
      }
      return;
    }
    if (recordingStatus == RECORDING_PAUSED) {
      recordingStatus = RECORDING_RESUME;
    }
  }

  /**
   * 创建显示的texture
   */
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

  public void useTexParameter() {
    //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
    //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
    //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
  }
}
