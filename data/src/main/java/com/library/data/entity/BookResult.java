package com.library.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sam on 2018/1/31.
 */

public class BookResult implements Serializable {

    private String count;
    private String start;
    private String total;
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
