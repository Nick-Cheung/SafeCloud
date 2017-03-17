package com.nick.safecloud.base;

import android.app.Application;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sparrow on 2017/2/20.
 */

public class BaseApplication extends Application {


    private static final String TAG = "BaseApplication";


    private volatile static  BaseApplication instance;

    public static BaseApplication getInstance() {
        if(instance == null) {
            instance = new BaseApplication();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        final CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .build();

                        Response response = chain.proceed(request);

                        Log.i(TAG, response.toString());
                        return response;
                    }
                })
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }
}
