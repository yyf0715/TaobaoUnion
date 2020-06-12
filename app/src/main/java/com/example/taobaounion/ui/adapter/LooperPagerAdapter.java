package com.example.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {


    private List<HomePagerContent.DataBean> mData = new ArrayList<>();

    public int getDataSize() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {//加载Item

        //越界问题处理
        int realPosition = position % mData.size();
        //  size = 5
        //  0 --> 0
        //  1 --> 1
        //  2 --> 2
        //  3 --> 3
        //  4 --> 4
        //  5 --> 0
        //  6 --> 1


        HomePagerContent.DataBean dataBean = mData.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();

        //LogUtils.d(this, "measuredHeight-->" + measuredHeight);
        //LogUtils.d(this, "measuredWidth-->" + measuredWidth);
        int ivSize = (measuredWidth > measuredHeight ? measuredWidth : measuredHeight) / 2;//节省流量
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(),ivSize );
        ImageView iv = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //Params->参数
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);//图形填充
        container.addView(iv);

        return iv;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {//销毁
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {//判断内容和View相同
        return view == object;
    }


    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }
}
