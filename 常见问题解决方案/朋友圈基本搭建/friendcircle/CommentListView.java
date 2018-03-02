package com.dsfa.lovepartybuild.ui.widget.friendcircle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.model.FriendsCircleBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {

    private int itemColor = 0xFF546a92;
    private int itemSelectorColor = 0xFF455C86;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<FriendsCircleBean.CHILDRENBean.COMMENTLISTBean> mDatas;
    private LayoutInflater layoutInflater;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<FriendsCircleBean.CHILDRENBean.COMMENTLISTBean> datas) {
        if (datas == null) {
            datas = new ArrayList<FriendsCircleBean.CHILDRENBean.COMMENTLISTBean>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<FriendsCircleBean.CHILDRENBean.COMMENTLISTBean> getDatas() {
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            final int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.include_custom_friends_circle_reply_name_text, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        final FriendsCircleBean.CHILDRENBean.COMMENTLISTBean bean = mDatas.get(position);
        String name = bean.getUNAME();
        String id = bean.getUSERID();
        String toReplyName = bean.getFUNAME();


        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, id));

        if (!TextUtils.isEmpty(toReplyName)) {
            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getFUSERID()));
        }
        builder.append(": ");
        String contentBodyStr = bean.getCONTENT();
        builder.append(contentBodyStr);
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    circleMovementMethod.cancelLongClickcStyle();
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }



    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        //点击用户名

                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }



    public  interface OnItemClickListener {
         void onItemClick(int position);
    }

    public  interface OnItemLongClickListener {
         void onItemLongClick(int position);
    }


}
