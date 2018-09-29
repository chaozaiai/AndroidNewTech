package com.techidea.library.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 提供定位功能
 * Created by sam on 2018/1/15.
 */

public class LocationWebChromeClient extends WebChromeClient {

    Context context;

    public LocationWebChromeClient(Context context) {
        this.context = context;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        //final boolean remember = true;
        callback.invoke(origin, true, true);
       /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("位置信息");
        builder.setMessage(origin + "允许获取您的地理位置信息吗？").setCancelable(true).setPositiveButton("允许",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int id) {

                    }
                })
                .setNegativeButton("不允许",
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                callback.invoke(origin, false, false);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();*/
    }


    //下面的不需要

    /**
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("对话框")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });
        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        if (null != view) {
            dialog.show();
        }
        return true;

        //return super.onJsConfirm(view, url, message, result);
    }
}
