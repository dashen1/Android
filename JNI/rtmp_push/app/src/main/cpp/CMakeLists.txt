cmake_minimum_required(VERSION 3.18.1)

project("rtmp_push")

add_subdirectory(librtmp)

# 导入头文件的路径 自动查看头文件
include_directories(librtmp libx264/include)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libx264/libs/${CMAKE_ANDROID_ARCH_ABI}")

add_library(
        rtmp_push

        # Sets the library as a shared library.
        SHARED

        native-lib.cpp

)

find_library( # Sets the name of the path variable.
        log-lib
        log)
target_link_libraries( # Specifies the target library.
        rtmp_push
        rtmp
        x264
        ${log-lib})