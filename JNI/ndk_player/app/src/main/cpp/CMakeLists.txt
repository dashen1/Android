cmake_minimum_required(VERSION 3.18.1)

project("ndk_player")

set(FFMPEG ${CMAKE_SOURCE_DIR}/ffmpeg)
set(RTMP ${CMAKE_SOURCE_DIR}/rtmp)
# 导入ffmpeg头文件
include_directories(${FFMPEG}/include)

# 设置环境变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${FFMPEG}/libs/${CMAKE_ANDROID_ARCH_ABI}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${RTMP}/libs/${CMAKE_ANDROID_ARCH_ABI}")

# 批量导入源文件
file(GLOB src_files *.cpp)

add_library( # Sets the name of the library.
        ndk_player
        SHARED
        # Provides a relative path to your source file(s).
        # native-lib.cpp
        ${src_files}
        )


find_library( # Sets the name of the path variable.
        log-lib
        log
        )

target_link_libraries( # Specifies the target library.
        ndk_player
        ${log-lib}
        # 忽略顺序的方式，导入
        -Wl,--start-group
        avcodec avfilter avformat avutil swresample swscale
        -Wl,--end-group

        z #  #libz.so库，是FFmpeg需要用ndk的z库，FFMpeg需要额外支持  libz.so
        rtmp # rtmp 后面会专门介绍
        android # android 后面会专门介绍，目前先要明白的是 ANativeWindow 用来渲染画面的
        OpenSLES # OpenSLES 后面会专门介绍，目前先要明白的是 OpenSLES 用来播放声音的
        )