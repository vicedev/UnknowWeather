package com.vice.unknowweather.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by vice on 2016/12/21 0021.
 */
public class WeatherModel {
    private static WeatherModel model;

    private WeatherModel() {
    }

    public static WeatherModel getInstance() {
        if (model == null) {
            synchronized (WeatherModel.class) {
                if (model == null) {
                    model = new WeatherModel();
                }
            }
        }
        return model;
    }

    public void getWeather(String cityName, final WeatherCallBack callBack) {
        OkGo.get(Constants.BASE_URL + "?city=" + cityName + "&key=" + Constants.HEWEATHER_KEY)
                .tag(Constants.BASE_URL)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String json, Call call, Response response) {
                        if (!TextUtils.isEmpty(json)) {
                            try {
                                JSONArray HeWeather5 = new JSONObject(json).getJSONArray("HeWeather5");
                                String status = HeWeather5.getJSONObject(0).getString("status");
                                if (status.equals("ok")) {
                                    Weather weather = formatJsonToWeather(json);
                                    if (weather != null) {
                                        callBack.onSuccess(weather);
                                    } else {
                                        throw new JSONException("weather为null");
                                    }
                                } else {
                                    throw new JSONException("status不为ok");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callBack.onFailure();
                                ToastUtils.showShort("获取天气失败");
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callBack.onFailure();
                    }
                });
    }

    private Weather formatJsonToWeather(String json) {
        Weather weather = new Gson().fromJson(json, Weather.class);
        return weather;
    }

    //取消所有请求
    public void cancelAllRequest(){
        OkGo.getInstance().cancelAll();
    }

    public interface WeatherCallBack {
        void onSuccess(Weather weather);

        void onFailure();
    }

}
