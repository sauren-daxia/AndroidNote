package com.rxretrofit_build.model;

/**
 * Created by Jason Chen on 2017/2/18.
 */
public class RxBusBean {
    private String requestMark;
    private Object o;

    public RxBusBean(String requestMark, Object o) {
        this.requestMark = requestMark;
        this.o = o;
    }

    public String getRequestMark() {
        return requestMark;
    }

    public void setRequestMark(String requestMark) {
        this.requestMark = requestMark;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
