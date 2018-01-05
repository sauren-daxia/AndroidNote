package com.nanbo.vocationalschools.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.chenzj.baselibrary.base.views.dialog.PhotoSelectDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nanbo.vocationalschools.R;
import com.nanbo.vocationalschools.entity.resulte.UploadPhotoBean;
import com.nanbo.vocationalschools.utils.FrescoLoad;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Jason Chen on 2017/12/11.
 * 上传图片专用
 */

public class UploadPhotoAdapter extends CommonAdapter<UploadPhotoBean> {
    private PhotoSelectDialog dialog;
    private int currentPosotion = 0;

    public UploadPhotoAdapter(Context context, int layoutId, List<UploadPhotoBean> datas) {
        super(context, layoutId, datas);
        dialog = new PhotoSelectDialog();
    }

    @Override
    protected void convert(ViewHolder holder, UploadPhotoBean bean, final int position) {
        SimpleDraweeView fresco = (SimpleDraweeView) holder.getView(R.id.fresco);
        holder.setText(R.id.tv_size, "添加图片(" + (mDatas.size() - 1) + "/3)");
        if (TextUtils.isEmpty(bean.getUrl())) {
            FrescoLoad.loadImg("https://developer.android.com/static/images/footer/logo-twitter.svg", fresco);//非要加一张错误的图片才可以清空上次选择的
            holder.setVisible(R.id.llty_defalut, true);
            holder.setVisible(R.id.iv_del, false);
        } else {
            //加载网络或本地
            FrescoLoad.loadImg(bean.getUrl(), fresco);
            holder.setVisible(R.id.llty_defalut, false);
            holder.setVisible(R.id.iv_del, true);
        }


        holder.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除图片
                deletePhoto(position);
            }
        });

        fresco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加图片
                currentPosotion = position;
                if (mContext instanceof AppCompatActivity && !((AppCompatActivity) mContext).isFinishing()) {
                    dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), 3);
                }
            }
        });

    }

    /**
     * 删除图片
     *
     * @param position
     */
    private void deletePhoto(int position) {
        mDatas.remove(position);
        if (!TextUtils.isEmpty(mDatas.get(mDatas.size() - 1).getUrl())) {
            mDatas.add(new UploadPhotoBean());
        }
        notifyDataSetChanged();
    }

    /**
     * 添加图片
     *
     * @param url
     */
    public void addPhoto(String url) {
        if (TextUtils.isEmpty(mDatas.get(currentPosotion).getUrl())) {
            mDatas.set(currentPosotion, new UploadPhotoBean(url));
            if (currentPosotion < 2) {
                mDatas.add(new UploadPhotoBean());
            }
            currentPosotion++;
            if (currentPosotion == mDatas.size()) {
                currentPosotion = mDatas.size() - 1;
            }
        } else {
            mDatas.set(currentPosotion, new UploadPhotoBean(url));
        }
        notifyDataSetChanged();
    }
}
