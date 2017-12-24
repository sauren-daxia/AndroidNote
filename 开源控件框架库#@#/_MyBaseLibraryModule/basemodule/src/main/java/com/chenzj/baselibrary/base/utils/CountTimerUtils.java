package com.chenzj.baselibrary.base.utils;

import android.os.CountDownTimer;

/**
 * Created by 陈志坚 on 2016/11/22.
 */

public class CountTimerUtils {
    private CountDownTimer timer;        //倒计时类
    private int day;
    private int hour;
    private int minute;
    private int second;

    public void startTimer(final long CountTimer, long d, OnCountTimeListener timeListener) {
        //判断是否创建过
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        this.timeListener = timeListener;

        //第一个参数是总值，第二个参数是间隔多少秒计算一次
        timer = new CountDownTimer(CountTimer+100, d - 500) {

            //每间隔一段时间都会在这个方法刷新一次数据
            @Override
            public void onTick(long arg0) {
                if (CountTimerUtils.this.timeListener != null) {
                    Integer ss = 1000;
                    Integer mi = ss * 60;
                    Integer hh = mi * 60;
                    Integer dd = hh * 24;

                    day = (int) (arg0 / dd);
                    hour = (int) ((arg0 - day * dd) / hh);
                    minute = (int) ((arg0 - day * dd - hour * hh) / mi);
                    second = (int) ((arg0 - day * dd - hour * hh - minute * mi) / ss);
                    CountTimerUtils.this.timeListener.onTime(day, hour, minute, second);
                }
            }

            //倒计时结束会调用该方法
            @Override
            public void onFinish() {
                if(CountTimerUtils.this.timeListener!= null){
                    CountTimerUtils.this.timeListener.onTime(-1 , -1, -1, -1);
                }
            }
        };
        //启动倒计时
        timer.start();
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public interface OnCountTimeListener {
        void onTime(int day, int hour, int minute, int second);
    }
    private OnCountTimeListener timeListener;

}