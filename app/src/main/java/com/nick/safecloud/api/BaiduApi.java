package com.nick.safecloud.api;

import android.util.Log;

import com.nick.safecloud.utils.CookieUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Sparrow on 2017/2/21.
 */

public interface BaiduApi {


    int TIMEOUT_IN_MILL = 10000;

    String BAIDU_PAN_URL = "http://pan.baidu.com/";


    String GET_QUOTE = "/api/quota";

    String GET_FILE_LIST = "/api/list";


    String GET_USER_INFO = "https://topmsg.baidu.com/v2/?ucentertopbarinit&tt=%s&callback=bd__cbs__d9xfpc";


    @GET(GET_QUOTE)
    Observable<String> getQuote();


    @GET(GET_FILE_LIST)
    Observable<String> getFileList(@Query("dir") String dir);


    @GET("")
    Observable<String> getUserInfo(@Url String url);



    class ApiBuilder {

        private static BaiduApi instance;

        public static BaiduApi build() {
            if(instance == null) {
                Retrofit.Builder builder = new Retrofit.Builder();
                builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(BAIDU_PAN_URL)
                        .client(new OkHttpClient.Builder()
                                .readTimeout(TIMEOUT_IN_MILL, TimeUnit.MILLISECONDS)
                                .writeTimeout(TIMEOUT_IN_MILL, TimeUnit.MILLISECONDS)
                                .connectTimeout(TIMEOUT_IN_MILL, TimeUnit.MILLISECONDS)
                                .addInterceptor(new SetCookieInterceptor())
                                .build())
                        .addConverterFactory(ResponseConverter.Factory.create());

                instance = builder.build().create(BaiduApi.class);

            }

            return instance;
        }
    }

    class SetCookieInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("cookie", CookieUtils.getCookie());
            Response response = chain.proceed(builder.build());

            Log.i("url", chain.request().url().toString());

            return response;
        }
    }






    class DirectResponse extends ResponseConverter<String> {

        @Override
        protected String convertData(String data) throws IOException {
            return data;
        }
    }


    abstract class ResponseConverter<R> implements Converter<ResponseBody, R> {

        @Override
        public R convert(ResponseBody value) throws IOException {
            return convertData(value.string());
        }


        protected abstract R convertData(String data) throws IOException;

        static class Factory extends Converter.Factory {
            public static Factory create() {
                return new Factory();
            }


            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new DirectResponse();
            }
        }
    }

}
