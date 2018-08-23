package com.zhang.baselib.http.interceptor;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by Administrator on 2018/5/30/030.
 * 拦截器，超时重新登录，还可以用来检测IM登录，总之可以用来做很多事。。。。。
 */

public class ReLoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 原始请求
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        String respString = source.buffer().clone().readString(Charset.defaultCharset());
        Log.i("Oking", "############>>>" + respString);
        if (respString.contains("parent.location.href='/gdWater/login/initPage")) {
            Log.i("Oking", "登录信息已失效请重新登录");
        }
        return response;
    }
}
