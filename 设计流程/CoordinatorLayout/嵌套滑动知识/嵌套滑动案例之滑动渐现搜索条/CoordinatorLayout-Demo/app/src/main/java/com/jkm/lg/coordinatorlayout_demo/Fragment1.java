package com.jkm.lg.coordinatorlayout_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Ligang on 2017/8/21.
 */

public class Fragment1 extends Fragment{
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(new MyAdapter(getActivity()));

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.dismissPopupMenus();
        imageView = (ImageView) getView().findViewById(R.id.img);
        AppBarLayout appBarLayout = (AppBarLayout) getView().findViewById(R.id.appbarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("Activity1", "verticalOffset:" + verticalOffset);
                Log.d("Activity1", "imageView.getHeight():" + imageView.getHeight());
                Log.d("Activity1", "toolbar.getHeight():" + toolbar.getHeight());

                /**
                 * 615    1
                 *
                 * 0       0
                 */

                float alpha = -verticalOffset*1f/(imageView.getHeight()-toolbar.getHeight());
                toolbar.setAlpha(alpha);

            }
        });
    }


    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        LayoutInflater inflater;
        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(inflater.inflate(R.layout.item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class MyHolder extends RecyclerView.ViewHolder{

            public MyHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
