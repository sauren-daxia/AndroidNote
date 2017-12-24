package com.chenzj.baselibrary.base.views;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Jason Chen on 2017/8/4.
 */

public class MultiAdapter extends MultiItemTypeAdapter {
    public MultiAdapter(Context context, List datas) {
        super(context, datas);
    }

}
