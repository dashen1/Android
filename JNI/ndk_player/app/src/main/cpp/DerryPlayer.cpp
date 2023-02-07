#include "DerryPlayer.h"


DerryPlayer::DerryPlayer(const char *data_source, JNICallbackHelper *helper) {
    //这句代码有问题 如果被释放了会造成悬空指针
//    this->dataSource = dataSource;
    // 坑 strlen 是不计算 \0的，所以需要手动添加长度
    this->data_source = new char[strlen(data_source) + 1];
    this->helper = helper;
}

DerryPlayer::~DerryPlayer() {
    if (data_source) {
        delete data_source;
        data_source = 0;
    }

    if (helper) {
        delete helper;
    }
}

// 此函数和DerryPlayer这个对象没有关系，没办法拿到DerryPlayer的私有成员
void * task_prepare(void * args) {
    //avformat_open_input(0, this->data_source);
    auto *player = static_cast<DerryPlayer *>(args);
    player->prepare_();
    // 必须返回 不然会报错 为什么？
    return 0;
}

void DerryPlayer::prepare_() {//此函数是子线程
    LOGD("DerryPlayer prepare_()");
    // 为什么 FFMPEG 大量使用上下文 context？
    // 因为FFmpeg源码是纯c的，不像Java、c++
    /**
     * 第一步：打开媒体地址（文件路径，直播地址rtmp）
     * */
     formatContext = avformat_alloc_context();
     /**
      *1 AVFormatContext *
      * 2 路径
      * 3 AVFormatContext *fmt Mac、window 的摄像头、麦克风，但是android用不到
      * 4 AVDictionary ** 各种设置
      * */
    LOGD("DerryPlayer av_dict_set");
    AVDictionary * dictionary = 0;
    av_dict_set(&dictionary, "timeout", "5000000", 0);
    int r = avformat_open_input(&formatContext, data_source, 0, &dictionary);// c++中 非0为真 这里执行成功的时候会返回0
    av_dict_free(&dictionary);

    LOGD("DerryPlayer avformat_open_input: %d",r);
    if (r) {// 因为data_source暂时不存在 所以肯定无法打开
        if (helper) {
            helper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_OPEN_URL);
        }
        return;
    }
    /**
     * 第二步：查找媒体中的视频流
     * */
    r = avformat_find_stream_info(formatContext, 0);
    LOGD("DerryPlayer avformat_find_stream_info： %d",r);
    if (r < 0) {
        if (helper) {
            helper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_FIND_STREAMS);
        }
        return;
    }
    /**
     * 第三步：根据流信息，流的个数
     * */
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        /**
         * 第四步：获取媒体流（视频，音频）
         * AVStream **streams; 二级指针使用
         *
         * **streams == arr{char *,char *}
         *
         * */
         AVStream * stream = formatContext->streams[i];
         /**
          * 第五步：从上面的流中获取编解码的[参数]
          * */
        AVCodecParameters *parameters = stream->codecpar;
        /**
         * 第六步：获取解码器（根据上面的参数）
         * */
        AVCodec *codec = avcodec_find_decoder(parameters->codec_id);
        if(!codec) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_FIND_DECODER_FAIL);
            }
            return;
        }
        /**
         * 第七步：编解码器上下文（这个才是真正干活的）
         * */
         AVCodecContext *codecContext = avcodec_alloc_context3(codec);
        if (!codecContext) {
            // TODO JNI 反射回调到java方法并提示
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_ALLOC_CODEC_CONTEXT_FAIL);
            }
            return;
        }
        /**
        * 第八步：它目前是一张白纸 parameters copy codecContext 非0即true
        * */
        r = avcodec_parameters_to_context(codecContext, parameters);
        if (r < 0) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL);
            }
            return;
        }
        /**
        * TODO 第九步：打开解码器
        */
        r = avcodec_open2(codecContext, codec, 0);
        if (r < 0) {
            if (helper) {
                helper->onError(THREAD_CHILD, FFMPEG_OPEN_DECODER_FAIL);
            }
            return;
        }
        /**
        * 第十步：从编码参数中，获取流的类型 codec_type === 音频 视频
        * */
        if (parameters->codec_type == AVMediaType::AVMEDIA_TYPE_AUDIO) {
           audio_channel = new AudioChannel(i, codecContext);
        } else if (parameters->codec_type == AVMediaType::AVMEDIA_TYPE_VIDEO) {
            video_channel = new VideoChannel(i, codecContext);
            video_channel->setRenderCallBack(renderCallBack);
        }
    }
    if (!audio_channel && !video_channel) {
        return;
    }
    LOGD("DerryPlayer onPrepared");
    if (helper) {
        helper->onPrepared(THREAD_CHILD);
    }
}

void DerryPlayer::prepare() {
    // 问题：当前prepare函数是子线程还是主线程
    // 答：此函数是被MainActivity的onResume调用下来的主线程

    //解封 ffmpeg来解析 data_source可以直接解封吗？
    // 答：不可以 data_source == 文件io流 很耗时，需要开启子线程
    pthread_create(&pid_prepare, 0, task_prepare, this);
}

void *task_start(void *args) {
    auto *player = static_cast<DerryPlayer *>(args);
    player->start_();
    return 0;
}

void DerryPlayer::start() {
    isPlaying = 1;

    if (video_channel) {
        video_channel->start();
    }

    if (audio_channel) {
        audio_channel->start();
    }

    pthread_create(&pid_start, 0, task_start, this);
}

void DerryPlayer::start_() {
    while (isPlaying) {
        //AVPack 可能是音频，也可能是视频压缩包
        AVPacket *packet = av_packet_alloc();
        int ret = av_read_frame(formatContext, packet);
        if (!ret) { // ret == 0 0是ok c++中非0为true
            // AudioChannel 队列
            if (video_channel && video_channel->stream_index == packet->stream_index) {
                video_channel->packets.insertToQueue(packet);
            } else if (audio_channel && audio_channel->stream_index == packet->stream_index){
                audio_channel->packets.insertToQueue(packet);
            }
            // VideoChannel 队列

            // 把我们的AVPacket* 加入到队列 音频和视频
        } else if (ret == AVERROR_EOF) { // 读到文件末尾
            // 表示读完了，要考虑是否播放完成 （读完了并不代表播放完毕）
        } else {
            break;
        }
    } // end while
    isPlaying = 0;
    video_channel->stop();
    audio_channel->stop();
}

void DerryPlayer::setRenderCallBack(RenderCallBack renderCallBack) {
    this->renderCallBack = renderCallBack;
}

