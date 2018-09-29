package com.library.data;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sam on 2018/1/31.
 */

public class RxUtils {

    // TODO: 2017/12/7 接口延时3秒测试加载中
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
//                        .delay(3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }
}
