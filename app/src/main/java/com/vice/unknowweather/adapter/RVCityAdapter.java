package com.vice.unknowweather.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vice.unknowweather.R;
import com.vice.unknowweather.activity.CityManageActivity;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.utils.ToastUtils;

import java.util.List;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class RVCityAdapter extends RecyclerView.Adapter<RVCityAdapter.MyViewHolder> {
    private List<City> cityList;
    private AlertDialog dialog;
    private Context mContext;
    private CityManageActivity.onRVCityAdapterItemClickListener listener;

    public RVCityAdapter(List<City> cityList, CityManageActivity.onRVCityAdapterItemClickListener listener) {
        this.cityList = cityList;
        this.listener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_city_manage_rv_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(layout);

        holder.ibDeleteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除城市
                deleteCity(holder.getAdapterPosition());

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    private void deleteCity(int position) {
        showDialog(true, position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        City city = cityList.get(holder.getAdapterPosition());
        String cityName = city.getCityName();
        String weatherJson = city.getWeather();
        Weather weather = new Gson().fromJson(weatherJson, Weather.class);

        holder.tvCity.setText(cityName);
        if (weather != null) {
            Weather.HeWeather5Bean.DailyForecastBean today = weather.getHeWeather5().get(0).getDaily_forecast().get(0);
            Weather.HeWeather5Bean.NowBean now = weather.getHeWeather5().get(0).getNow();
            String condTxt = now.getCond().getTxt();
            String maxTmp = today.getTmp().getMax();
            String minTmp = today.getTmp().getMin();
            String tmp = maxTmp + "/" + minTmp + "℃";

            holder.tvCond.setText(condTxt);
            holder.tvTmp.setText(tmp);

        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        ImageButton ibDeleteCity;
        TextView tvCond;
        TextView tvTmp;


        public MyViewHolder(View view) {
            super(view);
            tvCity = (TextView) view.findViewById(R.id.tv_city);
            ibDeleteCity = (ImageButton) view.findViewById(R.id.ib_delete_city);
            tvCond = (TextView) view.findViewById(R.id.tv_cond);
            tvTmp = (TextView) view.findViewById(R.id.tv_tmp);

        }
    }

    private void showDialog(boolean show, final int position) {
        if (show) {
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle("删除城市")
                    .setMessage("确定要删除吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //只剩一个城市的话不给删除
                            List<City> cityList = CityWeatherModel.getInstance().querrAllCityWeathers();
                            if (cityList.size() <= 1) {
                                ToastUtils.showShort("只剩一个了就别删了吧");
                                return;
                            }

                            boolean success = CityWeatherModel.getInstance().deleteCityWeather(RVCityAdapter.this.cityList.get(position).getCityName());
                            if (success) {
                                ToastUtils.showShort("删除成功");
                                RVCityAdapter.this.cityList.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                ToastUtils.showShort("删除失败");
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            dialog.show();
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    //添加城市
    public void addCity(City city) {
        if (city == null) {
            return;
        }
        String cityName = city.getCityName();
        String weather = city.getWeather();
        boolean has = CityWeatherModel.getInstance().hasCityWeather(cityName);
        if (!has) {
            boolean sucess = CityWeatherModel.getInstance().insertCityWeather(cityName, weather);
            if (sucess) {
                cityList.add(city);
                notifyItemInserted(cityList.size() - 1);
            } else {
                ToastUtils.showShort("添加城市失败");
            }
        } else {
            ToastUtils.showShort("该城市已经添加了");
        }
    }
}
