//
// Created by se0891 on 2022/4/6.
//

#ifndef BBOYPLAYER_LOG4C_H
#define BBOYPLAYER_LOG4C_H

#include <android/log.h>

#define TAG "Bboy"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__);
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__);
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__);

#endif //BBOYPLAYER_LOG4C_H
