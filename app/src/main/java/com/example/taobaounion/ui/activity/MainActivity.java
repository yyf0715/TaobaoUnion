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

                //Log.d(TAG, "title-->" + item.getTitle() + "|id-->" + item.getItemId());
                //测试
//                if (item.getItemId() == R.id.home) {
//                    LogUtils.d(MainActivity.class, "切换到首页");
//                    switchFragment(mHomeFragment);
//                } else if (item.getItemId() == R.id.selected) {
//                    LogUtils.d(MainActivity.class, "切换到精选");
//                    switchFragment(mSelectedFragment);
//                } else if (item.getItemId() == R.id.red_packet) {
//                    LogUtils.d(MainActivity.class, "切换到特惠");
//                    switchFragment(mRedPacketFragment);
//                } else if (item.getItemId() == R.id.search) {
//                    LogUtils.d(MainActivity.class, "切换到搜索");
//                    switchFragment(mSearchFragment);
//                }
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


//    @BindView(R.id.main_navigation_bar)
//    public BottomNavigationView mNavigationView;
//    private HomeFragment mHomeFragment;
//    private RedPacketFragment mRedPacketFragment;
//    private SelectedFragment mSelectedFragment;
//    private SearchFragment mSearchFragment;
//    private FragmentManager mFm;
//
//    @Override
//    protected void initPresenter() {
//
//    }
//
//    /**
//     * 跳转到搜索界面
//     */
//    public void switch2Search() {
//        // switchFragment(mSearchFragment);
//        //切换导航栏的选中项
//        mNavigationView.setSelectedItemId(R.id.search);
//    }
//
//
//    @Override
//    protected void initEvent() {
//        initListener();
//    }
//
//    @Override
//    protected void initView() {
//        initFragments();
//    }
//
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_main;
//    }
//
//
//    private void initFragments() {
//        mHomeFragment = new HomeFragment();
//        mRedPacketFragment = new RedPacketFragment();
//        mSelectedFragment = new SelectedFragment();
//        mSearchFragment = new SearchFragment();
//        mFm = getSupportFragmentManager();
//        switchFragment(mHomeFragment);
//    }
//
//    private void initListener() {
//        mNavigationView.setOnNavigationItemSelectedListener(item -> {
//            if(item.getItemId() == R.id.home) {
//                LogUtils.d(this,"切换到首页");
//                switchFragment(mHomeFragment);
//            } else if(item.getItemId() == R.id.selected) {
//                LogUtils.i(this,"切换到精选");
//                switchFragment(mSelectedFragment);
//            } else if(item.getItemId() == R.id.red_packet) {
//                LogUtils.w(this,"切换到特惠");
//                switchFragment(mRedPacketFragment);
//            } else if(item.getItemId() == R.id.search) {
//                LogUtils.e(this,"切换到搜索");
//                switchFragment(mSearchFragment);
//            }
//            return true;
//        });
//    }
//
//    /**
//     * 上一次显示的fragment
//     */
//    private BaseFragment lastOneFragment = null;
//
//    private void switchFragment(BaseFragment targetFragment) {
//        //如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
//        if(lastOneFragment == targetFragment) {
//            return;
//        }
//        //修改成add和hide的方式来控制Fragment的切换
//        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
//        if(!targetFragment.isAdded()) {
//            fragmentTransaction.add(R.id.main_page_container,targetFragment);
//        } else {
//            fragmentTransaction.show(targetFragment);
//        }
//        if(lastOneFragment != null) {
//            fragmentTransaction.hide(lastOneFragment);
//        }
//        lastOneFragment = targetFragment;
//        //fragmentTransaction.replace(R.id.main_page_container,targetFragment);
//        fragmentTransaction.commit();
//    }


}