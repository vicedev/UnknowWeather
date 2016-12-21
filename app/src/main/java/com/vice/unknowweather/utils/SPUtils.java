package com.vice.unknowweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vice.unknowweather.App;

/**
 * Created by vice on 2016/12/20 0020.
 */
public class SPUtils {
    public static final String CONFIG="config";
    public static final String FIRST_START="first_start";
    public static final String CURRENT_CITY="current_city";

    public static void setFirstStart(boolean flag){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(FIRST_START,flag);
        editor.apply();
    }

    public static boolean getFirstStart(){
        return App.getAppContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE).getBoolean(FIRST_START,true);
    }

    public static void setCurrentCity(String city){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE).edit();
        editor.putString(CURRENT_CITY,city);
        editor.apply();
    }

    public static String getCurrentCity(){
        return App.getAppContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE).getString(CURRENT_CITY,"杭州");
    }
}
