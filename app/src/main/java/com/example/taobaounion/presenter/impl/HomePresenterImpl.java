package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        //加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategoties();

        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "code-->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();

//                    LogUtils.d(HomePresenterImpl.this,"categories-->"+categories.toString());
                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        }else {
                            mCallback.onCategoriesLoaded(categories);//在UI上边获取数据
                        }

                    }
                } else {
                    //请求失败
                    LogUtils.d(HomePresenterImpl.this, "HomePresenterImpl请求失败请求失败。。。");
                    if (mCallback!=null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败
                LogUtils.d(this, "HomePresenterImpl请求失败" + t.toString());
                if (mCallback!=null) {
                    mCallback.onError();
                }
            }
        });

    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callbackallback) {
        mCallback = null;
    }
}
