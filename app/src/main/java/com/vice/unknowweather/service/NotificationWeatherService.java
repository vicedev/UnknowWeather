package com.vice.unknowweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.vice.unknowweather.R;
import com.vice.unknowweather.activity.MainActivity;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.model.WeatherModel;
import com.vice.unknowweather.utils.SPUtils;

public class NotificationWeatherService extends Service {

    private String currentCity;
    private String aqiNum;
    private String quality;

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
        currentCity = SPUtils.getCurrentCity();
        updateWeather();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int deltTime = 1 * 60 * 60 * 1000;
        long nextRefreshTime = deltTime + SystemClock.elapsedRealtime();
        Intent i = new Intent(this, NotificationWeatherService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextRefreshTime, pi);

        return super.onStartCommand(intent, flags, startId);

    }

    private void updateWeather() {

        WeatherModel.getInstance().getWeather(this, currentCity, new WeatherModel.WeatherCallBack() {
            @Override
            public void onSuccess(Weather weather) {
                updateDBWeather(weather);
                updateNotificationWeather(weather);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void updateNotificationWeather(Weather weather) {
        Weather.HeWeather5Bean heWeather5Bean = weather.getHeWeather5().get(0);
        Weather.HeWeather5Bean.NowBean now = heWeather5Bean.getNow();
        Weather.HeWeather5Bean.DailyForecastBean today = heWeather5Bean.getDaily_forecast().get(0);
        Weather.HeWeather5Bean.AqiBean aqi = heWeather5Bean.getAqi();
        String loc = heWeather5Bean.getBasic().getUpdate().getLoc();

        try {
            String tmp = now.getTmp() + "℃";//温度
            String condTxt = now.getCond().getTxt();//天气
            String maxTmp = today.getTmp().getMax();//最高温度
            String minTmp = today.getTmp().getMin();//最低温度
            //香港、澳门、台湾没有aqi，有毒
            if (aqi!=null){
                aqiNum = aqi.getCity().getAqi(); //aqi指数
                quality = aqi.getCity().getQlty(); //空气质量
            }

            String time = "发布时间：" + loc.split(" ")[1];//发布时间
            String city = heWeather5Bean.getBasic().getCity();//当前城市

            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_weather_layout);
            remoteViews.setTextViewText(R.id.tv_tmp_now, tmp);
            remoteViews.setTextViewText(R.id.tv_cond, condTxt);
            if (aqi!=null){
                remoteViews.setTextViewText(R.id.tv_aqi, quality + " " + aqiNum);
            }
            remoteViews.setTextViewText(R.id.tv_tmp, maxTmp + "/" + minTmp + "℃");
            remoteViews.setTextViewText(R.id.tv_city, city);
            remoteViews.setTextViewText(R.id.tv_publish_time, time);


            sendNotification(remoteViews);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateDBWeather(Weather weather) {
        String weatherJson = new Gson().toJson(weather);
        CityWeatherModel.getInstance().insertCityWeather(currentCity, weatherJson);
    }

    private void sendNotification(RemoteViews remoteViews) {

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.weather_small)
                .setCustomContentView(remoteViews)
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Intent intent=new Intent("com.vice.unknowweather.START_NOTIFICATION_ERATHER_SERVICE");
        sendBroadcast(intent);
    }
}
