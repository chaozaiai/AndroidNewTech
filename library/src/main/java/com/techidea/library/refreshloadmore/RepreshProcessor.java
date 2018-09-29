package com.techidea.library.refreshloadmore;

import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.techidea.library.utils.ScrollingUtil;

/**
 * Created by zc on 2017/9/15.
 */

public class RepreshProcessor implements IDecorator {

    protected RefreshLoadMoreLayout.CoContext cp;

    private float mTouchX, mTouchY;
    private boolean intercepted = false;
    private boolean willAnimHead = false;
    private boolean willAnimBottom = false;
    private boolean downEventSent = false;

    private MotionEvent mLastMoveEvent;

    public RepreshProcessor(RefreshLoadMoreLayout.CoContext cp) {
        if (cp == null) {
            throw new NullPointerException("cp param can not be null");
        }
        this.cp = cp;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downEventSent = false;
                intercepted = false;
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                if (cp.isEnableKeepIView()) {
                    if (!cp.isRefreshing()) {
                        cp.setPrepareFinishRefresh(false);
                    }
                    if (!cp.isLoadingMore()) {
                        cp.setPrepareFinishLoadMore(false);
                    }
                }
                cp.dispatchTouchEventSuper(ev);
                return false;
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = ev;
                float dx = ev.getX() - mTouchX;
                float dy = ev.getY() - mTouchY;
                if (!intercepted && Math.abs(dx) <= Math.abs(dy) && Math.abs(dy) > cp.getTouchSlop()) {
                    if (dy > 0 && ScrollingUtil.isViewToTop(cp.getTargetView(), cp.getTouchSlop()) && cp.allowPullDown()) {
                        cp.setStatePTD();
                        mTouchX = ev.getX();
                        mTouchY = ev.getY();
                        sendCancelEvent();
                        intercepted = true;
                        return true;
                    } else if (dy < 0 && ScrollingUtil.isViewToBottom(cp.getTargetView(), cp.getTouchSlop()) && cp.allowPullUp()) {
                        cp.setStatePBU();
                        mTouchX = ev.getX();
                        mTouchY = ev.getY();
                        intercepted = true;
                        intercepted = true;
                        sendCancelEvent();
                        return true;
                    }
                }
                if (intercepted) {
                    if (cp.isRefreshVisible() || cp.isLoadingVisible()) {
                        return cp.dispatchTouchEventSuper(ev);
                    }
                    if (!cp.isPrepareFinishRefresh() && cp.isStatePTD()) {
                        if (dy < -cp.getTouchSlop() || !ScrollingUtil.isViewToTop(cp.getTargetView(), cp.getTouchSlop())) {
                            cp.dispatchTouchEventSuper(ev);
                        }
                        dy = Math.min(cp.getMaxHeadHeight() * 2, dy);
                        dy = Math.max(0, dy);
                        cp.getAnimProcessor().scrollHeadByMove(dy);
                    } else if (!cp.isPrepareFinishLoadMore() && cp.isStatePBU()) {
                        if (dy > cp.getTouchSlop() || !ScrollingUtil.isViewToBottom(cp.getTargetView(), cp.getTouchSlop())) {
                            cp.dispatchTouchEventSuper(ev);
                        }
                        dy = Math.max(-cp.getMaxBottomHeight() * 2, dy);
                        dy = Math.min(0, dy);
                        cp.getAnimProcessor().scrollBottomByMove(Math.abs(dy));
                    }
                    if (dy == 0 && !downEventSent) {
                        downEventSent = true;
                        sendDownEvent();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (intercepted) {
                    if (cp.isStatePTD()) {
                        willAnimHead = true;
                    } else if (cp.isStatePBU()) {
                        willAnimBottom = true;
                    }
                    intercepted = false;
                    return true;
                }
                break;
        }
        return cp.dispatchTouchEventSuper(ev);
    }

    @Override
    public boolean interceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean dealTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void onFingerDown(MotionEvent ev) {

    }

    @Override
    public void onFingerUp(MotionEvent ev, boolean isFling) {
        if (!isFling && willAnimHead) {
            cp.getAnimProcessor().dealPullDownRelease();
        }
        if (!isFling && willAnimBottom) {
            cp.getAnimProcessor().dealPullUpRelease();
        }
        willAnimBottom = false;
        willAnimHead = false;
    }

    @Override
    public void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY) {
        int mTouchSlop = cp.getTouchSlop();
        if (cp.isRefreshVisible() && distanceY >= mTouchSlop && !cp.isOpenFloatRefresh()) {
            cp.getAnimProcessor().animHeadHideByVy((int) velocityY);
        }
        if (cp.isLoadingVisible() && distanceY <= -mTouchSlop) {
            cp.getAnimProcessor().animBottomHideByVy((int) velocityY);
        }
    }

    @Override
    public void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

    }

    private void sendCancelEvent() {
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(
                last.getDownTime(),
                last.getEventTime() + ViewConfiguration.getLongPressTimeout(),
                MotionEvent.ACTION_CANCEL,
                last.getX(),
                last.getY(),
                last.getMetaState()
        );
        cp.dispatchTouchEventSuper(e);
    }

    private void sendDownEvent() {
        final MotionEvent last = mLastMoveEvent;
        MotionEvent event = MotionEvent.obtain(
                last.getDownTime(),
                last.getEventTime(),
                MotionEvent.ACTION_DOWN,
                last.getX(),
                last.getY(),
                last.getMetaState()
        );
        cp.dispatchTouchEventSuper(event);
    }
}
