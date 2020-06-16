package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.utils.JsonCacheUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private static final int DEFAULT_PAGE = 0;
    private final Api mApi;
    private ISearchPageCallback mSearchPageCallback = null;
    private String mCurrentKeyword = null;
    private JsonCacheUtil mJsonCacheUtil;


    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    /**
     * 搜索当前的页面
     */
    int mCurrentPage = DEFAULT_PAGE;

    /**
     * 添加历史记录
     */
    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES,Histories.class);
        if(mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesLoaded(histories);
        }

    }

    @Override
    public void delHisetories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if(mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";
    private static final int DEFAULT_HISTORIES_MAX_SIZE = 10;
    public static final int historiesMaxSize = DEFAULT_HISTORIES_MAX_SIZE;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES,Histories.class);
        //如果说已经在了，就干掉，然后再添加
        List<String> historiesList = null;
        if(histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if(historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if(historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if(histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数进行限制
        if(historiesList.size() > historiesMaxSize) {
            historiesList = historiesList.subList(0,historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORIES,histories);
    }

    @Override
    public void doSearch(String keyword) {//keyword关键字

        if (mCurrentKeyword == null || mCurrentKeyword.equals(keyword)) {//mCurrentKeyword没有赋值,或者相同
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }

        this.mSearchPageCallback.onLoading();
        //更新UI状态
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "doSearch  code->> " + code);
                //多状态处理
                if (code == HttpURLConnection.HTTP_OK) {
                    //出来结果
                    handleSearchResult(response.body());

                    LogUtils.d(SearchPresenterImpl.this,keyword);
                    LogUtils.d(SearchPresenterImpl.this,"搜索结果数量"+response.body().getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size());

                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });

    }

    private void onError() {
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onError();
        }
    }


    private boolean isResultEmpty(SearchResult body) {
        try {
            return body.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void handleSearchResult(SearchResult body) {

        if (body == null || isResultEmpty(body)) {
            //数据为空
            mSearchPageCallback.onEmpty();
        } else {
            mSearchPageCallback.onSearchSuccess(body);
        }
    }


    @Override
    public void research() {
        if (mCurrentKeyword == null) {
            if (mSearchPageCallback != null) {
                mSearchPageCallback.onEmpty();
            }
        } else {
            //可以重新搜索
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;//页数加1
        //进行搜索
        if (mCurrentKeyword == null) {
            //数据为空
            mSearchPageCallback.onEmpty();
        } else {
            //做搜索的事
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "doSearch  code->> " + code);

                if (code == HttpURLConnection.HTTP_OK) {
                    //出来结果
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    /**
     * 处理加载更多的结果
     *
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if(mSearchPageCallback != null) {
            if(isResultEmpty(result)) {
                //数据为空
                mSearchPageCallback.onMoreLoadedEmpty();
            } else {
                mSearchPageCallback.onMoreLoaded(result);
            }
        }
    }

    /**
     * 加载更多失败
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onMoreLoaderError();
        }
    }

    @Override
    public void getRecommendWords() {
        //获得推荐词汇
        Call<SearchRecommend> task = mApi.getRemmendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "getRecommendWords  code->> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //出来结果
                    if(mSearchPageCallback != null) {
                        mSearchPageCallback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.mSearchPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        this.mSearchPageCallback = null;
    }
}
