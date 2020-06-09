package com.example.taobaounion;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.RedPacketFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.ui.fragment.SelectedFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;//绑定容器  绑定的方法不可以是 static 或者 private
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//绑定Activity

        initFragment();
        initListener();
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();//得到fragment支持包
        switchFragment(mHomeFragment);//默认选择首页
    }

    private void initListener() {//lambda表达式
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Log.d(TAG, "title-->" + item.getTitle() + "|id-->" + item.getItemId());
                //*测试
                if (item.getItemId() == R.id.home) {
                    LogUtils.d(MainActivity.class, "切换到首页");
                    switchFragment(mHomeFragment);
                } else if (item.getItemId() == R.id.selected) {
                    LogUtils.d(MainActivity.class, "切换到精选");
                    switchFragment(mSelectedFragment);
                } else if (item.getItemId() == R.id.red_packet) {
                    LogUtils.d(MainActivity.class, "切换到特惠");
                    switchFragment(mRedPacketFragment);
                } else if (item.getItemId() == R.id.search) {
                    LogUtils.d(MainActivity.class, "切换到搜索");
                    switchFragment(mSearchFragment);
                }
                return true;
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {//切换fragment方法
        FragmentTransaction transaction = mFm.beginTransaction();//开始事务
        transaction.replace(R.id.main_page_container,targetFragment);
        transaction.commit();//提交事务

    }


}