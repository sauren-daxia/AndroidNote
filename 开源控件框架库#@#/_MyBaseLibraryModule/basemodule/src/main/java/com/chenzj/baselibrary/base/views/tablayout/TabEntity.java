package com.chenzj.baselibrary.base.views.tablayout;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by Jason Chen on 2017/10/12.
 */

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {	//标题名称
        return title;
    }

    @Override
    public int getTabSelectedIcon() {	//选中时图标
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {	//未选中时图片
        return unSelectedIcon;
    }
}
