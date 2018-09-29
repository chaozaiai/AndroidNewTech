package cn.com.amway.content.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sam on 2018/9/28.
 */

public class NotificationBroadcastReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra("type", -1);

        if (type != -1) {

        }

        if (action.equals("notification_cancelled")) {
            //处理滑动清除和点击删除事件
            FileDownloadManager.getInstance(context.getApplicationContext()).unbind();
        }
    }
}
