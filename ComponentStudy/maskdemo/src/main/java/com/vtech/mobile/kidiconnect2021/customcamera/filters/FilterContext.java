package com.vtech.mobile.kidiconnect2021.customcamera.filters;

public class FilterContext {

    public float[] cameraMtx;

    public int width;
    public int height;

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setTransformMatrix(float[] mtx){
        this.cameraMtx = mtx;
    }

}
