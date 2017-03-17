package com.nick.safecloud.api;



/**
 * Created by Sparrow on 2017/2/22.
 */

public class ApiException extends RuntimeException {

    public static final int NEED_VERIFY_CODE = 0;

    private int code;
    private String message;

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
