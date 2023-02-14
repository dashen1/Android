package com.example.hilt.proxy.httpprocessor;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallback<Result> implements ICallback {

    @Override
    public void onSuccess(String result) {
        // 网络返回的数据以String的方式保存
        // 1.得到调用者用什么样的JavaBean来接收数据
        Class<?> aClass = analysisClassInfo(this);
        // 2. 把String转成JavaBean对象交给用户
        Gson gson = new Gson();
        Result objResult = (Result) gson.fromJson(result,aClass);
        // 3. 把结果交给用户
        onSuccess(objResult);
    }

    public abstract void onSuccess(Result objResult);

    /**
     * 通过输入参数得到传入泛型的实际类型
     * @param object
     * @return
     */
    private Class<?> analysisClassInfo(Object object){
        // getGenericSuperclass() 返回一个类型对象
        // 这个对象可以得到包含原始类型，参数化，数组，类型变量，基本数据类型
        Type type = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)type).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void onFailure(String e) {

    }
}
