package bwf.flowlayouttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MyAdapter extends BaseAdapter {
    private ArrayList<Data> list ;
    private LayoutInflater inflater;
    private Context mContext;
    public MyAdapter(ArrayList<Data> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item,null);
            holder.mLayout = (FlowLayout) convertView.findViewById(R.id.flowLayout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i < list.get(position).arr.length; i++) {
            TextView text = (TextView) inflater.inflate(R.layout.layout_item_item,null);
            text.setText(list.get(position).getArr()[i]);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            params.leftMargin = 10;
            params.topMargin = 5;
            params.bottomMargin =5;
            holder.mLayout.addView(text,params);
        }

//        for (int i = 0; i < holder.mLayout.getChildCount(); i++) {
//            TextView text = (TextView) holder.mLayout.getChildAt(i);
//            text.setText(list.get(position).getArr()[i]);
//        }

        return convertView;
    }

    public static class ViewHolder{
        FlowLayout mLayout;
    }
}
