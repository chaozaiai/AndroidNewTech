package com.library.data.net;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sam on 2018/2/6.
 */

public class OkHttpManager {

    public static final int ERROR_CODE = 0;

    private static final String TAG = "";
    private Map<String, String> headerParams;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private boolean isTest = false;

    private static volatile OkHttpManager Instance;

    private OkHttpManager() {
        okHttpClient = getOkHttpClient();
//        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpManager getInstance() {
        if (null == Instance) {
            synchronized (OkHttpManager.class) {
                if (null == Instance) {
                    Instance = new OkHttpManager();
                }
            }
        }
        return Instance;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public void getDataAsync(String url, final OnHttpResultListener onHttpResultListener) {
        Request.Builder requestBuilder = new Request.Builder();
        this.addHttpHead(requestBuilder, headerParams);
        Request request = requestBuilder.url(url)
                .tag(url)
                .get().build();
        this.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dealError(e, onHttpResultListener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dealResponse(response, onHttpResultListener);
            }
        });
    }

    public void getDataSync(String url, final OnHttpResultListener onHttpResultListener) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        this.addHttpHead(requestBuilder, headerParams);
        Request request = requestBuilder.url(url)
                .tag(url)
                .get().build();
        Response response = this.okHttpClient.newCall(request).execute();
        dealResponse(response, onHttpResultListener);
    }

    public void postDataAsync(String url, Map<String, String> params,
                              final OnHttpResultListener onHttpResultListener) {
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            paramsBuilder.add(entry.getKey(), entry.getValue());
        }
        Request.Builder requestBuilder = new Request.Builder();
        this.addHttpHead(requestBuilder, headerParams);
        Request request = requestBuilder.url(url)
                .tag(url)
                .post(paramsBuilder.build()).build();
        this.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                dealError(e, onHttpResultListener);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                dealResponse(response, onHttpResultListener);
            }
        });
    }

    public void postDataSync(String url, Map<String, String> params,
                             final OnHttpResultListener onHttpResultListener) throws IOException {
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            paramsBuilder.add(entry.getKey(), entry.getValue());
        }
        Request.Builder requestBuilder = new Request.Builder();
        this.addHttpHead(requestBuilder, headerParams);
        Request request = requestBuilder.url(url)
                .tag(url)
                .post(paramsBuilder.build()).build();
        Response response = this.okHttpClient.newCall(request).execute();
        dealResponse(response, onHttpResultListener);
        /*.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                dealError(e, onHttpResultListener);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                dealResponse(response, onHttpResultListener);
            }
        });*/
    }

    private void addHttpHead(Request.Builder builder, Map<String, String> heads) {
        if (heads != null && heads.size() > 0) {
            Log.v("OkHttpDataAcquisition", "Set http head:" + heads);
            Iterator i$ = heads.entrySet().iterator();

            while (i$.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) i$.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.header(key, val);
            }
        }
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }

    private void dealError(final IOException e, final OnHttpResultListener onHttpResultListener) {
        if (null != onHttpResultListener) {
            if (null != handler) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onHttpResultListener.onError(ERROR_CODE, e.getMessage());
                    }
                });
            }
        }
        if (isTest && null != onHttpResultListener) {
            onHttpResultListener.onError(ERROR_CODE, e.getMessage());
        }
    }

    private void dealResponse(final Response response, final OnHttpResultListener onHttpResultListener) throws IOException {
        if (!isTest) {
            if (response.isSuccessful()) {
                if (null != onHttpResultListener) {
                    final String result = response.body().string();
                    if (null != handler) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onHttpResultListener.onSuccess(result);
                            }
                        });
                    }
                }
            } else {
                if (null != onHttpResultListener) {
                    if (null != handler) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onHttpResultListener.onError(response.code(), response.message());
                            }
                        });
                    }
                }
            }
        } else {
            if (null != onHttpResultListener) {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    onHttpResultListener.onSuccess(result);
                } else {
                    onHttpResultListener.onError(response.code(), response.message());
                }
            }
        }
    }

    public void cancelRequest(String tag) {
        for (Call call : this.okHttpClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
        for (Call call : this.okHttpClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
    }
}
