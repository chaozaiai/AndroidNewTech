package cn.com.amway.content;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.techidea.library.utils.LogUtil;

import java.io.File;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.amway.content.constant.Constant;
import cn.com.amway.content.service.FileDownloadManager;
import cn.com.amway.content.service.JobIntentService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @BindView(R.id.et_yunzhidao_tag)
    EditText etYunzhidao;
    @BindView(R.id.et_boku_tag)
    EditText etBoku;
    @BindView(R.id.et_yunxuetang_tag)
    EditText etYunxuetang;

    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LogUtil.info(TAG, "onCreate");
        startUploadService();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.info(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.info(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.info(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.info(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.info(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.info(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.info(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.info(TAG, "onDestroy");
    }

    private void startUploadService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(this, JobIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskname", "task1");
        intent.putExtras(bundle);
        startService(intent);
//        ServiceConnection connection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//
//            }
//        }
//        bindService(intent, serviceConnection, IntentService.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.bt_h5)
    void h5Click() {
        Intent intent = new Intent();
        intent.setClass(this, WebViewActivity.class);
        intent.putExtra(Constant.PTEXTRA_URL, Constant.H5_URL);
        startActivity(intent);
    }

    @OnClick(R.id.bt_product)
    void productClick() {
        Intent intent = new Intent();
        intent.setClass(this, ProductActivity.class);
        intent.putExtra(Constant.PTEXTRA_URL, Constant.PRODUCT_URL);
        startActivity(intent);
    }

    @OnClick(R.id.bt_boku)
    void bokuClick() {

    }

    @OnClick(R.id.bt_yunzhidao)
    void yunzhidaoClick() {
        String tag = etYunzhidao.getText().toString().trim();
        Intent intent = new Intent();
        intent.setClass(this, ArticleListActivity.class);
        intent.putExtra(Constant.PTEXTRA_TAG, "");
        startActivity(intent);
    }

    @OnClick(R.id.bt_search)
    void searchActivity() {
        startActivity(new Intent(this, SearchArticleActivity.class));
    }

    @OnClick(R.id.bt_download)
    void downloadClick() {
        FileDownloadManager fileDownloadManager = FileDownloadManager.getInstance(this.getApplicationContext());
        fileDownloadManager.registDownload(new FileDownloadManager.DownloadCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart");
            }

            @Override
            public void onProgress(int progress) {
                Log.i(TAG, "onProgress: " + progress);
            }

            @Override
            public void onComplete(File file) {
                Log.i(TAG, "onComplete: " + file.getAbsolutePath());
            }

            @Override
            public void onFail(String msg) {
                Log.i(TAG, "onFail: " + msg);
            }
        });

        fileDownloadManager.startDownload(Constant.APP_DOWNLOAD_URL, R.mipmap.ic_launcher);
    }

    @OnClick(R.id.bt_downloadcancel)
    void cancelDownloadClick() {

    }

    @OnClick(R.id.bt_notification)
    void notifyClick() {
        showNotification();
        addNotification();
        showNotificationCompat();
    }

    private String channelId = "123";
    private int notifyId = 123;

    private void showNotificationCompat() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    private void showNotification() {
        Notification.Builder notificationCompat = new Notification.Builder(this);
        notificationCompat
                .setContentTitle("测试")
                .setContentText("测试")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = notificationCompat.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), flags);
        return pendingIntent;
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
