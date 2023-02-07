//
// Created by se0891 on 2022/4/7.
//

#include "BboyPlayer.h"

BboyPlayer::BboyPlayer(const char *data_source, JNICallbackHelper *helper) {

    this->data_source = new char[strlen(data_source) + 1];
    strcpy(this->data_source, data_source);
    this->helper = helper;
    pthread_mutex_init(&seek_mutex, nullptr);
}

BboyPlayer::~BboyPlayer() {
    if (data_source) {
        delete data_source;
        data_source = nullptr;
    }

    if (helper) {
        delete helper;
        helper = nullptr;
    }

    pthread_mutex_destroy(&seek_mutex);
}

void BboyPlayer::prepare_() {

    /*
     * 第一步 打开媒体地址（文件路径 直播地址）
     */
    formatContext = avformat_alloc_context();
    //字典 网络相关
    AVDictionary *dictionary = nullptr;
    //设置超时
    av_dict_set(&dictionary, "timeout", "5000000", 0);
    /*
     * AVInputFormat *fmt Mac、window 摄像头 麦克风，目前安卓用不到
     * AVDictionary **options 连接超时
     */
    int ret = avformat_open_input(&formatContext, data_source, nullptr, &dictionary);
    av_dict_free(&dictionary);
    //c++ 非0为 true
    if (ret) {
        if (helper) {
            helper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_OPEN_URL);
        }
        avformat_close_input(&formatContext);
        return;
    }

    /*
     * 第二步 查找媒体中的音视频流信息
     */
    ret = avformat_find_stream_info(formatContext, nullptr);
    if (ret < 0) {
        if (helper) {
            helper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_FIND_STREAMS);
        }
        avformat_close_input(&formatContext);
        return;
    }

    this->duration = formatContext->duration;
    /*
     * 第三步 根据流信息，流的个数 循环遍历
     */
    AVCodecContext *codecContext = nullptr;
    for (int stream_index = 0; stream_index < formatContext->nb_streams; ++stream_index) {
        //第四步 获取媒体流
        AVStream *stream = formatContext->streams[stream_index];
        //第五步 根据获取到的流 获取编解码 参数
        AVCodecParameters *parameters = stream->codecpar;
        //第六步 根据获取到的参数 获取解码器
        AVCodec *codec = avcodec_find_decoder(parameters->codec_id);
        if (!codec) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_FIND_DECODER_FAIL);
            }
            avformat_close_input(&formatContext);
        }
        /*
         * 第七步 根据获得的解码器 得到编解码器上下文
         */
        codecContext = avcodec_alloc_context3(codec);
        if (!codecContext) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_ALLOC_CODEC_CONTEXT_FAIL);
            }
            //avcodec 包含在codecContext 所以不用单独释放avcodec
            avcodec_free_context(&codecContext);
            avformat_close_input(&formatContext);
            return;
        }

        /*
         * 第八步 目前的编解码器上下文参数是没有赋值的
         */

        ret = avcodec_parameters_to_context(codecContext, parameters);
        if (ret < 0) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL);
            }
            avcodec_free_context(&codecContext);
            avformat_close_input(&formatContext);
            return;
        }
        /*
         * 第九步 打开解码器
         */
        ret = avcodec_open2(codecContext, codec, nullptr);
        if (ret) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_OPEN_DECODER_FAIL);
            }
            avcodec_free_context(&codecContext);
            avformat_close_input(&formatContext);
            return;
        }

        AVRational time_base = stream->time_base;
        /*
         * 第十步 从编解码器参数中 获取流的类型 codec_type 音频 视频
         */

        if (parameters->codec_type == AVMediaType::AVMEDIA_TYPE_AUDIO) {
            //音频
            audioChannel = new AudioChannel(stream_index, codecContext, time_base);
            if (this->duration != 0) {
                audioChannel->setJNICallHelper(helper);
            }

        } else if (parameters->codec_type == AVMediaType::AVMEDIA_TYPE_VIDEO) {

            //注意：这里可能是一帧封面 不是视频开始帧
            if (stream->disposition & AV_DISPOSITION_ATTACHED_PIC) {
                continue;
            }

            //音视频同步 获取 fps
            AVRational fps_rational = stream->avg_frame_rate;//得到码率
            int fps = av_q2d(fps_rational);
            //视频
            videoChannel = new VideoChannel(stream_index, codecContext, time_base, fps);
            videoChannel->setRenderCallback(renderCallback);

            //
            if (this->duration != 0) {//非直播才有意义
                videoChannel->setJNICallHelper(helper);
            }
        }
    }
    //第十一步 如果流中没有音频和视频
    if (!audioChannel && !videoChannel) {
        if (helper) {
            helper->onError(THREAD_CHILD, FFMPEG_NOMEDIA);
        }
        if (codecContext) {
            avcodec_free_context(&codecContext);
        }
        avformat_close_input(&formatContext);
        return;
    }
    //第十二步 准备成功 媒体文件准备ok
    if (helper) {
        helper->onPrepared(THREAD_CHILD);
    }
}

