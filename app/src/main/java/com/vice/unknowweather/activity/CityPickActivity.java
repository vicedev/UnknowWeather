package com.vice.unknowweather.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.vice.unknowweather.App;
import com.vice.unknowweather.R;
import com.vice.unknowweather.adapter.CityPickHotCityAdapter;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.utils.ToastUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CityPickActivity extends BaseActivity {

    private BDLocationListener myListener = new MyLocationListener();
    private AutoCompleteTextView autoCompleteTv;
    private RecyclerView rvHotCities;
    private CityPickHotCityAdapter cityPickHotCityAdapter;
    private TextView tvLocateCity;
    private TextView tvReLocate;
    private ImageButton ibBack;
    private boolean isLocating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pick);

        autoCompleteTv = (AutoCompleteTextView) findViewById(R.id.auto_complete_tv);
        rvHotCities = (RecyclerView) findViewById(R.id.rv_hot_cities);
        tvLocateCity = (TextView) findViewById(R.id.tv_locate_city);
        tvReLocate = (TextView) findViewById(R.id.tv_relocate);
        ibBack = (ImageButton) findViewById(R.id.ib_back);

        //autoCompleteTv初始化
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_dropdown_item_1line, Constants.CITY_NAMES);
        autoCompleteTv.setAdapter(adapter);

        autoCompleteTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String cityName = tv.getText().toString();
                cityPickHotCityAdapter.setCityPickResult(cityName);
            }
        });

        //rvHotCities初始化
        cityPickHotCityAdapter = new CityPickHotCityAdapter();
        rvHotCities.setAdapter(cityPickHotCityAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        rvHotCities.setLayoutManager(manager);


        tvReLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新定位位置
                if (tvReLocate.getText().toString().equals("（点击重新定位）")) {
                    requestLocate();
                }
            }
        });

        tvLocateCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocating) {
                    ToastUtils.showShort("正在定位中...");
                } else {
                    String cityName = tvLocateCity.getText().toString().trim();
                    if (cityName.equals("定位失败") || TextUtils.isEmpty(cityName)) {

                    } else {
                        cityPickHotCityAdapter.setCityPickResult(cityName);
                    }
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        requestLocate();


    }


    //开始定位
    private void startLocate() {
        App.mLocationClient.registerLocationListener(myListener);    //注册监听函数
        App.mLocationClient.start();
        tvReLocate.setText("（正在定位中...）");
        isLocating = true;
    }

    //停止定位
    private void stopLocate() {
        App.mLocationClient.stop();
        App.mLocationClient.unRegisterLocationListener(myListener);
        tvReLocate.setText("（点击重新定位）");
        isLocating = false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocate() {
        CityPickActivityPermissionsDispatcher.openLocateWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void openLocate() {
        startLocate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CityPickActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        ToastUtils.showLong("请允许所有权限，这样才可以自动定位");
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void permissionDenied() {
        tvLocateCity.setText("定位失败");
        tvReLocate.setText("（点击重新定位）");

    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void neverAskAgain() {
        tvReLocate.setText("");
        ToastUtils.showLong("在 设置--应用--未知天气--权限 中可以手动打开权限");
    }

    class MyLocationListener implements BDLocationListener {

        private String cityName;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                cityName = location.getCity();
                cityName = cityName.replace("市", "").trim();
            } else {
                cityName = "定位失败";
            }
            stopLocate();
            tvLocateCity.setText(cityName);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocate();
    }


}
