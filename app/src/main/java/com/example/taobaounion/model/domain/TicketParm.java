package com.example.taobaounion.model.domain;

public class TicketParm {//打折券信息bean
    private String url;
    private String title;

    public TicketParm(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
