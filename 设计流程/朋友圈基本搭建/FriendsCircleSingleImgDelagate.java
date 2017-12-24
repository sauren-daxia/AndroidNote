package com.dsfa.lovepartybuild.delegate;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsfa.common.base.adapter.base.ViewHolder;
import com.dsfa.lovepartybuild.Navigator;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.common.GlideUtils;
import com.dsfa.lovepartybuild.model.FriendsCircleBean;
import com.dsfa.lovepartybuild.model.ImageContent;
import com.dsfa.lovepartybuild.model.discovery.NewsImage;
import com.dsfa.lovepartybuild.ui.adapter.MultiItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/6/5.
 * 朋友圈一张图或无图
 */
public class FriendsCircleSingleImgDelagate extends BaseFriendsCircleDalegate {


    public FriendsCircleSingleImgDelagate(Context con, List<FriendsCircleBean.CHILDRENBean> list, View editLayout, View sendView, EditText commentEt, LinearLayoutManager layoutManager, MultiItemAdapter adapter, PulishCommentListener listener) {
        super(con, list, editLayout, sendView, commentEt, layoutManager, adapter, listener);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_friends_circle_singleimg;
    }

    @Override
    public boolean isForViewType(FriendsCircleBean.CHILDRENBean item, int position) {
        return item != null && item.getIMGLIST() == null || (item.getIMGLIST() != null && item.getIMGLIST().size() <= 1);
    }

    @Override
    public void convert(final ViewHolder holder, final FriendsCircleBean.CHILDRENBean o, final int position) {
        super.convert(holder, o, position);
        ImageView photoImg = (ImageView) holder.getView(R.id.iv_singleimg);

        if (o.getIMGLIST() != null) {
            photoImg.setVisibility(View.VISIBLE);
            GlideUtils.loadNotPlaceholderImg(con, o.getIMGLIST().get(0).getIMGURL(), photoImg);
        }else{
            photoImg.setVisibility(View.GONE);
        }

        photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NewsImage> newsImages = new ArrayList<>();
                newsImages.add(new NewsImage("",o.getIMGLIST().get(0).getIMGURL()));
                ImageContent imageContent = new ImageContent(newsImages);
                Navigator.startSketchActivity(con,imageContent);
            }
        });
    }


}
