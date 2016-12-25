package com.vice.unknowweather.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.utils.ToastUtils;

/**
 * Created by vice on 2016/12/25 0025.
 */
public class CityPickHotCityAdapter extends RecyclerView.Adapter<CityPickHotCityAdapter.MyViewHolder>{
    private Activity activity;

    @Override
    public CityPickHotCityAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        activity= (Activity) parent.getContext();
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_city_pick_hot_city_item,parent,false);
        final MyViewHolder holder=new MyViewHolder(layout);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName= Constants.HOT_CITYS[holder.getAdapterPosition()];
                setCityPickResult(cityName);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvCity.setText(Constants.HOT_CITYS[holder.getAdapterPosition()]);

    }

    @Override
    public int getItemCount() {
        return Constants.HOT_CITYS.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCity;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvCity= (TextView) itemView.findViewById(R.id.tv_city);
        }
    }
    //选中或添加城市后返回
    public void setCityPickResult(String cityName) {
        City city = CityWeatherModel.getInstance().queryCityWeather(cityName);
        if (city!=null){
            ToastUtils.showShort("该城市已存在");
        }else{
            if (activity!=null){
                Intent intent=new Intent();
                intent.putExtra(Constants.CITY_NAME,cityName);
                activity.setResult(activity.RESULT_OK,intent);
                activity.finish();
            }
        }
    }
}
