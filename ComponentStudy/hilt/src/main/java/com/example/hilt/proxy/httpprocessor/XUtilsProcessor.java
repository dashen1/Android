package com.example.hilt.proxy.httpprocessor;

import android.app.Application;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import javax.inject.Inject;

public class XUtilsProcessor implements IHttpProcessor{

    @Inject
    public XUtilsProcessor(Application application) {{
        x.Ext.init(application);
    }
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {
        RequestParams requestParams = new RequestParams(url);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallback callback) {

    }
}
