package com.vice.unknowweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.vice.unknowweather.R;
import com.vice.unknowweather.adapter.RVCityAdapter;
import com.vice.unknowweather.bean.City;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.model.CityWeatherModel;
import com.vice.unknowweather.utils.ToastUtils;

import java.util.List;
import java.util.Random;

public class CityManageActivity extends AppCompatActivity {

    private RecyclerView rvCity;
    private CityWeatherModel cityWeatherModel;
    private RVCityAdapter adapter;
    private ImageButton ibBack;
    private ImageButton ibAddCity;
    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);

        rvCity = (RecyclerView) findViewById(R.id.rv_city);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibAddCity = (ImageButton) findViewById(R.id.ib_add_city);


        cityWeatherModel = CityWeatherModel.getInstance();


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String citys[]={"杭州","上海","北京","深圳","成都","广州"};
                City city=new City();
                city.setCityName(citys[new Random().nextInt(6)]);
                adapter.addCity(city);
            }
        });

        initData();

    }


    private void initData() {
        cityList = cityWeatherModel.querrAllCityWeathers();

        adapter=new RVCityAdapter(cityList, new onRVCityAdapterItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent();
                intent.putExtra(Constants.CITY_NAME,cityList.get(position).getCityName());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        GridLayoutManager manager=new GridLayoutManager(this,2);
        rvCity.setLayoutManager(manager);
        rvCity.setAdapter(adapter);
    }

    public interface  onRVCityAdapterItemClickListener{
        void onItemClick(int position);
    }
}
