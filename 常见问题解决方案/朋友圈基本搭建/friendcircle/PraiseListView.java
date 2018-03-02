package com.dsfa.lovepartybuild.ui.widget.friendcircle;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.dsfa.lovepartybuild.APP;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.model.FriendsCircleBean;

import java.util.List;

/**
 * Created 17/6/1.
 */
public class PraiseListView extends TextView{



    private int itemColor;
    private int itemSelectorColor;
    private List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> datas;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PraiseListView(Context context) {
        super(context);
    }

    public PraiseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public PraiseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = 0xFF546a92;
            itemSelectorColor = 0xFF455C86;

        }finally {
            typedArray.recycle();
        }
    }

    public List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> getDatas() {
        return datas;
    }
    public void setDatas(List<FriendsCircleBean.CHILDRENBean.PARISELISTBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged(){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if(datas != null && datas.size() > 0){
            //添加点赞图标
            builder.append(setImageSpan());
            FriendsCircleBean.CHILDRENBean.PARISELISTBean item = null;
            for (int i=0; i<datas.size(); i++){
                item = datas.get(i);
                if(item != null){
                    builder.append(setClickableSpan(item.getUNAME(), i));
                    if(i != datas.size()-1){
                        builder.append(", ");
                    }
                }
            }
        }

        setText(builder);
        setMovementMethod(new CircleMovementMethod(itemSelectorColor));
    }


    private SpannableString setImageSpan(){
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(APP.getContext(), R.mipmap.icon_zan_cancel, DynamicDrawableSpan.ALIGN_BASELINE),
                0 , 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor){
                                    @Override
                                    public void onClick(View widget) {
                                        if(onItemClickListener!=null){
                                            onItemClickListener.onClick(position);
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }


    public static interface OnItemClickListener{
        public void onClick(int position);
    }

}
