#ifndef DERRYPLAYER_BASECHANNEL_H
#define DERRYPLAYER_BASECHANNEL_H

extern "C" {
#include <libavcodec/avcodec.h>
};

#include "safe_queue.h"
#include "log4c.h"

class BaseChannel {
public:
    int stream_index;
    SafeQueue<AVPacket *> packets;
    SafeQueue<AVFrame *> frames;
    bool isPlaying;
    AVCodecContext *codecContext = 0;

    BaseChannel(int streamIndex, AVCodecContext *codecContext) : stream_index(streamIndex),
                                                                 codecContext(codecContext) {
        packets.setReleaseCallback(releaseAVPacket);
        frames.setReleaseCallback(releaseAVFrame);
    }

    virtual ~BaseChannel() {
        packets.clear();
        frames.clear();
    }

    static void releaseAVPacket(AVPacket ** p) {
        if (p) {
            av_packet_free(p);
            *p = 0;
        }
    }

    static void releaseAVFrame(AVFrame ** f) {
        if (f) {
            av_frame_free(f);
            *f = 0;
        }
    }
};

#endif