package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.model.domain.TicketParm;
import com.example.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    //通过注解方式告诉接口请求方法和路径
    @GET("discovery/categories")
    Call<Categories> getCategoties();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);//内部区拼接

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParm ticketParm);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedContent> getSelectedContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRemmendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);
}
