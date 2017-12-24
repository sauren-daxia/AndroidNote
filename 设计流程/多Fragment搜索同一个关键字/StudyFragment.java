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
import com.dsfa.common.utils.util.LogUtils;
import com.dsfa.common.utils.util.MapUtils;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.api.ApiServiceCZJ;
import com.dsfa.lovepartybuild.model.DSFAModel;
import com.dsfa.lovepartybuild.ui.activity.SearchActivity;
import com.dsfa.lovepartybuild.ui.adapter.MultiItemAdapter;
import com.dsfa.study.model.Lession;
import com.dsfa.study.ui.delegate.BookDelegate;
import com.dsfa.study.ui.delegate.LiveLessionDelegate;
import com.dsfa.study.ui.delegate.MainLessionDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by hp on 2017/5/5.
 */
public class StudyFragment extends LazyFragment implements SearchActivity.OnSearchListener {
    @Bind(R.id.recycler_active)
    RecyclerView activeRecycler;
    @Bind(R.id.bga_rl)
    BGARefreshLayout bgaRl;
    private View view;

    private List<Lession> activeList;
    private MultiItemAdapter adapter;
    private SearchActivity activity;
    private String keyword = "";
    private String cureentKeyword = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SearchActivity) {
            this.activity = (SearchActivity) activity;
            LogUtils.d("STUDY","isvisible"+isVisible);
            if (isVisible) {
                this.activity.setOnSearchListener(this);
            }
        }
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_all, null);
        ButterKnife.bind(this, view);
        initList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return view;
    }

    private void initList() {
        activeList = new ArrayList();
    }

    private void initRecycler() {
        if (activeList == null) {
            activeList = new ArrayList();
        }
        adapter = new MultiItemAdapter(getActivity(), activeList);
        adapter.addItemViewDelegate(new MainLessionDelegate(getActivity()));
        adapter.addItemViewDelegate(new LiveLessionDelegate(getActivity()));
        adapter.addItemViewDelegate(new BookDelegate(getActivity()));
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
        LogUtils.d("JASON", "study =" + keyWord);
        this.keyword = keyWord;
        if (!keyword.equals(cureentKeyword)) {
            cureentKeyword = keyword;
            search(keyword);
        }
    }

    @Override
    protected void lazyLoad() {
        if (activity != null) {
            keyword = activity.getKeyWord();
        }
        if (!keyword.equals(cureentKeyword)) {
            cureentKeyword = keyword;
            search(keyword);
        }

    }

    /**
     * 搜索
     * @param keyword
     */
    public void search(String keyword) {
        LogUtils.d("JASON", "study = " + keyword);
        HashMap<String, Object> requestMap = MapUtils.Builder()
                .put("UNITID", "00000000000000000000000000000000")
                .put("USERID", "00000000000000000000000000000000")
                .put("PAGESTART", 1)
                .put("PAGECOUNT", 10)
                .put("KEYWORD", keyword)
                .builder();
        ApiServiceCZJ.searchStudy(requestMap, new DSFACallback() {
            @Override
            public void success(DSFAModel jsonBean) {

                initStudy(jsonBean.getLessions());
                initRecycler();
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
     *
     * @param lessions
     */
    private void initStudy(List<Lession> lessions) {
        activeList.addAll(lessions);
    }
}
