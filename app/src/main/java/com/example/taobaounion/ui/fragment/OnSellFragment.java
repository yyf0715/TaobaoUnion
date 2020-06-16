package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.OnSellContentAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback, OnSellContentAdapter.OnSellPageItemClickListener {


    public static final int DEFAULT_SPAN_COUNT = 2;

    private IOnSellPagePresenter mOnSellPagePresenter;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentRv;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    private OnSellContentAdapter mOnSellContentAdapter ;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        //注册逻辑层
        mOnSellPagePresenter = PresenterManager.getInstance().getmSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();//加载内容
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
    protected void initListener() {
        super.initListener();
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadedMore();
                }
            }
        });

        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mOnSellContentAdapter = new OnSellContentAdapter();
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),DEFAULT_SPAN_COUNT);//DEFAULT_SPAN_COUNT两列
        mContentRv.setLayoutManager(gridLayoutManager);
        mContentRv.setAdapter(mOnSellContentAdapter);
        mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });

        mTwinklingRefreshLayout.setEnableLoadmore(true);//是否允许加载更多
        mTwinklingRefreshLayout.setEnableRefresh(false);//是否允许下拉刷新
        mTwinklingRefreshLayout.setEnableOverScroll(true);//是否允许开启越界回弹模式


    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //数据回来了
        setUpState(State.SUCCESS);
        //更新Ui
        mOnSellContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {

        mTwinklingRefreshLayout.finishLoadmore();
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtil.showToast("加载了" + size + "个宝贝");
        //添加数据到适配器里
        mOnSellContentAdapter.onMoreLoaded(moreResult);
    }

    @Override
    public void onMoreLoadError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("没有更多的内容了");
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
    public void onSellItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data) {
        //特惠列表类容被点击时触发
        //处理数据
        String title = data.getTitle();//获取标题
        //String url = item.getClick_url();//获取url   getClick_url()详情地址
        String url = data.getCoupon_click_url();
        if (TextUtils.isEmpty(url)){//可能会有一些没有优惠券了
            ToastUtil.showToast("来晚了，没有优惠券了");
            url = data.getClick_url();
        }
        String cover = data.getPict_url();//获取图片url
        //拿TicketPresenter去加载数据
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
