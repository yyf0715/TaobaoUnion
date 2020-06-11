package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    //获取商品分类
    void getCategories();

    //注册UI通知接口


}
