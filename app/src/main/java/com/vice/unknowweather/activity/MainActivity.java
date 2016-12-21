package com.vice.unknowweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.vice.unknowweather.App;
import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.model.WeatherModel;
import com.vice.unknowweather.utils.SPUtils;
import com.vice.unknowweather.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "vvvLocation";


    public BDLocationListener myListener = new MyLocationListener();
    private TextView tvCity;
    private TextView tvWeather;
    private MaterialRefreshLayout refreshLayout;
    private ImageButton ibSettings;
    private ImageButton ibCityManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        ibSettings = (ImageButton) findViewById(R.id.ib_settings);
        ibCityManage = (ImageButton) findViewById(R.id.ib_city_manage);
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);


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
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setSunStyle(true);
//        refresh.setIsOverLay(true);
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }
        });
        refreshLayout.autoRefresh();
    }

    private void refresh() {
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
        //先从本地获取数据显示
        City city = CityWeatherModel.getInstance().queryCityWeather(cityName);
        if (city != null) {
            if (!TextUtils.isEmpty(city.getWeather())) {
                tvWeather.setText(city.getWeather());
            }
        }
        //再从网络获取数据显示
        WeatherModel.getInstance().getWeather(cityName, new WeatherModel.WeatherCallBack() {
            @Override
            public void onSuccess(Weather weather) {
                //天气存入数据库
                CityWeatherModel.getInstance().insertCityWeather(cityName, weather);
                //显示天气
                tvWeather.setText(new Gson().toJson(weather));

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


    private boolean checkIsFirstStart() {
        boolean isFirstStart = SPUtils.getFirstStart();
        return isFirstStart;
    }

    //开始定位
    private void startLocate() {
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
        WeatherModel.getInstance().cancelAllRequest();
    }
}
