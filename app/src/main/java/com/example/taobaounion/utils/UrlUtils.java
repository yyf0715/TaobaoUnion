package com.example.taobaounion.utils;

public class UrlUtils  {
    public static String creatHomepagerUrl(int material,int page){//获取分类和页数，并且拼接为一个URL
        return "discovery/"+material+"/"+page;
    }

    public static String getCoverPath(String pict_url) {
        return "https:"+pict_url;
    }
}
