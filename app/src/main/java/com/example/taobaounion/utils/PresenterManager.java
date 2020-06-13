package com.example.taobaounion.utils;


import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.ISelectedPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.presenter.impl.SelectedPresenterImpl;
import com.example.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectedPresenter mSelectedPresenter;

    public ISelectedPresenter getmSelectedPresenter() {
        return mSelectedPresenter;
    }

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
        mSelectedPresenter = new SelectedPresenterImpl();
    }
    public static PresenterManager getInstance() {
        return ourInstance;
    }


}
