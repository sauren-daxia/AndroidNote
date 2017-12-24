package com.dsfa.lovepartybuild.delegate;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsfa.common.base.adapter.CommonAdapter;
import com.dsfa.common.base.adapter.MultiItemTypeAdapter;
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
 * 朋友圈四张图
 */
public class FriendsCircleFourImgDelagate extends BaseFriendsCircleDalegate {


    public FriendsCircleFourImgDelagate(Context con, List<FriendsCircleBean.CHILDRENBean> list, View editLayout, View sendView, EditText commentEt, LinearLayoutManager layoutManager, MultiItemAdapter adapter, PulishCommentListener listener) {
        super(con, list, editLayout, sendView, commentEt, layoutManager, adapter, listener);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_friends_circle_fourimg;
    }

    @Override
    public boolean isForViewType(FriendsCircleBean.CHILDRENBean item, int position) {
        return item != null && item.getIMGLIST()!=null && item.getIMGLIST().size() > 1;
    }

    @Override
    public void convert(ViewHolder holder,final FriendsCircleBean.CHILDRENBean o, int position) {
        super.convert(holder, o, position);

        RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.recycler_img);
        if(o.getIMGLIST().size() == 4){
            recyclerView.setLayoutManager(new GridLayoutManager(con,2, LinearLayoutManager.VERTICAL,false));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(con,3, LinearLayoutManager.VERTICAL,false));
        }
        CommonAdapter<FriendsCircleBean.CHILDRENBean.IMGLISTBean> photoAdapter =  new CommonAdapter<FriendsCircleBean.CHILDRENBean.IMGLISTBean>(con, R.layout.item_friends_circle_img,o.getIMGLIST()) {

            @Override
            protected void convertView(ViewHolder holder, FriendsCircleBean.CHILDRENBean.IMGLISTBean bean, int position) {
                ImageView view = (ImageView) holder.getView(R.id.iv_photo);
                GlideUtils.loadNotPlaceholderImg(con,bean.getIMGURL(),view);
            }
        };
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ArrayList<NewsImage> newsImages = new ArrayList<>();
                for (int i = 0; i < o.getIMGLIST().size(); i++) {
                    newsImages.add(new NewsImage("",o.getIMGLIST().get(i).getIMGURL()));
                }
                ImageContent imageContent = new ImageContent(newsImages);
                imageContent.setStartIndex(position);
                Navigator.startSketchActivity(con,imageContent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
}
