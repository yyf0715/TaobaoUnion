package com.example.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageContentAdapter extends RecyclerView.Adapter<HomePageContentAdapter.InnerHolder> {


    List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);//绑定内容布局

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {//绑定数据
        //设置数据
        HomePagerContent.DataBean dataBean = data.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {//设置content的数据
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_prise)
        public TextView offPriseTv;

        @BindView(R.id.goods_after_off_prise)
        public TextView finalPriseTv;

        @BindView(R.id.goods_original_prise)
        public TextView originalPriseTv;

        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            title.setText(dataBean.getTitle());
            //LogUtils.d(this,"url -->"+dataBean.getPict_url());
            Glide.with(itemView.getContext()).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(cover);//把什么图片应用到哪儿
            String final_price = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();
            float resultPrise = Float.parseFloat(final_price) - couponAmount;
            //LogUtils.d(this,"券后价"+resultPrise);
            // LogUtils.d(this,"券后价"+final_price);
            offPriseTv.setText(String.format(itemView.getContext().getString(R.string.text_goods_off_prise),couponAmount));//优惠券
            finalPriseTv.setText(String.format("%.2f",resultPrise));//优惠后的价格
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中划线
            originalPriseTv.setText(String.format(itemView.getContext().getString(R.string.text_goods_original_prise),final_price));//优惠前的价格
            sellCountTv.setText(String.format(itemView.getContext().getString(R.string.text_goods_sell_count),dataBean.getVolume()));//30天内已购买人数

        }
    }
}
