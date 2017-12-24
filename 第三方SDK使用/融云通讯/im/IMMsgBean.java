package com.bjesc.app.rlimtest.im;

/**
 * Created by Jason Chen on 2017/8/25.
 */

public class IMMsgBean {
    public static int TYPE = 1;
    private String text;    //本文类型
    private String lat;     // 纬度信息
    private String lon;     // 经度信息

    public IMMsgBean(String text, String lat, String lon) {
        this.text = text;
        this.lat = lat;
        this.lon = lon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
