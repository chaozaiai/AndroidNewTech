package cn.com.amway.content.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by sam on 2018/9/27.
 */

public class FileDownloadManager {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver broadcastReceiver;
    private DownloadCallBack downloadCallBack;
    private Context context;
    public static boolean isStop = false;
    public final static String ACTION_TYPE_START = "action.type.start";
    public final static String ACTION_TYPE_PROGRESS = "action.type.progress";
    public final static String ACTION_TYPE_COMPLETE = "action.type.complete";
    public final static String ACTION_TYPE_FAIL = "action.type.fail";
    public final static String ACTION_TYPE_CANCEL = "notification_cancelled";

    private volatile static FileDownloadManager instance;

    public static FileDownloadManager getInstance(Context context) {
        if (null == instance) {
            synchronized (FileDownloadManager.class) {
                if (null == instance) {
                    instance = new FileDownloadManager(context);
                }
            }
        }
        return instance;
    }

    private FileDownloadManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public interface DownloadCallBack {
        void onStart();

        void onProgress(int progress);

        void onComplete(File file);

        void onFail(String msg);
    }

    public void registDownload(DownloadCallBack callback) {
        if (null == context) {
            return;
        }
        downloadCallBack = callback;
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TYPE_START);
        intentFilter.addAction(ACTION_TYPE_PROGRESS);
        intentFilter.addAction(ACTION_TYPE_COMPLETE);
        intentFilter.addAction(ACTION_TYPE_FAIL);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


    }

    public void startDownload(String url, int resId) {
        isStop = false;
        if (null == context) {
            return;
        }
        if (null == downloadCallBack) {
            return;
        }
        Intent intent = new Intent(context, AppUpdateService.class);
        intent.putExtra("url", url);
        intent.putExtra("resId", resId);
        context.startService(intent);
    }

    public void unbind() {
        isStop = true;
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(broadcastReceiver);
        }
        broadcastReceiver = null;
        localBroadcastManager = null;
        downloadCallBack = null;
        context = null;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = null == intent.getAction() ? "" : intent.getAction();
            switch (action) {
                case ACTION_TYPE_START:
                    if (null != downloadCallBack) {
                        downloadCallBack.onStart();
                    }
                    break;
                case ACTION_TYPE_PROGRESS:
                    if (null != downloadCallBack) {
                        downloadCallBack.onProgress(intent.getIntExtra("progress", 0));
                    }
                    break;
                case ACTION_TYPE_COMPLETE:
                    if (null != downloadCallBack) {
                        String file_path = intent.getStringExtra("file_path");
                        if (!TextUtils.isEmpty(file_path)) {
                            File file = new File(file_path);
                            if (file.exists()) {
                                downloadCallBack.onComplete(file);
                            }
                        }
                    }
                    break;
                case ACTION_TYPE_FAIL:
                    if (null != downloadCallBack) {
                        String error = intent.getStringExtra("error");
                        if (downloadCallBack != null) {
                            downloadCallBack.onFail(error);
                        }

                    }
                    break;
                case ACTION_TYPE_CANCEL:

                    break;
            }
        }
    }
}
