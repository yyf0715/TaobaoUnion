package com.example.taobaounion.utils;

import android.widget.Toast;

import com.example.taobaounion.base.BaseApplication;

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String tips){
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(tips);
        }

        mToast.show();
    }
}
