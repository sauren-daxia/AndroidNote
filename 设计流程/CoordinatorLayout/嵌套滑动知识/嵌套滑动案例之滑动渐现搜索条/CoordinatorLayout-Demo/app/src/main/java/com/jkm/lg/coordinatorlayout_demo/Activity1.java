package com.jkm.lg.coordinatorlayout_demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ligang on 2017/8/21.
 */

public class Activity1 extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        getSupportFragmentManager().beginTransaction().add(R.id.container,new Fragment1(),"fragment1").commit();
    }
}
