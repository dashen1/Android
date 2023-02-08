package com.example.thirdparty.okhttp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

public class ExMultipartBody extends RequestBody {

    private static final String TAG = "ExMultipartBody";

    private RequestBody mRequestBody;
    private int mCountLength;
    private UploadProgressListener mUploadProgressListener;

    public ExMultipartBody(RequestBody mRequestBody, UploadProgressListener mUploadProgressListener) {
        this.mRequestBody = mRequestBody;
        this.mUploadProgressListener = mUploadProgressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {

        Log.i(TAG, "start to call writeTo");
        long contentLength = contentLength();
        ForwardingSink forwardingSink = new ForwardingSink(bufferedSink){
            @Override
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                mCountLength += byteCount;
                if (mUploadProgressListener!=null){
                    mUploadProgressListener.onProgress(contentLength,mCountLength);
                }
                super.write(source, byteCount);
            }
        };
        BufferedSink buffer = Okio.buffer(forwardingSink);
        mRequestBody.writeTo(buffer);
        buffer.flush();
    }
}
