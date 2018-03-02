package com.ddtx.kexiansen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.http.HttpUrl;
import com.ddtx.kexiansen.http.IHttpResponseCallback;
import com.ddtx.kexiansen.http.OkHttp3Client;
import com.ddtx.kexiansen.model.DelGoodsBean;
import com.ddtx.kexiansen.model.ModifyGoods;
import com.ddtx.kexiansen.model.ShoppingCartBean;
import com.ddtx.kexiansen.model.ShoppingInfoBean;
import com.ddtx.kexiansen.util.ConstructorLabelView;
import com.ddtx.kexiansen.widget.FlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.RequestParams;


/**
 * Created by Suzy on 2016/8/11.
 * <p/>
 * 购物车列表
 */
public class ShoppingCartExpandAdapter extends BaseExpandableListAdapter implements AbsListView.OnScrollListener {
    //用于打log的
    private static final String TAG = "ShoppingCartExpandAdapt";
    private Context con;

    private List<ShoppingCartBean>  beanList;
    private LayoutInflater inflater;
    private ExpandableListView listView;
    private OkHttp3Client client;
    //记录每个商品的数量的TextView集合
    //用于记录选中的Item
    //用于保存选中的商品的二级对象

    public ShoppingCartExpandAdapter(Context con, List<ShoppingCartBean>  beanList, ExpandableListView listView) {
        this.con = con;
        this.beanList = beanList;
        inflater = LayoutInflater.from(con);
        this.listView = listView;
        client = new OkHttp3Client(con);
        this.listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }


    @Override
    public int getGroupCount() {
        return beanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return beanList.get(i).getGoods().size();
    }

    @Override
    public Object getGroup(int i) {
        return beanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return beanList.get(i).getGoods().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private static class GroupViewHolder {
        CheckBox cb;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.layout_cart_parent_item, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.iv_sel_parent);
            holder.cb.setOnCheckedChangeListener(groupListener);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }


        TextView merNameTv = (TextView) convertView.findViewById(R.id.tv_merchant_name);
        merNameTv.setText(beanList.get(groupPosition).getStore_name());
        holder.cb.setTag(groupPosition);