void BboyPlayer::setRenderCallback(RenderCallback renderCallback1) {
    this->renderCallback = renderCallback1;
}

void *task_prepare(void *args) {
    auto *player = static_cast<BboyPlayer *>(args);
    player->prepare_();
    return nullptr;
}

void BboyPlayer::prepare() {
    //FFMPEG 解析耗时 在子线程进行
    pthread_create(&pid_prepared, 0, task_prepare, this);
}

//把音视频的压缩包数据（AVPacket *）从队列循环取出来，加入到队列里面去
void BboyPlayer::start_() {
    while (isPlaying) {
        //如果视频压缩包队列已满，等待，直到队列空出位置
        if (videoChannel && videoChannel->packets.size() > 100) {
            av_usleep(10 * 1000);
            continue;
        }
        if (audioChannel && audioChannel->packets.size() > 100) {
            av_usleep(10 * 100);
            continue;
        }
        AVPacket *packet = av_packet_alloc();
        int ret = av_read_frame(formatContext, packet);
        if (!ret) {//读取成功
            if (videoChannel && videoChannel->stream_index == packet->stream_index) {
                videoChannel->packets.insertToQueue(packet);
            } else if (audioChannel && audioChannel->stream_index == packet->stream_index) {
                audioChannel->packets.insertToQueue(packet);
            }
        } else if (ret == AVERROR_EOF) {//读到文件末尾了
            if (videoChannel->packets.empty() && audioChannel->packets.empty()) {
                break;
            }
        } else {
            break;//出现错误
        }
    }//end while
    isPlaying = false;
    videoChannel->stop();
    audioChannel->stop();
}

void *task_start(void *args) {
    auto *player = static_cast<BboyPlayer *>(args);
    player->start_();
    return nullptr;
}

void BboyPlayer::start() {
    isPlaying = true;
    //视频 1.解码 2.播放
    //1.把队列里面的压缩包（AVPacket *）取出来然后解码成原始包 保存到队列（AVFrame *)
    //2.把队列里面的原始包（AVFrame *）取出来，播放视频
    if (videoChannel) {
        videoChannel->setAudioChannel(audioChannel);
        videoChannel->start();
    }

    if (audioChannel) {
        //audioChannel->start();
    }
    //把音频和视频压缩包
    pthread_create(&pid_start, nullptr, task_start, this);
}

void *task_stop(void *args){
    auto player = static_cast<BboyPlayer *>(args);
    player->stop_(player);
    return nullptr;
}

void BboyPlayer::stop_(BboyPlayer * bboyPlayer){
    isPlaying = false;
    pthread_join(pid_prepared, nullptr);
    pthread_join(pid_start, nullptr);
    if(formatContext){
        avformat_close_input(&formatContext);
        avformat_free_context(formatContext);
        formatContext = nullptr;
    }
    DELETE(audioChannel);
    DELETE(videoChannel);
    DELETE(bboyPlayer);
}

void BboyPlayer::stop() {

    //用户关闭后，不再回调给java层
    helper = nullptr;
    if (audioChannel) {
        audioChannel->jniCallbackHelper = nullptr;
    }

    if (videoChannel) {
        videoChannel->jniCallbackHelper = nullptr;
    }

    pthread_create(&pid_stop, nullptr,task_stop,this);
}

