//
// Created by se0891 on 2022/4/7.
//

#ifndef BBOYPLAYER_BBOYPLAYER_H
#define BBOYPLAYER_BBOYPLAYER_H

#include <cstring>
#include <pthread.h>
#include "AudioChannel.h"
#include "VideoChannel.h"
#include "JNICallbackHelper.h"
#include "util.h"

extern "C"{
#include <libavformat/avformat.h>
#include <libavutil/time.h>
};

class BboyPlayer{
private:
    char *data_source=0;
    pthread_t pid_prepared;
    pthread_t pid_start;
    AVFormatContext *formatContext=0;
    AudioChannel *audioChannel=0;
    VideoChannel *videoChannel=0;
    JNICallbackHelper *helper=0;
    bool isPlaying=0;
    RenderCallback renderCallback;
    int duration;

    pthread_mutex_t seek_mutex;
    pthread_t pid_stop;

public:
    BboyPlayer(const char *data_source,JNICallbackHelper *helper);
    ~BboyPlayer();

    void prepare();
    void prepare_();

    void start();
    void start_();

    void setRenderCallback(RenderCallback renderCallback1);
    int getDuration();

    void seek(int play_value);

    void stop();
    void stop_(BboyPlayer *);
};


#endif //BBOYPLAYER_BBOYPLAYER_H
