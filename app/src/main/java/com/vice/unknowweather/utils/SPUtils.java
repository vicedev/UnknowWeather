package com.vice.unknowweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.vice.unknowweather.App;
import com.vice.unknowweather.global.Constants;

/**
 * Created by vice on 2016/12/20 0020.
 */
public class SPUtils {


    public static void setFirstStart(boolean flag){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.FIRST_START,flag);
        editor.apply();
    }

    public static boolean getFirstStart(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).getBoolean(Constants.FIRST_START,true);
    }

    public static void setCurrentCity(String city){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.CURRENT_CITY,city);
        editor.apply();
    }

    public static String getCurrentCity(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).getString(Constants.CURRENT_CITY,"杭州");
    }

    public static String getCurrentBgWay(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG,Context.MODE_PRIVATE).getString(Constants.CURRENT_BG_WAY,Constants.BG_AUTO_CHANGE);
    }

    public static void setCurrentBgWay(String way){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.CURRENT_BG_WAY,way);
        editor.apply();
    }

    public static String getAutoChangeBg(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG,Context.MODE_PRIVATE).getString(Constants.BG_AUTO_CHANGE,"");
    }

    public static void setAutoChangeBg(String url){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.BG_AUTO_CHANGE,url);
        editor.apply();
    }

    public static int getCustomColorBg(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG,Context.MODE_PRIVATE).getInt(Constants.BG_PURE_COLOR,Color.WHITE);
    }

    public static void setCustomColorBg(int color){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putInt(Constants.BG_PURE_COLOR,color);
        editor.apply();
    }

    public static boolean getOpenNotificationWeather(){
        return App.getAppContext().getSharedPreferences(Constants.CONFIG,Context.MODE_PRIVATE).getBoolean(Constants.OPEN_NOTIFICATION_WEATHER,true);
    }

    public static void setOpenNotificationWeather(boolean isOpen){
        SharedPreferences.Editor editor= App.getAppContext().getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.OPEN_NOTIFICATION_WEATHER,isOpen);
        editor.apply();
    }
}
