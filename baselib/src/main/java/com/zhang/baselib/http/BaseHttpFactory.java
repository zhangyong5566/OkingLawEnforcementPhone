package com.zhang.baselib.http;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.interceptor.AddCookiesInterceptor;
import com.zhang.baselib.http.interceptor.CacheControlInterceptor;
import com.zhang.baselib.http.interceptor.HttpLoggingInterceptor;
import com.zhang.baselib.http.interceptor.SaveCookiesInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/3/14.
 */

public class BaseHttpFactory {
    private static BaseHttpFactory baseHttpFactory;
    private final Cache mCache;

    private BaseHttpFactory() {
        mCache = new Cache(new File(BaseApplication.getApplictaion().getCacheDir(), "HttpCache"),
                1024 * 1024 * 10);
    }

    public static BaseHttpFactory getInstence() {
        if (baseHttpFactory == null) {
            synchronized (BaseHttpFactory.class) {
                if (baseHttpFactory == null) {
                    baseHttpFactory = new BaseHttpFactory();
                }
            }
        }
        return baseHttpFactory;
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .client(getOkHttpClient())
                .build();
        return retrofit.create(serviceClass);
    }

    private final static long DEFAULT_TIMEOUT = 10;

    private OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

//        httpClientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("User-Agent", "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52")
//                        .build();
//                return chain.proceed(request);
//            }
//
//        });
        httpClientBuilder.cache(mCache);
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor());
        httpClientBuilder.addInterceptor(new CacheControlInterceptor());
        httpClientBuilder.addInterceptor(new AddCookiesInterceptor(BaseApplication.getApplictaion()));
        httpClientBuilder.addInterceptor(new SaveCookiesInterceptor(BaseApplication.getApplictaion()));
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        return httpClientBuilder.build();
    }
}
