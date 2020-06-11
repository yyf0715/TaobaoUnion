package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    //通知UI的一个回调

    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();

    /**
     * 加载更多时出错
     */
    void onLoaderMoreError();

    /**
     * 没有更多内容
     */
    void onLoaderMoreEmpty();

    /**
     * 加到了更多内容
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载到了
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

}
