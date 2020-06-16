package com.example.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.ISearchPageCallback;

import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchPageCallback {

    private ISearchPresenter mSearchPresenter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getmSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        //获取数据

        mSearchPresenter.doSearch("鼠标");
        //
        // mSearchPresenter.delHisetories();
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {//返回布局的ID
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }


    @Override
    public void onHistoriesLoaded(Histories history) {

    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        LogUtils.d(this, "result--->>" + result);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }


    @Override
    public void onMoreLoaderError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWord) {
        LogUtils.d(this, "recommendWord的大小--->" + recommendWord.size());
    }


    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}



