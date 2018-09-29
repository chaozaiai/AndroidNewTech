package com.techidea.library.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.techidea.library.R;
import com.techidea.library.utils.OsUtil;
import com.techidea.library.utils.StatusBarUtil;

/**
 * Created by sam on 2017/10/7.
 */

public class BaseActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private View viewStatusBar;
    private int colorStatusBar = 0xFFD1D5D8; //背景为白色时，因为只有6.0以上才能设置statusbar 字体为黑色，所以5.0 背景为灰色

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_statusbar);
        frameLayout = (FrameLayout) findViewById(R.id.base_frame_layout_content);
        viewStatusBar = (View) findViewById(R.id.base_view_status_bar);
        ViewGroup.LayoutParams params = viewStatusBar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(this);
        viewStatusBar.setLayoutParams(params);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        frameLayout.addView(contentView);
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    protected void setImmersiveStatusBar(boolean fontIconDark, int statusBarPlaceColor) {
        StatusBarUtil.setTranslucentStatus(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                || OsUtil.isMIUI()
                || OsUtil.isFlyme()) {
            StatusBarUtil.setStatusBarFontIconDark(this, fontIconDark);
        }
        if (statusBarPlaceColor == Color.WHITE
                && (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            statusBarPlaceColor = colorStatusBar;
        }
        setStatusBarColor(statusBarPlaceColor);
    }

    private void setStatusBarColor(int statusColor) {
        if (viewStatusBar != null) {
            viewStatusBar.setBackgroundColor(statusColor);
        }
    }
}
