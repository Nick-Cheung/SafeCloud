package com.nick.safecloud.api;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sparrow on 2017/2/22.
 */

public class ApiConfig {

    private ApiConfig() {

    }


    public static HashMap<String, String> getTokenParams() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("tpl", "netdisk");
        paramMap.put("apiver", "v3");
        paramMap.put("tt", String.valueOf(System.currentTimeMillis()));
        paramMap.put("class", "login");
        paramMap.put("logintype", "basicLogin");
        paramMap.put("isphone", String.valueOf(false));

        return paramMap;
    }

    public static HashMap<String, String> getKeyParam(String token) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("tpl", "netdisk");
        dataMap.put("subpro", "netdisk_web");
        dataMap.put("token", token);
        dataMap.put("apiver", "v3");
        dataMap.put("tt", String.valueOf(System.currentTimeMillis()));
        dataMap.put("gid", "3C29DE6-3DE5-4F8C-A6C9-14400D10B61A");
        dataMap.put("callback", "bd__cbs__hv7g8t");
        return dataMap;
    }




    public static HashMap<String, String> getLoginParams(String token, String rsaKey, String userName, String password, String codeString, String verifyCode) {
        HashMap<String, String> dataMap = new HashMap<>();

        dataMap.put("staticpage", "http://pan.baidu.com/res/static/thirdparty/pass_v3_jump.html");
        dataMap.put("charset", "utf-8");
        dataMap.put("token", token);
        dataMap.put("tpl", "netdisk");
        dataMap.put("subpro", "netdisk_web");
        dataMap.put("detect", "1");
        dataMap.put("apiver", "v3");
        dataMap.put("tt", String.valueOf(System.currentTimeMillis()));
        dataMap.put("isPhone", "false");
        dataMap.put("safeflg", "0");
        dataMap.put("u", "https://passport.baidu.com/");
        dataMap.put("username", userName);
        dataMap.put("logLoginType", "pc_loginBasic");
        dataMap.put("password", password);
        dataMap.put("quick_user", "0");
        dataMap.put("logintype", "basicLogin");
        dataMap.put("mem_pass", "on");
        dataMap.put("ppui_logintime", "37651");
        dataMap.put("callback", "bd__cbs__hv7g8t");
        dataMap.put("codestring", codeString);
        dataMap.put("verifycode", verifyCode);
        dataMap.put("rsakey", rsaKey);
        dataMap.put("gid", "81791E1-44BE-464A-B748-DFE1BF9104EF");
        dataMap.put("dv", "MDEwAAoANgAKAIAACAAAAF00AAICABDKysrLXFxcXQdYrq6GhLWBEAIAAcsXAgAUycnMzMOqxauLo4rxh-aUtNXos4MVAgAIy8vKkM86xKcBAgAGy8nJx1UvBQIABMvLy8EEAgAGycnLyv_JFgIAI-md9sbo2-3d7dXs2Ozb6dvv2uve59Dh1e3a7Nri1ODR4tPq");
        return dataMap;
    }






    public static class ApiScheduler<R> implements Observable.Transformer<R, R> {

        @Override
        public Observable<R> call(Observable<R> observable) {
            return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
    }


}
