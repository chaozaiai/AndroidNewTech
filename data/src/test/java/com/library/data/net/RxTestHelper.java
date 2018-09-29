package com.library.data.net;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by sam on 2018/1/31.
 */

public class RxTestHelper {

    public static void registAndroidImmediate() {
        RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        };

        RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);
    }

    public static void registJavaImmediate() {

        RxJavaSchedulersHook rxJavaSchedulersHook = new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        };

        RxJavaPlugins.getInstance().registerSchedulersHook(rxJavaSchedulersHook);
    }

    public static void rxReset() {
        RxJavaPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().reset();
    }
}
