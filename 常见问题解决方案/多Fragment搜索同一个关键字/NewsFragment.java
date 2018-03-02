package com.dsfa.lovepartybuild.ui.fragment.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dsfa.common.api.service.DSFACallback;
import com.dsfa.common.base.adapter.wrapper.EmptyWrapper;
import com.dsfa.common.ui.widget.DefineBAGRefreshWithLoadView;
import com.dsfa.common.utils.util.LogUtils;
import com.dsfa.common.utils.util.MapUtils;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.api.ApiServiceCZJ;
import com.dsfa.lovepartybuild.common.UserSession;
import com.dsfa.lovepartybuild.model.DSFAModel;
import com.dsfa.lovepartybuild.model.discovery.News;
import com.dsfa.lovepartybuild.ui.activity.SearchActivity;
import com.dsfa.lovepartybuild.ui.adapter.MultiItemAdapter;
import com.dsfa.lovepartybuild.ui.adapter.discovery.NewsBigImageDelegate;
import com.dsfa.lovepartybuild.ui.adapter.discovery.NewsMoreImageDelegate;
import com.dsfa.lovepartybuild.ui.adapter.discovery.NewsSmallImageDelegate;
import com.dsfa.lovepartybuild.ui.adapter.discovery.NewsTestDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by hp on 2017/5/5.
 */
public class NewsFragment extends LazyFragment implements SearchActivity.OnSearchListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    @Bind(R.id.recycler_active)
    RecyclerView activeRecycler;
    @Bind(R.id.bga_rl)
    BGARefreshLayout bgaRl;
    private View view;

    private List<News> activeList;
    private MultiItemAdapter adapter;
    private SearchActivity activity;
    private String keyword = "";
    private String cureentKeyword = "";
    private DefineBAGRefreshWithLoadView mDefineBAGRefreshWithLoadView;
    private int pager=1;      //加载页数

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SearchActivity) {
            this.activity = (SearchActivity) activity;
            if (isVisible) {
                this.activity.setOnSearchListener(this);
            }
        }
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_all, null);
        ButterKnife.bind(this, view);
        setBgaRefreshLayout();
        initList();
        initRecycler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    private void setBgaRefreshLayout() {
        bgaRl.setDelegate(this);
        mDefineBAGRefreshWithLoadView = new DefineBAGRefreshWithLoadView(getContext(), true, true);
        bgaRl.setRefreshViewHolder(mDefineBAGRefreshWithLoadView);
        mDefineBAGRefreshWithLoadView.updateLoadingMoreText("加载更多");
    }

    private void initList() {
        activeList = new ArrayList();
    }

    private void initRecycler() {
        if (activeList == null) {
            activeList = new ArrayList();
        }
        adapter = new MultiItemAdapter(getActivity(), activeList);
        adapter.addItemViewDelegate(new NewsBigImageDelegate(getActivity()));
        adapter.addItemViewDelegate(new NewsMoreImageDelegate(getActivity()));
        adapter.addItemViewDelegate(new NewsSmallImageDelegate(getActivity()));
        adapter.addItemViewDelegate(new NewsTestDelegate(getActivity()));
        EmptyWrapper emptyWrapper = new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.empty_load_error);
        activeRecycler.setAdapter(adapter);
        activeRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 关键字搜索
     *
     * @param keyWord
     */
    @Override
    public void onKeyWord(String keyWord) {
        LogUtils.d("JASON", "news =" + keyWord);
        this.keyword = keyWord;
        if (!keyword.equals(cureentKeyword)) {
            cureentKeyword = keyword;
            //搜索
            search(keyword);
        }
    }

    @Override
    protected void lazyLoad() {


        if (activity != null) {
            activity.setOnSearchListener(this);
            keyword = activity.getKeyWord();
        }

        if (!keyword.equals(cureentKeyword)) {
            cureentKeyword = keyword;
            //搜索
            search(keyword);
        }
    }

    /**
     * 搜索
     *
     * @param keyword
     */
    public void search(String keyword) {
        HashMap<String, Object> requestMap = MapUtils.Builder()
                .put("UNITID", UserSession.getInstance().getOrgUnit().getId())
                .put("USERID", UserSession.getInstance().getUser().getId())
                .put("PAGESTART", 1)
                .put("PAGECOUNT", 10)
                .put("KEYWORD", keyword)
                .builder();
        ApiServiceCZJ.searchNews(requestMap, new DSFACallback() {
            @Override
            public void success(DSFAModel jsonBean) {

                initNews(jsonBean.getNewses());

            }

            @Override
            public void fail(DSFAError dsfaError) {

            }

            @Override
            public Context getContext() {
                return null;
            }

            @Override
            public ImageView getBackImg() {
                return null;
            }

            @Override
            public int getDataSize() {
                return 0;
            }
        });
    }

    /**
     * 初始化新闻
     *
     * @param newses
     */
    private void initNews(List<News> newses) {
        if (pager == 1) {
            activeList.clear();
            activeList.addAll(newses);
        } else {
            activeList.addAll(newses);
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 刷新
     *
     * @param refreshLayout
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pager = 1;
        initList();
    }

    /**
     * 加载
     *
     * @param refreshLayout
     * @return
     */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pager++;
        initList();
        return true;
    }
}
