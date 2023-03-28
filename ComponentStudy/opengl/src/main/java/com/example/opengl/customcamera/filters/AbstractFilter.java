package com.example.opengl.customcamera.filters;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.opengl.customcamera.utils.MatrixUtils;
import com.example.opengl.customcamera.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class AbstractFilter {

    private final String TAG = "AbstractFilter";
    public int vPosition;
    public int vCoord;
    public int vTexture;
    public int program;
    FloatBuffer vertexBuffer;
    FloatBuffer textureBuffer;

    protected int mHMatrix;

    protected static final float[] OM = MatrixUtils.getOriginalMatrix();

    private float[] v_matrix = Arrays.copyOf(OM, 16);

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        vertexBuffer = ByteBuffer.allocateDirect(4*4*2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer = ByteBuffer.allocateDirect(4*4*2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        initCoord();
        initGL(context, vertexShaderId, fragmentShaderId);
    }

    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        String vertexShader = OpenGLUtils.readRawTextFile(context, vertexShaderId);
        String fragmentShader = OpenGLUtils.readRawTextFile(context, fragmentShaderId);

        program = OpenGLUtils.loadProgram(vertexShader, fragmentShader);
        //获得程序中的变量 索引
        vPosition = GLES20.glGetAttribLocation(program,"vPosition");
        vCoord = GLES20.glGetAttribLocation(program,"vCoord");
        vTexture = GLES20.glGetUniformLocation(program,"vTexture");
        mHMatrix = GLES20.glGetUniformLocation(program, "vM");
    }

    private void initCoord() {
        vertexBuffer.clear();
        vertexBuffer.put(OpenGLUtils.VERTEX);

        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXTURE);
    }

    public int onDraw(int texture, FilterChain filterChain){
        Log.d(TAG,"onDraw start.");
        FilterContext filterContext = filterChain.filterContext;
        GLES20.glViewport(0, 0,filterContext.width, filterContext.height);
        GLES20.glUseProgram(program);
        vertexBuffer.position(0);//从0位置开始
        //归一化 normalized 坐标系范围[-1,1],将超出的范围进行缩小
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //cpu数据到GPU，默认情况下着色器无法读取到这个数据，需要启用一下才能读取
        GLES20.glEnableVertexAttribArray(vPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //激活一个用来显示图片的画布 0号画布 ，最多可以开启 16个
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //将画布和纹理绑定 下面的设置都是针对 当前texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);
        //片元着色器就可以从0号画布取到数据
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, v_matrix, 0);
        beforeDraw(filterContext);//画之前调整摄像头旋转
        //通知画画 画矩形 因为所有形状都可以通过三角形绘制出来，从0号位置开始，共有4个顶点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        afterDraw(filterChain.filterContext);
        //解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        Log.d(TAG,"onDraw end.");
        return texture;
    }

    public void beforeDraw(FilterContext filterContext){
    }

    public void afterDraw(FilterContext filterContext){
    }

    public void release(){
        GLES20.glDeleteProgram(program);
    }
}
