package com.dsfa.lovepartybuild.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsfa.common.api.service.HttpCallback;
import com.dsfa.common.base.activity.BaseActivity;
import com.dsfa.common.ui.widget.DefineBAGRefreshWithLoadView;
import com.dsfa.common.ui.widget.MTitleBar;
import com.dsfa.common.utils.util.KeyboardUtils;
import com.dsfa.common.utils.util.MapUtils;
import com.dsfa.common.utils.util.ToastUtils;
import com.dsfa.lovepartybuild.Navigator;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.api.ApiRequestCZJ;
import com.dsfa.lovepartybuild.common.UserSession;
import com.dsfa.lovepartybuild.delegate.BaseFriendsCircleDalegate;
import com.dsfa.lovepartybuild.delegate.FriendsCircleFourImgDelagate;
import com.dsfa.lovepartybuild.delegate.FriendsCircleSingleImgDelagate;
import com.dsfa.lovepartybuild.model.FriendsCircleBean;
import com.dsfa.lovepartybuild.ui.adapter.MultiItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by hp on 2017/6/6.
 */
public class FriendsCircleActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BaseFriendsCircleDalegate.PulishCommentListener {
    @Bind(R.id.mt_bar)
    MTitleBar barMt;
    @Bind(R.id.recycler_friends_circle)
    RecyclerView friendsCircleRecycler;
    @Bind(R.id.bga_rl)
    BGARefreshLayout bgaRl;
    @Bind(R.id.circleEt)
    EditText circleEt;
    @Bind(R.id.sendIv)
    ImageView sendIv;
    @Bind(R.id.editTextBodyLl)
    LinearLayout editLayout;

    private List<FriendsCircleBean.CHILDRENBean> friendsCircles;
    private MultiItemAdapter adapter;
    private DefineBAGRefreshWithLoadView mDefineBAGRefreshWithLoadView;
    private int pager = 1;      //加载页数
    private String ACT_ID = "";

