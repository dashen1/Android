#include "AudioChannel.h"

AudioChannel::AudioChannel(int streamIndex, AVCodecContext *codecContext)
                : BaseChannel(streamIndex, codecContext){

    // 缓冲区的大小怎么定义？ out_buffers -- out_buffers_size
    // 初始化缓冲区
    /** 音频压缩数据 aac
     *
     * 44100
     * 32bit 浮点运算效率高
     * 2
     *
     * 音频三要素
     * 1.采样率
     * 2.位声/采样格式大小 16bit = 2字节
     * 3.声道数 2
     * */

    /**
     * 手机参数可能是
     * 44100
     * 16bit
     * 2
     * */

    out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//STEREO 双声道类型 获取声道数 2
    out_sample_size = av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);
    out_sample_rate = 44100;
    out_buffers_size = out_sample_size * out_sample_rate * out_channels;// 44100 * 2 * 2
    out_buffers = static_cast<uint8_t *>(malloc(out_buffers_size));



}
int AudioChannel::getPCM() {
    int pcm_data_size = 0;
    //获取PCM数据 从队列里获取 frame->data == PCM数据（）

}


AudioChannel::~AudioChannel() {

}

void *task_audio_decode(void *args) {
    auto *audio_channel = static_cast<AudioChannel *>(args);
    audio_channel->audio_decode();
    return 0;
}

void *task_audio_play(void *args) {
    auto *audio_channel = static_cast<AudioChannel *>(args);
    audio_channel->audio_play();
}

void AudioChannel::start() {
    isPlaying = 1;

    // 队列开始工作
    packets.setWork(1);
    frames.setWork(1);

    pthread_create(&pid_audio_decode, 0, task_audio_decode, this);
    pthread_create(&pid_audio_play, 0, task_audio_play, this);
}
void AudioChannel::stop() {

}

void AudioChannel::audio_decode() {
    AVPacket *pkt = 0;
    while (isPlaying) {
        int ret = packets.getQueueAndDel(pkt);
        if (!isPlaying) {
            break;
        }
        if (!ret) {
            continue;
        }
        ret = avcodec_send_packet(codecContext, pkt);
        releaseAVPacket(&pkt);
        if (ret) {
            break;
        }
        AVFrame *frame = av_frame_alloc();
        ret = avcodec_receive_frame(codecContext, frame);
        // 音频也有帧的概念 所以获取原始包的时候，最好判断下 严谨性
        if (ret == AVERROR(EAGAIN)) {
            continue;
        } else if (ret != 0) {
            break;
        }
        // 保存的是 PCM 音频数据 PCM不能直接播放 因为人家不知道PCM参数
        frames.insertToQueue(frame);
    } // while end
    releaseAVPacket(&pkt);
}

void AudioChannel::audio_play() {

    SLresult result;// 用于接收执行成功或失败的返回值
    /**
     * TODO 1.创建引擎对象
     *
     * */

    result = slCreateEngine(&engineObject, 0, 0, 0, 0, 0);
    if (SL_RESULT_SUCCESS != result) {
        LOGE("创建引擎 slCreateEngine error");
    }

    //1.2 初始化引擎
    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        LOGE("创建引擎接口 Realize error");
    }

}
