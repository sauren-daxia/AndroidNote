package com.dsfa.lovepartybuild.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.dsfa.common.api.service.HttpCallback;
import com.dsfa.common.ui.plugins.MPhotoActivity;
import com.dsfa.common.ui.widget.MTitleBar;
import com.dsfa.lovepartybuild.R;
import com.dsfa.lovepartybuild.api.ApiRequestCZJ;
import com.dsfa.lovepartybuild.common.UserSession;
import com.dsfa.lovepartybuild.model.PhotoUploadBean;
import com.dsfa.lovepartybuild.ui.adapter.PhotoUploadAdapter;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hp on 2017/6/6.
 */
public class PublishFriendsCircleActivity extends MPhotoActivity {

    @Bind(R.id.mt_bar)
    MTitleBar barMt;
    @Bind(R.id.recycler_photo)
    RecyclerView photoRecycler;
    @Bind(R.id.edit_content)
    EditText contentEt;
    private List<PhotoUploadBean> photoList;
    private PhotoUploadAdapter photoAdapter;
    private String ACT_ID;      //活动ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_friends_circle);
        ButterKnife.bind(this);
        ACT_ID = getIntent().getStringExtra("ACT_ID");
//        ACT_ID ="ce31fff4929a489d8f545d2ea4379db3";
        initBar();
        initPhoto();
    }

    private void initBar() {
        barMt.setLeftImg(R.mipmap.back);
        barMt.setLeftBtnOnClickListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        barMt.setTitle("发布活动圈");
        barMt.setRightText("发布");
        barMt.setRightBtnOnClickListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicsh();
            }
        });
    }


    /**
     * 初始化图片上传
     */
    private void initPhoto() {
        photoList = new ArrayList<>();
        photoList.add(new PhotoUploadBean(R.mipmap.ic_btn_camera));
        photoAdapter = new PhotoUploadAdapter(this, R.layout.item_photo_exhibition, photoList);
        photoRecycler.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        photoRecycler.setAdapter(photoAdapter);
    }


    @Override
    public void takeSuccess(TResult result) {
        if (photoAdapter != null) {
            photoAdapter.addImg(result.getImage().getOriginalPath());
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }


    /**
     * 发布朋友圈
     */
    private void publicsh() {
        ApiRequestCZJ.publishFriendsCircle(UserSession.getInstance().getAccount().getId(), contentEt.getText().toString().trim(), ACT_ID, photoList, new HttpCallback() {
            @Override
            public void success(Object model) {
                setResult(65);
                onBackPressed();
            }

            @Override
            public void fail(HttpError dsfaError) {

            }
        });
    }
}
