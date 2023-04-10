package com.vtech.mobile.kidiconnect2021.customcamera.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.vtech.mobile.kidiconnect2021.customcamera.service.CameraService;
import com.vtech.mobile.kidiconnect2021.customcamera.service.ICameraService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;

public class CameraSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{

    private final static String TAG = "CustomSurfaceView";

    private ICameraService cameraService;

    private CameraRender cameraRender;

    private Activity activity;

    private volatile boolean isSurfaceCreated = false;

    private boolean isSetPara = false;

    private static int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private int surfaceWidth = 0;
    private int surfaceHeight = 0;

    private int dataWidth = 0, dataHeight = 0;

    public CameraSurfaceView(Context context) {
        this(context,null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        //设置刷新方式 手动刷新 自动刷新RENDERMODE_CONTINUOUSLY 每隔16ms一次
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(true);

        setCameraDistance(100);
        // 初始化相机绘制类
        cameraRender = new CameraRender(this);
        // 相机管理服务
        cameraService = CameraService.getInstance();
    }



    private void openCameraAndPreview(int cameraId, boolean openFocus) {
        try {

            // 使用相机管理类操作打开底层相机
            cameraService.open(cameraId, activity);
            // 向CameraDrawer传递相机ID
            // cameraRender.setCameraId(cameraId);
            // 获取相机预览尺寸
            final Point previewSize = cameraService.getPreviewPoint();
            if(previewSize != null) {
                dataWidth = previewSize.x;
                dataHeight = previewSize.y;
            }
            SurfaceTexture texture = cameraRender.getTexture();
            texture.setOnFrameAvailableListener(this);
            cameraService.setPreviewTexture(texture);
            // 开启相机的预览
            cameraService.startPreview();
            // 如果要开启对焦，则调用openAutoFocus，该方法会创建一个定时器，每个一段时间的对焦一下

            // TiSDKManager.getInstance().setMask("CatR");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String copyAssetGetFilePath(String fileName) {
        try {
            File cacheDir = getContext().getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return null;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return outFile.getPath();
                }
            }
            InputStream is = getContext().getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return outFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        cameraRender.onSurfaceDestroyed();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        cameraRender.onSurfaceCreated(gl, config);
        if(!isSetPara){
            openCameraAndPreview(cameraId, true);
        }
        isSurfaceCreated = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (isSurfaceCreated && surfaceWidth == width && surfaceHeight == height) {
            return;
        }
        surfaceWidth = width;
        surfaceHeight = height;
        cameraRender.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        cameraRender.onDrawFrame(gl);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
