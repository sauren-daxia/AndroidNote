package com.dsfa.lovepartybuild.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dsfa.common.base.activity.BaseActivity;
import com.dsfa.common.utils.util.ToastUtils;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.ui.fragment.search.ActiveFragment;
import com.dsfa.lovepartybuild.ui.fragment.search.NewsFragment;
import com.dsfa.lovepartybuild.ui.fragment.search.StudyFragment;
import com.dsfa.study.ui.adapter.FragmentAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp on 2017/5/5.
 */
public class SearchActivity extends BaseActivity {
    @Bind(R.id.et_search)
    EditText searchEt;
    @Bind(R.id.slidingTabLayout)
    SlidingTabLayout tabLayoutSD;
    @Bind(R.id.viewPager)
    ViewPager pagerView;

    private OnSearchListener onSearchListener;
    private List<Fragment> fragments;
    private FragmentAdapter bookDetailAdapter;
    private String[] titles = {"新闻", "学习", "活动"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initBaseView();
        initFragment();
        initViewPager();
    }

    private void initViewPager() {
        bookDetailAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        pagerView.setAdapter(bookDetailAdapter);
        pagerView.setOffscreenPageLimit(1);
        tabLayoutSD.setViewPager(pagerView, titles);
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(new StudyFragment());
        fragments.add(new ActiveFragment());
    }

    /**
     * 设置键盘搜索监听
     */
    private void initBaseView() {
        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                }
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
    }

    /**
     * 搜索通知
     */
    private void search() {
        String searchContent = searchEt.getText().toString().trim();
        if (!TextUtils.isEmpty(searchContent)) {
            if (this.onSearchListener != null) {
                this.onSearchListener.onKeyWord(searchContent);
            }
        } else {
            ToastUtils.toast(this, "请输入搜索内容!");
        }
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        onBackPressed();
    }

    public interface OnSearchListener {
        void onKeyWord(String keyWrod);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public String getKeyWord(){
        return searchEt.getText().toString().trim();
    }
}
