package com.weiyin.card_merch.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weiyin.card_merch.R;
import com.weiyin.card_merch.base.BaseFragment;
import com.weiyin.card_merch.base.MultiAdapter;
import com.weiyin.card_merch.bean.OrderTypeBean;
import com.weiyin.card_merch.bean.UserInfoManager;
import com.weiyin.card_merch.delegate.BaseOrderTypeDelegate;
import com.weiyin.card_merch.delegate.OrderInExamineDelegate;
import com.weiyin.card_merch.delegate.OrderInSaleDelegate;
import com.weiyin.card_merch.delegate.OrderInStockDelegate;
import com.weiyin.card_merch.delegate.OrderRecycleBinDelegate;
import com.weiyin.card_merch.rxnet.base.BaseBean;
import com.weiyin.card_merch.rxnet.client.RxObserver;
import com.weiyin.card_merch.rxnet.client.RxRetrofit;
import com.weiyin.card_merch.rxnet.utils.MapUtils;
import com.weiyin.card_merch.rxurl.RxRequest;
import com.weiyin.card_merch.views.springview.MyDefaultFooter;
import com.weiyin.card_merch.views.springview.MyDefaultHeader;
import com.weiyin.card_merch.views.springview.MySpringView;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Jason Chen on 2017/8/4.
 */

public class OrderTypeFragment extends BaseFragment implements MySpringView.OnFreshListener, BaseOrderTypeDelegate.OnRefreshListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.springview)
    MySpringView springview;
    Unbinder unbinder;
    private int pager = 1;
    private MultiAdapter adapter;
    private int type;
    private List list;
    private EmptyWrapper emptyWrapper;
    private boolean isNotData;      //是否没有下一页
    private boolean isFristRun;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.fragment_order_type, null);
        initViews();
        initSpringView();
    }

    private void initViews() {
        recycler =(RecyclerView) rootView.findViewById(R.id.recycler);
        springview =(MySpringView) rootView.findViewById(R.id.springview);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFristRun == false){
            type = getArguments().getInt("type");
            getData();
            isFristRun = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void getData() {
        HashMap<String, Object> builder = new MapUtils()
                .put("merchId", UserInfoManager.getInstance().getUser().getIdentityList().get(0).getMerchid())
                .put("type", type)
                .put("pageNum", pager)
                .builder();
        RxRetrofit.request(RxRetrofit.create(builder, RxRequest.class).orderManagerList(), new RxObserver<BaseBean<List<OrderTypeBean>>>(getActivity(), !isFristRun) {
            @Override
            public void onDisposable(Disposable d) {
                if (mDisposables != null) {
                    mDisposables.add(d);
                }
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onSuccess(BaseBean<List<OrderTypeBean>> o) {
                if(o.getState() == 0){
                    initData(o.getData());
                }
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Throwable e) {
                initRecycler();
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 初始化数据
     *
     * @param newData
     */
    private void initData(List<OrderTypeBean> newData) {
        if (list == null) {
            list = new ArrayList();
        }
        if (pager == 1) {
            list.clear();
            list.addAll(newData);
        }

        if (newData != null && newData.size()>0) {
            springview.setClearFooter(false);
        } else if (newData == null || newData.size() == 0) {
            Toast.makeText(getActivity(), "没有更多数据了哦！", Toast.LENGTH_SHORT).show();
            springview.setClearFooter(true);
        }

        if (emptyWrapper == null) {
            initRecycler();
        }
        emptyWrapper.notifyDataSetChanged();
    }


    private void initRecycler() {
        if (list == null) {
            list = new ArrayList();
        }
        if (adapter == null) {
            adapter = new MultiAdapter(getActivity(), list);
		recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        emptyWrapper = new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.layout_null_data_default);
        switchDelegate();
        recycler.setAdapter(emptyWrapper);
        }
    }

    /**
     * 初始化刷新
     */
    private void initSpringView() {
        springview.setListener(this);
        springview.setHeader(new MyDefaultHeader(getActivity()));
        springview.setFooter(new MyDefaultFooter(getActivity()));
        springview.setType(MySpringView.Type.FOLLOW);
    }


    //刷新
    @Override
    public void onRefresh() {
        pager = 1;
        getData();

    }

    //加载
    @Override
    public void onLoadmore() {
        pager++;
        getData();

    }

    /**
     * 取消刷新
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (getActivity() != null && !getActivity().isFinishing() && springview != null) {
                springview.onFinishFreshAndLoad();
            }
            return false;
        }
    });

    /**
     * 根据状态来显示不同的视图
     */
    private void switchDelegate() {
        if (adapter != null) {
            switch (type) {
                case 1:          //出售中
                    adapter.addItemViewDelegate(new OrderInSaleDelegate(getActivity(), list, emptyWrapper, this));
                    break;
                case 2:          //审核中
                case 3:          //已售罄
                    adapter.addItemViewDelegate(new OrderInExamineDelegate(getActivity(), list, emptyWrapper, this));
                    break;
                case 4:          //仓库中
                    adapter.addItemViewDelegate(new OrderInStockDelegate(getActivity(), list, emptyWrapper, this));
                    break;
                case 5:          //回收站
                    adapter.addItemViewDelegate(new OrderRecycleBinDelegate(getActivity(), list, emptyWrapper));
                    break;
            }
        }
    }

    /**
     * 恢复商品，刷新一遍
     */
    @Override
    public void callRefresh() {
        if (springview != null) {
            onRefresh();
        }
    }
}
