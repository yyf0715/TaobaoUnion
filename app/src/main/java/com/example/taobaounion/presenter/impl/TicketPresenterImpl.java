package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.TicketParm;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketPageCallback mViewCallback = null;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        //触发时时Loading状态
        this.onTicketLoadedLoading();
        this.mCover = cover;
        //LogUtils.d(TicketPresenterImpl.this, "ticketResulttitle--->" + title);
        //LogUtils.d(TicketPresenterImpl.this, "ticketResulturl--->" + url);
        //LogUtils.d(TicketPresenterImpl.this, "ticketResultcover--->" + cover);
        String targetUrl = UrlUtils.getTicketUrl(url);//正确的地址
        //去获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);

        TicketParm ticketParm = new TicketParm(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParm);//拖过接口获取淘口令
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                //LogUtils.d(TicketPresenterImpl.this, "code--->" + code);

                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    mTicketResult = response.body();
                    //LogUtils.d(TicketPresenterImpl.this, "ticketResult--->" + ticketResult.toString());
                    //LogUtils.d(TicketPresenterImpl.this, "ticketResult.getData()--->" + ticketResult.getData().toString());
                    //LogUtils.d(TicketPresenterImpl.this, "getTbk_tpwd_create_response()--->" + ticketResult.getData().getTbk_tpwd_create_response().toString());
                    //通知UI更新
                    onTicketLoadedSuccess();
                } else {
                    //请求失败
                    onLoadedTicketError();
                    mCurrentState = LoadState.ERROR;
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                //失败
                onLoadedTicketError();
                mCurrentState = LoadState.ERROR;
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void registerViewCallback(ITicketPageCallback callback) {
        if (mCurrentState != LoadState.NONE) {
            //说明状态已经改变了
            //更新UI
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoadedLoading();
            }
        }
        this.mViewCallback = callback;
    }

    private void onTicketLoadedLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPageCallback callback) {

        this.mViewCallback = null;
    }
}
