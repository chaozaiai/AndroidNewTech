package com.techidea.library.refreshloadmore.bottomview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.techidea.library.R;
import com.techidea.library.refreshloadmore.IBottomView;

/**
 * Created by zc on 2017/9/14.
 */

public class DefaultBottomView extends FrameLayout implements IBottomView {

    private ImageView loadArrow;
    private ImageView loadingView;
    private TextView loadTextView;

    private String pullUpStr = "释放立即加载";
    private String releaseLoadStr = "加载完成";
    private String loadingStr = "正在加载……";

    public DefaultBottomView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultBottomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.view_default_bottom, null);
        loadArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        loadTextView = (TextView) rootView.findViewById(R.id.tv);
        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {
        if (fraction < 1f) {
            loadTextView.setText(pullUpStr);
        }
        if (fraction > 1f) loadTextView.setText(releaseLoadStr);
        loadArrow.setRotation(fraction * bottomHeight / maxBottomHeight * 180);
    }

    @Override
    public void startAnim() {

    }

    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {
        if (fraction < 1f) {
            loadTextView.setText(pullUpStr);
            loadArrow.setRotation(fraction * bottomHeight / maxBottomHeight * 180);
            if (loadArrow.getVisibility() == GONE) {
                loadArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {
        loadArrow.setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        loadTextView.setText(pullUpStr);
    }
}
