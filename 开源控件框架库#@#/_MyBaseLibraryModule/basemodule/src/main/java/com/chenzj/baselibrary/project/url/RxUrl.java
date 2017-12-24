package com.chenzj.baselibrary.project.url;


/**
 * Created by Administrator on 2017/7/27 0027.
 * 例如
 * String LOGIN = "api/xxx/login";
 * 注意：
 * 1、常量必须全部大写
 * 2、BASEURL不需要在这里写
 */
public interface RxUrl {
    //系统时间
    String SYSTEM_TIME = "Common/getSysTime.jhtml";

    //注册账号
    String REGISTER = "11";

    //首页
    String HOME = "11";

    //拍卖列表
    String AUCTION_LIST = "1";

    //报名拍卖
    String SUBSCRIBE = "1";

}
