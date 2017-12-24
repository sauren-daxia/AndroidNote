package com.chenzj.baselibrary.base.bean;

/**
 * Created by hp on 2017/5/22.
 */
public class BaseBean<T> {
    protected int code;
    protected String message;
    protected T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
