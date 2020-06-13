package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.TicketParm;
import com.example.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {
    //通过注解方式告诉接口请求方法和路径
    @GET("discovery/categories")
    Call<Categories> getCategoties();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);//内部区拼接

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParm ticketParm);
}
