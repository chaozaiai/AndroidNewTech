package com.library.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sam on 2018/6/21.
 */

public class QueryResponse<T> implements Serializable {

    private int numFound;
    private int start;
    private double maxScore;
    private List<T> docs;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public List<T> getDocs() {
        return docs;
    }

    public void setDocs(List<T> docs) {
        this.docs = docs;
    }
}
