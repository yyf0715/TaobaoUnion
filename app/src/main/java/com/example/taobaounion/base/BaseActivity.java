package com.example.taobaounion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //绑定Activity
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract void initPresenter();


    /**
     * 需要时复写
     */
    protected void initEvent() {

    }

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();//解除绑定
        }
        this.release();
    }

    /**
     * 如果子类需要释放资源，覆盖即可
     */
    protected void release() {

    }

    protected abstract int getLayoutResId();
}
