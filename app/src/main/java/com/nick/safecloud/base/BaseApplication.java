package com.nick.safecloud.base;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
                .connectTimeout(10000L, TimeUnit.SECONDS)
                .readTimeout(10000L, TimeUnit.SECONDS)
                .writeTimeout(10000L, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }
}
