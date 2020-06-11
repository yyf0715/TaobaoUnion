package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;

public class HomePagerFragment extends BaseFragment {

    public static HomePagerFragment newInstance(Categories.DataBean category){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());//抽出title和materialId
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void loadData() {//获取参数
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO:加载数据
        LogUtils.d(this,"title-->"+title);
        LogUtils.d(this,"materialId-->"+materialId);
    }
}
