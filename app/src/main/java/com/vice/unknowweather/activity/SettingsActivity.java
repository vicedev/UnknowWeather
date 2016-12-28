package com.vice.unknowweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.suke.widget.SwitchButton;
import com.vice.unknowweather.R;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.model.BgModel;
import com.vice.unknowweather.service.NotificationWeatherService;
import com.vice.unknowweather.utils.BitmapUtils;
import com.vice.unknowweather.utils.SPUtils;
import com.vice.unknowweather.utils.ToastUtils;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.util.List;

import cn.qqtheme.framework.picker.ColorPicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private RadioGroup rgBg;
    private RadioButton rbAutoChange;
    private RadioButton rbPhoto;
    private RadioButton rbPureColor;
    private ImageView ivCurrentBg;

    private ProgressDialog dialog = null;

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };
    private SwitchButton sbNotificationWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        rgBg = (RadioGroup) findViewById(R.id.rg_bg);
        rbAutoChange = (RadioButton) findViewById(R.id.rb_auto_change);
        rbPhoto = (RadioButton) findViewById(R.id.rb_photo);
        rbPureColor = (RadioButton) findViewById(R.id.rb_pure_color);
        ivCurrentBg = (ImageView) findViewById(R.id.iv_current_bg);
        sbNotificationWeather = (SwitchButton) findViewById(R.id.sb_notification_weather);


        sbNotificationWeather.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Intent intent=new Intent(SettingsActivity.this, NotificationWeatherService.class);
                if (isChecked){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rgBg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String currentBgWay = SPUtils.getCurrentBgWay();
                switch (checkedId) {
                    case R.id.rb_auto_change:
                        String url = BgModel.getInstance().getAutoChangeBg();
                        Glide.with(SettingsActivity.this).load(url).error(R.mipmap.bg).into(ivCurrentBg);
                        if (currentBgWay.equals(Constants.BG_AUTO_CHANGE)) {
                            return;
                        }
                        BgModel.getInstance().loadAutoChangeBg(SettingsActivity.this, new BgModel.AutoChangeBgCallBack() {
                            @Override
                            public void onSuccess(String url) {
                                Glide.with(SettingsActivity.this).load(url).error(R.mipmap.bg).into(ivCurrentBg);
                                BgModel.getInstance().setAutoChangeBgToSP(url);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
//                        ToastUtils.showShort("自动");
                        SPUtils.setCurrentBgWay(Constants.BG_AUTO_CHANGE);

                        break;

                    case R.id.rb_photo:
                        String path = getFilesDir() + File.separator + "bg";
                        Glide.with(SettingsActivity.this).load((path)).error(R.mipmap.bg)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(ivCurrentBg);
                        if (currentBgWay.equals(Constants.BG_PHOTO)) {
                            return;
                        }
                        selectPhoto();
//                        ToastUtils.showShort("photo");
                        SPUtils.setCurrentBgWay(Constants.BG_PHOTO);

                        break;

                    case R.id.rb_pure_color:
                        int color=SPUtils.getCustomColorBg();
                        ColorDrawable colorDrawable=new ColorDrawable(color);
                        ivCurrentBg.setImageDrawable(colorDrawable);
                        if (currentBgWay.equals(Constants.BG_PURE_COLOR)) {
                            return;
                        }
                        selectColor(color);
//                        ToastUtils.showShort("color");
                        SPUtils.setCurrentBgWay(Constants.BG_PURE_COLOR);

                        break;
                }
            }
        });

        //当前背景方式
        String currentBgWay = SPUtils.getCurrentBgWay();

        if (currentBgWay.equals(Constants.BG_PHOTO)) {
            rgBg.check(R.id.rb_photo);
        } else if (currentBgWay.equals(Constants.BG_PURE_COLOR)) {
            rgBg.check(R.id.rb_pure_color);
        } else {
            rgBg.check(R.id.rb_auto_change);
        }

        //默认通知栏是否开启
        boolean open=SPUtils.getOpenNotificationWeather();
        sbNotificationWeather.setChecked(open?true:false);


    }

    private void selectColor(int color) {

        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(color);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                ColorDrawable colorDrawable=new ColorDrawable(pickedColor);
                ivCurrentBg.setImageDrawable(colorDrawable);
                ToastUtils.showShort(ConvertUtils.toColorString(pickedColor));
                SPUtils.setCustomColorBg(pickedColor);
            }
        });
        picker.show();
    }

    private void selectPhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮背景色
                //.btnBgColor(Color.parseColor(""))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.back)
                .title("Images")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .allImagesText("All Images")
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .build();

        ImgSelActivity.startActivity(this, config, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            final List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    showProgress(true);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    for (String path : pathList) {
                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        BitmapUtils.saveImgToDisk(SettingsActivity.this,"bg",bmp);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    showProgress(false);
                    String path = getExternalFilesDir("bg") + File.separator + "bg";
//                    Bitmap bmp = BitmapUtils.getDiskBitmap(getFilesDir() + File.separator + "bg");
                    Glide.with(SettingsActivity.this).load(path).error(R.mipmap.bg)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).
                            into(ivCurrentBg);
                }
            }.execute();

        }
    }

    private void showProgress(boolean show) {
        if (show) {
            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("请稍后。。。");
            dialog.setCancelable(false);
            dialog.show();
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BgModel.getInstance().cancelByTag(this);
    }
}
