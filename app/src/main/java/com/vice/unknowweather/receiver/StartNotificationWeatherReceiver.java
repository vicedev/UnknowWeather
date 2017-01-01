package com.vice.unknowweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.vice.unknowweather.activity.SplashActivity;
import com.vice.unknowweather.service.NotificationWeatherService;
import com.vice.unknowweather.utils.SPUtils;

public class StartNotificationWeatherReceiver extends BroadcastReceiver {
    public StartNotificationWeatherReceiver() {
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        System.out.println("vvv"+"收到");
//        boolean open = SPUtils.getOpenNotificationWeather();
//        if (open){
        if (intent.getAction().equals("com.vice.unknowweather.START_NOTIFICATION_ERATHER_SERVICE")){
            System.out.println("vvv"+"进来了");
            Intent i=new Intent(context,NotificationWeatherService.class);
            context.startService(intent);
        }

//        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(2000);
//                Intent i=new Intent(context, NotificationWeatherService.class);
//                context.startService(intent);
//            }
//        }).start();
    }
}
