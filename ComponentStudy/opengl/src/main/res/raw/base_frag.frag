precision mediump float;

varying vec2 aCoord;

uniform sampler2D vTexture;

void main(){
    vec4 rgba = texture2D(vTexture,aCoord);
    gl_FragColor = rgba;
}