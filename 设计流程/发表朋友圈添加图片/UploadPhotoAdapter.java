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
        if (TextUtils.isEmpty(bean.getUrl())) {
            FrescoLoad.loadImg("", fresco);
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
                    dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "dialog");
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
        } else {
            mDatas.set(currentPosotion, new UploadPhotoBean(url));
        }
        notifyDataSetChanged();
    }

    /**
     * 存储本地图片和网路图片，但只加载本地图片
     */
    public void setPhoto(String url) {
        UploadPhotoBean photoBean = new UploadPhotoBean();
        photoBean.setUrl(url);
        mDatas.set(currentPosotion, photoBean);
        if (mDatas.size() < 3 && mDatas.size() - 1 == currentPosotion) {
            UploadPhotoBean bean = new UploadPhotoBean();
            mDatas.add(bean);
        }
        dialog.dismiss();
        notifyDataSetChanged();
    }
}
