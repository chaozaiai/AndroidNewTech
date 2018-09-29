package com.library.data;

/**
 * Created by sam on 2018/1/31.
 */

public class ApiException extends Exception{

    private int code;
    private String message;
    private String ecode;
    public ApiException(int code, String detailMessage,String ecode) {
        this.code = code;
        this.message = detailMessage;
        this.ecode=ecode;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
