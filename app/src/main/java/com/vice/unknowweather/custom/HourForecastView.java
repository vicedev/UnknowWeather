package com.vice.unknowweather.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.utils.DisplayUtils;

import java.util.List;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class HourForecastView extends LinearLayout {

    public HourForecastView(Context context) {
        super(context);
    }

    public HourForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public HourForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHourlyForecast(List<Weather.HeWeather5Bean.HourlyForecastBean> hourlyForecast) {
        removeAllViews();
        TextView tvName = new TextView(getContext());
        tvName.setText("小时预报");
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(DisplayUtils.sp2px(getContext(), 6.0f));
        addView(tvName);

        for (int i = 0; i < hourlyForecast.size(); i++) {
            Weather.HeWeather5Bean.HourlyForecastBean hourlyForecastBean = hourlyForecast.get(i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_hour_forecast_item, this, false);
            addView(itemView);
            TextView tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            TextView tvCond = (TextView) itemView.findViewById(R.id.tv_cond);
            TextView tvTmp = (TextView) itemView.findViewById(R.id.tv_tmp);

            tvDate.setText(hourlyForecastBean.getDate().split(" ")[1]);
            tvCond.setText(hourlyForecastBean.getCond().getTxt());
            tvTmp.setText(hourlyForecastBean.getTmp() + "℃");

        }
    }
}
