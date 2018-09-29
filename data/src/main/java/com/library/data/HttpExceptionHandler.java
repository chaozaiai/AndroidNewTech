package com.library.data;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by sam on 2018/1/31.
 */

public class HttpExceptionHandler {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int PARSE_ERROR = 1101;
    private static final int UNKNOWN = 1102;
    private static final int NETWORD_ERROR = 1103;
    private static final int NETWORD_ERROR_SERVER = 1104;
    private static final int NETWORD_ERROR_TIME_OUT = 1105;


    public static ApiException handleException(Throwable throwable) {
        ApiException apiException;
        if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            apiException = new ApiException(PARSE_ERROR, "数据解析错误，请稍后重试", "");//均视为解析错误
            return apiException;
        } else if (throwable instanceof HttpErrorException) {
            HttpErrorException exception = (HttpErrorException) throwable;
            apiException = new ApiException(exception.getCodeInt(), exception.getMessage(), exception.getEcode());
            return apiException;
        } else if (throwable instanceof SocketTimeoutException) {
            apiException = new ApiException(NETWORD_ERROR_TIME_OUT, "网络连接超时，请稍后重试", "");
            return apiException;
        } else if (throwable instanceof TimeoutException) {
            apiException = new ApiException(NETWORD_ERROR_TIME_OUT, "网络连接超时，请检查设备网络", "");
            return apiException;
        } else if (throwable instanceof UnknownHostException) {
            apiException = new ApiException(NETWORD_ERROR, "请检查设备网络后重试", "");
            return apiException;
        } else if (throwable instanceof IOException) {
            apiException = new ApiException(NETWORD_ERROR, "访问服务错误，请稍后重试", "");
            return apiException;
        } else if (throwable instanceof HttpErrorException) {
            HttpErrorException exceptions = (HttpErrorException) throwable;
            apiException = new ApiException(exceptions.getCodeInt(), exceptions.getMessage(), exceptions.getEcode());
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(apiException.getMessage());
            arr.add(apiException.getEcode());
            arr.add(String.valueOf(apiException.getCode()));
            return null;
        } else {
            apiException = new ApiException(UNKNOWN, "未知错误，请稍后重试", "");
            return apiException;
        }
    }
}
