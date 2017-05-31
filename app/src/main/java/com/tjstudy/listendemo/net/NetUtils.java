package com.tjstudy.listendemo.net;

import com.tjstudy.listendemo.base.GloableVariables;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络工具类 : Retrofit2 是基于ThreadPoolExecutor线程池来进行的网络访问
 */

public class NetUtils {
    private static NetService instance;
    private static String cookie;

    private NetUtils() {
    }

    public static NetService getInstance() {
        if (instance == null) {
            synchronized (NetUtils.class) {
                if (instance == null) {
                    buildNetServiceInstance();
                }
            }
        }
        return instance;
    }

    /**
     * 创建NetService的实例
     */
    private static void buildNetServiceInstance() {
        // 读取本地的cookie 在每一次访问的时候加上这个cookie
        Interceptor addHeaderInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("Cookie", cookie)
                        .build();
                return chain.proceed(request);
            }
        };
        //获取消息头中的cookie
        Interceptor getHeaderInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                //存入Session
                if (response.header("Set-Cookie") != null) {
                    cookie = response.header("Set-Cookie");
                }
                return response;
            }
        };
        Retrofit retrofit;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(GloableVariables.NET_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(GloableVariables.NET_CONN_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(addHeaderInterceptor)
                .addInterceptor(getHeaderInterceptor)
                .build();

        //设置Retrofit  可以进一步设置头-设置拦截器
        retrofit = new Retrofit.Builder()
                .baseUrl(GloableVariables.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        instance = retrofit.create(NetService.class);
    }
}