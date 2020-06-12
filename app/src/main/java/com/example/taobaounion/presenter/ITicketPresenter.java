package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ITicketPageCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPageCallback> {
    //父类实现了相关注册
    //只负责相关逻辑


    /**
     * 生成淘口令
     * title：标题
     * url：物品url
     * cover:物品图片
     *
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title,String url,String cover);

}
