#include <jni.h>
#include <string>
#include "DerryPlayer.h"
#include "JNICallbackHelper.h"
#include <android/native_window_jni.h> // ANativeWindow 用来渲染画面的 == Surface对象

extern "C"{
    #include <libavutil/avutil.h>
}

JavaVM *vm = 0;
ANativeWindow *window = 0;
pthread_mutex_t p_mutex = PTHREAD_MUTEX_INITIALIZER; // 静态初始化 锁

jint JNI_OnLoad(JavaVM *vm, void *args){
    ::vm = vm;
    return JNI_VERSION_1_6;
}

void renderFrame(uint8_t * src_data, int width, int height, int src_lineSize) {
    pthread_mutex_lock(&p_mutex);

    if (!window) {
        // 防止出现死锁
        pthread_mutex_unlock(&p_mutex);
    }
    // 设置窗口 这里的大小和layout里面的大小是不一样的 所以需要重新设置大小
    ANativeWindow_setBuffersGeometry(window, width, height, WINDOW_FORMAT_RGBA_8888);

    //ANativeWindow 自己有buffer 只要把数据给它就可以显示数据了
    // 只要是指针的才需要赋初始值
    // 如果再渲染的时候被锁住的，那我就无法渲染，必须释放，防止出现死锁
    ANativeWindow_Buffer window_buffer;
    if (ANativeWindow_lock(window, &window_buffer, 0)) {
        ANativeWindow_release(window);
        window = 0;
        pthread_mutex_lock(&p_mutex);
        return;
    }

    // 开始真正的渲染 因为window没有被锁住，就可以吧rgba数据 --》 字节对齐后渲染
    // 填充buffer 画面就能显示出来了
    uint8_t *dst_data = static_cast<uint8_t *>(window_buffer.bits);
    int dst_line_size = window_buffer.stride * 4;
    for (int i = 0; i < window_buffer.height; i++){
        // 视频分辨率 426*420
        //
        // 426*4（）
        // 下面会花屏 崩溃
        //ANativeWindow 64字节对齐算法 1704无法以64位字节整除
        //memcpy(dst_data+i*1704, src_data+i*1704, 1704);
        // memcpy(dst_data+i*1792, src_data+i*1704, 1792); ok的
        //memcpy(dst_data+i*1793, src_data+i*1704, 1793);// 部分花屏，无法64整除
        // 主要是内部的算法 会隐式多加一个占位
        // ffmpeg 默认是使用8字节对齐 ANativeWindow使用64对齐
        //memcpy(dst_data+i*1728, src_data+i*1704, 1728);// 1728能够被64整除 但是也会花屏

        // 通用写法
        memcpy(dst_data+i*dst_line_size, src_data+i*dst_line_size, src_lineSize);
    }
    // 数据刷新
    // 解锁后并且刷新window_buffer的数据显示画面
    ANativeWindow_unlockAndPost(window);
    pthread_mutex_unlock(&p_mutex);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_1player_DerryPlayer_prepareNative(JNIEnv *env, jobject job, jstring data_source) {
    LOGD("native-lib prepareNative start");
    const char * data_source_ = env->GetStringUTFChars(data_source, 0);
    auto *helper = new JNICallbackHelper(vm, env, job);// c++ 子线程回调 c++主线程回调 jvm
    auto *player = new DerryPlayer(data_source_, helper);
    player->setRenderCallBack(renderFrame);
    player->prepare();
    env->ReleaseStringUTFChars(data_source, data_source_);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_1player_DerryPlayer_startNative(JNIEnv *env, jobject thiz) {

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_1player_DerryPlayer_stopNative(JNIEnv *env, jobject thiz) {

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_1player_DerryPlayer_releaseNative(JNIEnv *env, jobject thiz) {

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_1player_DerryPlayer_setSurfaceNative(JNIEnv *env, jobject thiz,
                                                          jobject surface) {
    pthread_mutex_lock(&p_mutex);

    //界面发生改变 先释放之前的显示窗口
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
    window = ANativeWindow_fromSurface(env, surface);

    pthread_mutex_unlock(&p_mutex);
}