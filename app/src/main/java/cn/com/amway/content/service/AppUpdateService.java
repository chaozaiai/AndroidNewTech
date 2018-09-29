package cn.com.amway.content.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import cn.com.amway.content.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sam on 2018/9/27.
 */

public class AppUpdateService extends IntentService {

    private static final int NOTIFYCATION_ID = 0;
    private static String NOTIFYCATION_CHANNEL_ID = "download_apk";
    private static String NOTIFYCATION_CHANNEL_NAME = "download";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder compatBuilder;
    private Notification.Builder builder;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isDownloading = false;
    private int currentProgress = 0;
    private int imageResId = 0;
    private float rate = 1.0f;

    public AppUpdateService() {
        super("AppUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (isDownloading) {
            return;
        }
        String url = intent.getStringExtra("url");
        imageResId = intent.getIntExtra("resId", R.mipmap.ic_launcher);
        downloadFile(url);

    }

    private void start() {
        Intent intent = new Intent(FileDownloadManager.ACTION_TYPE_START);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void progress(int progress) {
        Intent intent = new Intent(FileDownloadManager.ACTION_TYPE_PROGRESS);
        intent.putExtra("progress", progress);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void complete(File file) {
        Intent intent = new Intent(FileDownloadManager.ACTION_TYPE_COMPLETE);
        intent.putExtra("file_path", file.getAbsoluteFile());
        localBroadcastManager.sendBroadcast(intent);
    }

    private void complete(String msg) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (builder != null) {
                builder.setContentTitle("新版本").setContentText(msg);
                Notification notification = builder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFYCATION_ID, notification);
            }
        } else {
            if (compatBuilder != null) {
                compatBuilder.setContentTitle("新版本").setContentText(msg);
                Notification notification = compatBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFYCATION_ID, notification);
            }
        }
        stopSelf();
    }

    private void fail(String message) {
        Intent intent = new Intent(FileDownloadManager.ACTION_TYPE_FAIL);
        intent.putExtra("error", message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void setNotification() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intentCancel = new Intent(this, NotificationBroadcastReciver.class);
            intentCancel.setAction("notification_cancelled");
            intentCancel.putExtra("type", 3);
            intentCancel.putExtra("message", "message");
            PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0,
                    intentCancel, PendingIntent.FLAG_ONE_SHOT);
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFYCATION_CHANNEL_ID, NOTIFYCATION_CHANNEL_NAME, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                builder = new Notification.Builder(this, NOTIFYCATION_CHANNEL_ID);
                builder.setContentTitle("开始下载")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("更新中...")
                        .setDeleteIntent(pendingIntentCancel)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis());
                notificationManager.createNotificationChannel(notificationChannel);
            } else {
                compatBuilder = new NotificationCompat.Builder(this, NOTIFYCATION_CHANNEL_ID);
                compatBuilder.setContentTitle("开始下载")
                        .setContentText("更新中...")
                        .setDeleteIntent(pendingIntentCancel)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis());
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notificationManager.notify(NOTIFYCATION_ID, builder.build());
        } else {
            notificationManager.notify(NOTIFYCATION_ID, compatBuilder.build());
        }
    }

    /**
     * 路径为根目录
     * 创建文件名称为 updateDemo.apk
     */
    private File createFile() {
        String root = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Metro/";
        File direc = new File(root);
        if (!direc.exists()) {
            direc.mkdirs();
        }
        File file = new File(root, "/updateDemo.apk");
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downloadFile(String url) {
        if (TextUtils.isEmpty(url)
                || !url.contains("http")) {
            complete("下载路径错误");
            return;
        }
        setNotification();
        handler.sendEmptyMessage(0);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (response.body() == null) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = "下载错误";
                handler.sendMessage(message);
                return;
            }
            InputStream is = null;
            byte[] buff = new byte[2048];
            int len;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
                File file = createFile();
                fos = new FileOutputStream(file);
                long sum = 0;
                boolean isStop = FileDownloadManager.isStop;
                while ((len = is.read(buff)) != -1 && isStop) {
                    isStop = FileDownloadManager.isStop;
                    fos.write(buff, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    if (rate != progress) {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = progress;
                        handler.sendMessage(message);
                        rate = progress;
                    }
                }
                fos.flush();
                if (isStop) {
                    stopSelf();
                } else {
                    Message message = Message.obtain();
                    message.what = 3;
                    message.obj = file.getAbsoluteFile();
                    handler.sendMessage(message);
                    notifyInstall(file.getAbsoluteFile());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = 4;
                handler.sendMessage(message);
            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Message message = Message.obtain();
            message.what = 1;
            message.obj = e.getMessage();
            handler.sendMessage(message);
        }

    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    start();
                    break;
                case 1:
                    notificationManager.cancel(NOTIFYCATION_ID);
                    fail((String) msg.obj);
                    break;
                case 2:
                    currentProgress = (int) msg.obj;
                    progress(currentProgress);
                    if (Build.VERSION.SDK_INT >= 26) {
                        builder.setContentTitle("正在下载")
                                .setContentText(String.format(Locale.CHINESE, "%d%%", currentProgress))
                                .setProgress(100, currentProgress, false)
                                .setWhen(System.currentTimeMillis());
                        Notification notification = builder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(NOTIFYCATION_ID, notification);
                    } else {
                        compatBuilder.setContentTitle("正在下载")
                                .setContentText(String.format(Locale.CHINESE, "%d%%", currentProgress))
                                .setProgress(100, currentProgress, false)
                                .setWhen(System.currentTimeMillis());
                        Notification notification = compatBuilder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(NOTIFYCATION_ID, notification);
                    }
                    break;
                case 3:
                    notificationManager.cancelAll();
                    complete((File) msg.obj);
                    break;
                case 4:
                    notificationManager.cancel(NOTIFYCATION_ID);
                    break;
            }
            return false;
        }
    });

    private void notifycationTest() {

    }

    private void notifyInstall(File file) {
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("apk".equals(prefix)) {
            if (onFront()) {
                Intent intent = installIntent(file);
                startActivity(intent);
            } else {
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    Intent intent = installIntent(file.getAbsoluteFile());
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
                            , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pIntent)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("下载完成，点击安装")
                            .setProgress(0, 0, false)
                            .setDefaults(Notification.DEFAULT_ALL);
                    Notification notification = builder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(NOTIFYCATION_ID, notification);
                } else {
                    Intent intent = installIntent(file.getAbsoluteFile());
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
                            , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    compatBuilder.setContentIntent(pIntent)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("下载完成，点击安装")
                            .setProgress(0, 0, false)
                            .setDefaults(Notification.DEFAULT_ALL);
                    Notification notification = compatBuilder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(NOTIFYCATION_ID, notification);
                }
            }
        }
    }

    /**
     * 是否运行在用户前面
     */
    private boolean onFront() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.isEmpty())
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(getPackageName()) &&
                    appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private Intent installIntent(File file) {
        try {
            String authority = getApplicationContext().getPackageName() + ".provider";
            Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), authority, file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        notificationManager = null;
        super.onDestroy();
    }
}
