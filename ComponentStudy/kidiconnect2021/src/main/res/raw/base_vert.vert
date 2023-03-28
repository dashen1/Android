attribute vec4 vPosition;

attribute vec2 vCoord;

varying vec2 aCoord;

uniform mat4 vM;

void main(){
    gl_Position = vM*vPosition;
    aCoord = vCoord;
}