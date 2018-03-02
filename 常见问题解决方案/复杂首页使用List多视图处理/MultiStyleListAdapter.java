package com.ddtx.kexiansen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.base.BaseMultiStyleListAdapter;
import com.ddtx.kexiansen.http.IApiAdapterRequest;
import com.ddtx.kexiansen.model.HomeDataBean;
import com.ddtx.kexiansen.model.ShowType;
import com.ddtx.kexiansen.model.StringBean;
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
import com.ddtx.kexiansen.ui.GoodsBriefActivity;
import com.ddtx.kexiansen.ui.MerchantDetailActivity;
import com.ddtx.kexiansen.ui.NewsActivity;
import com.ddtx.kexiansen.ui.WebActivity;
import com.ddtx.kexiansen.util.FrescoCompressUtils;
import com.ddtx.kexiansen.util.MetricsUtil;
import com.ddtx.kexiansen.widget.AutoScrollViewPager;
import com.ddtx.kexiansen.widget.AutoTextView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rxretrofit.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 陈志坚 on 2016/12/7.
 */

public class MultiStyleListAdapter extends BaseMultiStyleListAdapter<Object> {
    public static final int TYPE_ONE = 1;      //广告轮播
    public static final int TYPE_TWO = 2;      //快讯
    public static final int TYPE_THREE = 3;    //栏目标题
    public static final int TYPE_FOUR = 4;       //秒杀
    public static final int TYPE_FIVE = 5;      //连续的横幅广告
    public static final int TYPE_SIX = 6;      //特卖精品
    public static final int TYPE_SEVEN = 7;      //特卖下横向3个图片
    public static final int TYPE_EIGHT = 8;      //长条广告
    public static final int TYPE_NINE = 9;      //分类
    public static final int TYPE_TEN = 10;      //猜你喜欢标题
    public static final int TYPE_ELEVEN = 11;      //Grid
    private Class[] dataClasses;

