//顶点着色器主要用来确定形状
attribute vec4 vPosition;//顶点坐标

attribute vec2 vCoord;//纹理坐标

//易变变量 用于和片元着色器通信
varying vec2 aCoord;

//这个是 因为摄像头可能是旋转了90度或者270度，需要把摄像头数据摆正
uniform mat4 vMatrix;

void main(){
    //将顶点坐标赋值给内置变量
    gl_Position = vPosition;
    aCoord = (vMatrix * vec4(vCoord, 1.0,1.0)).xy;
    //aCoord = vCoord;
}

