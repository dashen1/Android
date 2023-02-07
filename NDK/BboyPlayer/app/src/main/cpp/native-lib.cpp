#include <jni.h>
#include <string>
#include "log4c.h"
#include "BboyPlayer.h"
#include "JNICallbackHelper.h"
#include <android/native_window_jni.h>//ANativeWindow 用来渲染画面 == surface对象

extern "C" {
#include <libavutil/avutil.h>
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_bboyplayer_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

BboyPlayer *player = nullptr;
JavaVM *vm = nullptr;
ANativeWindow *window = nullptr;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;//静态初始化锁

jint JNI_OnLoad(JavaVM *vm, void *args) {
    ::vm = vm;
    return JNI_VERSION_1_6;
}

//uint8_t == unsigned char
//函数指针 实现渲染工作
void renderFrame(uint8_t *src_data, int width, int height, int src_lineSize) {
    pthread_mutex_lock(&mutex);
    if (!window) {
        pthread_mutex_unlock(&mutex);//出现问题后 释放锁 避免死锁
    }
    //设置窗口大小
    ANativeWindow_setBuffersGeometry(window, width, height, WINDOW_FORMAT_RGBA_8888);
    //window 有自己的缓冲区 最后渲染的时候直接从这个缓冲区拿数据进行渲染
    ANativeWindow_Buffer window_buffer;
    //如果在渲染的时候，window被锁住了，需要先释放才能继续渲染
    if (ANativeWindow_lock(window, &window_buffer, 0)) {
        ANativeWindow_release(window);
        window = 0;
        pthread_mutex_unlock(&mutex);
        return;
    }
    /*
     * 开始真正的渲染 此时window是没有被锁住的
     * 只要填充window_buffer 画面就出来了
     * 把rgba数据 -- 字节对齐 渲染
     */
    uint8_t *dst_data = static_cast<uint8_t *>(window_buffer.bits);
    int dst_line_size = window_buffer.stride * 4;

    for (int i = 0; i < window_buffer.height; ++i) {//图：一行行显示 宽度不用管，循环遍历高度
        //视频分辨率 426 * 240
        //426 * 4(rgba8888) = 1704
        //memcpy(dst_data+i * 1704,src_data+i * 1704,1704); //花屏
        //花屏原因：1704 无法与64字节对齐

        //ANativeWindow_Buffer 64字节对齐的算法 1704无法以64位字节对齐
        //memcpy(src_data+i * 1792,src_data+i * 1704,1792); //是ok的
        //memcpy(src_data+i * 1793,src_data+i * 1704,1793); //部分花屏
        //memcpy(src_data+i * 1728,src_data+i * 1704,1728); //花屏

        //ANativeWindow_Buffer 64字节对齐算法 1728
        // 占位 占位 占位 占位 占位 占位 占位 占位
        // 数据 数据 数据 数据 数据 数据 数据 空值

        // ANativeWindow_Buffer 64字节对齐的算法  1792  空间换时间
        // 占位 占位 占位 占位 占位 占位 占位 占位 占位
        // 数据 数据 数据 数据 数据 数据 数据 空值 空值

        //FFMPEG 为什么认为1704没有问题
        //ffmpeg 默认是采用8字节对齐，但ANativeWindow_Buffer 是64字节对齐

        //通用
        memcpy(dst_data + i * dst_line_size, src_data + i * src_lineSize, dst_line_size);
    }
    ANativeWindow_unlockAndPost(window);
    pthread_mutex_unlock(&mutex);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_prepareNative(JNIEnv *env, jobject thiz,
                                                     jstring data_source_) {
    const char *data_source = env->GetStringUTFChars(data_source_, 0);
    auto *helper = new JNICallbackHelper(vm, env, thiz);
    player = new BboyPlayer(data_source, helper);
    player->setRenderCallback(renderFrame);
    player->prepare();
    //1024~65535
    env->ReleaseStringUTFChars(data_source_, data_source);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_startNative(JNIEnv *env, jobject thiz) {
    // TODO: implement startNative()
    if (player) {
        player->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_stopNative(JNIEnv *env, jobject thiz) {
    // TODO: implement stopNative()
    if (player) {
        player->stop();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_releaseNative(JNIEnv *env, jobject thiz) {
    // TODO: implement releaseNative()

    pthread_mutex_lock(&mutex);
    //先释放之前的窗口
    if (window) {
        ANativeWindow_release(window);
        window = nullptr;
    }

    pthread_mutex_unlock(&mutex);

    //释放工作
    DELETE(player);
    DELETE(vm);
    DELETE(window);
}

extern "C"
JNIEXPORT int JNICALL
Java_com_example_bboyplayer_BboyPlayer_getDurationNative(JNIEnv *env, jobject thiz) {
    // TODO: implement getDurationNative()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_setSurfaceNative(JNIEnv *env, jobject thiz,
                                                        jobject surface) {
    // TODO: implement setSurfaceNative()
    pthread_mutex_lock(&mutex);
    if (window) {
        ANativeWindow_release(window);
        window = nullptr;
    }
    window = ANativeWindow_fromSurface(env, surface);
    pthread_mutex_unlock(&mutex);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bboyplayer_BboyPlayer_seekNative(JNIEnv *env, jobject thiz, jint play_value) {
    // TODO: implement seekNative()
}