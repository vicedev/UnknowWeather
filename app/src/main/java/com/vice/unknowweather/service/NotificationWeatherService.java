package com.vice.unknowweather.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.vice.unknowweather.R;
import com.vice.unknowweather.activity.MainActivity;

public class NotificationWeatherService extends Service {
    public NotificationWeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent i=new Intent(this, MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);

//        RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.activity_city_manage);

        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("title")
                .setContentText("content")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);

        return super.onStartCommand(intent, flags, startId);

    }
}
