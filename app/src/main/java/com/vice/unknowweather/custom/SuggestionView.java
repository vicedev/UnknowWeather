package com.vice.unknowweather.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vice.unknowweather.R;
import com.vice.unknowweather.bean.Weather;
import com.vice.unknowweather.utils.DisplayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vice on 2016/12/22 0022.
 */
public class SuggestionView extends LinearLayout {
    private Map<String, String> suggestionNmaeMap;

    public SuggestionView(Context context) {
        super(context);
    }

    public SuggestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        suggestionNmaeMap = new HashMap<>();

        suggestionNmaeMap.put("air", "空气指数");
        suggestionNmaeMap.put("comf", "舒适度指数");
        suggestionNmaeMap.put("cw", "洗车指数");
        suggestionNmaeMap.put("drsg", "穿衣指数");
        suggestionNmaeMap.put("flu", "感冒指数");
        suggestionNmaeMap.put("sport", "运动指数");
        suggestionNmaeMap.put("trav", "旅游指数");
        suggestionNmaeMap.put("uv", "紫外线指数");
    }

    public SuggestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSuggestiont(Weather.HeWeather5Bean.SuggestionBean suggestions) {
        removeAllViews();

        TextView tvName = new TextView(getContext());
        tvName.setText("生活建议");
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(18);
        addView(tvName);

        Weather.HeWeather5Bean.SuggestionBean.AirBean air = suggestions.getAir();
        Weather.HeWeather5Bean.SuggestionBean.ComfBean comf = suggestions.getComf();
        Weather.HeWeather5Bean.SuggestionBean.CwBean cw = suggestions.getCw();
        Weather.HeWeather5Bean.SuggestionBean.DrsgBean drsg = suggestions.getDrsg();
        Weather.HeWeather5Bean.SuggestionBean.FluBean flu = suggestions.getFlu();
        Weather.HeWeather5Bean.SuggestionBean.SportBean sport = suggestions.getSport();
        Weather.HeWeather5Bean.SuggestionBean.TravBean trav = suggestions.getTrav();
        Weather.HeWeather5Bean.SuggestionBean.UvBean uv = suggestions.getUv();

        addSuggestion("air", air.getTxt());
        addSuggestion("comf", comf.getTxt());
        addSuggestion("cw", cw.getTxt());
        addSuggestion("drsg", drsg.getTxt());
        addSuggestion("flu", flu.getTxt());
        addSuggestion("sport", sport.getTxt());
        addSuggestion("trav", trav.getTxt());
        addSuggestion("uv", uv.getTxt());

    }

    private void addSuggestion(String name, String txt) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(txt)) {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_suggestion_item, this, false);
            TextView tv = (TextView) layout.findViewById(R.id.tv_suggestion);
            tv.setText(suggestionNmaeMap.get(name) + "：" + txt);
            addView(layout);
        }
    }
}
