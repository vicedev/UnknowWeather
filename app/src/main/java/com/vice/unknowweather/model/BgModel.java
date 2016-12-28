package com.vice.unknowweather.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.vice.unknowweather.global.Constants;
import com.vice.unknowweather.utils.SPUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by vice on 2016/12/28 0028.
 */
public class BgModel {
    private static BgModel bgModel;
    public BgModel() {
    }
    public static BgModel getInstance(){
        if (bgModel==null){
            synchronized (BgModel.class){
                if (bgModel==null){
                    bgModel=new BgModel();
                }
            }
        }
        return bgModel;
    }

    public void loadAutoChangeBg(Object tag, final AutoChangeBgCallBack callBack){
       OkGo.get(Constants.AUTO_CHANGE_PIC_URL)
               .tag(tag)
               .execute(new StringCallback() {
                   @Override
                   public void onSuccess(String s, Call call, Response response) {
                       callBack.onSuccess(s);
                   }

                   @Override
                   public void onError(Call call, Response response, Exception e) {
                       super.onError(call, response, e);
                       callBack.onFailure();
                   }
               });
    }

    public void setAutoChangeBgToSP(String url){
        SPUtils.setAutoChangeBg(url);
    }

    public String getAutoChangeBg(){
        return SPUtils.getAutoChangeBg();
    }



    public void cancelAllReauest(){
        OkGo.getInstance().cancelAll();
    }

    public void cancelByTag(Object tag){
        OkGo.getInstance().cancelTag(tag);
    }


    public interface AutoChangeBgCallBack{
        void onSuccess(String url);
        void onFailure();
    }

}
