package com.dsfa.lovepartybuild.ui.fragment.search;

import com.dsfa.common.base.fragment.BaseFragment;

/**
 * Created by hp on 2017/5/5.
 */
public abstract class LazyFragment extends BaseFragment{
    //用于判断Fragment是否显示
    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }

    //主要重写该方法，需要加载的方法重写在里面，
    protected abstract void lazyLoad();

    //不可见的时候调用，可以想OnVisible一样写一个抽象方法在里面调用
    protected void onInvisible(){}

}
