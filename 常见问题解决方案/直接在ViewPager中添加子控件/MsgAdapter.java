package com.weiyin.flyman.adapter;

import android.content.Context;

import com.weiyin.flyman.R;
import com.weiyin.flyman.bean.MsgBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Jason Chen on 2017/8/29.
 */

public class MsgAdapter extends CommonAdapter<MsgBean> {

    public MsgAdapter(Context context, int layoutId, List<MsgBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MsgBean msgBean, int position) {
        holder.setText(R.id.tv_time,"2017-8-29 14:00");
        holder.setText(R.id.tv_msg,"尊敬的用户xxxxxxxxxxxx = "+position);
    }
}
