package app.pinggushi.bjesc.com.baserecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import app.pinggushi.bjesc.com.baserecyclerview.adapter.base.ItemViewDelegate;
import app.pinggushi.bjesc.com.baserecyclerview.adapter.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convertView(holder, t, position);
            }
        });
    }

    protected abstract void convertView(ViewHolder holder, T t, int position);


}
