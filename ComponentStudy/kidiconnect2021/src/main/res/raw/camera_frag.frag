#extension GL_OES_EGL_image_external : require
//摄像头数据比较特殊的地方
//必须给定数据精度
precision mediump float;
//接收来自顶点着色器传来的顶点坐标
//注意：这里的坐标是经过光栅化后得到的一个个像素所在位置的坐标，和顶点着色器所传入的顶点坐标不是同一个
varying vec2 aCoord;

uniform samplerExternalOES vTexture;//samplerExternalOES:图片

void main(){
    //texture2D vTexture采样器 获得aCoord这个像素点的RGBA值
    vec4 rgba = texture2D(vTexture,aCoord);
    //灰度化公式 30 59 11
    float r = 0.33*rgba.r+0.59*rgba.g+0.11*rgba.b;
    gl_FragColor = rgba;
}
