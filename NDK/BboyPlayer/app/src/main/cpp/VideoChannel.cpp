//
// Created by se0891 on 2022/4/7.
//

#include "VideoChannel.h"

VideoChannel::VideoChannel(int stream_index, AVCodecContext *codecContext1, AVRational time_base,
                           int fps)
        : BaseChannel(stream_index, codecContext1, time_base), fps(fps) {

}

VideoChannel::~VideoChannel() {
    DELETE(audioChannel);
}

void VideoChannel::stop() {
    //解码结束才停止播放
    pthread_join(pid_video_decode, nullptr);
    pthread_join(pid_video_play, nullptr);

    isPlaying = false;
    packets.setWork(0);
    frames.setWork(0);

    packets.clear();
    frames.clear();
}

void *task_video_decode(void *args) {
    auto *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->video_decode();
    return nullptr;
}

void *task_video_play(void *args) {
    auto *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->video_play();
    return nullptr;
}

void VideoChannel::start() {
    isPlaying = true;
    //队列开始工作
    packets.setWork(1);
    frames.setWork(1);

    //第一个线程，视频：取出队列的压缩包 进行解码 解码后的原始包 再push到队列中（YUV）
    pthread_create(&pid_video_decode, nullptr, task_video_decode, this);
    //第二个线程 视频：从队列取出原始包，播放
    pthread_create(&pid_video_play, nullptr, task_video_play, this);
}

//1.把队列里面的压缩包（AVPackets *）取出来，然后解码成（AVFrame *）原始包
void VideoChannel::video_decode() {
    AVPacket *pkt = nullptr;
    while (isPlaying) {
        // 内存泄漏点
        if (isPlaying && frames.size() > 100) {//??
            av_usleep(10 * 1000);
            continue;
        }

        int ret = packets.getQueueAndDel(pkt);//阻塞式队列
        if (!isPlaying) {
            break;
        }

        if (!ret) {
            continue;//没有成功从队列中取出，也要继续（有可能是生产太慢，压缩包加入队列太慢），等一下
        }
        ret = avcodec_send_packet(codecContext, pkt);
        //ffmpeg 会缓存一份 pkt
        if (ret) {
            break;//avcodec_send_packet 出现错误
        }
        AVFrame *frame = av_frame_alloc();
        ret = avcodec_receive_frame(codecContext, frame);//得到解码后的原始数据包
        if (ret == AVERROR(EAGAIN)) {//获取数据包失败
            //B帧 B帧参考前面成功，参考后面失败，可能p帧没有出来，再拿一次
            continue;
        } else if (ret != 0) {
            //解码失败
            if (frame) {
                releaseAVFrame(&frame);
            }
            break;
        }
        //拿到数据包
        frames.insertToQueue(frame);

        //释放pkt空间
        av_packet_unref(pkt);//减1=0，释放成员指向的堆区
        releaseAVPacket(&pkt);
    }
    av_packet_unref(pkt); // 减1 = 0 释放成员指向的堆区
    releaseAVPacket(&pkt);
}

//2.把队列里面的原始包拿出来 播放
void VideoChannel::video_play() {
    //SWS_FAST_BILINEAR
    // SWS_BILINEAR 适中算法
    AVFrame *frame = nullptr;
    uint8_t *dst_data[4];//存放RGBA四个字符
    int dst_line_size[4];//??
    //原始包 YUV数据
    //给 dst_data申请内存 width*height*4
    av_image_alloc(dst_data, dst_line_size,
                   codecContext->width, codecContext->height, AV_PIX_FMT_RGBA, 1);
    //yuv 转 rgba
    SwsContext *sws_ctx = sws_getContext(
            //输入
            codecContext->width,
            codecContext->height,
            codecContext->pix_fmt,//自动获取xxx.mp4的像素格式 AV_PIX_FMT_YUV420P//写死的
            //输出
            codecContext->width,
            codecContext->height,
            AV_PIX_FMT_RGBA,
            SWS_BILINEAR, NULL, NULL, NULL
    );


    while (isPlaying) {
        int ret = frames.getQueueAndDel(frame);
        if (!isPlaying) {
            break;
        }
        if (!ret) {
            continue;//取出原始包失败，原始包加入队列太慢
        }
        LOGD("VideoChannel->开始取出原始包");
        sws_scale(sws_ctx,
                //输入
                  frame->data, frame->linesize,
                  0, codecContext->height,
                //输出
                  dst_data, dst_line_size
        );
        LOGD("VideoChannel->取出原始包");
        //音视频同步
        //根据fps来休眠
        //0.04是这一帧的真实事件加上延迟事件
        //公式 extra_play=repeat_pict/（2*fps）
        //经验 extra_delay:0.040000
//        double extra_delay = frame->repeat_pict / (2 * fps);//在之前的编码中，加入的额外延时时间（可能获取不到）
//        double fps_delay = 1.0 / fps;//计算每一帧的延时时间 fps25
//        double real_delay = fps_delay + extra_delay;//

        //下面音视频同步
//        double video_time = frame->best_effort_timestamp * av_q2d(time_base);
//        double audio_time = audioChannel->audio_time;

        LOGD("VideoChannel->回调给native进行渲染");
        renderCallback(dst_data[0],codecContext->width,codecContext->coded_height,dst_line_size[0]);

        av_frame_unref(frame); // 减1 = 0 释放成员指向的堆区
        releaseAVFrame(&frame); // 释放AVFrame * 本身的堆区空间
    }
    av_frame_unref(frame); // 减1 = 0 释放成员指向的堆区
    releaseAVFrame(&frame); // 释放AVFrame * 本身的堆区空间
}

void VideoChannel::setRenderCallback(RenderCallback renderCallback1) {
    this->renderCallback = renderCallback1;
}

void VideoChannel::setAudioChannel(AudioChannel *audioChannel1) {
    this->audioChannel = audioChannel1;
}



