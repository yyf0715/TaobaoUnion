package com.example.taobaounion.utils;


import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;

    public ICategoryPagerPresenter getmCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public IHomePresenter getmHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getmTicketPresenter() {
        return mTicketPresenter;
    }

    private PresenterManager(){
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
    }
    public static PresenterManager getInstance() {
        return ourInstance;
    }


}
