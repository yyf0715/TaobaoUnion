package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.ILinearItemInfo;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.KeyboardUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoriesView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_container)
    public View mHistoriesContainer;


    @BindView(R.id.search_history_delete)
    public View mHistoryDelete;


    @BindView(R.id.search_result_list)
    public RecyclerView mSearchList;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mCleanInputBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;


    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshContainer;


    private ISearchPresenter mSearchPresenter;
    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getmSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        //获取数据
        mSearchPresenter.getHistories();

    }

    @Override
    protected void onRetryCilck() {
        //重新加载内容
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
        }
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
    protected void initListener() {
        mHistoriesView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);
        //发起搜索
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果有内容搜索
                //如果输入框没有内容则取消
                if (hasInput(false)) {
                    //发起否所
                    if (mSearchPresenter != null) {
                        //mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                        //进行搜索并且隐藏键盘
                        toSearch(mSearchInputBox.getText().toString().trim());
                        KeyboardUtil.hide(getContext(), v);
                    }
                } else {
                    //隐藏键盘
                    KeyboardUtil.hide(getContext(), v);
                }
            }
        });
        //清除输入框里的内容
        mCleanInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInputBox.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });

        //监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化时候的通知
                //LogUtils.d(SearchFragment.this,"input text === > " + s.toString().trim());
                //如果长度不为0，那么显示删除按钮
                //否则隐藏删除按钮
                mCleanInputBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //LogUtils.d(SearchFragment.this,"actionId === > " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    //执行搜索时
//                    软键盘的Enter键默认显示的是“完成”文本，通过设置android:imeOptions来改变默认的“完成”文本。这里举几个常用的常量值：
//                    actionUnspecified  未指定，对应常量EditorInfo.IME_ACTION_UNSPECIFIED.
//                    actionNone 没有动作,对应常量EditorInfo.IME_ACTION_NONE
//                    actionGo 去往，对应常量EditorInfo.IME_ACTION_GO
//                    actionSearch 搜索，对应常量EditorInfo.IME_ACTION_SEARCH
//                    actionSend 发送，对应常量EditorInfo.IME_ACTION_SEND
//                    actionNext 下一个，对应常量EditorInfo.IME_ACTION_NEXT
//                    actionDone 完成，对应常量EditorInfo.IME_ACTION_DONE


                    String keyword = v.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        return false;//不进行下次个事件操作
                    }
                    //判断拿到的内容是否为空
                    LogUtils.d(SearchFragment.this, " input text === > " + keyword);
                    //发起搜索
                    toSearch(keyword);
                    //mSearchPresenter.doSearch(keyword);
                }
                return false;//不进行下次个事件操作
            }
        });
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除历史记录
                mSearchPresenter.delHisetories();
            }
        });

        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多内容
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });

        mSearchResultAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(ILinearItemInfo item) {


                //处理数据
                String title = item.getTitle();
                //详情的地址
                String url = item.getUrl();
                if(TextUtils.isEmpty(url)) {
                    url = item.getUrl();
                }
                String cover = item.getCover();
                //拿到tiketPresenter去加载数据
                ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
                ticketPresenter.getTicket(title,url,cover);
                //转到淘口令Activity
                getContext().startActivity(new Intent(getContext(), TicketActivity.class));
            }
        });
    }

    /**
     * 切换到 历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();//重新读取一次
        }
        if (mRecommendView.getContentSize() != 0) {
            mRecommendContainer.setVisibility(View.VISIBLE);//显示推荐关键字
        } else {
            mRecommendContainer.setVisibility(View.GONE);//隐藏推荐关键字
        }
        //内容要隐藏
        mRefreshContainer.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {//有没有空格
            return mSearchInputBox.getText().toString().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        }
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        //设置刷新控件
        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(false);
        mRefreshContainer.setEnableOverScroll(true);
        //间距
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //  根据手机的分辨率从 dp 的单位 转成为 px(像素)
                outRect.top = SizeUtils.dip2px(getContext(), 1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 1.5f);
                ;
            }
        });

    }


    @Override
    public void onHistoriesLoaded(Histories history) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "histories -- > " + history);
        if (history == null || history.getHistories().size() == 0) {
            mHistoriesContainer.setVisibility(View.GONE);
        } else {
            mHistoriesContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(history.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        //更新历史记录
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        //LogUtils.d(this," result -=- > " + result);
        //隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoriesContainer.setVisibility(View.GONE);
        //显示搜索界面
        mRefreshContainer.setVisibility(View.VISIBLE);
        //设置数据
        try {
            mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
            //切换到搜搜内容为空
            setUpState(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mRefreshContainer.finishLoadmore();
        //加载到更多的结果
        //拿到结果，添加到适配器的尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(moreData);
        //提示用户加载到的内容
        ToastUtil.showToast("加载到了" + moreData.size() + "条记录");
    }


    @Override
    public void onMoreLoaderError() {
        mRefreshContainer.finishLoadmore();
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshContainer.finishLoadmore();
        ToastUtil.showToast("没有更多数据");
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWord) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "recommendWord的大小--->" + recommendWord.size());
        List<String> recommendKeywords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWord) {
            recommendKeywords.add(item.getKeyword());
        }
        if (recommendWord == null || recommendWord.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendView.setTextList(recommendKeywords);
            mRecommendContainer.setVisibility(View.VISIBLE);
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
    public void onFlowItemClick(String text) {
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
            //mSearchInputBox.setSelection(text.length());
            mSearchInputBox.setSelection(text.length(), text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}



