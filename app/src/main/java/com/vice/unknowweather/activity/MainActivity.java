package com.vice.unknowweather.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.vice.unknowweather.App;
import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.custom.DayForecastView;
import com.vice.unknowweather.custom.HourForecastView;
import com.vice.unknowweather.custom.NowView;
import com.vice.unknowweather.custom.SuggestionView;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.model.BgModel;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.model.WeatherModel;
import com.vice.unknowweather.service.NotificationWeatherService;
import com.vice.unknowweather.utils.SPUtils;
import com.vice.unknowweather.utils.ToastUtils;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "vvvLocation";


    private BDLocationListener myListener = new MyLocationListener();
    private TextView tvCity;
    private MaterialRefreshLayout refreshLayout;
    private ImageButton ibSettings;
    private ImageButton ibCityManage;
    private HourForecastView hfView;//显示每小时的天气
    private DayForecastView dfView;//显示后面几天的天气
    private SuggestionView suggestionView;//显示生活建议
    private NowView nowView;//显示当前天气
    private ImageView ivBg;//显示背景
    private ImageView ivUnknow;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCity = (TextView) findViewById(R.id.tv_city);
        ibSettings = (ImageButton) findViewById(R.id.ib_settings);
        ibCityManage = (ImageButton) findViewById(R.id.ib_city_manage);
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        hfView = (HourForecastView) findViewById(R.id.hf_view);
        dfView = (DayForecastView) findViewById(R.id.df_view);
        suggestionView = (SuggestionView) findViewById(R.id.suggestion_view);
        nowView = (NowView) findViewById(R.id.now_view);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        ivUnknow = (ImageView) findViewById(R.id.iv_unknow);

        ibCityManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityManageActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CITY);
            }
        });

        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setSunStyle(true);
        refreshLayout.setIsOverLay(true);
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }
        });
        refreshLayout.autoRefresh();

        //是否下每日一图
        String currentBgWay = SPUtils.getCurrentBgWay();
        if (currentBgWay.equals(Constants.BG_AUTO_CHANGE)) {
            BgModel.getInstance().loadAutoChangeBg(MainActivity.this, new BgModel.AutoChangeBgCallBack() {
                @Override
                public void onSuccess(String url) {
                    BgModel.getInstance().setAutoChangeBgToSP(url);
                    Glide.with(MainActivity.this).load(url).error(R.mipmap.bg).into(ivBg);
                }

                @Override
                public void onFailure() {

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        setBackGround();

    }

    private void setBackGround() {
        String currentBgWay = SPUtils.getCurrentBgWay();
        switch (currentBgWay) {
            case Constants.BG_AUTO_CHANGE:
                String bg = SPUtils.getAutoChangeBg();
                Glide.with(MainActivity.this).load(bg).error(R.mipmap.bg).into(ivBg);
                break;

            case Constants.BG_PHOTO:
                String path = getFilesDir() + File.separator + "bg";
                Glide.with(MainActivity.this).load(path)
                        .error(R.mipmap.bg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(ivBg);

                break;

            case Constants.BG_PURE_COLOR:
                int color = SPUtils.getCustomColorBg();
                ColorDrawable colorDrawable = new ColorDrawable(color);
                ivBg.setImageDrawable(colorDrawable);

                break;
        }
    }

    private void refresh() {
//        startTime = SystemClock.elapsedRealtime();

        boolean isFirstStart = checkIsFirstStart();
        if (isFirstStart) {
            startLocate();
        } else {
            String cityName = SPUtils.getCurrentCity();
            tvCity.setText(cityName);

            //获取并显示天气
            getAndshowWeather(cityName);
        }
    }

    private void getAndshowWeather(final String cityName) {
        //是否开启通知栏天气
        boolean open = SPUtils.getOpenNotificationWeather();
        if (open) {
            Intent intent = new Intent(MainActivity.this, NotificationWeatherService.class);
            startService(intent);
        }

        //先从本地获取数据显示
        City city = CityWeatherModel.getInstance().queryCityWeather(cityName);
        if (city != null) {
            if (!TextUtils.isEmpty(city.getWeather())) {
                showWeather(new Gson().fromJson(city.getWeather(), Weather.class));
            } else {
                hideWeatherView();
            }
        }
        //再从网络获取数据显示
        WeatherModel.getInstance().getWeather(this, cityName, new WeatherModel.WeatherCallBack() {
            @Override
            public void onSuccess(Weather weather) {
                //天气存入数据库
                CityWeatherModel.getInstance().insertCityWeather(cityName, weather);
                showWeather(weather);
                //结束刷新状态
                refreshLayout.finishRefreshing();

            }

            @Override
            public void onFailure() {
                ToastUtils.showShort("获取天气信息失败");
                //结束刷新状态
                refreshLayout.finishRefreshing();
            }
        });
    }

//    private void finishRefreshing(final Weather weather) {
//        long endTime = SystemClock.elapsedRealtime();
//        long deltTime=endTime-startTime;
//        long expectTime=1*1000;
//        long delayTime=expectTime-deltTime;
//        if ((deltTime)<expectTime){
//            refreshLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    refreshLayout.finishRefreshing();
//                    //显示天气
//                    if (weather!=null){
//                        showWeather(weather);
//                    }
//                }
//            }, delayTime );
//        }else{
//            refreshLayout.finishRefreshing();
//        }
//    }

    //没有天气信息的时候隐藏显示天气的控件
    private void hideWeatherView() {
        ivUnknow.setVisibility(View.VISIBLE);

        dfView.setVisibility(View.GONE);
        hfView.setVisibility(View.GONE);
        nowView.setVisibility(View.GONE);
        suggestionView.setVisibility(View.GONE);

    }

    private void showWeather(Weather weather) {
        ivUnknow.setVisibility(View.GONE);

        showNow(weather);
        showHourForecast(weather);
        showDayForecast(weather);
        showSuggestion(weather);
    }

    //显示当前天气
    private void showNow(Weather weather) {
        try {
            Weather.HeWeather5Bean.NowBean now = weather.getHeWeather5().get(0).getNow();
            String loc = weather.getHeWeather5().get(0).getBasic().getUpdate().getLoc();
            Weather.HeWeather5Bean.AqiBean aqi = weather.getHeWeather5().get(0).getAqi();
            if (now != null) {
                nowView.setVisibility(View.VISIBLE);
                nowView.setNow(now, loc, aqi);
            } else {
                nowView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //显示生活建议
    private void showSuggestion(Weather weather) {
        try {
            Weather.HeWeather5Bean.SuggestionBean suggestions = weather.getHeWeather5().get(0).getSuggestion();
            if (suggestions != null) {
                suggestionView.setVisibility(View.VISIBLE);
                suggestionView.setSuggestiont(suggestions);
            } else {
                suggestionView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示后几天的天气
    private void showDayForecast(Weather weather) {
        try {
            List<Weather.HeWeather5Bean.DailyForecastBean> dailyForecast = weather.getHeWeather5().get(0).getDaily_forecast();
            if (dailyForecast != null) {
                dfView.setVisibility(View.VISIBLE);
                dfView.setDailyForecast(dailyForecast);
            } else {
                dfView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示每小时的天气
    private void showHourForecast(Weather weather) {
        try {
            List<Weather.HeWeather5Bean.HourlyForecastBean> hourlyForecast = weather.getHeWeather5().get(0).getHourly_forecast();
            if (hourlyForecast != null) {
                hfView.setVisibility(View.VISIBLE);
                hfView.setHourlyForecast(hourlyForecast);
            } else {
                hfView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkIsFirstStart() {
        boolean isFirstStart = SPUtils.getFirstStart();
        return isFirstStart;
    }

    //开始定位
    private void startLocate() {
        tvCity.setText("定位中。。。");
        App.mLocationClient.registerLocationListener(myListener);    //注册监听函数
        App.mLocationClient.start();
    }

    //停止定位
    private void stopLocate() {
        App.mLocationClient.stop();
        App.mLocationClient.unRegisterLocationListener(myListener);
    }


    class MyLocationListener implements BDLocationListener {

        private String cityName;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                cityName = location.getCity();
                cityName = cityName.replace("市", "").trim();
            } else {
                Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                cityName = "杭州";
            }
            stopLocate();
            tvCity.setText(cityName);
            getAndshowWeather(cityName);

            //城市存入数据库
            CityWeatherModel.getInstance().insertCityWeather(cityName, "");
            //存入sp
            SPUtils.setFirstStart(false);
            SPUtils.setCurrentCity(cityName);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CITY) {
            String cityName = data.getStringExtra(Constants.CITY_NAME);
            if (!TextUtils.isEmpty(cityName)) {
                tvCity.setText(cityName);
                SPUtils.setCurrentCity(cityName);
                getAndshowWeather(cityName);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocate();
        WeatherModel.getInstance().cancelByTag(this);
    }
}
