package com.techidea.library.refreshloadmore;

/**
 * Created by zc on 2017/9/15.
 */

public interface IAnimRefresh {

    void scrollHeadByMove(float moveY);

    void scrollBottomByMove(float moveY);

    void animHeadToRefresh();

    void animHeadBack(boolean isFinishRefresh);

    void animHeadHideByVy(int vy);

    void animBottomToLoad();

    void animBottomBack(boolean isFinishLoad);

    void animBottomHideByVy(int vy);
}
