package com.vice.unknowweather.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.Weather;

import java.util.List;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class DayForecastView extends LinearLayout {

    public DayForecastView(Context context) {
        super(context);
    }

    public DayForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public DayForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDailyForecast(List<Weather.HeWeather5Bean.DailyForecastBean> dailyForecast) {
        removeAllViews();
        for (int i = 0; i < dailyForecast.size(); i++) {
            Weather.HeWeather5Bean.DailyForecastBean dailyForecastBean = dailyForecast.get(i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_day_forecast_item, this, false);
            addView(itemView);
            TextView tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            TextView tvCond = (TextView) itemView.findViewById(R.id.tv_cond);
            TextView tvDayTmp = (TextView) itemView.findViewById(R.id.tv_day_tmp);
            TextView tvNightTmp = (TextView) itemView.findViewById(R.id.tv_night_tmp);

            tvDate.setText(dailyForecastBean.getDate());
            tvCond.setText(dailyForecastBean.getCond().getTxt_d());
            tvDayTmp.setText(dailyForecastBean.getTmp().getMax()+"°C");
            tvNightTmp.setText(dailyForecastBean.getTmp().getMin()+"°C");


        }
    }
}
