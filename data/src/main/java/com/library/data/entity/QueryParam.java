package com.library.data.entity;

import java.io.Serializable;

/**
 * Created by sam on 2018/6/21.
 */

public class QueryParam implements Serializable {

    private String sort;
    private String q;
    private String fq;
    private String wt;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFq() {
        return fq;
    }

    public void setFq(String fq) {
        this.fq = fq;
    }

    public String getWt() {
        return wt;
    }

    public void setWt(String wt) {
        this.wt = wt;
    }
}
