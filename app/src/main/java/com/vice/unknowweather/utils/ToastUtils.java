package com.vice.unknowweather.utils;

import android.widget.Toast;

import com.vice.unknowweather.App;

/**
 * Created by vice on 2016/12/21 0021.
 */
public class ToastUtils {
    public static void showShort(String content){
        Toast.makeText(App.getAppContext(),content,Toast.LENGTH_SHORT).show();
    }
    public static void showLong(String content){
        Toast.makeText(App.getAppContext(),content,Toast.LENGTH_LONG).show();
    }
}
