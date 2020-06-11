package com.example.taobaounion.base;

public interface IBasePresenter<T> {
    /**
     * 注册UI通知接口
     * @param callback
     */
    void registerViewCallback(T callback);  //注册

    /**
     * 取消UI通知的接口
     * @param allback
     */
    void unregisterViewCallback(T allback); //取消注册
}
