package com.rxretrofit_build.model;

/**
 * Title:
 * Description:网络返回基类 支持泛型
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/28  09:54
 *
 * @author cree
 * @version 1.0
 */
public class BaseResponse<T> {

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return code == 0;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
