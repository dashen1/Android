#include "VideoChannel.h"

VideoChannel::VideoChannel(int streamIndex, AVCodecContext *codecContext)
        : BaseChannel(streamIndex, codecContext) {

}

VideoChannel::~VideoChannel() {

}
void VideoChannel::stop() {

}

void * task_video_decode(void * args) {
    auto *video_channel = static_cast<VideoChannel *>(args);
    video_channel->video_decode();
    return 0;
}

void * task_video_play(void *args) {
    auto *video_channel = static_cast<VideoChannel *>(args);
    video_channel->video_play();
    return 0;
}

void VideoChannel::start() {
    isPlaying = 1;

    //队列开始工作
    packets.setWork(1);
    frames.setWork(1);
    //第一个线程 取出队列的压缩包 进行编码再放到队列中
    pthread_create(&pid_video_decode, 0, task_video_decode, this);
    //第二个线程 从队列取出原始包 播放
    pthread_create(&pid_video_play, 0, task_video_play, this);
}

void VideoChannel::video_decode() {
    AVPacket *pkt = 0;
    while (isPlaying) {
        int ret = packets.getQueueAndDel(pkt);//阻塞式函数 从队列中取出压缩数据进行解码 成功 ret=1
        if (!isPlaying) {
            break;//停止播放，就跳出循环 不再解码
        }
        if (!ret) { //取出数据失败 继续
            continue;
        }
        //开始解码
        //最新的FFmpeg和旧版本差别很大 新版本：1.发送pkt（压缩包）缓冲区 2.从缓冲区中拿到原始包数据
        ret = avcodec_send_packet(codecContext, pkt);
        //编码完毕 释放缓存 FFmpeg源码自己缓存了一份
        releaseAVPacket(&pkt);
        if (!ret) {
            break;
        }
        AVFrame *frame = av_frame_alloc();
        ret = avcodec_receive_frame(codecContext, frame);
        if (ret == AVERROR(EAGAIN)) {
            //B帧 B帧参考前面成功 B帧参考后面失败 可能p帧没有出来 再拿一次
            continue;
        } else if (ret != 0) {
            break;
        }
        //成功拿到原始包
        frames.insertToQueue(frame);
    }
    releaseAVPacket(&pkt);
}

void VideoChannel::video_play() {//第二个线程 从队列中取出原始包开始播放

    //SWS_FAST_BILINEAR 速度很快，但可能很模糊
    //SWS_BILINEAR 速度适中
    AVFrame *frame = 0;
    //指针数组，说明数组里面的元素都是指针 所以dst_data[0]就是指向第一个元素的指针
    /**
     * 数组指针 (* ptr)[10] ptr 指的就是数组元素首地址
     * */
    uint8_t *dst_data[4]; // RGBA  unsigned char == uint8_t 无符号 如果选有符号的 显示可能会有问题（花屏 黑点问题）
    int dst_line_size[4]; //RGBA
    //原始包是YUV数据 ---> libswscale Android(RGBA数据) 格式转换
    av_image_alloc(dst_data, dst_line_size,
                   codecContext->width, codecContext->height, AV_PIX_FMT_RGBA, 1);

    SwsContext *sws_ctx = sws_getContext(
            codecContext->width,
            codecContext->height,
            codecContext->pix_fmt,//自动获取xxx.mp4的像素格式 AV_PIX_FMT_YUV420P 写死的
            // 下面输出
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
            continue;//数据取出失败 假设原始包加入队列太慢，消费者再等一下
        }

        sws_scale(sws_ctx,
                  //下面是输入数据
                  frame->data,//Android屏幕 一行的数据
                  frame->linesize,// 一行大小
                  0, codecContext->height,//从上到下渲染
                  //下面是输出数据
                  dst_data,
                  dst_line_size
                  );

        // ANativeWindows 渲染
        // SurfaceView --- ANativeWindows
        //渲染图片 宽、高、数据  --> 函数指针声明
        //这里拿不到Surfaceview
        // 数组被传递会退化成指针
        renderCallBack(dst_data[0], codecContext->width, codecContext->height, dst_line_size[0]);
        releaseAVFrame(&frame);//渲染完毕后释放掉
    }
    releaseAVFrame(&frame);//AVFrame 非常大 1920*1280*4 出现错误 退出所有循环都要释放
    isPlaying = 0;
    av_free(dst_data[0]);//FFmpeg 必须使用人家的函数释放
    sws_freeContext(sws_ctx);

}

void VideoChannel::setRenderCallBack(RenderCallBack renderCallBack) {
    this->renderCallBack = renderCallBack;
}