        if (groupIsCheck(groupPosition)) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setOnCheckedChangeListener(null);
            holder.cb.setChecked(false);
            holder.cb.setOnCheckedChangeListener(groupListener);
        }

        RelativeLayout selStoreRlty = (RelativeLayout) convertView.findViewById(R.id.rlty_sel_store);
        selStore(selStoreRlty, holder.cb);
        return convertView;
    }

    private boolean groupIsCheck(int groupPosition) {
        for (int i = 0; i < beanList.get(groupPosition).getGoods().size(); i++) {
            if (beanList.get(groupPosition).getGoods().get(i).getIsCheckde() == 0) {
                return false;
            }
        }
        return true;
    }

    private CompoundButton.OnCheckedChangeListener groupListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Integer groupPos = (Integer) buttonView.getTag();
            if (isChecked) {
                beanList.get(groupPos).setIsChecked(1);
                for (int i = 0; i < beanList.get(groupPos).getGoods().size(); i++) {
                    beanList.get(groupPos).getGoods().get(i).setIsCheckde(1);
                }
            } else {
                beanList.get(groupPos).setIsChecked(0);
                for (int i = 0; i < beanList.get(groupPos).getGoods().size(); i++) {
                    beanList.get(groupPos).getGoods().get(i).setIsCheckde(0);
                }
            }
            notifyDataSetChanged();
        }
    };

    private void selStore(RelativeLayout selStoreRlty, final CheckBox selCb) {
        selStoreRlty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selCb.isChecked()) {
                    selCb.setChecked(false);
                } else {
                    selCb.setChecked(true);
                }
            }
        });
    }

    private static class ChildViewHolder {
        TextView goodsNumTv;
        ImageView addIv, subIv;
        CheckBox cb;
        FlowLayout saleFlow;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ShoppingInfoBean bean = beanList.get(groupPosition).getGoods().get(childPosition);
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.layout_cart_child_item, null);
            holder.goodsNumTv = (TextView) convertView.findViewById(R.id.tv_num);
            holder.cb = (CheckBox) convertView.findViewById(R.id.iv_sel_cart);
            holder.addIv = (ImageView) convertView.findViewById(R.id.iv_add);
            holder.subIv = (ImageView) convertView.findViewById(R.id.iv_cut);
            holder.saleFlow = (FlowLayout) convertView.findViewById(R.id.flow_sale);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.saleFlow.removeAllViews();

        //流式布局标签

        String[] label = new String[]{"78免运费", "优惠券", "满减"};
        if (true) {       //78免运费
            TextView labelView = ConstructorLabelView.getLabelView(con);
            labelView.setText(label[0]);
            holder.saleFlow.addView(labelView);
        }
        if (bean.getIs_full() == 1) {//是否有满减
            TextView labelView = ConstructorLabelView.getLabelView(con);
            labelView.setText(label[2]);
            holder.saleFlow.addView(labelView);
        }

        if (bean.getIs_coupons() == 1) {  //是否有优惠券
            TextView labelView = ConstructorLabelView.getLabelView(con);
            labelView.setText(label[1]);
            holder.saleFlow.addView(labelView);
        }


        SimpleDraweeView goodsPicIv = (SimpleDraweeView) convertView.findViewById(R.id.iv_goods_pic);
        goodsPicIv.setImageURI(bean.getCover());
        TextView goodsNameTv = (TextView) convertView.findViewById(R.id.tv_goods_title);
        goodsNameTv.setText(bean.getTitle());
        TextView priceTv = (TextView) convertView.findViewById(R.id.tv_price);
        priceTv.setText(bean.getFinal_price() + "");
        TextView attrsTv = (TextView) convertView.findViewById(R.id.tv_goods_standard);

        //商品属性attrs需要拼接
        String attrs = "";
        if (bean.getSelect_attrs() != null && bean.getSelect_attrs().size() > 0) {
            for (int i = 0; i < bean.getSelect_attrs().size(); i++) {
                if (i == bean.getSelect_attrs().size() - 1) {
                    attrs = attrs + (bean.getSelect_attrs().get(i).getName() + "：" + bean.getSelect_attrs().get(i).getValue());
                } else {
                    attrs = attrs + (bean.getSelect_attrs().get(i).getName() + "：" + bean.getSelect_attrs().get(i).getValue()) + "\t";
                }
            }
        }
        attrsTv.setText(attrs);
        //陈志坚8.31新增代码
        holder.goodsNumTv.setText(beanList.get(groupPosition).getGoods().get(childPosition).getAmount() + "");    //源数据的数量，但会在添加或减少的时候操作源数据
        if (holder.cb.getTag() == null) {
            holder.cb.setTag(new int[]{groupPosition, childPosition, 0});
        } else {
            int[] tags = (int[]) holder.cb.getTag();
            tags[0] = groupPosition;
            tags[1] = childPosition;
        }

        if (bean.getIsCheckde() == 1) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }


        if (bean.getLeftBackground() == 0) {
            holder.subIv.setImageResource(R.drawable.ic_unsubgoods);
        } else {
            holder.subIv.setImageResource(bean.getLeftBackground());
        }
        if (bean.getRightBackground() == 0) {
            holder.addIv.setImageResource(R.drawable.ic_unaddgoods);
        } else {
            holder.addIv.setImageResource(bean.getRightBackground());
        }

        addOnSubClick(holder.goodsNumTv, holder.addIv, holder.subIv, holder.cb, childPosition, groupPosition);//处理add或sum商品
        selCbHandler(holder.cb, holder.addIv, holder.subIv, groupPosition, childPosition); //处理选中事件
        RelativeLayout selCartRlty = (RelativeLayout) convertView.findViewById(R.id.rlty_sel_cart);
        selCart(selCartRlty, holder.cb);

        double money = 0;
        int num = 0;
        for (int i = 0; i < beanList.size(); i++) {
            for (int j = 0; j < beanList.get(i).getGoods().size(); j++) {
                if (beanList.get(i).getGoods().get(j).getIsCheckde() == 1) {
                    num++;
                    money += (Double.parseDouble(beanList.get(i).getGoods().get(j).getFinal_price())) * beanList.get(i).getGoods().get(j).getAmount();
                }
            }
        }
        if (onClickNumListener != null) {
            onClickNumListener.onMoneySum(String.format("%.2f", money));
            onClickNumListener.onClickNum(num);
        }
        checkAll();
        return convertView;
    }

    private void selCart(RelativeLayout selCartRlty, final CheckBox selCb) {
        selCartRlty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selCb.isChecked()) {
                    selCb.setChecked(false);
                } else {
                    selCb.setChecked(true);
                }
            }
        });
    }

    private void selCbHandler(final CheckBox selCb, final ImageView addIv, final ImageView subIv, final int groupPosition, final int childPosition) {
        selCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int[] pos = (int[]) buttonView.getTag();
                int groupPos = pos[0];
                int childPos = pos[1];

                if (isChecked) {
                    beanList.get(groupPos).getGoods().get(childPos).setIsCheckde(1);
                    if (beanList.get(groupPosition).getGoods().get(childPosition).getAmount() == 1) {
                        beanList.get(groupPosition).getGoods().get(childPosition).setLeftBackground(R.drawable.ic_unsubgoods);
                    } else {
                        beanList.get(groupPosition).getGoods().get(childPosition).setLeftBackground(R.drawable.ic_subgoods);
                    }

                } else {
                    beanList.get(groupPos).getGoods().get(childPos).setIsCheckde(0);

                    //当没选中的时候，将图片设为不可点击样式
                    beanList.get(groupPos).getGoods().get(childPos).setLeftBackground(R.drawable.ic_unsubgoods);
                    beanList.get(groupPos).getGoods().get(childPos).setRightBackground(R.drawable.ic_unaddgoods);
                }

                notifyDataSetChanged();
            }
        });
    }


    public List<ShoppingCartBean> getBeanList() {
        return this.beanList;
    }

    /**
     * 添加商品和减少商品
     *
     * @param goodsNumTv
     * @param addIv
     * @param subIv
     */
    private void addOnSubClick(final TextView goodsNumTv, final ImageView addIv, final ImageView subIv, final CheckBox cb, final int childPosition, final int groupPosition) {
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb.isChecked()) {
                    return;
                }
                if (beanList.get(groupPosition).getGoods().get(childPosition).getIsCheckde() == 1) {
                    int num = beanList.get(groupPosition).getGoods().get(childPosition).getAmount();
                    num += 1;
                    beanList.get(groupPosition).getGoods().get(childPosition).setAmount(num);
                    //改变样式
                    beanList.get(groupPosition).getGoods().get(childPosition).setLeftBackground(R.drawable.ic_subgoods);
                    beanList.get(groupPosition).getGoods().get(childPosition).setRightBackground(R.drawable.ic_addgoods);

                } else {
                    //取消样式
                    beanList.get(groupPosition).getGoods().get(childPosition).setRightBackground(R.drawable.ic_unaddgoods);
                }
                //每次点击成功的时候,提交服务器保存商品数量
                postToServer(beanList.get(groupPosition).getGoods().get(childPosition).getKey(), beanList.get(groupPosition).getGoods().get(childPosition).getAmount());
                notifyDataSetChanged();
            }
        });

        subIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb.isChecked()) {
                    return;
                }
                if (beanList.get(groupPosition).getGoods().get(childPosition).getIsCheckde() == 1) {
                    int num = beanList.get(groupPosition).getGoods().get(childPosition).getAmount();
                    num -= 1;
                    if (num < 1) {
                        num = 1;
                    }
                    beanList.get(groupPosition).getGoods().get(childPosition).setAmount(num);
                    beanList.get(groupPosition).getGoods().get(childPosition).setLeftBackground(R.drawable.ic_subgoods);
                } else {
                    beanList.get(groupPosition).getGoods().get(childPosition).setLeftBackground(R.drawable.ic_unsubgoods);
                }
                //每次点击成功的时候,提交服务器保存商品数量
                postToServer(beanList.get(groupPosition).getGoods().get(childPosition).getKey(), beanList.get(groupPosition).getGoods().get(childPosition).getAmount());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    //回调提供商品选中数给外面
    public interface OnClickNumListener {
        void onClickNum(int size);

        void onMoneySum(String moneySum);

        void onSelectAll(boolean isAll);
    }

    public OnClickNumListener onClickNumListener;

    public void setOnClickNumListener(OnClickNumListener onClickNumListener) {
        this.onClickNumListener = onClickNumListener;
    }


    /**
     * 是否全选
     *
     * @return
     */
    private boolean isAll() {
        for (int i = 0; i < beanList.size(); i++) {
            for (int j = 0; j < beanList.get(i).getGoods().size(); j++) {
                if (beanList.get(i).getGoods().get(j).getIsCheckde() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 全选/取消全选
     *
     * @param isSelectAll
     */
    public void selectAllGroup(boolean isSelectAll) {
        if (isSelectAll) {
            for (int i = 0; i < beanList.size(); i++) {
                beanList.get(i).setIsChecked(1);
                for (int j = 0; j < beanList.get(i).getGoods().size(); j++) {
                    beanList.get(i).getGoods().get(j).setIsCheckde(1);
                }
            }
        } else {
            for (int i = 0; i < beanList.size(); i++) {
                beanList.get(i).setIsChecked(0);
                for (int j = 0; j < beanList.get(i).getGoods().size(); j++) {
                    beanList.get(i).getGoods().get(j).setIsCheckde(0);
                }
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 判断是否全选
     */
    private void checkAll() {
        if (onClickNumListener != null) {
            boolean isall = isAll();
            Logger.d("isasll = "+isall);
            onClickNumListener.onSelectAll(isall);
        }
    }

    public int deleteGoods() {
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < this.beanList.size(); i++) {
            List<ShoppingInfoBean> goods = new ArrayList<>();
            for (ShoppingInfoBean goodsBean : this.beanList.get(i).getGoods()) {
                if (goodsBean.getIsCheckde() == 1) {
                    keyList.add(goodsBean.getKey());
                    goods.add(goodsBean);
                }
            }
            this.beanList.get(i).getGoods().removeAll(goods);
        }

        List<ShoppingCartBean> beanList =new ArrayList<>();
        for (int i = 0; i < this.beanList.size(); i++) {
            if(this.beanList.get(i).getGoods().size() == 0){
                beanList.add(this.beanList.get(i));
            }
        }
        this.beanList.removeAll(beanList);
        notifyDataSetChanged();
        String keys = new Gson().toJson(keyList);
//        删除商品要调用接口，把服务器的也给删了
        RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPart("keys", keys);
        client.post(false, HttpUrl.DELETE_GOODS, requestParams, new TypeToken<DelGoodsBean>() {
        }, new IHttpResponseCallback() {
            @Override
            public void onSuccess(Object content) {

            }
        });
        return this.beanList.size();
    }
    //调用修改的接口
    public void postToServer(String key, int goodsNum) {
        RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPart("key", "" + key);
        requestParams.addFormDataPart("num", goodsNum);
        client.post(false, HttpUrl.MODIFY_GOODS, requestParams, new TypeToken<ModifyGoods>() {
        }, new IHttpResponseCallback<ModifyGoods>() {
            @Override
            public void onSuccess(ModifyGoods content) {
            }
        });
    }
    public List<String> getAllKey(){
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < this.beanList.size(); i++) {
            for (ShoppingInfoBean goodsBean : this.beanList.get(i).getGoods()) {
                if (goodsBean.getIsCheckde() == 1) {
                    keyList.add(goodsBean.getKey());
                }
            }
        }
        return keyList;
    }
}

