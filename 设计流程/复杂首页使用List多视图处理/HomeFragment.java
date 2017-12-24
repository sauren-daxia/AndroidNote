package com.ddtx.kexiansen.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.adapter.MultiStyleListAdapter;
import com.ddtx.kexiansen.base.BaseFragment;
import com.ddtx.kexiansen.http.IApiActivityRequest;
import com.ddtx.kexiansen.model.HomeDataBean;
import com.ddtx.kexiansen.model.HomeRefreshBean;
import com.ddtx.kexiansen.model.ShowType;
import com.ddtx.kexiansen.model.TokenBean;
import com.ddtx.kexiansen.model.home.DataEightBean;
import com.ddtx.kexiansen.model.home.DataElevenBean;
import com.ddtx.kexiansen.model.home.DataFiveBean;
import com.ddtx.kexiansen.model.home.DataFourBean;
import com.ddtx.kexiansen.model.home.DataNineBean;
import com.ddtx.kexiansen.model.home.DataOneBean;
import com.ddtx.kexiansen.model.home.DataSevenBean;
import com.ddtx.kexiansen.model.home.DataSixBean;
import com.ddtx.kexiansen.model.home.DataTenBean;
import com.ddtx.kexiansen.model.home.DataThreeBean;
import com.ddtx.kexiansen.model.home.DataTwoBean;
import com.ddtx.kexiansen.net.BaseSubscriber;
import com.ddtx.kexiansen.net.RxRequest;
import com.ddtx.kexiansen.ui.ContactActivity;
import com.ddtx.kexiansen.ui.LoginActivity;
import com.ddtx.kexiansen.ui.PushMsgActivity;
import com.ddtx.kexiansen.ui.SearchActivity;
import com.ddtx.kexiansen.ui.WebActivity;
import com.ddtx.kexiansen.util.AloneSharedUtils;
import com.ddtx.kexiansen.util.SharedPreferencesUtil;
import com.ddtx.kexiansen.widget.SharedFriendPopupWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rxretrofit.net.RxCallBack;
import com.rxretrofit.util.MapUtil;
import com.rxretrofit.util.RxPermissUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static com.ddtx.kexiansen.R.id.llty_search;


