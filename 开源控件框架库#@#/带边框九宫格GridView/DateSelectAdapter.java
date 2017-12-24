package com.ddtx.kexiansen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.base.BaseMyAdapter;
import com.ddtx.kexiansen.model.DateSelectBean;
import com.ddtx.kexiansen.widget.NoScrollGridView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */

public class DateSelectAdapter extends BaseMyAdapter<DateSelectBean> {
    //用于判断是否选中
    private int checkedIndex;
    private NoScrollGridView selectGrid;


    public DateSelectAdapter(Context mContext, List mList , NoScrollGridView selectGrid) {
        super(mContext, mList);
        this.selectGrid = selectGrid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.layout_dateselect_item,null);
        }
        TextView monthAndDayTv = (TextView) convertView.findViewById(R.id.tv_moth_and_day);
        TextView weekTv = (TextView) convertView.findViewById(R.id.tv_week);
        TextView timeToTimeTv = (TextView) convertView.findViewById(R.id.tv_time_to_time);

        monthAndDayTv.setText(mList.get(position).getMonth()+"-"+mList.get(position).getDay());
        int week = mList.get(position).getWeek();
        String weekNum = null;
        if(position == 0){
            weekTv.setText("[今天]");
        }else{
            switch (week){
                case 1:
                    weekNum ="一";
                    break;
                case 2:
                    weekNum ="二";
                    break;
                case 3:
                    weekNum ="三";
                    break;
                case 4:
                    weekNum ="四";
                    break;
                case 5:
                    weekNum ="五";
                    break;
                case 6:
                    weekNum ="六";
                    break;
                case 0:
                    weekNum ="日";
                    break;
            }
            weekTv.setText("[周"+weekNum+"]");
        }

        if(checkedIndex == position ){
            weekTv.setTextColor(Color.WHITE);
            timeToTimeTv.setTextColor(Color.WHITE);
            monthAndDayTv.setTextColor(Color.WHITE);
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color6abd3b));
        }else{
            weekTv.setTextColor(mContext.getResources().getColor(R.color.color848689));
            timeToTimeTv.setTextColor(mContext.getResources().getColor(R.color.color6abd3b));
            monthAndDayTv.setTextColor(mContext.getResources().getColor(R.color.color333333));
            convertView.setBackgroundColor(Color.WHITE);
        }

        if(week == 6 || week == 0){
            timeToTimeTv.setText("10:00-17:00 送达");
            if(position ==0 && mList.get(0).getHour() > 17){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorE1E1E3));
                //设置不能点击
                convertView.setClickable(true);
                timeToTimeTv.setTextColor(mContext.getResources().getColor(R.color.color848689));
                //设置不绘制GridView第一个格子的边框
                selectGrid.setDraw(true);
            }
        }else{
            timeToTimeTv.setText("9:00-18:00 送达");
            if(position ==0 && mList.get(0).getHour() > 18){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorE1E1E3));
                //设置不能点击
                convertView.setClickable(true);
                timeToTimeTv.setTextColor(mContext.getResources().getColor(R.color.color848689));
                //设置不绘制GridView第一个格子的边框
                selectGrid.setDraw(true);
            }
        }


        return convertView;
    }

    public int getCheckedIndex() {
        return checkedIndex;
    }

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
        notifyDataSetChanged();
    }
}
