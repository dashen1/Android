package com.example.opengl.customcamera.filters;

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
