package com.example.taobaounion.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {//同一个父类,用于获取context

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}

