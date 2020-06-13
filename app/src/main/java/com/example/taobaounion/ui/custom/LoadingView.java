package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.taobaounion.R;

public class LoadingView extends AppCompatImageView {

    private float mDegrees = 0;

    private boolean mNeedRotate = true;//判断是否需要

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {//view可见时调用
        super.onAttachedToWindow();
        //LogUtils.d(LoadingView.class, "onAttachedToWindow.....");
        mNeedRotate = true;
        startRotate();//开始旋转
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 10;
                if (mDegrees >= 360) {
                    mDegrees = 0;
                }
                invalidate();//呼叫onDraw（）运行
                //LogUtils.d(LoadingView.class, "loading.....");
                //判断是否继续旋转
                //如果不可见，或者已经onDetachedFromWindow就不再转动
                if (getVisibility() != VISIBLE && !mNeedRotate) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {//view不可见时调用
        super.onDetachedFromWindow();
        //LogUtils.d(LoadingView.class, "onDetachedFromWindow.....");

        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth() / 2, getHeight() / 2);//进行偏转  degrees：偏转多少  px，py确认圆心
        super.onDraw(canvas);
    }
}