    private LinearLayoutManager layoutManager;
    private int selectCommentItemOffset;
    private int selectCircleItemH;
    private String FID;
    private String PID;
    private int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_circle);
        ButterKnife.bind(this);

        ACT_ID = getIntent().getStringExtra("ACT_ID");

        setBgaRefreshLayout();
        initBar();
        initList();

    }

    private void setBgaRefreshLayout() {
        bgaRl.setDelegate(this);
        mDefineBAGRefreshWithLoadView = new DefineBAGRefreshWithLoadView(this, true, true);
        bgaRl.setRefreshViewHolder(mDefineBAGRefreshWithLoadView);
        mDefineBAGRefreshWithLoadView.updateLoadingMoreText("加载更多");
    }

    private void initBar() {
        barMt.setLeftImg(R.mipmap.back);
        barMt.setRightImg(R.mipmap.home_btn_more);
        barMt.setTitle("活动圈");
        barMt.setLeftBtnOnClickListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        barMt.setRightBtnOnClickListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转发朋友圈
                Navigator.startPublishFriendsCircle(FriendsCircleActivity.this, ACT_ID);
            }
        });
    }


    private void initList() {
        friendsCircles = new ArrayList();
        request();
    }

    private void request() {
        HashMap<String, Object> requestMap = MapUtils.Builder()
                .put("ACT_ID", ACT_ID)
//                .put("ACT_ID", "ce31fff4929a489d8f545d2ea4379db3")
                .put("PAGESTART", pager)
                .put("PAGECOUNT", 10)
                .builder();

        ApiRequestCZJ.getAllFriendsCircle(requestMap, new HttpCallback<FriendsCircleBean>() {
            @Override
            public void fail(HttpError dsfaError) {
                ToastUtils.toast(FriendsCircleActivity.this, "加载失败，请检查网络是否正常连接或再次尝试刷新");
                handler.sendEmptyMessageDelayed(0, 500);
            }

            @Override
            public void success(FriendsCircleBean model) {
                handler.sendEmptyMessageDelayed(0, 500);
                if (model != null) {
                    initFriendsCircle(model.getCHILDREN());
                }
            }
        });

    }

    private void initFriendsCircle(List<FriendsCircleBean.CHILDRENBean> list) {
        if (friendsCircles != null) {
            if (pager == 1) {
                friendsCircles.clear();
                addAllData(list);
            } else {
                addAllData(list);
            }
            bgaRl.setVisibility(View.VISIBLE);
        } else {
            bgaRl.setVisibility(View.GONE);
        }
        if (adapter == null) {
            initRecycler();
        }

        adapter.notifyDataSetChanged();

    }

    /**
     * 添加数据
     *
     * @param list
     */
    private void addAllData(List<FriendsCircleBean.CHILDRENBean> list) {
        friendsCircles.addAll(list);
    }

    private void initRecycler() {
        if (friendsCircles == null) {
            friendsCircles = new ArrayList();
        }
        adapter = new MultiItemAdapter(this, friendsCircles);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter.addItemViewDelegate(new FriendsCircleSingleImgDelagate(this, friendsCircles, editLayout, sendIv, circleEt, layoutManager, adapter, this));
        adapter.addItemViewDelegate(new FriendsCircleFourImgDelagate(this, friendsCircles, editLayout, sendIv, circleEt, layoutManager, adapter, this));
        friendsCircleRecycler.setAdapter(adapter);
        friendsCircleRecycler.setLayoutManager(layoutManager);
        friendsCircleRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editLayout.getVisibility() == View.VISIBLE) {
                    changedKeyBoard();
                    return true;
                }
                return false;
            }
        });

    }


    /**
     * 刷新
     *
     * @param refreshLayout
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pager = 1;
        request();
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
        request();
        return true;
    }

    /**
     * 取消刷新
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (pager == 1) {
                if (bgaRl != null) {
                    bgaRl.endRefreshing();
                }
            } else if (pager > 1) {
                if (bgaRl != null) {
                    bgaRl.endLoadingMore();
                }
            }
            return false;
        }
    });


    @OnClick(R.id.sendIv)
    public void onViewClicked() {
        String content = circleEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        commitComment(content);
        changedKeyBoard();
        //清空回复
        circleEt.setText("");
    }

    /**
     * 监听对谁回复
     *
     * @param FID
     * @param PID
     * @param currentPosition
     */
    @Override
    public void publish(String FID, String PID, int currentPosition) {
        this.FID = FID;
        this.PID = PID;
        this.currentPosition = currentPosition;
    }


    /**
     * 提交评论
     *
     * @param contnet
     */
    private void commitComment(final String contnet) {
        HashMap<String, Object> reuqestMap = MapUtils.Builder()
                .put("FID", FID)
                .put("CHAT_ID", PID)
                .put("USERID", UserSession.getInstance().getAccount().getId())
                .put("CONTENT", contnet)
                .builder();
        ApiRequestCZJ.commitComment(reuqestMap, new HttpCallback() {
            @Override
            public void fail(HttpError dsfaError) {

            }

            @Override
            public void success(Object model) {
                refreshSingleFriendsCircle(friendsCircles.get(currentPosition).getPID(), currentPosition);
            }
        });
    }

    /**
     * 更新单个Item
     *
     * @param pid
     * @param position
     */
    private void refreshSingleFriendsCircle(String pid, final int position) {
        ApiRequestCZJ.refreshSingleFriendsCircle(MapUtils.Builder().put("PID", pid).builder(), new HttpCallback<FriendsCircleBean>() {
            @Override
            public void success(FriendsCircleBean model) {
                if (model != null && model.getCHILDREN() != null && model.getCHILDREN().size() > 0) {
                    friendsCircles.set(position, model.getCHILDREN().get(0));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void fail(HttpError dsfaError) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (editLayout != null && editLayout.getVisibility() == View.VISIBLE) {
                //edittextbody.setVisibility(View.GONE);
                changedKeyBoard();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changedKeyBoard() {
        editLayout.setVisibility(View.GONE);
        if (View.VISIBLE == View.GONE) {
            circleEt.requestFocus();
            //弹出键盘
            KeyboardUtils.showSoftInput(this, circleEt);
        } else if (View.GONE == View.GONE) {
            //隐藏键盘
            KeyboardUtils.hideSoftInput(this, circleEt);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 56 && resultCode == 65) {
            bgaRl.beginRefreshing();
        }
    }

}
