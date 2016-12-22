package com.vice.unknowweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.utils.ToastUtils;

import java.util.List;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class RVCityAdapter extends RecyclerView.Adapter<RVCityAdapter.MyViewHolder> {
    private List<City> cityList;

    public RVCityAdapter(List<City> cityList) {
        this.cityList = cityList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_city_manage_rv_item, null);
        final MyViewHolder holder = new MyViewHolder(layout);

        holder.ibDeleteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除城市
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        City city = cityList.get(position);
        String cityName = city.getCityName();
        String weather = city.getWeather();

        holder.tvCity.setText(cityName);
        if (!TextUtils.isEmpty(weather)) {
            holder.tvWeather.setText(weather);
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        TextView tvWeather;
        ImageButton ibDeleteCity;

        public MyViewHolder(View view) {
            super(view);
            tvCity = (TextView) view.findViewById(R.id.tv_city);
            tvWeather = (TextView) view.findViewById(R.id.tv_weather);
            ibDeleteCity= (ImageButton) view.findViewById(R.id.ib_delete_city);
        }
    }
}