    public MultiStyleListAdapter(Context mContext) {
        super(mContext);
        dataClasses = new Class[]{DataOneBean.class, DataTwoBean.class, DataThreeBean.class, DataFourBean.class, DataFiveBean.class
                , DataSixBean.class, DataSevenBean.class, DataEightBean.class, DataNineBean.class, DataTenBean.class, DataElevenBean.class};
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {

            convertView = getStyleTypeOne(position, convertView, parent);
        } else if (viewType == 1) {

            convertView = getStyleTypeTwo(position, convertView, parent);
        } else if (viewType == 2) {

            convertView = getStyleTypeThree(position, convertView, parent);
        } else if (viewType == 3) {

            convertView = getStyleTypeFour(position, convertView, parent);
        } else if (viewType == 4) {

            convertView = getStyleTypeFive(position, convertView, parent);
        } else if (viewType == 5) {

            convertView = getStyleTypeSix(position, convertView, parent);
        } else if (viewType == 6) {

            convertView = getStyleTypeSeven(position, convertView, parent);
        } else if (viewType == 7) {

            convertView = getStyleTypeEight(position, convertView, parent);
        } else if (viewType == 8) {

            convertView = getStyleTypeNine(position, convertView, parent);
        } else if (viewType == 9) {

            convertView = getStyleTypeTen(position, convertView, parent);
        } else if (viewType == 10) {

            convertView = getStyleTypeEleven(position, convertView, parent);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        for (int i = 0, size = dataClasses.length; i < size; i++) {
            if (item.getClass() == dataClasses[i]) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return dataClasses.length;
    }

    /**
     * 轮播广告
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View getStyleTypeOne(final int position, View convertView, ViewGroup parent) {
        ViewHolderOne holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_one, null);
            holder = new ViewHolderOne(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderOne) convertView.getTag();
        }


        DataOneBean dataOneBean = (DataOneBean) mList.get(position);
        HomeBannerVPAdapter adapter = initImgViewPager(dataOneBean.getImgList());
        holder.autoVp.setAdapter(adapter);
        holder.autoVp.setCurrentItem(0);
        addTips(holder.pointLayout, imgPathList.size());
        holder.autoVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tips.length; i++) {
                    if (i == position) {
                        tips[i].setBackgroundResource(R.drawable.ic_dot);
                    } else {
                        tips[i].setBackgroundResource(R.drawable.ic_dot_no);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return convertView;
    }

    /**
     * 给轮播图添加点点
     */
    private void addTips(ViewGroup mViewGroup, int size) {
        tips = new ImageView[size];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
        params.setMargins(5, 0, 5, 0);
        mViewGroup.removeAllViews();
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.ic_dot);
            } else {
                tips[i].setBackgroundResource(R.drawable.ic_dot_no);
            }
            mViewGroup.addView(imageView);
        }
    }

    /**
     * 初始化图片viewpager
     */
    private List<View> mImageViews;
    private List<HomeDataBean.HomeBannerBean> imgPathList;
    private ImageView[] tips;

    private HomeBannerVPAdapter initImgViewPager(List<HomeDataBean.HomeBannerBean> imgList) {
        if (imgList == null || imgList.size() < 1) {
            return null;
        }
        imgPathList = imgList;

        mImageViews = new ArrayList<>();

        try {
            for (int i = 0; i < imgPathList.size(); i++) {
                SimpleDraweeView imageView = new SimpleDraweeView(mContext);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mContext.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setPlaceholderImage(R.drawable.ic_null_home_head)
                        .setFailureImage(R.drawable.ic_null_goods)
                        .build();
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                PointF focusPoint = new PointF(imageView.getWidth() / 2, imageView.getHeight() / 2);
                hierarchy.setActualImageFocusPoint(focusPoint);

                imageView.setHierarchy(hierarchy);
                FrescoCompressUtils.displayImage(mContext, Uri.parse(imgPathList.get(i).getImage_url()), imageView);
                imageView.setMaxWidth(MetricsUtil.getWidth(mContext));
                imageView.setMaxHeight(MetricsUtil.getWidth(mContext) * 4 / 9);
                mImageViews.add(imageView);
            }
        } catch (NullPointerException e) {

        }
        return new HomeBannerVPAdapter(mContext, mImageViews, imgList);


    }


    /**
     * 快讯
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private int autoTextIndex;  //快讯自动翻页的索引

    private View getStyleTypeTwo(final int position, View convertView, ViewGroup parent) {
        ViewHolderTwo holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_two, null);
            holder = new ViewHolderTwo(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderTwo) convertView.getTag();
        }
        DataTwoBean dataTwoBean = (DataTwoBean) mList.get(position);
        holder.autoTv.setTextList(dataTwoBean.getNews());
        holder.autoTv.setOnIndexChangeListener(new AutoTextView.OnIndexChangeListener() {
            @Override
            public void onIndex(int index) {
                autoTextIndex = index;
            }
        });
        holder.newsLlty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                intent.putExtra("newsId", dataTwoBean.getNews().get(autoTextIndex).getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 栏目标题
     */
    private View getStyleTypeThree(final int position, View convertView, ViewGroup parent) {
        ViewHolderThree holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_three, null);
            holder = new ViewHolderThree(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderThree) convertView.getTag();
        }

        DataThreeBean dataThreeBean = (DataThreeBean) mList.get(position);
        if (!TextUtils.isEmpty(dataThreeBean.getTitleImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataThreeBean.getTitleImg()), holder.columnNameFreco); //TODO
        }

        return convertView;
    }

    /**
     * 秒杀
     */
    private View getStyleTypeFour(final int position, View convertView, ViewGroup parent) {
        ViewHolderFour holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_four, null);
            holder = new ViewHolderFour(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderFour) convertView.getTag();
        }

        DataFourBean dataFourBean = (DataFourBean) mList.get(position);
        if (!TextUtils.isEmpty(dataFourBean.getLeftBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataFourBean.getLeftBigImg()), holder.adverOneFreco);
            holder.adverOneFreco.setTag(new String[]{dataFourBean.getLeftJump(), dataFourBean.getLeftId(), dataFourBean.getLeftLink()});
            holder.adverOneFreco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataFourBean.getRightBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataFourBean.getRightBigImg()), holder.adverTwoFreco);
            holder.adverTwoFreco.setTag(new String[]{dataFourBean.getRightJump(), dataFourBean.getRightId(), dataFourBean.getRightLink()});
            holder.adverTwoFreco.setOnClickListener(imgClickListener);
        }

        return convertView;
    }

    /**
     * 连续横幅广告
     */
    private View getStyleTypeFive(final int position, View convertView, ViewGroup parent) {
        ViewHolderFive holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_five, null);
            holder = new ViewHolderFive(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderFive) convertView.getTag();
        }
        DataFiveBean dataFiveBean = (DataFiveBean) mList.get(position);
        if (!TextUtils.isEmpty(dataFiveBean.getAdverBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataFiveBean.getAdverBigImg()), holder.specialSalesFreco);   //TODO
            holder.specialSalesFreco.setTag(new String[]{dataFiveBean.getJump(), dataFiveBean.getId(), dataFiveBean.getLink()});
            holder.specialSalesFreco.setOnClickListener(imgClickListener);
        }

        return convertView;
    }

    /**
     * 精品特卖
     */
    private View getStyleTypeSix(final int position, View convertView, ViewGroup parent) {
        ViewHolderSix holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_six, null);
            holder = new ViewHolderSix(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderSix) convertView.getTag();
        }
        DataSixBean dataSixBean = (DataSixBean) mList.get(position);
        if (!TextUtils.isEmpty(dataSixBean.getLeftBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSixBean.getLeftBigImg()), holder.leftBigPictureFresco);
            holder.leftBigPictureFresco.setTag(new String[]{dataSixBean.getLeftJump(), dataSixBean.getLeftId(), dataSixBean.getLeftLink()});
            holder.leftBigPictureFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataSixBean.getTopRightImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSixBean.getTopRightImg()), holder.topRightPictureFreco);
            holder.topRightPictureFreco.setTag(new String[]{dataSixBean.getTopRightJump(), dataSixBean.getTopRightId(), dataSixBean.getTopRightLink()});
            holder.topRightPictureFreco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataSixBean.getBottomRightImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSixBean.getBottomRightImg()), holder.bottomRightPictureFreco);
            holder.bottomRightPictureFreco.setTag(new String[]{dataSixBean.getBottomRightJump(), dataSixBean.getBottomRightId(), dataSixBean.getTopRightLink()});
            holder.bottomRightPictureFreco.setOnClickListener(imgClickListener);
        }


        return convertView;
    }

    /**
     * 精品特卖下面横向三个广告图
     */
    private View getStyleTypeSeven(final int position, View convertView, ViewGroup parent) {
        ViewHolderSeven holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_seven, null);
            holder = new ViewHolderSeven(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderSeven) convertView.getTag();
        }
        DataSevenBean dataSevenBean = (DataSevenBean) mList.get(position);
        if (!TextUtils.isEmpty(dataSevenBean.getOneImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSevenBean.getOneImg()), holder.oneAdverFresco);
            holder.oneAdverFresco.setTag(new String[]{dataSevenBean.getOneJump(), dataSevenBean.getOneId(), dataSevenBean.getOneLink()});
            holder.oneAdverFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataSevenBean.getTwoImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSevenBean.getTwoImg()), holder.twoAdverFresco);
            holder.twoAdverFresco.setTag(new String[]{dataSevenBean.getTwoJump(), dataSevenBean.getTwoId(), dataSevenBean.getTwoLink()});
            holder.twoAdverFresco.setOnClickListener(imgClickListener);
        }

        if (!TextUtils.isEmpty(dataSevenBean.getThreeImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataSevenBean.getThreeImg()), holder.threeAdverFresco);
            holder.threeAdverFresco.setTag(new String[]{dataSevenBean.getThreeJump(), dataSevenBean.getThreeId(), dataSevenBean.getThreeLink()});
            holder.threeAdverFresco.setOnClickListener(imgClickListener);
        }

        return convertView;
    }

    /**
     * 横幅广告
     */
    private View getStyleTypeEight(final int position, View convertView, ViewGroup parent) {
        ViewHolderEight holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_eight, null);
            holder = new ViewHolderEight(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderEight) convertView.getTag();
        }

        DataEightBean dataEightBean = (DataEightBean) mList.get(position);
        if (!TextUtils.isEmpty(dataEightBean.getBannerImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataEightBean.getBannerImg()), holder.bannerFresco);
            holder.bannerFresco.setTag(new String[]{dataEightBean.getBannerJump(), dataEightBean.getBannerId(), dataEightBean.getBannerLink()});
            holder.bannerFresco.setOnClickListener(imgClickListener);
        }
        return convertView;
    }

    /**
     * 分类
     */
    private View getStyleTypeNine(final int position, View convertView, ViewGroup parent) {
        ViewHolderNine holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_nine, null);
            holder = new ViewHolderNine(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderNine) convertView.getTag();
        }

        DataNineBean dataNineBean = (DataNineBean) mList.get(position);
        if (!TextUtils.isEmpty(dataNineBean.getLeftBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataNineBean.getLeftBigImg()), holder.leftBigPictureFresco);
            holder.leftBigPictureFresco.setTag(new String[]{dataNineBean.getLeftJump(), dataNineBean.getLeftId(), dataNineBean.getLeftLink()});
            holder.leftBigPictureFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataNineBean.getTopLeftImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataNineBean.getTopLeftImg()), holder.topLeftFresco);
            holder.topLeftFresco.setTag(new String[]{dataNineBean.getTopLeftJump(), dataNineBean.getTopLeftId(), dataNineBean.getTopLeftLink()});
            holder.topLeftFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataNineBean.getTopRightImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataNineBean.getTopRightImg()), holder.topRightFresco);
            holder.topRightFresco.setTag(new String[]{dataNineBean.getTopRightJump(), dataNineBean.getTopRightId(), dataNineBean.getTopRightLink()});
            holder.topRightFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataNineBean.getBottomLeftImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataNineBean.getBottomLeftImg()), holder.bottomLeftFresco);
            holder.bottomLeftFresco.setTag(new String[]{dataNineBean.getBottomLeftJump(), dataNineBean.getBottomLeftId(), dataNineBean.getBottomLeftLink()});
            holder.bottomLeftFresco.setOnClickListener(imgClickListener);
        }
        if (!TextUtils.isEmpty(dataNineBean.getBottomRightImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataNineBean.getBottomRightImg()), holder.bottomRightFresco);
            holder.bottomRightFresco.setTag(new String[]{dataNineBean.getBottomRightJump(), dataNineBean.getBottomRightId(), dataNineBean.getBottomRightLink()});
            holder.bottomRightFresco.setOnClickListener(imgClickListener);
        }
        return convertView;
    }

    /**
     * 手动设置猜你喜欢标题图片
     */
    private View getStyleTypeTen(final int position, View convertView, ViewGroup parent) {
        ViewHolderTen holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_ten, null);
            holder = new ViewHolderTen(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderTen) convertView.getTag();
        }
        DataTenBean dataTenBean = (DataTenBean) mList.get(position);
        if (!TextUtils.isEmpty(dataTenBean.getYouLikeTitleImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataTenBean.getYouLikeTitleImg()), holder.titleFresco);
        }
        return convertView;
    }

    /**
     * 猜你喜欢商品
     */
    private View getStyleTypeEleven(final int position, View convertView, ViewGroup parent) {
        ViewHolderEleven holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home_type_eleven, null);
            holder = new ViewHolderEleven(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderEleven) convertView.getTag();
        }
        DataElevenBean dataElevenBean = (DataElevenBean) mList.get(position);
        if (!TextUtils.isEmpty(dataElevenBean.getLeftBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataElevenBean.getLeftBigImg()), holder.leftGoodsFresco);
            holder.leftNameTv.setText(dataElevenBean.getLeftGoodsName());                                  //标题
            holder.leftPriceTv.setText("¥" + dataElevenBean.getLeftPrice());                               //价钱
            holder.leftOldPriceTv.setText("¥" + dataElevenBean.getLeftOldPrice());                            //原价
            holder.leftOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线并加清晰
            holder.leftAddCartIv.setTag(dataElevenBean.getLeftGoodsId());
            holder.leftAddCartIv.setOnClickListener(addCartClickListener);
            holder.leftGoodsFresco.setTag(dataElevenBean.getLeftGoodsId());
            holder.leftGoodsFresco.setOnClickListener(intentGoodsClickListener);
            if (dataElevenBean.getLeftIs_specia() == 1) {
                holder.leftLabel.setVisibility(View.VISIBLE);
            } else {
                holder.leftLabel.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(dataElevenBean.getRightBigImg())) {
            FrescoCompressUtils.displayImage(mContext, Uri.parse(dataElevenBean.getRightBigImg()), holder.rightGoodsFresco);
            holder.rightNameTv.setText(dataElevenBean.getRightGoodsName());                                  //标题
            holder.rightPriceTv.setText("¥" + dataElevenBean.getRightPrice());                               //价钱
            holder.rightOldPriceTv.setText("¥" + dataElevenBean.getRightOldPrice());                            //原价
            holder.rightOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线并加清晰
            holder.rightAddCartIv.setTag(dataElevenBean.getRightGoodsId());
            holder.rightAddCartIv.setOnClickListener(addCartClickListener);
            holder.rightGoodsFresco.setTag(dataElevenBean.getRightGoodsId());
            holder.rightGoodsFresco.setOnClickListener(intentGoodsClickListener);
            if (dataElevenBean.getLeftIs_specia() == 1) {
                holder.rightLabel.setVisibility(View.VISIBLE);
            } else {
                holder.rightLabel.setVisibility(View.GONE);
            }
        }

        return convertView;
    }


    class ViewHolderOne {
        private AutoScrollViewPager autoVp;

        private LinearLayout pointLayout;

        public ViewHolderOne(View view) {

            autoVp = (AutoScrollViewPager) view.findViewById(R.id.vp_home);
            pointLayout = (LinearLayout) view.findViewById(R.id.llyt_vp_dot);
        }
    }

    static class ViewHolderTwo {
        private AutoTextView autoTv;
        private LinearLayout newsLlty;

        public ViewHolderTwo(View view) {
            autoTv = (AutoTextView) view.findViewById(R.id.tv_adver);
            newsLlty = (LinearLayout) view.findViewById(R.id.llty_news);
        }
    }

    static class ViewHolderThree {
        private SimpleDraweeView columnNameFreco;

        public ViewHolderThree(View view) {
            columnNameFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_column_name);
        }
    }

    static class ViewHolderFour {
        private SimpleDraweeView adverOneFreco, adverTwoFreco;

        public ViewHolderFour(View view) {
            adverOneFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_kill_adver1);
            adverTwoFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_kill_adver2);
        }
    }

    static class ViewHolderFive {
        private SimpleDraweeView specialSalesFreco;

        public ViewHolderFive(View view) {
            specialSalesFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_special_sales);
        }
    }

    static class ViewHolderSix {
        SimpleDraweeView leftBigPictureFresco, topRightPictureFreco, bottomRightPictureFreco;

        public ViewHolderSix(View view) {
            leftBigPictureFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_left_big_picture);
            topRightPictureFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_top_right_picture);
            bottomRightPictureFreco = (SimpleDraweeView) view.findViewById(R.id.fresco_bottom_right_picture);
        }
    }

    static class ViewHolderSeven {
        SimpleDraweeView oneAdverFresco, twoAdverFresco, threeAdverFresco;

        public ViewHolderSeven(View view) {
            oneAdverFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_one_adver);
            twoAdverFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_two_adver);
            threeAdverFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_three_adver);
        }
    }

    static class ViewHolderEight {
        SimpleDraweeView bannerFresco;

        public ViewHolderEight(View view) {
            bannerFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_banner);
        }
    }

    static class ViewHolderNine {
        private SimpleDraweeView leftBigPictureFresco, topLeftFresco, bottomLeftFresco, topRightFresco, bottomRightFresco;

        public ViewHolderNine(View view) {
            leftBigPictureFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_left_big_picture);
            topLeftFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_top_left);
            bottomLeftFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_bottom_left);
            topRightFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_top_right);
            bottomRightFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_bottom_right);
        }
    }

    static class ViewHolderTen {
        SimpleDraweeView titleFresco;

        public ViewHolderTen(View view) {
            titleFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_title);
        }
    }

    static class ViewHolderEleven {
        TextView leftNameTv, rightNameTv, leftPriceTv, rightPriceTv, leftOldPriceTv, rightOldPriceTv;
        ImageView leftAddCartIv, rightAddCartIv, leftLabel, rightLabel;
        SimpleDraweeView leftGoodsFresco, rightGoodsFresco;

        public ViewHolderEleven(View view) {
            leftAddCartIv = (ImageView) view.findViewById(R.id.iv_left_addcart);
            leftGoodsFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_left_goods);
            leftNameTv = (TextView) view.findViewById(R.id.tv_left_goods_name);
            leftPriceTv = (TextView) view.findViewById(R.id.tv_left_price);
            leftOldPriceTv = (TextView) view.findViewById(R.id.tv_left_old_price);
            leftLabel = (ImageView) view.findViewById(R.id.iv_left_label);

            rightAddCartIv = (ImageView) view.findViewById(R.id.iv_right_addcart);
            rightGoodsFresco = (SimpleDraweeView) view.findViewById(R.id.fresco_right_goods);
            rightNameTv = (TextView) view.findViewById(R.id.tv_right_goods_name);
            rightPriceTv = (TextView) view.findViewById(R.id.tv_right_price);
            rightOldPriceTv = (TextView) view.findViewById(R.id.tv_right_old_price);
            rightLabel = (ImageView) view.findViewById(R.id.iv_right_label);
        }
    }

    private View.OnClickListener intentGoodsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            Intent intent = new Intent(mContext, GoodsBriefActivity.class);
            intent.putExtra("gid", tag);
            mContext.startActivity(intent);
        }
    };

    private View.OnClickListener imgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            arr[0] = jump
            arr[1] = id;
            arr[2] = link
             */
            String[] arr = (String[]) v.getTag();

            String jump_page = arr[0];
            Intent intent = new Intent();
            if (jump_page.equals("1")) {
                intent = new Intent(mContext, GoodsBriefActivity.class);
                intent.putExtra("gid", arr[1]);

            } else if (jump_page.equals("2")) {
                intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtra("id", arr[1]);

            } else if (jump_page.equals("3")) {
                intentWebView(arr[2]);
                return;
            }
            mContext.startActivity(intent);
        }
    };

    private View.OnClickListener addCartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            HashMap<String, Object> hashMap = new MapUtil().put("gid", id)        //TODO
                    .put("num", 1)
                    .put("action", 2)
                    .put("attrs_1_unite_code", "")
                    .put("attrs_2_unite_code", "")
                    .put("attrs_3_unite_code", "")
                    .build();
            RxRequest.request(ShowType.DISMISS
                    , RxRequest.getClient(mContext, hashMap).create(IApiAdapterRequest.class).addShoppIngCart()
                    , new BaseSubscriber<TokenBean<StringBean>>(mContext) {
                        @Override
                        public void onSuccess(TokenBean<StringBean> o) {
                            if (o.getData() != null) {
                                Toast.makeText(mContext, "加入购物车成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {

                        }
                    });
        }
    };

    private void intentWebView(String url) {
        Intent it = new Intent(mContext, WebActivity.class);
        it.putExtra("url", url);
        mContext.startActivity(it);
    }
}
