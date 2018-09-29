package com.techidea.library.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by sam.zhang on 2017/8/11.
 */

public class GradationScrollView extends NestedScrollView {

    private static final String TAG = GradationScrollView.class.getSimpleName();

    public interface ScrollViewListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public interface ScrollListener {
        void onScrollToBottom();

        void onScrollToTop();

        void onScroll(int scrollY);

        void notBottom();
    }

    private ScrollViewListener scrollViewListener;

    private ScrollListener mScrollListener;

    public GradationScrollView(Context context) {
        super(context);
    }

    public GradationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:

                if (mScrollListener != null) {
                    int contentHeight = getChildAt(0).getHeight();
                    int scrollHeight = getHeight();
                    Log.d(TAG, "scrollY:" + getScrollY() + "contentHeight:" + contentHeight + " scrollHeight" + scrollHeight + "object:" + this);

                    int scrollY = getScrollY();
                    mScrollListener.onScroll(scrollY);

                    if (scrollY + scrollHeight >= contentHeight || contentHeight <= scrollHeight) {
                        mScrollListener.onScrollToBottom();
                    } else {
                        mScrollListener.notBottom();
                    }

                    if (scrollY == 0) {
                        mScrollListener.onScrollToTop();
                    }

                }

                break;
        }
        boolean result = super.onTouchEvent(ev);
        requestDisallowInterceptTouchEvent(false);

        return result;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

}
