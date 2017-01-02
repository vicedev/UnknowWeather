package com.vice.unknowweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vice.unknowweather.service.NotificationWeatherService;
import com.vice.unknowweather.utils.SPUtils;

public class StartNotificationWeatherReceiver extends BroadcastReceiver {
    public StartNotificationWeatherReceiver() {
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        System.out.println("vvv" + "收到");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("com.vice.unknowweather.START_NOTIFICATION_ERATHER_SERVICE")) {
            boolean open = SPUtils.getOpenNotificationWeather();
            if (open) {
                System.out.println("vvv" + "进来了");
                Intent i = new Intent(context, NotificationWeatherService.class);
                context.startService(i);
            }
        }
    }
}
