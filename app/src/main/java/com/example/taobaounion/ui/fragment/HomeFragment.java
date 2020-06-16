package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.ui.activity.IMainActivity;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTablayout;

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_pager)
    public ViewPager homePager;

    @BindView(R.id.home_search_input_box)
    public View mSearchInputBox;
    private HomePagerAdapter mHomePagerAdapter;

    @BindView(R.id.home_search_icon)
    public ImageView mHomesearchicon;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(this,"onCreateView....");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        LogUtils.d(this,"onDestroyView....");
        super.onDestroyView();
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
        mHomePresenter =PresenterManager.getInstance().getmHomePresenter();
        mHomePresenter.registerViewCallback(this);//注册接口
        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索页面
                FragmentActivity activity = getActivity();
                if(activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2Search();
                }
            }
        });
        mHomesearchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索页面
                FragmentActivity activity = getActivity();
                if(activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2Search();
                }
            }
        });
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
            //homePager.setOffscreenPageLimit(categories.getData().size());//设置为全部设置,一般不设置，暂内存
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
    public void onError() {
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



