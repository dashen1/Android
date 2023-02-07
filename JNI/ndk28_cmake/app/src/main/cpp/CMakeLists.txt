# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.18.1)

include_directories("inc")
# 当前工程名 可以不设置
#project("ndk28_cmake")

# 定义全局变量 SOURCE
file(GLOB SOURCE *.c *.h *.cpp)

# 导入头文件 1
# include_directories(${CMAKE_SOURCE_DIR}/cpp/inc)

# 导入库文件 -L 库路径 -l具体的库
# CMAKE_CXX_FLAGS == path; JAVA_HOME
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jinLibs/${CMAKE_ANDROID_ARCH_ABI}")


# 声明系统变量 给内置变量赋值 在CMake中所以变量都是字符串
# set()

# 移除变量 unset()

# 声明列表 set(list_var 1 2 3 4) 或者 set(list_var “1;2;3;4”)

# TODO CMake 流程控制-条件命令
# true(1. ON. YES. TRUE. Y, 非0的值)
# false(0. OFF. NO. FALSE. N, 非0的值)
set(if_tap OFF)
set(elseif_tap ON)

#if(${if_tap})
#    message("if")
#elseif(${elseif_tap})
#    message("elseif")
#else(${if_tap})
#    message("else")
#endif()



add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library. 动态库
        SHARED
        # Provides a relative path to your source file(s).
        #native-lib.cpp # libnative-lib.so
        # cpp 源文件：把cpp源文件编译成libnative-lib.so库
        ${SOURCE}
        )
# Sets the name of the path variable.
find_library(
        log-lib
        log
        )

target_link_libraries( # Specifies the target library.# ${log-lib}
        native-lib
        ${log-lib}
        fmod # 具体的库 链接到 libnative-lib.so里面去
        fmodL # 具体的库 链接到 libnative-lib.so里面去
        )

# TODO CMake 函数
message("AAAA")

function(num_method n1 n2 n3)
    message("call")
    message("n1 = ${n1}")
    message("n2 = ${n2}")
    message("n3 = ${n3}")
    message("ARGC = ${ARGC}")
    message("arg1 = ${ARGV0} arg2 = ${ARGV1} arg3 = ${ARGV2}")
endfunction()

#[[

]]
