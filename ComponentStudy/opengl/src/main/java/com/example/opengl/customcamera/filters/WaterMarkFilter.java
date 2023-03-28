package com.example.opengl.customcamera.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.opengl.R;
import com.example.opengl.customcamera.utils.MatrixUtils;
import com.example.opengl.customcamera.utils.OpenGLUtils;

import java.util.Arrays;

public class WaterMarkFilter extends AbstractFilter{

    private final String TAG = "WaterMarkFilter";


    private float[] matrix = Arrays.copyOf(OM, 16);

    /**水印的放置位置和宽高*/
    private int x,y,w,h;
    /**控件的大小*/
    private int width,height;
    /**水印图片的bitmap*/
    private Bitmap mBitmap;
    private int[] textures;


    public WaterMarkFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
        createTexture(context);
    }

    private void createTexture(Context context){
        textures = new int[1];
        OpenGLUtils.glGenTextures(textures);
        //把图片加载到创建的纹理中
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile_12);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        Log.d(TAG,"createTexture matrix : "+Arrays.toString(matrix));
        MatrixUtils.flip(matrix,false,true);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        return super.onDraw(texture, filterChain);
    }


    @Override
    public void afterDraw(FilterContext filterContext) {
        super.afterDraw(filterContext);
        Log.d(TAG,"afterDraw matrix : "+Arrays.toString(matrix));
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0);
        //开启混合模式 因为是纹理和图片叠加的，所以需要设置
        //如果不开启混合模式的话，此时画的图像会覆盖原来已经绘制的图像
        GLES20.glEnable(GLES20.GL_BLEND);
        //GLES20.GL_ONE：表示使用鼻子原颜色为主，如果原颜色是透明的，则以摄像头数据为主
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        //计算坐标
        //基于画布的鼻子中心点的x
        //给定鼻子贴纸的窗口大小
        GLES20.glViewport(30, 50, 300, 400);

        GLES20.glUseProgram(program);
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(vTexture, 0);

        //通知画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        //关闭混合模式
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
