package com.library.data.net;

/**
 * Created by sam on 2018/2/6.
 */

public interface OnHttpResultListener {

    void onError(int code, String message);

    void onSuccess(String response);
}
