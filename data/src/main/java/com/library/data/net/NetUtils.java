package com.library.data.net;

/**
 * Created by sam on 2018/2/6.
 */

public class NetUtils {
    public static boolean isEmpty(CharSequence string) {
        return (string == null
                || string.equals("null")
                || string.equals("NULL")
                || string.toString().trim().length() <= 0);
    }
}
