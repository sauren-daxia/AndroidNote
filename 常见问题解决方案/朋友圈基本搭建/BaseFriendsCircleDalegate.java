package com.dsfa.lovepartybuild.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dsfa.common.api.service.HttpCallback;
import com.dsfa.common.base.adapter.base.ItemViewDelegate;
import com.dsfa.common.base.adapter.base.ViewHolder;
import com.dsfa.common.ui.widget.CircleImageView;
import com.dsfa.common.utils.util.DiffCallBackUtil;
import com.dsfa.common.utils.util.DisplayUtil;
import com.dsfa.common.utils.util.KeyboardUtils;
import com.dsfa.common.utils.util.MapUtils;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.api.ApiRequestCZJ;
import com.dsfa.lovepartybuild.common.GlideUtils;
import com.dsfa.lovepartybuild.common.UserSession;
import com.dsfa.lovepartybuild.model.FriendsCircleBean;
import com.dsfa.lovepartybuild.ui.adapter.MultiItemAdapter;
import com.dsfa.lovepartybuild.ui.widget.friendcircle.CommentListView;
import com.dsfa.lovepartybuild.ui.widget.friendcircle.ExpandTextView;
import com.dsfa.lovepartybuild.ui.widget.friendcircle.PraiseListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hp on 2017/6/7.
 */
public abstract class BaseFriendsCircleDalegate implements ItemViewDelegate<FriendsCircleBean.CHILDRENBean> {
    protected Context con;
    protected MultiItemAdapter adapter;
    protected List<FriendsCircleBean.CHILDRENBean> list;
    protected View editLayout;
    protected View sendView;
    protected EditText commentEt;
    protected int commentType;        // 0 == 评论  1 == 回复
    protected int currentPosition;    //当前position
    protected LinearLayoutManager layoutManager;
    protected int selectCircleItemH;
    protected int selectCommentItemOffset;
    protected String FID = "";         //回复的ID
    protected String PID = "";        //回复内容的ID
    protected String FUNAME = "";       //回复对方的名字
    protected PulishCommentListener listener;
    private long lastTime;          //上一次点击的时间，防止快速点赞

    public BaseFriendsCircleDalegate(Context con, List<FriendsCircleBean.CHILDRENBean> list, View editLayout,
                                     View sendView, EditText commentEt, LinearLayoutManager layoutManager, MultiItemAdapter adapter, PulishCommentListener listener) {
        this.con = con;
        this.list = list;
        this.editLayout = editLayout;
        this.commentEt = commentEt;
        this.sendView = sendView;
        this.layoutManager = layoutManager;
        this.adapter = adapter;
        this.listener = listener;

    }


