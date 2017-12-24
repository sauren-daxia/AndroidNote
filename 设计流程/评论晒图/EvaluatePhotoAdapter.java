package com.ddtx.kexiansen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ddtx.kexiansen.KeXianSenApplication;
import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.http.OkHttp3Client;
import com.ddtx.kexiansen.model.PictureBean;
import com.ddtx.kexiansen.ui.EvaluateActivity;
import com.ddtx.kexiansen.widget.PicSelectPopupWindow;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


/**
 * Created by 陈志坚 on 2016/10/31.
 */

public class EvaluatePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "EvaluatePhotoAdapter";
    private List<String> imgList;
    private LayoutInflater inflater;
    private Context mContext;
    private PicSelectPopupWindow selectPicPop;
    private LinearLayout window;
    private OkHttp3Client client;
    private List<PictureBean.DataBean> picList;
    private  int photoPosition;              //每个商品的位置
    private String photoIndex;
    public int currentPosition;                  //当前的图片位置
    private ImageLoader imageLoader;


    public EvaluatePhotoAdapter(Context mContext, List<String> imgList, LinearLayout window, int photoPosition) {
        this.mContext = mContext;
        this.imgList = imgList;
        inflater = LayoutInflater.from(mContext);
        this.window = window;
        client = new OkHttp3Client(mContext);
        this.photoPosition = photoPosition;
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_img_item, null);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myHolder = (MyViewHolder) holder;
        myHolder.itemView.setTag(position);
        imageLoader.displayImage("file://"+imgList.get(position),myHolder.imgIv);
        Logger.d("<><><><><><><><position"+imgList.toString());
        if ((imgList.size() - 1) == position) {
            myHolder.deleteIv.setVisibility(GONE);
        } else {
            myHolder.deleteIv.setVisibility(View.VISIBLE);
        }
        if(imgList.get(position).contains("drawable://"+R.drawable.ic_add130)) {
            imageLoader.displayImage("drawable://"+R.drawable.ic_add130,myHolder.imgIv);
            myHolder.deleteIv.setVisibility(GONE);
        }else{
            myHolder.deleteIv.setVisibility(View.VISIBLE);
        }


        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  int position = (int) v.getTag();
                currentPosition = position;
                photoIndex  = photoPosition+"";
                selectPicPop = new PicSelectPopupWindow(mContext, true, 5 - (imgList.size() - 1), new PicSelectPopupWindow.SelectPictureCallback() {
                    @Override
                    public void pictureCallback(List<String> filePaths) {
                        if (filePaths.size() > 0) {
                            showPic(photoPosition,filePaths);
                        }
                    }
                });
                ((EvaluateActivity) mContext).setOnActivityResultListener(new EvaluateActivity.OnActivityResultListener() {
                    @Override
                    public void onResult(int requestCode, int resultCode, Intent data) {
                        selectPicPop.onActivityResult(requestCode,resultCode,data);
                    }
                });
                selectPicPop.showAtLocation(window, Gravity.CENTER, 0, 0);
            }
        });

        myHolder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgList.remove((int)holder.itemView.getTag());
                if(!imgList.contains("drawable://"+R.drawable.ic_add130)){
                    imgList.add(("drawable://"+R.drawable.ic_add130));
                }
                if (notifyListListener != null) {
                    notifyListListener.notifyList(photoPosition, imgList);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void updateList(List<String> list) {
        this.imgList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    /**
     * 展示图片
     * @param files
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPic(final int position ,List<String> files) {

        if (imgList.size() == 1) {
            imgList.add(0, files.get(0));
        } else if (imgList.size() > 1) {
            imgList.set(currentPosition, files.get(0));
            if (!imgList.get(imgList.size() - 1).contains("drawable://"+R.drawable.ic_add130)) {
                imgList.add(("drawable://"+R.drawable.ic_add130));
            }
        }

        if (imgList.size() == 6) {
            imgList.remove(("drawable://"+R.drawable.ic_add130));
        } else if (imgList.size() == 4) {
            if (!imgList.get(3).contains("drawable://"+R.drawable.ic_add130)) {
                imgList.add(("drawable://"+R.drawable.ic_add130));
            }
        }
        List<String> newImgList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            if(!imgList.get(i).contains(("drawable://"+R.drawable.ic_add130))){
                newImgList.add(imgList.get(i));
            }
        }
        Logger.d("img 666= "+newImgList.toString());
        KeXianSenApplication.getInstance().uploadPic(client, newImgList);
        KeXianSenApplication.getInstance().setOnPhotoSrcBackListener(new KeXianSenApplication.OnPhotoSrcBackListener(){
            @Override
            public void onBackSrc(List<PictureBean.DataBean> pi) {
                List<String> newImgList = new ArrayList<String>();
                for (int i = 0; i < pi.size(); i++) {
                    newImgList.add(pi.get(i).getSrc());
                }
                if (notifyListListener != null) {
                    notifyListListener.notifyList(position, newImgList);
                }
                Logger.d("<><><><><><><><"+newImgList.toString());
                updateList(imgList);
            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imgIv;
        ImageView deleteIv;

        MyViewHolder(View view) {
            super(view);
            imgIv = (SimpleDraweeView) view.findViewById(R.id.iv_img);
            deleteIv = (ImageView) view.findViewById(R.id.iv_delete_pic);
        }
    }

    public interface OnNotifyListListener {
        void notifyList(int photoPosition, List<String> list);
    }

    public OnNotifyListListener notifyListListener;

    public void setOnNotifyListListener(OnNotifyListListener onNotifyListListener) {
        this.notifyListListener = onNotifyListListener;
    }

}
