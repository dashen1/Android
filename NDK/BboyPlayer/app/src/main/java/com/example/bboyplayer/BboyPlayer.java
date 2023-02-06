package com.example.bboyplayer;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class BboyPlayer implements SurfaceHolder.Callback{

  static {
    System.loadLibrary("native-lib");
  }

  private static final String TAG = "JAVA BboyPlayer";


  /* TODO 第二节课新增 --- start */

  // 错误代码 ================ 如下
  // 打不开视频
  // #define FFMPEG_CAN_NOT_OPEN_URL 1
  private static final int FFMPEG_CAN_NOT_OPEN_URL = 1;

  // 找不到流媒体
  // #define FFMPEG_CAN_NOT_FIND_STREAMS 2
  private static final int FFMPEG_CAN_NOT_FIND_STREAMS = 2;

  // 找不到解码器
  // #define FFMPEG_FIND_DECODER_FAIL 3
  private static final int FFMPEG_FIND_DECODER_FAIL = 3;

  // 无法根据解码器创建上下文
  // #define FFMPEG_ALLOC_CODEC_CONTEXT_FAIL 4
  private static final int FFMPEG_ALLOC_CODEC_CONTEXT_FAIL = 4;

  //  根据流信息 配置上下文参数失败
  // #define FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL 6
  private static final int FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL = 6;

  // 打开解码器失败
  // #define FFMPEG_OPEN_DECODER_FAIL 7
  private static final int FFMPEG_OPEN_DECODER_FAIL = 7;

  // 没有音视频
  // #define FFMPEG_NOMEDIA 8
  private static final int FFMPEG_NOMEDIA = 8;

  static {
    System.loadLibrary("native-lib");
  }

  private OnPreparedListener onPreparedListener;
  private OnErrorListener onErrorListener;
  private OnProgressListener onProgressListener;

  private String dataSource;

  private SurfaceHolder surfaceHolder;

  public BboyPlayer() {

  }

  public void setDataSource( String dataSource){
    this.dataSource = dataSource;
  }

  public void prepare(){
    Log.d(TAG,"BboyPlayer prepare");
    prepareNative(dataSource);
  }

  public void start(){
    startNative();
  }

  public void stop(){
    stopNative();
  }

  public void release(){
    releaseNative();
  }

  public int getDuration(){
    return getDurationNative();
  }

  public void seek(int playProgress){

  }

  public void onPrepared(){
    if (onPreparedListener!=null){
      onPreparedListener.onPrepared();
    }
  }

  public void onError(int errorCode){
    if(onErrorListener!=null){
      String msg=null;
      switch (errorCode){
        case FFMPEG_CAN_NOT_OPEN_URL:
          msg = "打不开视频";
          break;
        case FFMPEG_CAN_NOT_FIND_STREAMS:
          msg = "找不到流媒体";
          break;
        case FFMPEG_FIND_DECODER_FAIL:
          msg = "找不到解码器";
          break;
        case FFMPEG_ALLOC_CODEC_CONTEXT_FAIL:
          msg = "无法根据解码器创建上下文";
          break;
        case FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL:
          msg = "根据流信息 配置上下文参数失败";
          break;
        case FFMPEG_OPEN_DECODER_FAIL:
          msg = "打开解码器失败";
          break;
        case FFMPEG_NOMEDIA:
          msg = "没有音视频";
          break;
      }
      onErrorListener.onError(msg);
    }
  }

  public void setSurfaceView(SurfaceView surfaceView){
    if(this.surfaceHolder!=null){
      surfaceHolder.removeCallback(this);
    }
    surfaceHolder = surfaceView.getHolder();
    surfaceHolder.addCallback(this);
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    //界面发生了改变 横屏 竖屏
    setSurfaceNative(surfaceHolder.getSurface());
  }

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

  }

  public void onProgress(int progress){
    if (onProgressListener!=null){
      onProgressListener.onProgress(progress);
    }
  }

  public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
    this.onPreparedListener = onPreparedListener;
  }

  public void setOnErrorListener(OnErrorListener onErrorListener) {
    this.onErrorListener = onErrorListener;
  }

  public void setOnProgressListener(OnProgressListener onProgressListener) {
    this.onProgressListener = onProgressListener;
  }


  public interface OnPreparedListener{
    void onPrepared();
  }

  public interface OnErrorListener{
    void onError(String errorInfo);
  }

  public interface OnProgressListener{
    void onProgress(int progress);
  }


  private native void prepareNative(String dataSource);
  private native void startNative();
  private native void stopNative();
  private native void releaseNative();
  private native void setSurfaceNative(Surface surface);
  private native int getDurationNative();
  private native void seekNative(int playValue);
}
