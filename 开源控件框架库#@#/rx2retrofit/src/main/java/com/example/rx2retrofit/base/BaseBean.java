package com.example.rx2retrofit.base;

/**
 * Created by hp on 2017/5/22.
 */
public class BaseBean<T> {
    protected int code;
    protected String message;
    protected T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
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
