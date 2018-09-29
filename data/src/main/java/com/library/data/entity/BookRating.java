package com.library.data.entity;

import java.io.Serializable;

/**
 * Created by sam on 2018/1/31.
 */

public class BookRating implements Serializable {
//    "rating":{"max":10,"numRaters":9438,"average":"9.1","min":0}
    private String max;
    private String numRaters;
    private String average;
    private String min;

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getNumRaters() {
        return numRaters;
    }

    public void setNumRaters(String numRaters) {
        this.numRaters = numRaters;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
