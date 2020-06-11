package com.example.taobaounion.view;

import com.example.taobaounion.model.domain.Categories;

public interface IHomeCallback {
    //通知UI的一个回调
    void onCategoriesLoaded(Categories categories);


    void onNetworkError();
    void onLoading();
    void onEmpty();
}
