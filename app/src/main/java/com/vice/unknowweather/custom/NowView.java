package com.vice.unknowweather.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.Weather;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class NowView extends FrameLayout {

    private TextView tvTmp;
    private TextView tvCondTxt;
    private TextView tvHum;
    private TextView tvVis;
    private TextView tvWind;
    private TextView tvTime;
    private TextView tvAqi;
    private String time;
    private String aqiTxt;

    public NowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.activity_main_now_view, this, true);

        ImageView ivImage= (ImageView) findViewById(R.id.iv_image);
        Glide.with(getContext()).load(R.mipmap.xx).into(ivImage);

        tvTmp = (TextView) findViewById(R.id.tv_tmp);
        tvCondTxt = (TextView) findViewById(R.id.tv_cond);
        tvHum = (TextView) findViewById(R.id.tv_hum);
        tvVis = (TextView) findViewById(R.id.tv_vis);
        tvWind = (TextView) findViewById(R.id.tv_wind);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAqi = (TextView) findViewById(R.id.tv_aqi);
    }

    public NowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public NowView(Context context) {
        super(context);
    }

    public void setNow(Weather.HeWeather5Bean.NowBean now, String updateTime, Weather.HeWeather5Bean.AqiBean aqi){
        String tmp = now.getTmp()+"℃";//温度
        String condTxt = now.getCond().getTxt();//天气
        String hum = "相对湿度："+now.getHum()+"%";//相对湿度
        String vis = "能见度："+now.getVis()+"km";//能见度
        String dir = now.getWind().getDir();//风向
        String sc = now.getWind().getSc()+"级";//风力
        String wind=dir+"  "+sc;
        //香港、澳门、台湾没有aqi，有毒
        if (!TextUtils.isEmpty(updateTime)){
            time = "发布时间："+updateTime.split(" ")[1];
        }
        if (aqi!=null){
            aqiTxt = aqi.getCity().getQlty()+" "+aqi.getCity().getAqi();
        }

        tvTmp.setText(tmp);
        tvCondTxt.setText(condTxt);
        tvHum.setText(hum);
        tvVis.setText(vis);
        tvWind.setText(wind);
        if (!TextUtils.isEmpty(time)){
            tvTime.setText(time);
        }
        if (!TextUtils.isEmpty(aqiTxt)){
            tvAqi.setText(aqiTxt);
        }
    }
}
