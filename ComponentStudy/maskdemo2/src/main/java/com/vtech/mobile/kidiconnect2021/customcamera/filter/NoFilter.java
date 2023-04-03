package com.vtech.mobile.kidiconnect2021.customcamera.filter;

import android.content.res.Resources;
import android.opengl.GLES20;

public class NoFilter extends AFilter {

  public NoFilter(Resources res) {
    super(res);
  }

  @Override
  protected void onCreate() {
    createProgramByAssetsFile("shader/base_vertex.sh",
            "shader/base_fragment.sh");
  }

  /**
   * 背景默认为灰色 #666
   */
  @Override
  protected void onClear() {
    // 背景黑色
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    // 背景灰色
//        GLES20.glClearColor(0.6f, 0.6f, 0.6f, 0.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
  }

  @Override
  protected void onSizeChanged(int width, int height) {

  }
}
