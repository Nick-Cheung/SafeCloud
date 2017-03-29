package com.nick.safecloud.api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 线程调度类
 */

public class ApiScheduler<R> implements Observable.Transformer<R, R> {

    @Override
    public Observable<R> call(Observable<R> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}