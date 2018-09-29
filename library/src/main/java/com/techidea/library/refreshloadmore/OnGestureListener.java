package com.techidea.library.refreshloadmore;

import android.view.MotionEvent;

/**
 * Created by zc on 2017/9/14.
 */

public interface OnGestureListener {

    void onDown(MotionEvent ev);

    void onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);

    void onUp(MotionEvent ev, boolean isFling);

    void onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY);
}
