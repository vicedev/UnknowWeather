package com.vice.unknowweather.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by vice on 2016/12/21 0021.
 */
public class City extends DataSupport {
    private String cityName;
    private String weather;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
