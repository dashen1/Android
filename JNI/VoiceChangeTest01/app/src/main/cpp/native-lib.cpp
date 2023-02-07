#include <jni.h>
#include <string>
#include <fmod.hpp>
#include <unistd.h>

using namespace FMOD;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_voicechangetest01_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_voicechangetest01_MainActivity_voiceChangeNative(JNIEnv *env, jobject thiz,
                                                                  jint mode, jstring path) {
    // TODO: implement voiceChangeNative()
    char * content_ = "默认播放完毕";
    //c 认识的字符串
    const char * path_ = env->GetStringUTFChars(path, NULL);

    // 音效引擎系统 指针
    System * system = 0;

    Sound * sound = 0;
    // 通道，音轨
    Channel * channel = 0;
    //digital signal process == 数字信号处理
    DSP * dsp = 0;

    //TODO 第一步 创建系统
    System_Create(&system);

    //TODO 第二步 系统的初始化 参数1：最大音轨数 参数2：初始化标记 参数3：额外数据
    system->init(32, FMOD_INIT_NORMAL, 0);

    //TODO 第三步 创建声音
    system->createSound(path_, FMOD_DEFAULT, 0, &sound);

    //TODO 第四步 播放声音 音轨
    //参数1：声音 参数2：分组音轨 参数3：控制 参数4：通道
    system->playSound(sound, 0, false, &channel);
    
    //TODO 第五步 增加特效
    switch (mode) {
        case 0:
            content_ = "原生 播放完毕";
            break;
        case 1:
            content_ = "萝莉 播放完毕";
            break;
        case 2:
            content_ = "大叔 播放完毕";
            break;
        case 3:
            content_ = "搞怪 小黄人播放完毕";
            float mFrequency;
            channel->getFrequency(&mFrequency);
            channel->setFrequency(mFrequency * 1.5f);
            break;
            //小黄人播放频率快 音轨有关
        case 4:
            content_ = "惊悚 播放完毕";
            //TODO 音调低

            //TODO 回声

            //TODO 颤抖
            break;
        case 5:
            content_ = "空灵 播放完毕";
            system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 200);
            dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 10);
            channel->addDSP(0, dsp);
            break;
    }
    // 等待播放完毕后 再回收
    bool isPlay = true; // 你不是一级指针 我用一级指针来接收
    while (isPlay) {
        channel->isPlaying(&isPlay);
        usleep(1000 * 1000);
    }
    sound->release();
    system->close();
    system->release();
    env->ReleaseStringUTFChars(path, path_);

    // 告知java播放完毕
    jclass mainClass = env->GetObjectClass(thiz);
    jmethodID playerEndMethod = env->GetMethodID(mainClass, "playerEnd", "(Ljava/lang/String;)V");
    jstring value = env->NewStringUTF(content_);
    env->CallVoidMethod(thiz, playerEndMethod, value);
}