package com.library.data;

/**
 * Created by sam on 2018/1/31.
 */

public class HttpErrorException extends RuntimeException {

    private int intCode = -1;//默认无Code
    private String code;
    private String message;
    private String ecode;

    public HttpErrorException(String code, String detailMessage,String ecode) {
        super(detailMessage);
        this.code = code;
        this.message = detailMessage;
        this.ecode=ecode;
    }
    public HttpErrorException(String code, String detailMessage,String ecode, Object obj) {
        super(detailMessage);
        this.code = code;
        this.message = detailMessage;
        this.ecode=ecode;
    }
    public HttpErrorException(String code, String detailMessage) {
        super(detailMessage);
        this.code = code;
        this.message = detailMessage;

    }
    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCodeInt() {
        if (intCode == -1) {
            try {
                intCode = Integer.parseInt(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return intCode;
    }
}
