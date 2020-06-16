package com.example.taobaounion.model.domain;

public interface ILinearItemInfo {//item物品接口数据

    /**
     * 商品的封面
     *
     * @return
     */
    String getCover();

    /**
     * 商品的标题
     *
     * @return
     */
    String getTitle();

    /**
     * 商品的Url
     *
     * @return
     */
    String getUrl();

    /**
     * 获取原价
     *
     * @return
     */
    String getFinalPrise();


    /**
     * 获取优惠价格
     *
     * @return
     */
    long getCouponAmount();


    /**
     * 获取销量
     *
     * @return
     */
    long getVolume();

}
