package com.techidea.library.utils;

import android.util.Log;

/**
 * Created by zc on 2017/9/17.
 */

public class LogUtil {
    private static final boolean DEBUG = true;

    public static void i(String log) {
        if (!DEBUG) return;
        Log.i("RefreshLoadMoreLayout", log);
    }

    public static void info(String tag,String log) {
        if (!DEBUG) return;
        Log.i(tag, log);
    }
}
