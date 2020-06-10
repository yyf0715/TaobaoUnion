package com.example.taobaounion.presenter;

import com.example.taobaounion.view.IHomeCallback;

public interface IHomePresenter {
    //获取商品分类
    void getCategories();

    //注册UI通知接口

    /**
     * 注册UI通知接口
     * @param callback
     */
    void registerCallback(IHomeCallback callback);  //注册

    /**
     * 取消UI通知的接口
     * @param allback
     */
    void unregisterCallback(IHomeCallback allback); //取消注册
}
