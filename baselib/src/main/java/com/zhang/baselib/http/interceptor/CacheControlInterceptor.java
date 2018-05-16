package com.zhang.baselib.http.interceptor;

import android.util.Log;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.utils.NetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/3/6.
 * 缓存拦截器
 */

public class CacheControlInterceptor implements Interceptor {
    //短缓存1分钟
    public static final int CACHE_AGE_SHORT = 60;
    //长缓存有效期为1天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtil.isConnected(BaseApplication.getApplictaion())) {
            //没网时只使用缓存
            //自定义请求头，可以在响应头对请求头的header进行拦截，配置不同的缓存策略
            request = request.newBuilder()
                    .header("head-request", request.toString())
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (!NetUtil.isConnected(BaseApplication.getApplictaion())) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            Log.e("Interceptor", "response: " + response.toString());
            //添加头信息，配置Cache-Control
            //removeHeader("Pragma") 使缓存生效
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + CACHE_AGE_SHORT)
                    .removeHeader("Pragma")
                    .build();
        } else {
            Log.e("Interceptor", "net not connect");
            return response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached, max-stale=" + CACHE_STALE_LONG)
                    .removeHeader("Pragma")
                    .build();
        }
    }

}
