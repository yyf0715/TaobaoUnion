package com.example.taobaounion.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.OnSellFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.ui.fragment.SelectedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;//绑定容器  绑定的方法不可以是 static 或者 private

    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private OnSellFragment mOnSellFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mOnSellFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();//得到fragment支持包
        switchFragment(mHomeFragment);//默认选择首页
    }

    private void initListener() {//lambda表达式
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //LogUtils.d(this, "HomeFragmentItemID -->" + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.home:
                        //LogUtils.d(this,"切换到首页");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        //LogUtils.d(this,"切换到精选页面");
                        switchFragment(mSelectedFragment);
                        break;
                    case R.id.red_packet:
                        //LogUtils.d(this,"切换到特惠");
                        switchFragment(mOnSellFragment);
                        break;
                    case R.id.search:
                        //LogUtils.d(this,"切换到搜索页面");
                        switchFragment(mSearchFragment);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 上一次显示的fragment
     */

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {//切换fragment方法

        //如果和上一个一样，就不需要更改
        if (lastOneFragment == targetFragment) {
            return;
        }

        //修改为add和hide的方式来控制Fragment的切换

        FragmentTransaction transaction = mFm.beginTransaction();//开始事务
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        } else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        // transaction.replace(R.id.main_page_container,targetFragment);
        transaction.commit();//提交事务

    }


}