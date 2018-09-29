package cn.com.amway.content.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sam on 2018/9/25.
 */

public class JobIntentService extends IntentService {

    private static final String TAG = JobIntentService.class.getCanonicalName();

    public JobIntentService() {
        super("JobIntentService");
    }

    private int step = 0;
    private boolean isStop = false;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String taskName = intent.getExtras().getString("taskname");
        Log.i(TAG, taskName);
        while (!isStop) {
            try {
                Thread.sleep(2000);
                step++;
                //service 执行完成自动结束
                if (step > 5)
                    isStop = true;
                Log.i("onHandleIntent", "sleep");
            } catch (Exception e) {

            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "service destory");
    }
}
