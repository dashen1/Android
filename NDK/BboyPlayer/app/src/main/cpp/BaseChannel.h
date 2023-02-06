//
// Created by se0891 on 2022/4/6.
//

#ifndef BBOYPLAYER_BASECHANNEL_H
#define BBOYPLAYER_BASECHANNEL_H

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavutil/time.h>
};

#include "safe_queue.h"
#include "log4c.h"
#include "JNICallbackHelper.h"

class BaseChannel{
public:
    int stream_index;//视频或音频的下标
    SafeQueue<AVPacket *> packets;//压缩的数据包
    SafeQueue<AVFrame *> frames;//解码后的原始数据包
    bool isPlaying;
    AVCodecContext *codecContext=0;//音视频上下文解码器

    AVRational time_base;

    JNICallbackHelper *jniCallbackHelper=0;

    void setJNICallHelper(JNICallbackHelper *jniCallbackHelper1){
        this->jniCallbackHelper=jniCallbackHelper1;
    }

    BaseChannel(int stream_index_,AVCodecContext *avCodecContext_, AVRational time_base_):
            stream_index(stream_index_),
            codecContext(avCodecContext_),
            time_base(time_base_){
        packets.setReleaseCallback(releaseAVPacket);
        frames.setReleaseCallback(releaseAVFrame);
    }

    virtual ~BaseChannel(){
        packets.clear();
        frames.clear();
    }

    static void releaseAVPacket(AVPacket **p){
        if(p){
            av_packet_free(p);
            *p=0;
        }
    }

    static void releaseAVFrame(AVFrame **f){
        if(f){
            av_frame_free(f);
            *f=0;
        }
    }

};

#endif //BBOYPLAYER_BASECHANNEL_H
