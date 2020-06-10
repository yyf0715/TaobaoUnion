package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    //通过注解方式告诉接口请求方法和路径
    @GET("discovery/categories")
    Call<Categories> getCategoties();

}