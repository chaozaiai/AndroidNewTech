package com.techidea.library.refreshloadmore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.techidea.library.utils.LogUtil;
import com.techidea.library.utils.ScrollingUtil;

/**
 * Created by zc on 2017/9/15.
 */

public class AnimaProcess implements IAnimRefresh, IAnimOverRehresh {

    private static final float animFraction = 1f;
    private RefreshLoadMoreLayout.CoContext cp;
    //动画变化率
    private DecelerateInterpolator decelerateInterpolator;

    public AnimaProcess(RefreshLoadMoreLayout.CoContext coContext) {
        this.cp = coContext;
        this.decelerateInterpolator = new DecelerateInterpolator(8);
    }

    private boolean scrollHeadLocked = false;
    private boolean scrollBottomLocked = false;
    private boolean isAnimOsTop = false;
    private boolean isOverScrollTopLocked = false;
    private boolean isAnimHeadToRefresh = false;
    private boolean isAnimHeadBack = false;
    private boolean isAnimHeadHide = false;

    private boolean isAnimOsBottom = false;
    private boolean isOverScrollBottomLocked = false;
    private boolean isAnimBottomToLoad = false;
    private boolean isAnimBottomBack = false;
    private boolean isAnimBottomHide = false;

    @Override
    public void scrollHeadByMove(float moveY) {
        float offsetY = decelerateInterpolator
                .getInterpolation(moveY / cp.getMaxHeadHeight() / 2) * moveY / 2;
        //不能滑动，不显示head
        if (cp.isPureScrollModeOn() || (!cp.enableRefresh() && !cp.isOverScrollTopShow())) {
            if (cp.getHeader().getVisibility() != View.GONE) {
                cp.getHeader().setVisibility(View.GONE);
            }
        } else {
            if (cp.getHeader().getVisibility() != View.VISIBLE) {
                cp.getHeader().setVisibility(View.VISIBLE);
            }
        }
        //
        if (scrollHeadLocked && cp.isEnableKeepIView()) {
            cp.getHeader().setTranslationY(offsetY - cp.getHeader().getLayoutParams().height);
        } else {
            //重新设置头部高度
            cp.getHeader().setTranslationY(0);
            cp.getHeader().getLayoutParams().height = (int) Math.abs(offsetY);
            cp.getHeader().requestLayout();
            cp.onPullingDown(offsetY);
        }
        //设置目标View
        if (!cp.isOpenFloatRefresh()) {
            cp.getTargetView().setTranslationY(offsetY);

        }
    }

    //多设置的头部
    private void translateExHead(int offsetY) {
        if (!cp.isExHeadFixed()) {
            cp.getExHead().setTranslationY(offsetY);
        }
    }

    @Override
    public void scrollBottomByMove(float moveY) {
        float offsetY = decelerateInterpolator
                .getInterpolation(moveY / cp.getMaxHeadHeight() / 2) * moveY / 2;
        //不能滑动，不显示head
        if (cp.isPureScrollModeOn() || (!cp.enableLoadmore() && !cp.isOverScrollBottomShow())) {
            if (cp.getFooter().getVisibility() != View.GONE) {
                cp.getFooter().setVisibility(View.GONE);
            }
        } else {
            if (cp.getFooter().getVisibility() != View.VISIBLE) {
                cp.getFooter().setVisibility(View.VISIBLE);
            }
        }
        //
        if (scrollBottomLocked && cp.isEnableKeepIView()) {
            cp.getFooter().setTranslationY(cp.getHeader().getLayoutParams().height - offsetY);
        } else {
            //重新设置底部高度
            cp.getFooter().setTranslationY(0);
            cp.getFooter().getLayoutParams().height = (int) Math.abs(offsetY);
            cp.getFooter().requestLayout();
            cp.onPullingUp(-offsetY);
        }
        //目标view 向上滑动
        cp.getTargetView().setTranslationY(-offsetY);
    }

