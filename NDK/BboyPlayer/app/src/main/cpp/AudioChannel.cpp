//
// Created by se0891 on 2022/4/7.
//
#include "AudioChannel.h"

AudioChannel::AudioChannel(int stream_index, AVCodecContext *codecContext1, AVRational time_base)
        : BaseChannel(stream_index, codecContext1, time_base) {

    //音频三要素
    /**
     * 1、采样率 44100 48000
     * 2、位声/采样格式 16bit 2个字节
     * 3、声道数 2 两个耳朵
     */
    //音频压缩数据包 aac
    /**
     * 44100
     * 32bit 算法效率高 浮点运算高
     * 2
     */
    //重采样 音频格式转换
    //缓冲区大小怎么定义？ out_buffers  out_buffers_size
    //初始化缓冲区 out_buffers,如何定义缓冲区？
    //根据数据类型 44100 16bit 2声道
    //AV_SAMPLE_FMT_S16:位声，采样格式大小，存放大小
    out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//STEREO:双声道类型 获取声道数
    out_sample_size = av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);//每个sample是16bit 2字节
    out_sample_rate = 44100;

    //out_buffers_size = 176,400
    out_buffers_size = out_sample_rate * out_buffers_size * out_channels;//44100*2*2
    out_buffers = static_cast<uint8_t *>(malloc(out_buffers_size));

    //ffmpeg 音频 重采样 音频重采样上下文
    swr_ctx = swr_alloc_set_opts(0,
            //输入
                                 AV_CH_LAYOUT_STEREO,
                                 AV_SAMPLE_FMT_S16,
                                 out_sample_rate,
            //输出
                                 codecContext->channel_layout,//声道布局 双声道
                                 codecContext->sample_fmt,//采样大小
                                 codecContext->sample_rate,//采样率
                                 0, 0
    );
    //初始化 重采样上下文
    swr_init(swr_ctx);
}

AudioChannel::~AudioChannel() {
    if (swr_ctx) {
        swr_free(&swr_ctx);
    }
    DELETE(out_buffers);
}

void AudioChannel::stop() {

    pthread_join(pid_audio_decode, nullptr);
    pthread_join(pid_audio_play, nullptr);

    //保证两个线程都执行完毕，再释放
    isPlaying = false;
    packets.setWork(0);
    frames.setWork(0);

    //设置停止状态
    if (bqPlayerPlay) {
        (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_STOPPED);
        bqPlayerPlay = nullptr;
        bqPlayerBufferQueue = nullptr;
    }

    //销毁混音器
    if (outputMixObject) {
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject = nullptr;
    }

    //销毁引擎
    if (engineObject) {
        (*engineObject)->Destroy(engineObject);
        engineObject = nullptr;
        engineInterface = nullptr;
    }
    //清空队列
    packets.clear();
    frames.clear();
}

void *task_audio_decode(void *args) {
    auto *audioChannel = static_cast<AudioChannel *>(args);
    audioChannel->audio_decode();
    return nullptr;
}

void *task_audio_play(void *args) {
    auto *audioChannel = static_cast<AudioChannel *>(args);
    audioChannel->audio_play();
    return nullptr;
}

void AudioChannel::start() {
    isPlaying = true;

    packets.setWork(1);
    frames.setWork(1);

    pthread_create(&pid_audio_decode, nullptr, task_audio_decode, this);
    pthread_create(&pid_audio_play, nullptr, task_audio_play, this);
}

void AudioChannel::audio_decode() {
    AVPacket *pkt = nullptr;
    while (isPlaying) {
        //正在播放 但是队列已满
        if (isPlaying && frames.size() > 100) {
            av_usleep(10 * 1000);//10毫秒
            continue;
        }
        int ret = packets.getQueueAndDel(pkt);
        if (!isPlaying) {
            break;
        }
        if (!ret) {
            continue;
        }
        ret = avcodec_send_packet(codecContext, pkt);
        if (ret) {
            break;//avcodec_send_packet 出现错误
        }
        AVFrame *frame = av_frame_alloc();
        ret = avcodec_receive_frame(codecContext, frame);
        if (ret == AVERROR(EAGAIN)) {
            continue;//音频帧可能获取失败 重新拿一次
        } else if (ret != 0) {
            if (frame) {
                releaseAVFrame(&frame);
            }
            break;
        }
        frames.insertToQueue(frame);
        av_packet_unref(pkt);
        releaseAVPacket(&pkt);
    }
    av_packet_unref(pkt);
    releaseAVPacket(&pkt);
}