/**
 * 首页fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AbsListView.OnScrollListener {

    private View view;
    private PullToRefreshListView goodsPtrgv;
    private int page = 1, row = 8;
    private MultiStyleListAdapter homeAdapter;          //新adapter
    private List<Object> typeList;          //所有状态的集合
    private View titleView;                 //标题
    private View searchView;                //搜索
    private ListView homeListView;
    private ImageView suspensionSearchIv;       //悬浮按钮

    //分享相关
    private SharedFriendPopupWindow sharedPopupWindow; //分享
    private String title = "朋友，一起加入科先森-同城数码特卖会";
    private String url = "http://h5.kexiansen.com/#/indexShare?rcode=";
    private String contentText = "数码特卖，正品行货，特惠福利与你分享！";
    private String id;
    private String imgUrl = "http://img.kexiansen.com/appicon-12.png";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPopupWindow = new SharedFriendPopupWindow(getActivity(), this);
        initView();
        initShared();

        return view;
    }

    private void initShared() {
        RxPermissUtil.getPermission(getActivity(), new RxCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                TelephonyManager systemService = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String line1Number = systemService.getLine1Number();
                if (!TextUtils.isEmpty(line1Number)) {
                    url = url + line1Number;
                }
            }
            @Override
            public void onFailure(Throwable e) {

            }
        },new String[]{Manifest.permission.READ_SMS});
    }


    private void initView() {
        typeList = new ArrayList<>();
        goodsPtrgv = (PullToRefreshListView) view.findViewById(R.id.ptrgv_home_list);
        homeListView = goodsPtrgv.getRefreshableView();
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_fragment_home_head, null);
        homeListView.addHeaderView(headView);
        suspensionSearchIv = (ImageView) view.findViewById(R.id.iv_suspension_search);
        titleView = view.findViewById(R.id.rlty_title);
        searchView = view.findViewById(R.id.llty_search);
        view.findViewById(R.id.rlty_newhome_message).setOnClickListener(this);      //消息监听
        view.findViewById(llty_search).setOnClickListener(this);               //搜索监听
        view.findViewById(R.id.tv_newhome_shared).setOnClickListener(this);         //分享监听
        suspensionSearchIv.setOnClickListener(this);      //悬浮按钮
        goodsPtrgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                typeList.clear();
                getHomeData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getMore();
            }
        });
        getHomeData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlty_newhome_message:   //消息
                if (SharedPreferencesUtil.getLoginData(getActivity()) != null) {
                    startIntent(PushMsgActivity.class, false);
                } else {
                    startIntent(LoginActivity.class, false);
                }
                break;
            case R.id.iv_suspension_search:
            case R.id.llty_search:  //搜索
                startIntent(SearchActivity.class, false);
                break;
            case R.id.tv_newhome_shared:        //推荐注册
                sharedPopupWindow.showAtLocation(view.findViewById(R.id.rlty_root), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_shared_wx:           //分享微信
                AloneSharedUtils.sharedWX(title, url, imgUrl, null);
                break;
            case R.id.iv_shared_wxfriend:     //分享微信朋友圈
                AloneSharedUtils.sharedWXFRIEND(title, url, imgUrl, null);
                break;
            case R.id.iv_shared_qq:           //分享QQ
                AloneSharedUtils.sharedQQ(title, url, contentText, imgUrl, null);
                break;
            case R.id.iv_shared_contact:      //分享联系人

                RxPermissUtil.getPermission(getActivity(), new RxCallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Intent intent = new Intent(getActivity(), ContactActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }, new String[]{Manifest.permission.READ_CONTACTS});
                break;
        }
    }


    /**
     * 获取首页数据
     */
    private void getHomeData() {
        RxRequest.request(ShowType.SHOW, RxRequest.getClient(getActivity(), new MapUtil().build()).create(IApiActivityRequest.class).homeIndex()
                , new BaseSubscriber<TokenBean<HomeDataBean>>(getActivity()) {
                    @Override
                    public void onSuccess(TokenBean<HomeDataBean> o) {
                        if (goodsPtrgv != null) {
                            goodsPtrgv.onRefreshComplete();
                        }
                        if (o.getData() != null) {
                            setHomeData(o.getData());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });


    }

    private void setHomeData(HomeDataBean data) {

        setOneType(data);   //轮播广告
        setTwoType(data);   //快讯
        setkillType(data); //秒杀组
        setSpecialType(data);  //特卖广告组
        setDelicateType(data);  //精品特卖组
        setClassType(data);   //分类组
        setYouLikeTitleType();      //猜你喜欢头部
        setYouLikeType(data.getLike()); //猜你喜欢组
        if (homeAdapter == null) {
            homeAdapter = new MultiStyleListAdapter(getActivity());
        }
        homeAdapter.setList(typeList);
        homeListView.setAdapter(homeAdapter);
        homeListView.setOnScrollListener(this);


    }

    private void setYouLikeTitleType() {
        typeList.add(new DataTenBean("res://" + getActivity().getPackageName() + "/" + R.drawable.ic_newhome_youliketitle));
    }

    /**
     * 猜你喜欢
     */
    private void setYouLikeType(List<HomeRefreshBean> like) {
        if (like == null || like.size() < 1) {
            return;
        }

        for (int i = 0; i < like.size(); i++) {
            DataElevenBean dataElevenBean = new DataElevenBean();
            dataElevenBean.setLeftBigImg(like.get(i).getCover());
            dataElevenBean.setLeftGoodsName(like.get(i).getTitle());
            dataElevenBean.setLeftPrice(like.get(i).getLast_price());
            dataElevenBean.setLeftOldPrice(like.get(i).getPrice_agora());
            dataElevenBean.setLeftGoodsId(like.get(i).getId());
            dataElevenBean.setLeftIs_specia(like.get(i).getIs_specia());
            if (i + 1 < like.size()) {
                dataElevenBean.setRightBigImg(like.get(i + 1).getCover());
                dataElevenBean.setRightGoodsName(like.get(i + 1).getTitle());
                dataElevenBean.setRightPrice(like.get(i + 1).getLast_price());
                dataElevenBean.setRightOldPrice(like.get(i + 1).getPrice_agora());
                dataElevenBean.setRightGoodsId(like.get(i + 1).getId());
                dataElevenBean.setLeftIs_specia(like.get(i + 1).getIs_specia());
            }
            typeList.add(dataElevenBean);
            i++;
        }
    }

    /**
     * 分类组
     *
     * @param data
     */
    private void setClassType(HomeDataBean data) {
        List<HomeDataBean.ClassBean> cla = data.getCla();
        if (cla == null || cla.size() < 1) {
            return;
        }
        for (int i = 0; i < cla.size(); i++) {

            if (cla.get(i).getOne_img() != null && cla.get(i).getOne_img().size() > 0) {
                typeList.add(new DataThreeBean(cla.get(i).getOne_img().get(0).getImage_url()));
            }

            DataNineBean dataNineBean = new DataNineBean();
            if (cla.get(i).getTwo_img() != null && cla.get(i).getTwo_img().size() > 0) {
                dataNineBean.setLeftBigImg(cla.get(i).getTwo_img().get(0).getImage_url());
                dataNineBean.setLeftJump(cla.get(i).getTwo_img().get(0).getJump_page());
                dataNineBean.setLeftId(cla.get(i).getTwo_img().get(0).getExt_id());
                dataNineBean.setLeftLink(cla.get(i).getTwo_img().get(0).getLink());
            }

            if (cla.get(i).getThree_img() != null && cla.get(i).getThree_img().size() > 0) {
                for (int j = 0; j < cla.get(i).getThree_img().size(); j++) {
                    if (j == 0) {
                        dataNineBean.setTopLeftImg(cla.get(i).getThree_img().get(j).getImage_url());
                        dataNineBean.setTopLeftJump(cla.get(i).getThree_img().get(j).getJump_page());
                        dataNineBean.setTopLeftId(cla.get(i).getThree_img().get(j).getExt_id());
                        dataNineBean.setTopLeftLink(cla.get(i).getThree_img().get(j).getLink());
                    }
                    if (j == 1) {
                        dataNineBean.setTopRightImg(cla.get(i).getThree_img().get(j).getImage_url());
                        dataNineBean.setTopRightJump(cla.get(i).getThree_img().get(j).getJump_page());
                        dataNineBean.setTopRightId(cla.get(i).getThree_img().get(j).getExt_id());
                        dataNineBean.setTopRightLink(cla.get(i).getThree_img().get(j).getLink());
                    }
                    if (j == 2) {
                        dataNineBean.setBottomLeftImg(cla.get(i).getThree_img().get(j).getImage_url());
                        dataNineBean.setBottomLeftJump(cla.get(i).getThree_img().get(j).getJump_page());
                        dataNineBean.setBottomLeftId(cla.get(i).getThree_img().get(j).getExt_id());
                        dataNineBean.setBottomLeftLink(cla.get(i).getThree_img().get(j).getLink());
                    }
                    if (j == 3) {
                        dataNineBean.setBottomRightImg(cla.get(i).getThree_img().get(j).getImage_url());
                        dataNineBean.setBottomRightJump(cla.get(i).getThree_img().get(j).getJump_page());
                        dataNineBean.setBottomRightId(cla.get(i).getThree_img().get(j).getExt_id());
                        dataNineBean.setBottomRightLink(cla.get(i).getThree_img().get(j).getLink());
                    }
                }
                typeList.add(dataNineBean);
            }


            if (cla.get(i).getFour_img() != null && cla.get(i).getFour_img().size() > 0) {
                DataEightBean dataEightBean = new DataEightBean();
                dataEightBean.setBannerImg(cla.get(i).getFour_img().get(0).getImage_url());
                dataEightBean.setBannerJump(cla.get(i).getFour_img().get(0).getJump_page());
                dataEightBean.setBannerId(cla.get(i).getFour_img().get(0).getExt_id());
                dataEightBean.setBannerLink(cla.get(i).getFour_img().get(0).getLink());
                typeList.add(dataEightBean);
            }
        }
    }

    /**
     * 精品特卖组
     *
     * @param data
     */
    private void setDelicateType(HomeDataBean data) {
        List<HomeDataBean.CompBean> comp = data.getComp();
        if (comp == null || comp.size() < 1) {
            return;
        }
        for (int i = 0; i < comp.size(); i++) {
            if (comp.get(i).getOne_img() != null && comp.get(i).getOne_img().size() > 0) {
                typeList.add(new DataThreeBean(comp.get(i).getOne_img().get(0).getImage_url()));
            }

            DataSixBean dataSixBean = new DataSixBean();
            if (comp.get(i).getTwo_img() != null && comp.get(i).getTwo_img().size() > 0) {
                dataSixBean.setLeftBigImg(comp.get(i).getTwo_img().get(0).getImage_url());
                dataSixBean.setLeftJump(comp.get(i).getTwo_img().get(0).getJump_page());
                dataSixBean.setLeftId(comp.get(i).getTwo_img().get(0).getExt_id());
                dataSixBean.setLeftLink(comp.get(i).getTwo_img().get(0).getLink());
            }
            if (comp.get(i).getThree_img() != null && comp.get(i).getThree_img().size() > 0) {
                for (int j = 0; j < comp.get(i).getThree_img().size(); j++) {
                    if (j == 0) {
                        dataSixBean.setTopRightImg(comp.get(i).getThree_img().get(j).getImage_url());
                        dataSixBean.setTopRightJump(comp.get(i).getThree_img().get(j).getJump_page());
                        dataSixBean.setTopRightId(comp.get(i).getThree_img().get(j).getExt_id());
                        dataSixBean.setTopRightLink(comp.get(i).getThree_img().get(j).getLink());
                    }
                    if (j == 1) {
                        dataSixBean.setBottomRightImg(comp.get(i).getThree_img().get(j).getImage_url());
                        dataSixBean.setBottomRightJump(comp.get(i).getThree_img().get(j).getJump_page());
                        dataSixBean.setBottomRightId(comp.get(i).getThree_img().get(j).getExt_id());
                        dataSixBean.setBottomRightLink(comp.get(i).getThree_img().get(j).getLink());
                    }
                }
                typeList.add(dataSixBean);
            }


            if (comp.get(i).getFour_img() != null && comp.get(i).getFour_img().size() > 0) {
                DataSevenBean dataSevenBean = new DataSevenBean();
                for (int j = 0; j < comp.get(i).getFour_img().size(); j++) {
                    if (j == 0) {
                        dataSevenBean.setOneImg(comp.get(i).getFour_img().get(j).getImage_url());
                        dataSevenBean.setOneJump(comp.get(i).getFour_img().get(j).getJump_page());
                        dataSevenBean.setOneId(comp.get(i).getFour_img().get(j).getExt_id());
                        dataSevenBean.setOneLink(comp.get(i).getFour_img().get(j).getLink());
                    }
                    if (j == 1) {
                        dataSevenBean.setTwoImg(comp.get(i).getFour_img().get(j).getImage_url());
                        dataSevenBean.setTwoJump(comp.get(i).getFour_img().get(j).getJump_page());
                        dataSevenBean.setTwoId(comp.get(i).getFour_img().get(j).getExt_id());
                        dataSevenBean.setTwoLink(comp.get(i).getFour_img().get(j).getLink());
                    }
                    if (j == 2) {
                        dataSevenBean.setThreeImg(comp.get(i).getFour_img().get(j).getImage_url());
                        dataSevenBean.setThreeJump(comp.get(i).getFour_img().get(j).getJump_page());
                        dataSevenBean.setThreeId(comp.get(i).getFour_img().get(j).getExt_id());
                        dataSevenBean.setThreeLink(comp.get(i).getFour_img().get(j).getLink());
                    }
                }
                typeList.add(dataSevenBean);
            }

            if (comp.get(i).getFive_img() != null && comp.get(i).getFive_img().size() > 0) {
                DataEightBean dataEightBean = new DataEightBean();
                dataEightBean.setBannerImg(comp.get(i).getFive_img().get(0).getImage_url());
                dataEightBean.setBannerJump(comp.get(i).getFive_img().get(0).getJump_page());
                dataEightBean.setBannerId(comp.get(i).getFive_img().get(0).getExt_id());
                dataEightBean.setBannerLink(comp.get(i).getFive_img().get(0).getLink());
                typeList.add(dataEightBean);
            }
        }
    }

    /**
     * 特卖广告组
     *
     * @param data
     */
    private void setSpecialType(HomeDataBean data) {
        if (data.getSpecial() == null || data.getSpecial().size() < 1) {
            return;
        }
        for (int i = 0; i < data.getSpecial().size(); i++) {
            HomeDataBean.SpecialBean specialBean = data.getSpecial().get(i);
            if (specialBean.getOne_img() != null && specialBean.getOne_img().size() > 0) {
                typeList.add(new DataThreeBean(specialBean.getOne_img().get(0).getImage_url()));
            }

            if (specialBean.getTwo_img() != null && specialBean.getTwo_img().size() > 0) {
                DataFiveBean dataFiveBean = new DataFiveBean();
                dataFiveBean.setAdverBigImg(specialBean.getTwo_img().get(0).getImage_url());
                dataFiveBean.setJump(specialBean.getTwo_img().get(0).getJump_page());
                dataFiveBean.setId(specialBean.getTwo_img().get(0).getExt_id());
                dataFiveBean.setLink(specialBean.getTwo_img().get(0).getLink());
                typeList.add(dataFiveBean);
            }
        }
    }

    /**
     * 秒杀组
     *
     * @param data
     */
    private void setkillType(HomeDataBean data) {
        HomeDataBean.KillBean kill = data.getKill();
        if (kill == null) {
            return;
        }
        if (kill.getTwo_img() == null || kill.getOne_img() == null) {
            return;
        }
        if (kill.getOne_img().size() < 1 || kill.getTwo_img().size() < 1) {
            return;
        }
        for (int i = 0; i < kill.getOne_img().size(); i++) {
            typeList.add(new DataThreeBean(kill.getOne_img().get(i).getImage_url()));
            DataFourBean dataFourBean = new DataFourBean();

            dataFourBean.setLeftBigImg(kill.getTwo_img().get(i).getImage_url());
            dataFourBean.setLeftJump(kill.getTwo_img().get(i).getJump_page());
            dataFourBean.setLeftLink(kill.getTwo_img().get(i).getLink());
            dataFourBean.setLeftId(kill.getTwo_img().get(i).getExt_id());

            if (i + 1 < kill.getTwo_img().size()) {
                dataFourBean.setRightBigImg(kill.getTwo_img().get(i + 1).getImage_url());
                dataFourBean.setRightJump(kill.getTwo_img().get(i + 1).getJump_page());
                dataFourBean.setRightLink(kill.getTwo_img().get(i + 1).getLink());
                dataFourBean.setRightId(kill.getTwo_img().get(i + 1).getExt_id());
            }
            typeList.add(dataFourBean);
        }
    }


    /**
     * 设置自动滚动快讯
     *
     * @param data
     */
    private void setTwoType(HomeDataBean data) {
        if (data.getNews() == null || data.getNews().size() < 1) {
            return;
        }
        typeList.add(new DataTwoBean(data.getNews()));
    }

    /**
     * 设置轮播广告
     *
     * @param data
     */
    private void setOneType(HomeDataBean data) {
        if (data.getBanner() == null || data.getBanner().size() < 1) {
            return;
        }
        typeList.add(new DataOneBean(data.getBanner()));
    }


    /**
     * 上拉加载更多
     */
    private void getMore() {
        HashMap<String, Object> buildMap = new MapUtil()
                .put("page", page)
                .put("row", row)
                .build();
        RxRequest.request(ShowType.SHOW, RxRequest.getClient(getActivity(), buildMap).create(IApiActivityRequest.class).homeRecommend()
                , new BaseSubscriber<TokenBean<List<HomeRefreshBean>>>(getActivity()) {
                    @Override
                    public void onSuccess(TokenBean<List<HomeRefreshBean>> o) {
                        if (goodsPtrgv != null) {
                            goodsPtrgv.onRefreshComplete();
                        }
                        if (o.getData() != null && o.getData().size() > 0) {
                            loadingMore(o.getData());
                        } else if (page > 0) {
                            showToast("加载完啦，没有更多啦！");
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        showToast("数据加载失败，再刷新一下呗！");
                        goodsPtrgv.onRefreshComplete();
                    }
                });
    }

    private void loadingMore(List<HomeRefreshBean> data) {

        setYouLikeType(data);
        homeAdapter.notifyDataSetChanged();
    }

    private void intentWebView(String url) {
        Intent it = new Intent(getActivity(), WebActivity.class);
        it.putExtra("url", url);
        startActivity(it);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && visibleItemCount == 0) {
            return;
        }

        if (firstVisibleItem >= 1) {
            searchView.setVisibility(GONE);
            suspensionSearchIv.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.VISIBLE);
            suspensionSearchIv.setVisibility(View.GONE);
        }

//        View childView = homeListView.getChildAt(firstVisibleItem);
//        if (childView == null) {
//            return;
//        }
//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) searchView.getLayoutParams();
//        titleSignHeight = titleView.getHeight() + searchView.getHeight();
//
//        if (firstVisibleItem == 0) {
//            int offSetY = childView.getBottom() - titleSignHeight;
//            if (offSetY < 0) {
//                layoutParams.topMargin = offSetY;
//                searchView.setLayoutParams(layoutParams);
//            } else {
//                layoutParams.topMargin = 0;
//                searchView.setLayoutParams(layoutParams);
//            }
//        } else {
//            layoutParams.topMargin = 0;
//            searchView.setLayoutParams(layoutParams);
//        }
    }
}