    @Override
    public void convert(ViewHolder holder, final FriendsCircleBean.CHILDRENBean o, final int position) {
        final CircleImageView view = (CircleImageView) holder.getView(R.id.civ_head);
        final CommentListView comm = (CommentListView) holder.getView(R.id.commentList);
        ExpandTextView expandTextView = (ExpandTextView) holder.getView(R.id.tv_expand);
        PraiseListView praiseListView = (PraiseListView) holder.getView(R.id.list_appreciate);
        final TextView praiseTv = (TextView) holder.getView(R.id.tv_praise);


        holder.setText(R.id.tv_name, o.getUNAME());            //名字
        GlideUtils.loadActiveImg(con, o.getHEADIMAGEURL(), view);      //头像
        expandTextView.setText(o.getCONTENT());     //说说


        if (o.getPARISELIST() != null && o.getPARISELIST().size() > 0) {
            praiseListView.setVisibility(View.VISIBLE);
            praiseListView.setDatas(o.getPARISELIST());    //点赞

            praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                @Override
                public void onClick(int position) {

                }
            });
        } else {
            praiseListView.setVisibility(View.GONE);
        }

        if (o.getCOMMENTLIST() != null && o.getCOMMENTLIST().size() > 0) {
            comm.setVisibility(View.VISIBLE);
            comm.setDatas(o.getCOMMENTLIST());      //评论内容
            comm.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int clickPosition) {
                    //弹出对话框
                    if(o.getCOMMENTLIST().get(clickPosition).getUSERID().equals(UserSession.getInstance().getAccount().getId())){
                        return;
                    }
                    commentType = 1;
                    currentPosition = position;
                    FID = o.getCOMMENTLIST().get(clickPosition).getACTCON_ID();
                    PID = o.getCOMMENTLIST().get(clickPosition).getPID();
                    FUNAME = o.getCOMMENTLIST().get(clickPosition).getFUNAME();
                    if(TextUtils.isEmpty(FUNAME)){
                        FUNAME = o.getCOMMENTLIST().get(clickPosition).getUNAME();
                    }
                    commentEt.setHint("对" + FUNAME + "说点什么...");
                    if (listener != null) {
                        listener.publish(FID, PID, currentPosition);
                    }
                    updateEditTextBodyVisible(View.VISIBLE, currentPosition, clickPosition);

                }
            });
        }else{
            comm.setVisibility(View.GONE);
        }

        if (praiseListView.getVisibility() == View.VISIBLE ||comm.getVisibility() == View.VISIBLE) {
            holder.setVisible(R.id.digCommentBody, true);
        }else{
            holder.setVisible(R.id.digCommentBody, false);
        }

        if (praiseListView.getVisibility() == View.VISIBLE && comm.getVisibility() == View.VISIBLE) {
            holder.setVisible(R.id.lin_dig, true);
        }else{
            holder.setVisible(R.id.lin_dig, false);
        }


        if (o.getPARISELIST() != null && o.getPARISELIST().size() > 0) {
            boolean isPraise = false;
            for (int i = 0; i < o.getPARISELIST().size(); i++) {
                if (o.getPARISELIST().get(i).getUSERID().equals(UserSession.getInstance().getAccount().getId())) {
                    isPraise = true;
                    break;
                }
            }
            if (isPraise) {
                changePraiseAddDrawable(praiseTv, o);
            } else {
                changPraiseCancelDrawable(praiseTv, o);
            }
        } else {
            changPraiseCancelDrawable(praiseTv, o);
        }
        holder.setOnClickListener(R.id.tv_praise, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                if (lastTime + 500 < System.currentTimeMillis()) {
                    lastTime = System.currentTimeMillis();
                } else {
                    return;
                }
                if (o.isPraise()) {   //已经点赞
                    cancelPraise(praiseTv, o.getPID(), o.getPARISELIST(), o, position);
                } else {          //没有点赞
                    addPraise(praiseTv, o.getPID(), o.getPARISELIST(), o, position);
                }
            }
        });
        holder.setOnClickListener(R.id.tv_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                commentType = 0;
                currentPosition = position;
                FID = "0";
                PID = o.getPID();
                commentEt.setHint("对他说点什么...");
                if (listener != null) {
                    listener.publish(FID, PID, currentPosition);
                }
                updateEditTextBodyVisible(View.VISIBLE, currentPosition, 0);
            }
        });
    }

    /**
     * 添加点赞
     *
     * @param pid
     */
    private void addPraise(final TextView view, final String pid, List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> pariselist, final FriendsCircleBean.CHILDRENBean bean, final int position) {
        if (pariselist == null) {
            pariselist = new ArrayList<FriendsCircleBean.CHILDRENBean.PARISELISTBean>();
        }
        final List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> finalPariselist = pariselist;

        HashMap<String, Object> requestMap = MapUtils.Builder()
                .put("USERID", UserSession.getInstance().getAccount().getId())
                .put("CHAT_ID", pid)
                .builder();

        ApiRequestCZJ.addPraise(requestMap, new HttpCallback() {
            @Override
            public void fail(HttpError dsfaError) {

            }

            @Override
            public void success(Object model) {
                refreshSingleFriendsCircle(pid, position);
            }
        });
    }


    /**
     * 修改点赞控件为已经点赞的图片
     */
    private void changePraiseAddDrawable(TextView view, FriendsCircleBean.CHILDRENBean bean) {
        bean.setPraise(true);
        view.setText("取消");
        Drawable drawable = con.getResources().getDrawable(R.mipmap.icon_zan);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(DisplayUtil.dip2px(con, 5));
    }

    private void changPraiseCancelDrawable(TextView view, FriendsCircleBean.CHILDRENBean bean) {
        bean.setPraise(false);
        view.setText("点赞");
        Drawable drawable = con.getResources().getDrawable(R.mipmap.icon_zan_cancel);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(DisplayUtil.dip2px(con, 5));

    }

    /**
     * 取消点赞
     *
     * @param pid
     */
    private void cancelPraise(final TextView view, final String pid, List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> pariselist, final FriendsCircleBean.CHILDRENBean bean, final int position) {
        if (pariselist == null) {
            pariselist = new ArrayList<FriendsCircleBean.CHILDRENBean.PARISELISTBean>();
        }
        final List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> finalPariselist = pariselist;

        HashMap<String, Object> requestMap = MapUtils.Builder()
                .put("USERID", UserSession.getInstance().getAccount().getId())
                .put("CHAT_ID", pid)
                .builder();

        ApiRequestCZJ.cancelPraise(requestMap, new HttpCallback() {
            @Override
            public void fail(HttpError dsfaError) {

            }

            @Override
            public void success(Object model) {
                refreshSingleFriendsCircle(pid, position);
            }
        });
    }


    private void updateEditTextBodyVisible(int visibility, int position, int commentPosition) {
        editLayout.setVisibility(visibility);
        if (position != -1) {
            measureCircleItemHighAndCommentItemOffset(position, commentPosition);
        }
        if (View.VISIBLE == visibility) {
            commentEt.requestFocus();
            //弹出键盘
            KeyboardUtils.showSoftInput(con, commentEt);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            KeyboardUtils.hideSoftInput(con, commentEt);
        }
    }

    /**
     * 计算点击的Item的位置
     *
     * @param position
     * @param clickCommentItem
     */
    private void measureCircleItemHighAndCommentItemOffset(int position, int clickCommentItem) {
        if (position == -1)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(position - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }
        //回复评论的情况
        CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
        if (commentLv != null) {
            //找到要回复的评论view,计算出该view距离所属动态底部的距离
            View selectCommentItem = commentLv.getChildAt(clickCommentItem);
            if (selectCommentItem != null) {
                //选择的commentItem距选择的CircleItem底部的距离
                selectCommentItemOffset = 0;
                View parentView = selectCommentItem;
                do {
                    int subItemBottom = parentView.getBottom();
                    parentView = (View) parentView.getParent();
                    if (parentView != null) {
                        selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                    }
                } while (parentView != null && parentView != selectCircleItem);
            }
        }
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
//                    List<FriendsCircleBean.CHILDRENBean> newList = new ArrayList<FriendsCircleBean.CHILDRENBean>( new  ArrayList(Arrays.asList( new Object[list.size()])));
//                    Collections.copy(newList,list);
//                    newList.set(position, model.getCHILDREN().get(0));
//                    refresh(newList);


                    list.set(position, model.getCHILDREN().get(0));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void fail(HttpError dsfaError) {

            }
        });
    }

    public interface PulishCommentListener {
        void publish(String FID, String PID, int currentPosition);

    }

    /**
     * 刷新，没用....
     * @param newList
     */
    protected void refresh(List<FriendsCircleBean.CHILDRENBean> newList){
        DiffCallBackUtil diffCallBack = new DiffCallBackUtil(list, newList){};
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack,true);
        adapter.setData(newList);
        diffResult.dispatchUpdatesTo(adapter);
    }

}
