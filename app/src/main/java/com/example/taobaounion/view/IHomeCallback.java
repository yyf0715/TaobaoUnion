package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {
    //通知UI的一个回调
    void onCategoriesLoaded(Categories categories);
}
