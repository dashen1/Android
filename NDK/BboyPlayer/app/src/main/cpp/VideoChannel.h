//
// Created by se0891 on 2022/4/7.
//

#ifndef BBOYPLAYER_VIDEOCHANNEL_H
#define BBOYPLAYER_VIDEOCHANNEL_H

#include "BaseChannel.h"
#include "AudioChannel.h"
#include "log4c.h"

extern "C"{
#include <libswscale/swscale.h>
#include <libavutil/avutil.h>
#include <libavutil/imgutils.h>
};

typedef void (*RenderCallback) (uint8_t *,int,int,int);

class VideoChannel:public BaseChannel{
private:
    pthread_t pid_video_decode;
    pthread_t pid_video_play;
    RenderCallback renderCallback;

    int fps;
    AudioChannel *audioChannel=0;

public:
    VideoChannel(int stream_index,AVCodecContext *codecContext1,AVRational rational,int fps);

    ~VideoChannel();

    void start();
    void stop();

    void video_decode();
    void video_play();

    void setRenderCallback(RenderCallback renderCallback1);

    void setAudioChannel(AudioChannel *audioChannel1);

};

#endif //BBOYPLAYER_VIDEOCHANNEL_H
