package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;


/**
 * 功能：自动轮播
 */
public class AuttoLoopViewPager extends ViewPager {

    //切换间隔时长，单位毫秒
    public static long DEFAULT_DURATION = 3000;
    public long mDuration = DEFAULT_DURATION;

    public AuttoLoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AuttoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopeStyle);
        //获取属性
        mDuration = t.getInteger(R.styleable.AutoLoopeStyle_duration, (int) DEFAULT_DURATION);
        LogUtils.d(this,"mDuration--->"+mDuration);
        //回收资源
        t.recycle();
    }

    private boolean isLoop = false;

    public void startLoop() {//开始轮播
       /* //进行一次轮播
        int currentItem = getCurrentItem();
        currentItem++;
        setCurrentItem(currentItem);
        //**********************/

        isLoop = true;

        post(mTask);
    }

    /**
     * 设置切换时长
     *
     * @param duration
     */
    public void setDuratioo(long duration) {
        this.mDuration = duration;
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, mDuration);
            }
        }
    };

    public void stopLoop() {//结束轮播
        isLoop = false;
        removeCallbacks(mTask);
    }
}
