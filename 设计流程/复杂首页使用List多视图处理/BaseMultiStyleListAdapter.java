package com.ddtx.kexiansen.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志坚 on 2016/12/7.
 */

public abstract class BaseMultiStyleListAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected Context mContext;
    private AdapterView mListView;
    ;
    private LayoutInflater mInflater;

    public BaseMultiStyleListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        this.mList = list;
    }

    public List<T> getList() {
        return this.mList;
    }

    public AdapterView getListView() {
        return this.mListView;
    }

    public void setListView(AdapterView listView) {
        this.mListView = listView;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setList(T[] list) {
        if (list == null) {
            return;
        }
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }
    public void clear(){
        if(mList!= null && mList.size()> 0){
            mList.clear();
        }
    }
}
