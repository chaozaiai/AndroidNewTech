package com.techidea.library.refreshloadmore;

/**
 * Created by zc on 2017/9/17.
 */

public abstract class Decorator implements IDecorator{

    protected RefreshLoadMoreLayout.CoContext cp;
    protected IDecorator decorator;

    public Decorator(RefreshLoadMoreLayout.CoContext cp, IDecorator decorator) {
        this.cp = cp;
        this.decorator = decorator;
    }
}
