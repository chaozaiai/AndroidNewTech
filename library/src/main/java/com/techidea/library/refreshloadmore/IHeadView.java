package com.techidea.library.refreshloadmore;

import android.view.View;

/**
 * Created by zc on 2017/9/14.
 */

public interface IHeadView {
    View getView();

    void onPullingDown(float fraction, float maxHeadHeight, float headHeight);

    void onPullReleasing(float fraction, float maxHeadHeight, float headHeight);

    void startAnim();

    void onFinish();

    void reset();
}
