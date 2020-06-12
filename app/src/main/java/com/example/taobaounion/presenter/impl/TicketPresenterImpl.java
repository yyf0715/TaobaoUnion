package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.view.ITicketPageCallback;

public class TicketPresenterImpl implements ITicketPresenter {
    @Override
    public void getTicket(String title, String url, String cover) {
        //TODO:去获取淘口令
    }

    @Override
    public void registerViewCallback(ITicketPageCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ITicketPageCallback callback) {

    }
}
