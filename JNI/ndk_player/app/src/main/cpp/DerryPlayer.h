#ifndef DERRYPLAYER_DERRYPLAYER_H
#define DERRYPLAYER_DERRYPLAYER_H

#include <iostream>
#include <cstring>
#include <pthread.h>
#include "AudioChannel.h"
#include "VideoChannel.h"
#include "JNICallbackHelper.h"
#include "util.h"
#include <android/log.h>
#define TAG "DerryPlayer"
//__VA_ARGS__ 代表 ...的可变参数
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__);


extern "C" {
    #include <libavformat/avformat.h>
}


class DerryPlayer {
private:
    char *data_source = 0;
    pthread_t pid_prepare;
    AVFormatContext *formatContext;
    AudioChannel *audio_channel = 0;
    VideoChannel *video_channel = 0;
    JNICallbackHelper *helper = 0;
    bool isPlaying;
    pthread_t pid_start;
    RenderCallBack renderCallBack;


public:
    DerryPlayer(const char *data_source, JNICallbackHelper *pHelper);
    virtual ~DerryPlayer();
    void prepare();
    void prepare_();

    void start();
    void start_();

    void setRenderCallBack(RenderCallBack renderCallBack);
};

#endif