    //释放刷新
    public void dealPullDownRelease() {
        if (!cp.isPureScrollModeOn()
                && cp.enableRefresh()
                && getVisibleBottomHeight() >= cp.getHeadHeight() - cp.getTouchSlop()) {
            animHeadToRefresh();
        } else {
            animHeadBack(false);
        }
    }

    public void dealPullUpRelease() {
        if (!cp.isPureScrollModeOn()
                && cp.enableLoadmore()
                && getVisibleBottomHeight() >= cp.getBottomHeight() - cp.getTouchSlop()) {
            animBottomToLoad();
        } else {
            animBottomBack(false);
        }
    }

    private int getVisibleHeadHeight() {
        return (int) (cp.getHeader().getLayoutParams().height + cp.getHeader().getTranslationY());
    }

    private int getVisibleBottomHeight() {
        return (int) (cp.getFooter().getLayoutParams().height - cp.getFooter().getTranslationY());
    }

    private void transHeader(float offsetY) {
        cp.getHeader().setTranslationY(offsetY - cp.getHeader().getLayoutParams().height);
    }

    private void transBottom(float offsetY) {
        cp.getFooter().setTranslationY(cp.getFooter().getLayoutParams().height - offsetY);
    }


    public void animLayoutByTime(int start, int end,
                                 ValueAnimator.AnimatorUpdateListener updateListener,
                                 Animator.AnimatorListener listener) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(updateListener);
        va.addListener(listener);
        va.setDuration((int) (Math.abs(start - end) * animFraction));
        va.start();
    }

    public void animLayoutByTime(int start, int end, long time,
                                 ValueAnimator.AnimatorUpdateListener updateListener,
                                 Animator.AnimatorListener listener) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(updateListener);
        va.addListener(listener);
        va.setDuration(time);
        va.start();
    }

    private ValueAnimator.AnimatorUpdateListener animHeadUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (scrollHeadLocked && cp.isEnableKeepIView()) {
                transHeader(height);
            } else {
                cp.getHeader().getLayoutParams().height = height;
                cp.getHeader().requestLayout();
                cp.getHeader().setTranslationY(0);
                cp.onPullDownReleasing(height);
            }
            if (!cp.isOpenFloatRefresh()) {
                cp.getTargetView().setTranslationY(height);
                translateExHead(height);
            }
        }
    };

    private ValueAnimator.AnimatorUpdateListener animBottomUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (scrollBottomLocked && cp.isEnableKeepIView()) {
                transBottom(height);
            } else {
                cp.getFooter().getLayoutParams().height = height;
                cp.getFooter().requestLayout();
                cp.getFooter().setTranslationY(0);
                cp.onPullUpReleasing(height);
            }
            cp.getTargetView().setTranslationY(-height);
        }
    };


    private ValueAnimator.AnimatorUpdateListener overScrolllTopUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (cp.isOverScrollTopShow()) {
                if (cp.getHeader().getVisibility() != View.VISIBLE) {
                    cp.getHeader().setVisibility(View.VISIBLE);
                }
            } else {
                if (cp.getHeader().getVisibility() != View.GONE) {
                    cp.getHeader().setVisibility(View.GONE);
                }
            }
            if (scrollHeadLocked && cp.isEnableKeepIView()) {
                transHeader(height);
            } else {
                cp.getHeader().setTranslationY(0);
                cp.getHeader().getLayoutParams().height = height;
                cp.getHeader().requestLayout();
                cp.onPullDownReleasing(height);
            }
            cp.getTargetView().setTranslationY(height);
            translateExHead(height);
        }
    };

    private ValueAnimator.AnimatorUpdateListener overScrollBottomUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (cp.isOverScrollBottomShow()) {
                if (cp.getFooter().getVisibility() != View.VISIBLE) {
                    cp.getFooter().setVisibility(View.VISIBLE);
                }
            } else {
                if (cp.getFooter().getVisibility() != View.GONE) {
                    cp.getFooter().setVisibility(View.GONE);
                }
            }
            if (scrollBottomLocked && cp.isEnableKeepIView()) {
                transBottom(height);
            } else {
                cp.getFooter().getLayoutParams().height = height;
                cp.getFooter().requestLayout();
                cp.getFooter().setTranslationY(0);
                cp.onPullUpReleasing(height);
            }
            cp.getTargetView().setTranslationY(-height);
        }
    };

    /**
     * 顶部越界
     *
     * @param vy
     * @param computeTimes
     */
    @Override
    public void animOverScrollTop(float vy, int computeTimes) {
        Log.i("animOverScrollTop", "vy->" + vy + "computeTimes->" + computeTimes);
        if (isOverScrollTopLocked) return;
        isOverScrollTopLocked = true;
        isAnimOsTop = true;
        cp.setStatePTD();
        int oh = (int) Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > cp.getOsHeight() ? cp.getOsHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int) (0.3 * overHeight + 100);
        animLayoutByTime(getVisibleHeadHeight(), overHeight, time, overScrollBottomUpListener
                , new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (scrollHeadLocked && cp.isEnableKeepIView() && cp.showRefreshingWhenOverScroll()) {
                            animHeadToRefresh();
                            isAnimOsTop = false;
                            isOverScrollTopLocked = false;
                            return;
                        }
                        animLayoutByTime(overHeight, 0, 2 * time, overScrolllTopUpListener,
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        isAnimOsTop = false;
                                        isOverScrollTopLocked = false;
                                    }
                                });
                    }
                });
    }

    //底部越界
    @Override
    public void animOverScrollBottom(float vy, int computeTimes) {
        Log.i("animOverScrollBottom", "vy->" + vy + "computeTimes->" + computeTimes);
        if (isOverScrollBottomLocked) return;
        cp.setStatePBU();
        int oh = (int) Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > cp.getOsHeight() ? cp.getOsHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int) (0.3 * overHeight + 100);
        if (!isOverScrollBottomLocked && cp.autoLoadMore()) {
            cp.startLoadMore();
        } else {
            isOverScrollBottomLocked = true;
            isAnimOsBottom = true;
            animLayoutByTime(0, overHeight, time, overScrollBottomUpListener,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (scrollBottomLocked && cp.isEnableKeepIView() && cp.showLoadingWhenOverScroll()) {
                                animBottomToLoad();
                                isAnimOsBottom = false;
                                isOverScrollBottomLocked = false;
                                return;
                            }
                            animLayoutByTime(overHeight, 0, 2 * time, overScrollBottomUpListener,
                                    new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            isAnimOsBottom = false;
                                            isOverScrollBottomLocked = false;
                                        }
                                    });
                        }
                    });
        }
    }

    /**
     * 满足刷新条件，或者主动刷新
     */
    @Override
    public void animHeadToRefresh() {
        LogUtil.i("animHeadToRefresh");
        isAnimHeadToRefresh = true;
        animLayoutByTime(getVisibleHeadHeight(),
                cp.getHeadHeight(),
                animHeadUpListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadToRefresh = false;
                        if (cp.getHeader().getVisibility() != View.VISIBLE) {
                            cp.getHeader().setVisibility(View.VISIBLE);
                        }
                        cp.setRefreshVisible(true);
                        if (cp.isEnableKeepIView()) {
                            if (!scrollHeadLocked) {
                                cp.setRefreshing(true);
                                cp.onRefresh();
                                scrollHeadLocked = true;
                            }
                        } else {
                            cp.setRefreshing(true);
                            cp.onRefresh();
                        }
                    }
                }
        );
    }

    //动画结束或不满足进入刷新条件，收起头部
    @Override
    public void animHeadBack(final boolean isFinishRefresh) {
        LogUtil.i("animHeadBack");
        isAnimHeadBack = true;
        if (isFinishRefresh && scrollHeadLocked && cp.isEnableKeepIView()) {
            cp.setPrepareFinishRefresh(true);
        }
        animLayoutByTime(getVisibleHeadHeight(), 0, animHeadUpListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadBack = false;
                        cp.setRefreshVisible(false);
                        if (isFinishRefresh && scrollHeadLocked && cp.isEnableKeepIView()) {
                            cp.getHeader().getLayoutParams().height = 0;
                            cp.getHeader().requestLayout();
                            cp.getHeader().setTranslationY(0);
                            scrollHeadLocked = false;
                            cp.setRefreshing(false);
                            cp.resetHeaderView();
                        }
                    }
                });
    }


    //满足加载更多或者主动加载更多
    @Override
    public void animBottomToLoad() {
        LogUtil.i("animBottomToLoad");
        isAnimBottomToLoad = true;
        animLayoutByTime(getVisibleBottomHeight(), cp.getBottomHeight(), animBottomUpListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimBottomToLoad = false;
                        if (cp.getFooter().getVisibility() != View.VISIBLE) {
                            cp.getFooter().setVisibility(View.VISIBLE);
                        }
                        cp.setLoadVisible(true);
                        if (cp.isEnableKeepIView()) {
                            if (!scrollBottomLocked) {
                                cp.setLoadingMore(true);
                                cp.onLoadMore();
                                scrollBottomLocked = true;
                            }
                        } else {
                            cp.setLoadingMore(true);
                            cp.onLoadMore();
                        }
                    }
                });
    }

    //加载更多完成，或者不满足加载更多条件，收起底部
    @Override
    public void animBottomBack(boolean isFinishLoad) {
        LogUtil.i("animBottomBack->" + isFinishLoad);
        isAnimBottomBack = true;
        if (isFinishLoad && scrollBottomLocked && cp.isEnableKeepIView()) {
            cp.setPrepareFinishLoadMore(true);
        }
        animLayoutByTime(getVisibleBottomHeight(), 0, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                if (!ScrollingUtil.isViewToBottom(cp.getTargetView(), cp.getTouchSlop())) {
                    int dy = getVisibleBottomHeight() - height;
                    if (dy > 0) {
                        if (cp.getTargetView() instanceof RecyclerView) {
                            ScrollingUtil.scrollAViewBy(cp.getTargetView(), dy);
                        } else {
                            ScrollingUtil.scrollAViewBy(cp.getTargetView(), dy / 2);
                        }
                    }
                }
                animBottomUpListener.onAnimationUpdate(animation);
            }
        }, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBottomBack = false;
                cp.setLoadVisible(false);
                if (scrollBottomLocked && cp.isEnableKeepIView()) {
                    cp.getFooter().getLayoutParams().height = 0;
                    cp.getFooter().requestLayout();
                    cp.getFooter().setTranslationY(0);
                    scrollBottomLocked = false;
                    cp.resetBottomView();
                    cp.setLoadingMore(false);
                }
            }
        });
    }

    //当刷新时候，向上滑动屏幕,隐藏刷新头部
    @Override
    public void animHeadHideByVy(int vy) {
        if (isAnimHeadHide) return;
        isAnimHeadHide = true;
        LogUtil.i("animBottomHideByVy->" + vy);
        vy = Math.abs(vy);
        if (vy < 5000) vy = 8000;
        animLayoutByTime(getVisibleHeadHeight(),
                0,
                5 * Math.abs(getVisibleHeadHeight() * 1000 / vy),
                animHeadUpListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadHide = false;
                        cp.setRefreshVisible(false);
                        if (!cp.isEnableKeepIView()) {
                            cp.setRefreshing(false);
                            cp.onRefreshCanceled();
                            cp.resetHeaderView();
                        }
                    }
                });
    }

    //当加载时候，向下滑动，隐藏加载更多底部
    @Override
    public void animBottomHideByVy(int vy) {
        LogUtil.i("animBottomHideByVy->" + vy);
        if (isAnimBottomHide) return;
        isAnimBottomHide = true;
        vy = Math.abs(vy);
        if (vy < 5000) vy = 8000;
        animLayoutByTime(getVisibleBottomHeight(),
                0,
                5 * getVisibleBottomHeight() * 1000 / vy,
                animBottomUpListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimBottomHide = false;
                        cp.setLoadVisible(false);
                        if (!cp.isEnableKeepIView()) {
                            cp.setLoadingMore(false);
                            cp.onLoadmoreCanceled();
                            cp.resetBottomView();
                        }
                    }
                });
    }
}
