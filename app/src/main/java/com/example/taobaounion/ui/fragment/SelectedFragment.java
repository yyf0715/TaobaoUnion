package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.ISelectedPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.SelectedPageContentAdapter;
import com.example.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ISelectedPageCallback;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {

    @BindView(R.id.left_category_list)
    public RecyclerView leftcategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    private ISelectedPresenter mSelectedPresenter;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPresenter = PresenterManager.getInstance().getmSelectedPresenter();
        mSelectedPresenter.registerViewCallback(this);//注册
        mSelectedPresenter.getCategorises();
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPresenter != null) {
            mSelectedPresenter.unregisterViewCallback(this);//撤销注册
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        leftcategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftcategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
               int topAndButtom = SizeUtils.dip2px(getContext(),4);//讲px转换为pd
                int leftAndRight = SizeUtils.dip2px(getContext(),6);

                outRect.top = topAndButtom;//内边距
                outRect.bottom = topAndButtom;//内边距
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });

    }


    //实现状态方法
    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setmData(categories);
        //分类内容
        //LogUtils.d(this,"onCategoriesLoaded-->"+ category.toString());
        //根据当前选中的分类，获取分类内容
        //List<SelectedPageCategory.DataBean> data = categories.getData();
        //mSelectedPresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        //setUpState(State.SUCCESS);
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);//滑动到第一个

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

    }




    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        //左边的分类被点击了
        //LogUtils.d(this,"current selected item --> "+ item.getFavorites_title());
        mSelectedPresenter.getContentByCategory(item);//数据传给右边页面
    }

    @Override
    public void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
        //右边内容被点击了

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

        //LogUtils.d(this,"cover-->>>>"+cover);
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}


