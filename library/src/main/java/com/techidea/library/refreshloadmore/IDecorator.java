package com.techidea.library.refreshloadmore;

import android.view.MotionEvent;

/**
 * Created by zc on 2017/9/15.
 */

public interface IDecorator {

    boolean dispatchTouchEvent(MotionEvent ev);

    boolean interceptTouchEvent(MotionEvent ev);

    boolean dealTouchEvent(MotionEvent ev);

    void onFingerDown(MotionEvent ev);

    void onFingerUp(MotionEvent ev, boolean isFling);

    void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY);

    void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
}
