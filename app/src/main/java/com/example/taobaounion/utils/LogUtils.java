package com.example.taobaounion.utils;

import android.util.Log;

public class LogUtils {


    private static int currentLev = 4;//当前log等级

    //级别  只显示相应级别的debug
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Object obj, String log) {//debug
        if (currentLev >= DEBUG_LEV) {
            Log.d(obj.getClass().getSimpleName(),log);
        }
    }

    public static void i(Object obj, String log) {//info
        if (currentLev >= INFO_LEV) {
            Log.i(obj.getClass().getSimpleName(), log);
        }
    }

    public static void w(Object obj, String log) {//waring
        if (currentLev >= WARNING_LEV) {
            Log.w(obj.getClass().getSimpleName(), log);
        }
    }

    public static void e(Object obj, String log) {//erro
        if (currentLev >= ERROR_LEV) {
            Log.e(obj.getClass().getSimpleName(), log);
        }
    }
}
