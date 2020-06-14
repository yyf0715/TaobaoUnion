package com.example.taobaounion.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.ui.adapter.OnSellContentAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.IOnSellPageCallback;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {


    public static final int DEFAULT_SPAN_COUNT = 2;

    private IOnSellPagePresenter mOnSellPagePresenter;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentRv;
    private OnSellContentAdapter mOnSellContentAdapte;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        //注册逻辑层
        mOnSellPagePresenter = PresenterManager.getInstance().getmSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
    }

    @Override
    protected void release() {
        super.release();
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }

    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        mOnSellContentAdapte = new OnSellContentAdapter();
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),DEFAULT_SPAN_COUNT);//DEFAULT_SPAN_COUNT两列
        mContentRv.setLayoutManager(gridLayoutManager);
        mContentRv.setAdapter(mOnSellContentAdapte);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //数据回来了
        //TODO:更新Ui

        mOnSellContentAdapte.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {

    }

    @Override
    public void onMoreLoadError() {

    }

    @Override
    public void onMoreLoadEmpty() {

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
