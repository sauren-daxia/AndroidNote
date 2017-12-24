package com.ddtx.kexiansen.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.model.OrderListBean;
import com.ddtx.kexiansen.util.MetricsUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 陈志坚 on 2016/10/31.
 */

public class EvaluateAdapter extends BaseAdapter {
    private static final String TAG = "EvaluateAdapter";
    private LayoutInflater inflater;
    private Context mContext;
    private List<String> imgUrls;
    private List<EvaluatePhotoAdapter> photoAdapters;
    private EvaluatePhotoAdapter photoAdapter;
    private LinearLayout window;
    private OrderListBean orderListBean;
    private HashMap<String, EditText> remarkMap;           //留言集合
    private HashMap<String, CheckBox> anonymityMap;            //匿名集合
    private HashMap<String, RatingBar> goodsScoreMap;            //评分集合
    private HashMap<String, List<String>> photoMap;                  //图片集合

    public EvaluateAdapter(Context mContext, LinearLayout window, OrderListBean orderListBean) {
        this.orderListBean = orderListBean;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.window = window;
        remarkMap = new HashMap<>();
        anonymityMap = new HashMap<>();
        goodsScoreMap = new HashMap<>();
        photoMap = new HashMap<>();
        for (int i = 0; i <orderListBean.getGoods().size() ; i++) {
            photoMap.put(orderListBean.getGoods().get(i).getGoods_id(),new ArrayList<String>());
        }
    }

    @Override
    public int getCount() {
        return orderListBean.getGoods().size();
    }

    @Override
    public Object getItem(int position) {
        return orderListBean.getGoods().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_evaluate_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.goodsIv.setImageURI(orderListBean.getGoods().get(position).getGoods_cover());         //商品图片
        holder.titleTv.setText(orderListBean.getGoods().get(position).getGoods_title());             //商品标题
        String attrs = "";
        if (orderListBean.getGoods().get(position).getGoods_attrs() != null && orderListBean.getGoods().get(position).getGoods_attrs().size() > 0) {
            for (int i = 0; i < orderListBean.getGoods().get(position).getGoods_attrs().size(); i++) {
                if (i == orderListBean.getGoods().get(position).getGoods_attrs().size() - 1) {
                    attrs = attrs + (orderListBean.getGoods().get(position).getGoods_attrs().get(i).getAttrs_name() + "：" + orderListBean.getGoods().get(position).getGoods_attrs().get(i).getAttrs_value());
                } else {
                    attrs = attrs + (orderListBean.getGoods().get(position).getGoods_attrs().get(i).getAttrs_name() + "：" + orderListBean.getGoods().get(position).getGoods_attrs().get(i).getAttrs_value()) + "\t";
                }
            }
        }
        holder.standardTv.setText(attrs);          //商品属性
        holder.evaluateEt.getText().toString(); //商品评价
        holder.evaluateRb.getRating();        //商品评分
        holder.anonymousCb.isChecked();         //是否匿名
        List<String> photoList = photoMap.get(orderListBean.getGoods().get(position).getGoods_id());
        if (photoList.size() == 0) {
            photoList.add("drawable://"+R.drawable.ic_add130);
        }

        if(holder.photoRecycler.getTag() == null){
            holder.photoRecycler.setTag(position);
            EvaluatePhotoAdapter photoAdapter = new EvaluatePhotoAdapter(mContext, photoList, window, position);
            photoAdapter.setOnNotifyListListener(new EvaluatePhotoAdapter.OnNotifyListListener() {
                @Override
                public void notifyList(int photoPosition, List<String> list) {
                    photoMap.put(orderListBean.getGoods().get(photoPosition).getGoods_id(), list);
                }
            });
            holder.photoRecycler.addItemDecoration(new SpaceItemDecoration(40));
            holder.photoRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            holder.photoRecycler.setAdapter(photoAdapter);   //晒图
        }


        if (!remarkMap.containsKey(orderListBean.getGoods().get(position).getGoods_id())) {
            remarkMap.put(orderListBean.getGoods().get(position).getGoods_id(), holder.evaluateEt);
            anonymityMap.put(orderListBean.getGoods().get(position).getGoods_id(), holder.anonymousCb);
            goodsScoreMap.put(orderListBean.getGoods().get(position).getGoods_id(), holder.evaluateRb);
        }


        return convertView;
    }
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildPosition(view) != 0)
                outRect.left = -MetricsUtil.dip2px(mContext,16);
        }
    }

    public HashMap<String, EditText> getRemarkMap() {
        return this.remarkMap;
    }

    public HashMap<String, CheckBox> getAnonymityMap() {
        return this.anonymityMap;
    }

    public HashMap<String, RatingBar> getGoodsScoreMap() {
        return goodsScoreMap;
    }

    public HashMap<String, List<String>> getPhotoMap() {
        return this.photoMap;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_goods_pic)
        SimpleDraweeView goodsIv;
        @InjectView(R.id.tv_goods_title)
        TextView titleTv;
        @InjectView(R.id.tv_goods_standard)
        TextView standardTv;
        @InjectView(R.id.edit_evaluate)
        EditText evaluateEt;
        @InjectView(R.id.rb_goods_evaluate)
        RatingBar evaluateRb;
        @InjectView(R.id.recycler_photo)
        RecyclerView photoRecycler;
        @InjectView(R.id.cb_anonymous)
        CheckBox anonymousCb;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
