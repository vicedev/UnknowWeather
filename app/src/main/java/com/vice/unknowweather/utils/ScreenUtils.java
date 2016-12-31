package com.vice.unknowweather.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.vice.unknowweather.App;

/**
 * Created by vice on 2016/12/31 0031.
 */
public class ScreenUtils {
    public static int getScreenWidth(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }
    public static int getScreenHeight(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return height;
    }
    public static int[] getScreenWidthAndHeight(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return new int[]{width,height};
    }

}
