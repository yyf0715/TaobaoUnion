package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.HomePageContentAdapter;
import com.example.taobaounion.ui.adapter.LooperPagerAdapter;
import com.example.taobaounion.ui.custom.AuttoLoopViewPager;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePageContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());//抽出title和materialId
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AuttoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;


    //加载更多数据的控件
    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        //可见时，开始轮播
        looperPager.startLoop();
        //LogUtils.d(this,"onResume......");
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
        //LogUtils.d(this,"onPause......");
    }

    @Override
    protected void initListener() {

        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);

        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null){
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                int measuredHeight = homePagerParent.getMeasuredHeight();
                //获取整体高度和开头的高度
                //LogUtils.d(HomePagerFragment.this, "headerHeight-->" + headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);
                //homePagerNestedView整体高度和头部整体相同，保证滑动时的流畅
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                //LogUtils.d(HomePagerFragment.this,"measuredHeight->"+layoutParams.height);
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        /*currentCategoryTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measuredHeight = mContentList.getMeasuredHeight();
                //LogUtils.d(HomePagerFragment.this,"measuredHeight-->"+measuredHeight);
            }
        });*/
        //LooperPager监听
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % mLooperPagerAdapter.getDataSize();

                //切换指示器
                updatelooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {//加载更多
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //LogUtils.d(HomePagerFragment.class, "触发了LoaderMore。。。");
                //加载更多的内容
                if (mCategoryPagePresenter != null) {
                    mCategoryPagePresenter.loaderMore(mMaterialId);//接口通知
                }

            }
            /*    //刷新没写
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {//刷新加载
                super.onRefresh(refreshLayout);
            }*/
        });
    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */
    private void updatelooperIndicator(int targetPosition) {//轮播图的选中与非选中
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);//获取子组件
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {//设置边距
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        //创建适配器
        mContentAdapter = new HomePageContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);

        //创建轮播图looperPager适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置适配器
        looperPager.setAdapter(mLooperPagerAdapter);
        //looperPager.setDuratioo(3000);//设置轮播间隔
        //设置Refresh相关属性
        twinklingRefreshLayout.setEnableRefresh(false);//禁止上拉加载更多属性
        twinklingRefreshLayout.setEnableLoadmore(true);//下拉加载更多


    }

    @Override
    protected void initPresenter() {
        mCategoryPagePresenter = PresenterManager.getInstance().getmCategoryPagePresenter();
        mCategoryPagePresenter.registerViewCallback(this);//注册
    }

    @Override
    protected void loadData() {//获取参数
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);

        //LogUtils.d(this, "title-->" + title);
        //LogUtils.d(this, "materialId-->" + mMaterialId);

        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.getContentByCategoryId(mMaterialId);//接口获取ID
        }
        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText(title);
        }
    }

    //实现ICategoryPagerCallback接口
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载
        mContentAdapter.setmData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {

        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误

        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {

        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        ToastUtil.showToast("网络异常，请稍后重试");
        if (twinklingRefreshLayout!=null) {
            twinklingRefreshLayout.finishLoadmore();//结束加载更多动画
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtil.showToast("没有更多的数据了");
        if (twinklingRefreshLayout!=null) {
            twinklingRefreshLayout.finishLoadmore();//结束加载更多动画
        }
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的底部
        mContentAdapter.addData(contents);
        if (twinklingRefreshLayout!=null) {
            twinklingRefreshLayout.finishLoadmore();//结束加载更多动画
        }
        ToastUtil.showToast("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {//looper数据的加载
        //LogUtils.d(this,"looper size -->"+contents.size());

        mLooperPagerAdapter.setData(contents);//添加looperPager的数据

        //设置到中间点  不一定为第一个
        //
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        looperPager.setCurrentItem(targetCenterPosition);
        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());

            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);//高宽为像素，手机需要使用的是dp
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);

            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {//释放资源
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        //列表内容被点击了

        //LogUtils.d(this,"item click --- >"+item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        //处理数据
        String title = item.getTitle();//获取标题
        //String url = item.getClick_url();//获取url   getClick_url()详情地址
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)){//可能会有一些没有优惠券了
            ToastUtil.showToast("来晚了，没有优惠券了");
            url = item.getClick_url();
        }
        String cover = item.getPict_url();//获取图片url
        //拿TicketPresenter去加载数据
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void onLooperItem(HomePagerContent.DataBean item) {
        //轮播图内容被点击了
        LogUtils.d(this,"轮播图item click --- >"+item.getTitle());
        handleItemClick(item);
    }
}
