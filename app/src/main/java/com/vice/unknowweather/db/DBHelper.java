package com.vice.unknowweather.db;

import com.google.gson.Gson;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.bean.Weather;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by vice on 2016/12/21 0021.
 */
public class DBHelper {
    private static DBHelper dbHelper;

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper();
                }
            }
        }
        return dbHelper;
    }

    //增
    public synchronized boolean createCityWeather(String cityName, Weather weather) {
        City city = new City();
        city.setCityName(cityName);
        city.setWeather(new Gson().toJson(weather));
        city.save();
        return city.isSaved();
    }
    public synchronized boolean createCityWeather(String cityName, String weather) {
        City city = new City();
        city.setCityName(cityName);
        city.setWeather(weather);
        return city.isSaved();
    }

    //查
    public City retrieveCityWeather(String cityName){
        List<City> cityList = DataSupport.findAll(City.class);
        for (City city:cityList){
            if (city.getCityName().equals(cityName)){
                return city;
            }
        }
        return null;
    }

    //删
    public synchronized boolean deleteCityWeather(String cityName){
        int i = DataSupport.deleteAll(City.class, "cityName=?", cityName);
        if(i==0){
            return false;
        }else{
            return true;
        }
    }

    //改
    public synchronized boolean updateCityWeather(City city,String weather){
        city.setWeather(weather);
        return city.save();
    }




}
