package com.nick.safecloud.activity;

import com.google.gson.Gson;

import android.os.Bundle;
import android.text.TextUtils;

import com.nick.safecloud.R;
import com.nick.safecloud.api.ApiScheduler;
import com.nick.safecloud.api.BaiduApi;
import com.nick.safecloud.base.BaseActivity;
import com.nick.safecloud.model.CloudInfoModel;
import com.nick.safecloud.utils.CookieUtils;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class LoadingActivity extends BaseActivity {


    public static final int LOADING_TIME_MILLISECOND = 3000;

    private boolean needLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(TextUtils.isEmpty(CookieUtils.getCookie())) {
            needLogin = true;
        } else {
            getQuote();
        }

        Observable.timer(LOADING_TIME_MILLISECOND, TimeUnit.MILLISECONDS)
                .compose(new ApiScheduler<Long>())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if(needLogin) {
                            WebLoginActivity.startMe(LoadingActivity.this);

                        } else {
                            NetDiskActivity.startMe(LoadingActivity.this, "/");
                        }
                        finish();
                    }
                });
    }



    private void getQuote() {

        BaiduApi.ApiBuilder.build().getQuote()
                .map(new Func1<String, CloudInfoModel>() {
                    @Override
                    public CloudInfoModel call(String s) {
                        Gson gson = new Gson();
                        return gson.fromJson(s, CloudInfoModel.class);
                    }
                }).compose(this.<CloudInfoModel>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new ApiScheduler<CloudInfoModel>())
                .subscribe(new Subscriber<CloudInfoModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        WebLoginActivity.startMe(LoadingActivity.this);
                    }

                    @Override
                    public void onNext(CloudInfoModel s) {
                        if(s.getErrno() == CloudInfoModel.SUCCESS_CODE) {
                            needLogin = false;
                        } else {
                           needLogin = true;
                        }
                    }
                });
    }
}
