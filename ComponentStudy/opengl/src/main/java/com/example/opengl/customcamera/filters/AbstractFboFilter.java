package com.example.opengl.customcamera.filters;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.opengl.customcamera.utils.OpenGLUtils;

public class AbstractFboFilter extends AbstractFilter{

    private final String TAG = "AbstractFboFilter";
    int[] frameBuffer;
    int[] frameTexture;

    private int viewport_offset_x = 0;
    private int viewport_offset_y = 0;

    private int viewport_width = 0;
    private int viewport_height = 0;


    public AbstractFboFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    public void createFBO(int width, int height){
        releaseFrame();
        //1.创建FBO+FBO中的纹理
        frameBuffer = new int[1];
        frameTexture = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        OpenGLUtils.glGenTextures(frameTexture);
        /**
         * 2.fbo与纹理关联
         */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTexture[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //关联纹理fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameTexture[0], 0);
        /**
         * 3.解除绑定
         */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        FilterContext filterContext = filterChain.filterContext;
        createFBO(filterContext.width, filterContext.height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        //设置绘制区域

        super.onDraw(texture, filterChain);
        Log.d(TAG,"beforeDraw");
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return filterChain.process(frameTexture[0]);
    }

    @Override
    public void release() {
        super.release();
        releaseFrame();
    }

    private void releaseFrame() {
        if (frameTexture != null) {
            GLES20.glDeleteTextures(1, frameTexture, 0);
            frameTexture = null;
        }

        if (frameBuffer != null) {
            // GLES20.glDeleteTextures 这里由于删除的是纹理而不是framebuffer,导致进程内存占用过多被系统杀死
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
        }
    }

}