/**
 * 4.3 TODO 回调函数
 * @param bq  队列
 * @param args  this // 给回调函数的参数
 */

void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *args) {

    LOGD("1、开始回调播放 bqPlayerCallback")
    auto *audio_channel = static_cast<AudioChannel *>(args);
    int pcm_size = audio_channel->getPCM();

    // 添加数据到缓冲区里面去
    (*bq)->Enqueue(
            bq, // 传递自己，为什么（因为没有this，为什么没有this，因为不是C++对象，所以需要传递自己） JNI讲过了
            audio_channel->out_buffers, // PCM数据
            pcm_size); // PCM数据对应的大小，缓冲区大小怎么定义？（复杂）
}

/**
 * 1. out_buffers给与数据
 * 2. out_buffer给予数据的大小计算工作
 * @return 大小要重新计算，因为我们需要做重采样工作，重采样之后，大小不同了
 */
int AudioChannel::getPCM() {
    int pcm_data_size = 0;
    // 获取PCM 数据
    // PCM 数据在哪里？在frames队列中 frame->data == PCM 数据（待重采样 32bite）
    AVFrame *frame = nullptr;
    while (isPlaying) {
        int ret = frames.getQueueAndDel(frame);
        if (!isPlaying) {
            break;
        }
        if (!ret) {
            continue;//取出数据失败，继续，有可能数据生产太慢（原始包数据加入队列），消费等待
        }
        //开始重采样
        // 来源：10个48000 --》目标：44100 11个44100
        // 获取单通道的样本数（计算目标样本数 10个48000 48000/44100除不尽，所以需要11个44100）
        int dst_nb_samples = av_rescale_rnd(
                swr_get_delay(swr_ctx, frame->sample_rate) + frame->nb_samples,//获取下一个输入样本相对于下一个输出样本将经历的延迟
                out_sample_rate,
                frame->sample_rate,
                AV_ROUND_UP);
        // pcm 的处理逻辑
        // 音频播放器的数据格式是我们自己在下面定义的
        // 而原始数据（待播放的音频pcm数据）
        // 重采样工作
        // 返回的结果：每个通道输出的样本数（注意是转换后的）做一个简单的重采样实验（通道基本上都是：1024）
        int samples_per_channel = swr_convert(swr_ctx,
                                              //下面是输出区域
                                              &out_buffers, // 重采样后
                                              dst_nb_samples, // [成果的单通道样本数 无法与out_buffers对应，所以有下面的pcm_data_size计算]
                                               // 下面是输入区域
                                              (const uint8_t **) frame->data, // 队列的AAVFrame* 未重采样的PCM数据
                                              frame->nb_samples // 输入的样本数
        );
        // 由于out_buffers和dst_nb_samples无法对应，所以需要重新计算
        // 941 通道样本数 * 2 样本格式字节数 * 2声道数 =3764
        pcm_data_size = samples_per_channel * out_sample_size * out_channels;
        // 单通道样本数：1024*2*2（16bite） = 4096
        break;
    }// while end

    av_frame_unref(frame);
    releaseAVFrame(&frame);
    return pcm_data_size;


}

