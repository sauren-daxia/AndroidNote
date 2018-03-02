package com.ddtx.kexiansen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.adapter.ShoppingCartExpandAdapter;
import com.ddtx.kexiansen.base.BaseFragment;
import com.ddtx.kexiansen.http.HttpUrl;
import com.ddtx.kexiansen.http.IHttpResponseCallback;
import com.ddtx.kexiansen.http.OkHttp3Client;
import com.ddtx.kexiansen.model.ShoppingCartBean;
import com.ddtx.kexiansen.model.ShoppingCartListBean;
import com.ddtx.kexiansen.ui.ConfirmOrderActivity;
import com.ddtx.kexiansen.ui.GoodsBriefActivity;
import com.ddtx.kexiansen.ui.MerchantDetailActivity;
import com.ddtx.kexiansen.ui.PushMsgActivity;
import com.ddtx.kexiansen.ui.ShoppingCartActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.RequestParams;

import static android.view.View.GONE;


/**
 * 购物车fragment
 */
public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener, ShoppingCartExpandAdapter.OnClickNumListener {

    private View view;
    private ExpandableListView expandableListView;
    private TextView editTv, deleteTv, gotoPayTv;
    private RelativeLayout gotoPayRlyt;
    private TextView totalTv;
    private CheckBox wholeCb;
    private TextView wholeTv;
    private ImageView returnIv, msgIv;

    private ShoppingCartExpandAdapter expandAdapter;
    private OkHttp3Client client;
    private String goto_settlement;//字符串资源
    private int selectCount;       //选中了多少商品


    private static final String TAG = "ShoppingCartFragment";
    public static final int INTENT_SHOPPINGCART = 1009;    //购物车的请求码
    public static final int RESULT_SHOPPING = 1008;        //购物车的返回码

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        client = new OkHttp3Client(getActivity());
        initView();
        FragmentActivity activity = getActivity();
        //获取字符串资源，免得每次修改都频繁获取
        goto_settlement = getActivity().getResources().getString(R.string.goto_settlement);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onDestroy();
            onDestroyView();
            onDetach();
        } else {
            if (wholeCb != null && gotoPayTv != null && totalTv != null) {
                deleteTv.setVisibility(View.VISIBLE);
                onClick(editTv);
                gotoPayTv.setText("去结算");
                totalTv.setText("合计：¥0.00");
                getCartList();
            }
        }
    }

    private void initView() {
        expandableListView = (ExpandableListView) view.findViewById(R.id.ptr_goodslist);
        expandableListView.setGroupIndicator(null);
        View emptyView = view.findViewById(R.id.llty_empty);
        expandableListView.setEmptyView(emptyView);

        editTv = (TextView) view.findViewById(R.id.btn_edit);
        gotoPayRlyt = (RelativeLayout) view.findViewById(R.id.rlyt_goto_pay);
        deleteTv = (TextView) view.findViewById(R.id.tv_delete_cart);
        gotoPayTv = (TextView) view.findViewById(R.id.tv_goto_pay);
        totalTv = (TextView) view.findViewById(R.id.tv_total_price);
        wholeCb = (CheckBox) view.findViewById(R.id.iv_select);
        returnIv = (ImageView) view.findViewById(R.id.iv_return);
        wholeTv = (TextView) view.findViewById(R.id.tv_all);
        msgIv = (ImageView) view.findViewById(R.id.iv_shopping_msg);
        getCartList();
        if (ShoppingCartActivity.isShowBack) {
            returnIv.setVisibility(View.VISIBLE);
        }
        msgIv.setOnClickListener(this);
        wholeTv.setOnClickListener(this);
        editTv.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
        gotoPayTv.setOnClickListener(this);
        returnIv.setOnClickListener(this);
        wholeCb.setOnCheckedChangeListener(checkedChangeListener);


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                List<ShoppingCartBean> beanList = expandAdapter.getBeanList();
                Intent intent = new Intent(getActivity(), MerchantDetailActivity.class);
                intent.putExtra("id", beanList.get(i).getStore_id() + "");
                startActivity(intent);
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<ShoppingCartBean> beanList = expandAdapter.getBeanList();
                Intent intent = new Intent(getActivity(), GoodsBriefActivity.class);
                intent.putExtra("gid", beanList.get(groupPosition).getGoods().get(childPosition).getId());
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shopping_msg:
                startIntent(PushMsgActivity.class, false);
                break;
            case R.id.btn_edit: //编辑
                if (deleteTv.getVisibility() == GONE) {
                    deleteTv.setVisibility(View.VISIBLE);
                    gotoPayRlyt.setVisibility(GONE);
                    editTv.setText("完成");


//                    returnIv.setVisibility(View.VISIBLE);
                } else {
                    deleteTv.setVisibility(GONE);
                    gotoPayRlyt.setVisibility(View.VISIBLE);
                    editTv.setText("编辑");
//                    returnIv.setVisibility(View.GONE);

                }
                break;
            case R.id.tv_goto_pay://去结算->确认订单
                try {
                    if (expandAdapter.getBeanList().size() == 0 || selectCount == 0) {
                        Toast.makeText(getActivity(), "还没选购商品呢，快去选购吧！", Toast.LENGTH_SHORT).show();
                    } else {

                        String keys = new Gson().toJson(expandAdapter.getAllKey());
                        Intent payIntent = new Intent(getActivity(), ConfirmOrderActivity.class);
                        payIntent.putExtra("goodsKeys", keys);
                        startActivity(payIntent);
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "onClick: -------用户还没登陆，调用会报空指针");
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_delete_cart:               //删除商品
                try {
                   expandAdapter.deleteGoods();
                    totalTv.setText("合计：¥0.00");
                    gotoPayTv.setText("去结算");
                    if (expandAdapter.getBeanList().size() == 0) {
                        showToast("请勾选要删除的商品");
                        break;
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "onClick: -------用户还没登陆，调用会报空指针");
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                }
                break;
            //撤销编辑状态
            case R.id.iv_return:
                getActivity().finish();
                break;
            case R.id.tv_all:
                if (wholeCb.isChecked()) {
                    expandAdapter.selectAllGroup(true);
                } else {
                    expandAdapter.selectAllGroup(false);
                }
                break;
        }
    }

    /**
     * 购物车列表
     */
    private void getCartList() {

        client.post(true, HttpUrl.SHOPPINGCART_LIST, new RequestParams(), new TypeToken<ShoppingCartListBean>() {
        }, new IHttpResponseCallback<ShoppingCartListBean>() {
            @Override
            public void onSuccess(ShoppingCartListBean content) {
                if (content != null && content.getCart() != null) {
                    //判断如果没有数据的时候不让点击
                    if (content.getCart().size() == 0) {
//                        msgIv.setVisibility(View.GONE);   //TODO 2期取消
                        editTv.setVisibility(View.GONE);

                        gotoPayTv.setClickable(false);
                    } else {
                        msgIv.setVisibility(View.GONE);
                        editTv.setVisibility(View.VISIBLE);
                        view.findViewById(R.id.llty_empty).setVisibility(GONE);
                        gotoPayTv.setClickable(true);
                        int num = 0;
                    }
                    if (content.getCart().size() != 0) {
                        expandAdapter = new ShoppingCartExpandAdapter(getActivity(), content.getCart(), expandableListView);
                        expandableListView.setAdapter(expandAdapter);
                        expandAdapter.setOnClickNumListener(ShoppingCartFragment.this);
                    } else {

                    }
                    //打开所有一级菜单展示二级菜单
                    for (int i = 0; i < content.getCart().size(); i++) {
                        expandableListView.expandGroup(i);
                    }

                    if(expandAdapter !=null){
                        expandAdapter.selectAllGroup(true);
                    }

                    if(content.getCart()== null || content.getCart().size() == 0){
                        gotoPayTv.setText(goto_settlement + "(" + 0 + ")");
                        totalTv.setText("合计：¥" + 0);
                        expandAdapter = new ShoppingCartExpandAdapter(getActivity(),new ArrayList<ShoppingCartBean>(),expandableListView);
                        expandableListView.setAdapter(expandAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.INTENT_SHOPPINGCART) {
            if (resultCode == RESULT_SHOPPING) {
                Log.d(TAG, "onActivityResult: 重新刷新了一遍购物车");
                getCartList();
            }
        }
    }

    //----------回调商品总数
    @Override
    public void onClickNum(int size) {
        Logger.d("----<><><><回调数量"+size);
        gotoPayTv.setText("去结算(" + size + ")");
        if (size == 0) {
            gotoPayTv.setText(goto_settlement);
        } else {
            gotoPayTv.setText(goto_settlement + "(" + size + ")");
        }
        selectCount = size;
    }

    //----------回调商品总金额
    @Override
    public void onMoneySum(String moneySum) {
        Logger.d("----<><><><回调价钱"+moneySum);
        totalTv.setText("合计：¥" + moneySum);
    }

    @Override
    public void onSelectAll(boolean isAll) {
        wholeCb.setOnCheckedChangeListener(null);
        wholeCb.setChecked(isAll);
        wholeCb.setOnCheckedChangeListener(checkedChangeListener);
    }

    public CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (expandAdapter != null) {
                expandAdapter.selectAllGroup(isChecked);
            }
        }
    };
}
