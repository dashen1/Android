package com.vtech.mobile.kidiconnect2021.customcamera.filter;

import android.content.res.Resources;


/**
 * Description:
 */
public class CameraFilter extends OesFilter {

    public CameraFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    public void setFlag(int flag) {
        super.setFlag(flag);
        float[] coord;
        if(getFlag()==1){    //前置摄像头
//            coord=new float[]{
//                    1.0f, 1.0f,
//                    0.0f, 1.0f,
//                    1.0f, 0.0f,
//                    0.0f, 0.0f,
//            };
            coord=new float[]{
                    1.0f, 1.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f,
            };
        }else{               //后置摄像头
//            coord=new float[]{
//                    0.0f, 1.0f,
//                    1.0f, 1.0f,
//                    0.0f, 0.0f,
//                    1.0f, 0.0f,
//            };
            coord=new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
            };
        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }
}
