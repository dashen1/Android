package com.example.thirdparty.okhttp;

public interface UploadProgressListener {
    void onProgress(long total,long current);
}
