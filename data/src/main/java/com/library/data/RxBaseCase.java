package com.library.data;

import io.reactivex.Observable;

/**
 * Created by sam on 2018/1/31.
 */

public abstract class RxBaseCase<T> {

    protected RxBaseCase(){}

    public abstract RxBaseCase initParams(String... paras);
    public abstract Observable<T> execute();
}
