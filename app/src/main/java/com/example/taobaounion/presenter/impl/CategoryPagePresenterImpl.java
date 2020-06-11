package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();//存储页面

    public static final int DEFAULT_PAGE = 1;

    private CategoryPagePresenterImpl() {

    }

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getsInstance() {//单例模式
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        //根据分类ID去加载内容

        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId) {
                callback.onLoading();
            }
        }

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);//获取Page
        if (targetPage == null) {//传入默认的Page
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, DEFAULT_PAGE);
        }
        String homePageUrl = UrlUtils.creatHomepagerUrl(categoryId, targetPage);
        LogUtils.d(CategoryPagePresenterImpl.this, "home page url -->" + homePageUrl);
        Call<HomePagerContent> task = api.getHomePagerContent(homePageUrl);//获取相应页面的数据回调
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "code-->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this, "pagerContent-->" + pageContent);
                    //把数据给到UI更新
                    handleHomePageContentResult(pageContent, categoryId);

                } else {
                    handleNetworkError(categoryId);//网络错误

                }
            }


            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagePresenterImpl.this, "onFailure-->" + t.toString());
                handleNetworkError(categoryId);//网络错误
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {//相匹配才通知UI层更新
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pageContent, int categoryId) {
        //通知UI层更新数据
        for (ICategoryPagerCallback callback : callbacks) {

            if (callback.getCategoryId() == categoryId) {
                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    callback.onContentLoaded(pageContent.getData());
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {//判断是否存在，不存在的情况下添加进去
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }


}
