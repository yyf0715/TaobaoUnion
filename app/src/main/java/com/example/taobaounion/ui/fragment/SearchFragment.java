package com.example.taobaounion.ui.fragment;

import android.view.View;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {//返回布局的ID
        return R.layout.fragment_search;
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}



