# 这里能够决定编译 Login.c Test.c

# 1.源文件的位置。宏函数my-dir返回当前目录(包含Android.mk文件本省的目录)的路径

# LOCAL_PATH 其实就是Android.mk文件本身的目录的路径
LOCAL_PATH :=$(call my-dir)

$(info "LOCAL_PATH: ===${LOCAL_PATH}")

# 2.清理
include $(CLEAR_VARS)

LOCAL_MODULE := libgetndk.so

