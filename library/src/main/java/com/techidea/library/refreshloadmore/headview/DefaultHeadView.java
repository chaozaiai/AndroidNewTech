package com.techidea.library.refreshloadmore.headview;

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
import com.techidea.library.refreshloadmore.IHeadView;

/**
 * Created by zc on 2017/9/14.
 */

public class DefaultHeadView extends FrameLayout implements IHeadView {

    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;
    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    public DefaultHeadView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultHeadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultHeadView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.view_default_head, null);
        refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        refreshTextView = (TextView) rootView.findViewById(R.id.tv);
        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            refreshTextView.setText(pullDownStr);
        }
        if (fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            refreshTextView.setText(pullDownStr);
            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (refreshArrow.getVisibility() == GONE) {
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {
        refreshArrow.setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        refreshTextView.setText(pullDownStr);
    }
}
