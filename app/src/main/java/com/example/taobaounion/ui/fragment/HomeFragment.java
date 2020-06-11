package com.example.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTablayout;

    private HomePresenterImpl mHomePresenter;

    @BindView(R.id.home_pager)
    public ViewPager homePager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTablayout.setupWithViewPager(homePager);
        //给ViewPager设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(mHomePagerAdapter);

    }

    @Override
    protected void initPresenter() {
        //创建Presenter
        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerViewCallback(this);//注册接口
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {//通过这里加载数据
        //        加载的数据冲这里来
        //LogUtils.d(this,"onCategoriesLoaded.....");

        setUpState(State.SUCCESS);
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategoryes(categories);
        }


    }

    @Override
    protected void release() {
        //取消注册
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void onRetryCilck() {
        //网络错误，点击了重试
        //重新加载分类
        if (mHomePresenter != null){
            mHomePresenter.getCategories();
        }
    }
}



