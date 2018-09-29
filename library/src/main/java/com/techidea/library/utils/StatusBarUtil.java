package com.techidea.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by sam on 2017/10/7.
 */

public class StatusBarUtil {

    /**
     * 设置状态栏透明
     */
    public static void setTranslucentStatus(Activity activity) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            setSystemUiVisibility(int visibility)传入的实参类型如下：
//            1.View.SYSTEM_UI_FLAG_VISIBLE ：状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
//            2.View.SYSTEM_UI_FLAG_FULLSCREEN ：Activity全屏显示，且状态栏被覆盖掉
//            3. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ：Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布   局会被覆盖住
//            4.View.INVISIBLE ： Activity全屏显示，隐藏状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    //设置全屏
    public static void setFullToStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    activity.getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    public static void setStatusBarFontIconDark(Activity activity, boolean dark) {
        // 小米MIUI
        try {
            Window window = activity.getWindow();
            Class clazz = activity.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) { //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else { //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 魅族FlymeUI
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                activity.getWindow().getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }


    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //在4.4版本以上才设置为statusbar的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }
}
