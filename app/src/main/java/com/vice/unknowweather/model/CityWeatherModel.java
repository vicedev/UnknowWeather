package com.vice.unknowweather.model;

import com.google.gson.Gson;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.db.DBHelper;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by vice on 2016/12/21 0021.
 */
public class CityWeatherModel {
    private static CityWeatherModel model;

    private CityWeatherModel() {

    }

    public static CityWeatherModel getInstance() {
        if (model == null) {
            synchronized (CityWeatherModel.class) {
                if (model == null) {
                    model = new CityWeatherModel();
                }
            }
        }
        return model;
    }

    public  synchronized boolean insertCityWeather(String cityName,String weather) {
        City city = DBHelper.getInstance().retrieveCityWeather(cityName);
        if (city == null) {
            //空说明数据库里没有
            boolean success = DBHelper.getInstance().createCityWeather(cityName, weather);
            return success;
        } else {
            //有就修改
            boolean success = DBHelper.getInstance().updateCityWeather(city, weather);
            return success;
        }
    }

    public  synchronized boolean insertCityWeather(String cityName,Weather weather){
        City city = DBHelper.getInstance().retrieveCityWeather(cityName);
        if (city==null){
            //空说明数据库里没有
            boolean success = DBHelper.getInstance().createCityWeather(cityName, weather);
            return success;
        }else{
            //有就修改
            boolean success = DBHelper.getInstance().updateCityWeather(city,new Gson().toJson(weather));
            return success;
        }

    }

    public synchronized boolean deleteCityWeather(String cityName){
        return DBHelper.getInstance().deleteCityWeather(cityName);
    }

    public synchronized City queryCityWeather(String cityName){
        City city = DBHelper.getInstance().retrieveCityWeather(cityName);
        return city;
    }
}
