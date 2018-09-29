package com.techidea.library.refreshloadmore;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.techidea.library.utils.ScrollingUtil;

/**
 * Created by zc on 2017/9/17.
 */

public class OverScrollDecorator extends Decorator {

    //监测Fling的动作，实现越界回弹
    private float mVelocityY;

    //越界最低速度
    private static final int OVER_SCROLL_MIN_VX = 3000;

    //针对View的延时策略
    private static final int MSG_START_COMPUTE_SCROLL = 0; //开始计算
    private static final int MSG_CONTINUE_COMPUTE_SCROLL = 1;//继续计算
    private static final int MSG_STOP_COMPUTE_SCROLL = 2; //停止计算

    private boolean preventTopOverScroll = false;
    private boolean preventBottomOverScroll = false;
    private boolean checkOverScroll = false;

    private int cur_delay_times = 0; //当前计算次数
    private static final int ALL_DELAY_TIMES = 60;  //10ms计算一次,总共计算20次

    public OverScrollDecorator(RefreshLoadMoreLayout.CoContext cp, IDecorator decorator) {
        super(cp, decorator);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return decorator != null && decorator.dispatchTouchEvent(ev);
    }

    @Override
    public boolean interceptTouchEvent(MotionEvent ev) {
        return decorator != null && decorator.interceptTouchEvent(ev);
    }

    @Override
    public boolean dealTouchEvent(MotionEvent ev) {
        return decorator != null && decorator.dealTouchEvent(ev);
    }

    @Override
    public void onFingerDown(MotionEvent ev) {
        if (decorator != null) decorator.onFingerDown(ev);
        preventTopOverScroll = ScrollingUtil.isViewToTop(cp.getTargetView(), cp.getTouchSlop());
        preventBottomOverScroll = ScrollingUtil.isViewToBottom(cp.getTargetView(), cp.getTouchSlop());
    }

    @Override
    public void onFingerUp(MotionEvent ev, boolean isFling) {
        if (decorator != null) {
            decorator.onFingerUp(ev, checkOverScroll && isFling);
        }
        checkOverScroll = false;
    }

    @Override
    public void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY) {
        if (decorator != null) {
            decorator.onFingerScroll(e1, e2, distanceX, distanceY, velocityX, velocityY);
        }
    }

    @Override
    public void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (decorator != null) decorator.onFingerFling(e1, e2, velocityX, velocityY);
        if (!cp.enableOverScroll()) return;
        int dy = (int) (e2.getY() - e1.getY());
        if (dy < -cp.getTouchSlop() && preventBottomOverScroll) return;
        if (dy > cp.getTouchSlop() && preventTopOverScroll) return;
        mVelocityY = velocityY;
        if (Math.abs(mVelocityY) >= OVER_SCROLL_MIN_VX) {
            mHandler.sendEmptyMessage(MSG_START_COMPUTE_SCROLL);
            checkOverScroll = true;
        } else {
            mVelocityY = 0;
            cur_delay_times = ALL_DELAY_TIMES;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mTouchSlop = cp.getTouchSlop();
            switch (msg.what) {
                case MSG_START_COMPUTE_SCROLL:
                    cur_delay_times = -1;
                case MSG_CONTINUE_COMPUTE_SCROLL:
                    cur_delay_times++;
                    View mChildView = cp.getTargetView();
                    if (cp.allowOverScroll()) {
                        if (mVelocityY >= OVER_SCROLL_MIN_VX) {
                            if (ScrollingUtil.isViewToTop(mChildView, mTouchSlop)) {
                                cp.getAnimProcessor().animOverScrollTop(mVelocityY, cur_delay_times);
                                mVelocityY = 0;
                                cur_delay_times = ALL_DELAY_TIMES;
                            }
                        } else if (mVelocityY <= -OVER_SCROLL_MIN_VX) {
                            if (ScrollingUtil.isViewToBottom(mChildView, mTouchSlop)) {
                                cp.getAnimProcessor().animOverScrollBottom(mVelocityY, cur_delay_times);
                                mVelocityY = 0;
                                cur_delay_times = ALL_DELAY_TIMES;
                            }
                        }
                    }
                    if (cur_delay_times < ALL_DELAY_TIMES) {
                        mHandler.sendEmptyMessageDelayed(MSG_CONTINUE_COMPUTE_SCROLL, 10);
                    }
                    break;
                case MSG_STOP_COMPUTE_SCROLL:
                    cur_delay_times = ALL_DELAY_TIMES;
                    break;
            }
        }
    };
}
