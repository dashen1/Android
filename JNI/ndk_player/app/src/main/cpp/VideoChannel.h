#ifndef DERRYPLAYER_VIDEOCHANNEL_H
#define DERRYPLAYER_VIDEOCHANNEL_H

#include "BaseChannel.h"

extern "C" {
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
};


typedef void(*RenderCallBack) (uint8_t *, int, int, int);

class VideoChannel : public BaseChannel {

private:
    pthread_t pid_video_decode;
    pthread_t pid_video_play;
    RenderCallBack renderCallBack;
public:
    VideoChannel(int streamIndex, AVCodecContext *codecContext);

    virtual ~VideoChannel();

    void stop();

    void start();

    void video_decode();

    void video_play();

    void setRenderCallBack(RenderCallBack renderCallBack);

};
#endif
