package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     * @param history
     */
    void onHistoriesLoaded(Histories history);

    /**
     * 删除历史记录
     */
    void onHistoriesDeleted();

    /**
     * 搜搜结果：成功
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载了更多内容
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络出错
     */
    void onMoreLoaderError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWord
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean > recommendWord);
}
