package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import com.vtech.mobile.kidiconnect2021.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OpenGlUtils {

    public static final float[] VERTEX = {
            -1.0f,-1.0f,
            1.0f,-1.0f,
            -1.0f,1.0f,
            1.0f,1.0f
    };

    public static final float[] TEXTURE = {
            0.0f,0.0f,
            1.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f
    };

    public static void glGenTextures(int[] textures){
        GLES20.glGenTextures(textures.length, textures, 0);
        for (int i = 0; i < textures.length; i++) {
            //绑定纹理，后续配置纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[i]);
            //设置纹理过滤参数
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);

                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //通过资源路径加载shader脚本文件
    public static String uRes(String path) {
        Resources resources = MyApplication.getContext().getResources();
        StringBuilder result = new StringBuilder();
        try (InputStream is = resources.getAssets().open(path)){
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))) {
                result.append(new String(buffer, 0, ch));
            }
        } catch (Exception e) {
            return null;
        }
        return result.toString().replaceAll("\\r\\n", "\n");
    }

    public static int loadProgram(String vSource, String fSource) {
        /**
         * 创建顶点着色器
         */
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //加载着色器代码
        GLES20.glShaderSource(vShader, vSource);
        //编译
        GLES20.glCompileShader(vShader);
        //查看编译状态
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("compile vShader:" + GLES20.glGetShaderInfoLog(vShader));
        }
        /**
         * 创建片元着色器
         */
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        //加载片元着色器代码
        GLES20.glShaderSource(fShader, fSource);
        //编译
        GLES20.glCompileShader(fShader);
        //查看编译状态
        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("compile fShader:" + GLES20.glGetShaderInfoLog(fShader));
        }
        /**
         * 创建着色器程序
         */
        int program = GLES20.glCreateProgram();
        //将着色器程序和着色器关联
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        //链接着色器程序
        GLES20.glLinkProgram(program);
        //查看链接状态
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("link program:" + GLES20.glGetProgramInfoLog(program));
        }
        //着色器程序链接成功 删除着色器
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        return program;
    }

    public static void copyAssets2SdCard(Context context, String src, String dst){
        File file = new File(dst);
        if(!file.exists()){
            try {
                InputStream is = context.getAssets().open(src);
                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[2048];
                while((len = is.read(buffer)) !=-1){
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
