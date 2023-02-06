cmake_minimum_required(VERSION 3.18.1)

file(GLOB rtmp_src *.c)
# 不修改源码的情况下 解决报错 添加宏 -D
# 纯C使用 CMAKE_C_FLAGS （但是只要有一个C++文件就使用 CMAKE_CXX_FLAGS）
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DNO_CRYPTO")

add_library(
        rtmp
        STATIC # librtmp.a
        ${rtmp_src}

)