void AudioChannel::audio_play() {
    SLresult result;//用于接收成功或者失败的返回值
    /**
     * 1.创建引擎对象并获取【引擎接口】
     */
    //1.1 创建引擎对象 SLObjectItf engineObject
    result = slCreateEngine(&engineObject, 0, 0, 0, 0, 0);
    if (SL_RESULT_SUCCESS != result) {
        LOGE("创建引擎 slCreateEngine error");
        return;
    }
    //1.2 初始化引擎
    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);//SL_BOOLEAN_FALSE 延时等待你创建成功
    if (SL_RESULT_SUCCESS != result) {
        LOGE("初始化引擎 Realize error");
        return;
    }
    //1.3 获取引擎接口
    result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineInterface);
    if (SL_RESULT_SUCCESS != result) {
        LOGE("创建引擎接口 GetInterface error");
        return;
    }

    //健壮判断
    if (engineInterface) {
        LOGE("创建引擎接口 create success");
    } else {
        LOGE("创建引擎接口 create error");
        return;
    }
    /**
     * 2.设置混音器
     */
    //2.1 创建混音器
    result = (*engineInterface)->CreateOutputMix(engineInterface, &outputMixObject, 0, 0,
                                                 0);//环境特效 混响特效。。。都不需要
    if (SL_RESULT_SUCCESS != result) {
        LOGE("创建混音器 CreateOutputMix error");
        return;
    }

    //2.2 初始化混音器
    result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        LOGE("初始化混音器 Realize error");
        return;
    }

    LOGI("2、设置混音器 success");
    /**
     * 3.创建播放器
     */
    //创建buffer缓存类型的队列
    SLDataLocator_AndroidSimpleBufferQueue loc_bufq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 10};

    /**
     * pcm数据格式 == PCM 是不能直接播放的，mp3可以直接播放(参数集)，但人家不知道是PCM参数
     *
     * SL_DATAFORMAT_PCM:数据格式为pcm格式
     * 2.双声道
     * SL_SAMPLINGRATE_44_1:采样率为44100
     * SL_PCMSAMPLEFORMAT_FIXED_16:采样格式为 16bit
     * SL_PCMSAMPLEFORMAT_FIXED_16:数据大小为 16bit
     * SL_SPEAKER_FRONT_LET | SL_SPEAKER_FRONT_RIGHT:左右声道（双声道）
     * SL_BYTEORDER_LITTLEENDIAN：小端模式  第一个字节是最低位字节
     *
     */
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM,
                                   2,
                                   SL_SAMPLINGRATE_44_1,
                                   SL_PCMSAMPLEFORMAT_FIXED_16,
                                   SL_PCMSAMPLEFORMAT_FIXED_16,
                                   SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
                                   SL_BYTEORDER_LITTLEENDIAN
    };
    //数据源 将上述配置信息放到这个数据源中
    //audioSrc最终配置音频信息的成果，给后面的代码使用
    SLDataSource audioSrc = {&loc_bufq, &format_pcm};
    //独立声卡：24bite 继承声卡 16bite
    // 3.2 配置音轨（输出）
    //设置混音器

    SLDataLocator_OutputMix loc_out_mix = {SL_DATALOCATOR_OUTPUTMIX,outputMixObject};//SL_DATALOCATOR_OUTPUTMIX 输出混音器类型
    SLDataSink audioSink = {&loc_out_mix, nullptr};//loc_out_mix最终混音器的成果
    //需要的接口 操作队列的接口
    const SLInterfaceID ids[1] = {SL_IID_BUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};
    //3.3 创建播放器 SLObjectItf bqPlayerObject
    result = (*engineInterface)->CreateAudioPlayer(engineInterface,//参数1：引擎接口
                                                   &bqPlayerObject,//参数2： 播放器
                                                   &audioSrc,//参数3：音频配置信息
                                                   &audioSink,//参数4：混音器
                                                   //下面是打开队列
                                                   1,//参数5：开放的参数个数
                                                   ids,//参数6：代表我们需要的 buff
                                                   req //参数7：代表我们上面的buff需要开放出去
    );
    if (SL_RESULT_SUCCESS != result) {
        LOGD("创建播放器 CreateAudioPlayer failed %d",result);
        return;
    }
    //3.4 初始化播放器：SLObjectItf bqPlayerObject
    result = (*bqPlayerObject)->Realize(bqPlayerObject,
                                        SL_BOOLEAN_FALSE);//SL_BOOLEAN_FALSE 延时等待创建成功
    if (SL_RESULT_SUCCESS != result) {
        LOGD("创建播放器 Realize failed");
        return;
    }
    LOGD("创建播放器 CreateAudioPlayer success!");
    //3.5 获取播放器接口 【以后播放全部使用播放器接口去做（核心）】
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_PLAY, &bqPlayerPlay);
    if (SL_RESULT_SUCCESS != result) {
        LOGD("创建播放器 GetInterface failed");
        return;
    }
    LOGD("3、创建播放器 success!");
    /**
     * 4. 设置回调接口
     */
    //4.1 获取播放器队列接口： SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue 播放器需要的队列
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_BUFFERQUEUE,
                                             &bqPlayerBufferQueue);
    if (SL_RESULT_SUCCESS != result) {
        LOGD("获取播放队列 GetInterface SL_IID_BUFFERQUEUE failed");
        return;
    }
    //4.2 设置回调 void bqPlayerCallback(SLAndroidSampleBufferQueueItf bq,void *context)
    (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue,  // 传入刚刚设置好的队列
                                             bqPlayerCallback,  // 回调函数
                                             this); // 给回调函数的参数
    LOGD("4、设置播放回调函数 Success");
    /**
     * 5、设置播放器状态为播放状态
     */
    (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);
    LOGD("5、设置播放器状态为播放状态 Success");
    //6、手动激活回调函数
    bqPlayerCallback(bqPlayerBufferQueue, this);
    LOGD("6、手动激活回调函数 Success")
}

