package com.library.data.entity;


import java.io.Serializable;

/**
 * Created by sam on 2018/6/21.
 */

public class ReponseHeader implements Serializable {

    private int status;
    private int QTime;
    private QueryParam params;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQTime() {
        return QTime;
    }

    public void setQTime(int QTime) {
        this.QTime = QTime;
    }

    public QueryParam getParams() {
        return params;
    }

    public void setParams(QueryParam params) {
        this.params = params;
    }
}
