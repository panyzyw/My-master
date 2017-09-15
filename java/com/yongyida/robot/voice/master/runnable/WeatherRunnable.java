package com.yongyida.robot.voice.master.runnable;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationProviderProxy;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.WeatherBean;
import com.yongyida.robot.voice.master.bean.WeatherBean.Datetime;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WeatherRunnable extends BaseRunnable{
    private static final String TAG = "WeatherRunnable";
    private static AMapLocationListener aMapLocationListener;
    private Datetime mDatetime;
    private String cityName = "";//不带市字的城市名
    private String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "log";
    private WeatherBean mWeatherBean;
    public WeatherRunnable(String result){
        mPriority = PRIORITY_OTHER;
        mInterrupt_state = INTERRUPT_STATE_STOP;
        mResult = result;
    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  1:
                    if(aMapLocationListener != null){
                        MasterApplication.aMapLocManager.removeUpdates(aMapLocationListener);
                        aMapLocationListener = null;
                    }
                    requestServerForWeatherInfo(cityName,mDatetime);
                    break;
            }
        }
    };

    @Override
    public void run() {
        MyLog.i("WeatherRunnable", "run()");
        WeatherBean weatherBean = BeanUtils.parseWeatherJson(mResult, WeatherBean.class);
        mWeatherBean = weatherBean;
        if(  weatherBean == null ){
            MyLog.e("WeatherRunnable", "jokeBean == null");
            voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
            return ;
        }
        //补丁,将 老版本 里的这个拦截的语义转换成没有拦截的普通语义  (吐槽: 语义不能乱写)
        if(weatherBean.operation.equals("123")){   //当查询天气 没说地点时  会被拦截
            if(weatherBean.semantic.slots.datetime == null && weatherBean.semantic.slots.location == null){//只识别到或只说“天气”二字时，引导用户查询天气情况
                mIsEndSendRecycle = true;
                voiceTTS("要查询天气情况请指定日期或地点");
                return;
            }
            if( doPatch(weatherBean) != 0){
                voiceTTS("天气语义解析异常");
                return ;
            }
        }
        doWeather(weatherBean);
    }

    private int doPatch(WeatherBean wb) {
        MyLog.i("WeatherRunnable", "doPatch");
        if(wb.semantic.slots.datetime.date.equals("昨天")){
            wb.semantic.slots.datetime.dateOrig=wb.semantic.slots.datetime.date;
            wb.semantic.slots.datetime.date = getYMD(System.currentTimeMillis() - 24*60*60*1000);
            return 0;
        }

        if(wb.semantic.slots.datetime.date.equals("今天") || wb.semantic.slots.datetime.date.equals("现在")){
            wb.semantic.slots.datetime.dateOrig="今天";
            wb.semantic.slots.datetime.date = getYMD(System.currentTimeMillis());
            return 0;
        }

        if(wb.semantic.slots.datetime.date.equals("明天")){
            wb.semantic.slots.datetime.dateOrig=wb.semantic.slots.datetime.date;
            wb.semantic.slots.datetime.date = getYMD(System.currentTimeMillis() + 24*60*60*1000);
            return 0;
        }

        if(wb.semantic.slots.datetime.date.equals("后天")){
            wb.semantic.slots.datetime.dateOrig=wb.semantic.slots.datetime.date;
            wb.semantic.slots.datetime.date = getYMD(System.currentTimeMillis() + 24*60*60*1000*2);
            return 0;
        }

        if(wb.semantic.slots.datetime.date.equals("大后天")){
            wb.semantic.slots.datetime.dateOrig=wb.semantic.slots.datetime.date;
            wb.semantic.slots.datetime.date = getYMD(System.currentTimeMillis() + 24*60*60*1000*3);
            return 0;
        }
        return -1;
    }

    //获取年月日 eg: 2016-01-01
    private String getYMD(long timestamp){
        MyLog.i("WeatherRunnable", "getYMD");
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ymdString = formatter.format(date);
        MyLog.i("WeatherRunnable", "getYMD : ymdString = " + ymdString);
        return ymdString;
    }

    private void doWeather(WeatherBean wb) {
        MyLog.i("WeatherRunnable", "doWeather");
        try{
            Datetime datetime = wb.semantic.slots.datetime;
            mDatetime = datetime;
            com.yongyida.robot.voice.master.bean.WeatherBean.Location location = wb.semantic.slots.location;

            if(location != null && location.province != null && location.province.equals("吉林省")){ //此为特殊情况，吉林市与吉林省同名，如果问“吉林的天气”则默认为吉林市的天气
                requestServerForWeatherInfo("吉林",datetime);
            }else if( location == null || location.city == null || location.city.equals("CURRENT_CITY") ){ //没有指定城市 , 需要定位城市
                startLocateCurrentCity();//开启城市定位
            }else{ //指定了城市
                if(location.city.contains("市")){
                    cityName = location.city.substring(0,location.city.lastIndexOf("市"));
                }else{
                    cityName = location.city;
                }
                requestServerForWeatherInfo(cityName,datetime);
            }

        }catch(Exception e){
            MyLog.e("WeatherRunnable", MyExceptionUtils.getStringThrowable(e));
        }
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if( TextUtils.isEmpty(cityName) ){
                voiceTTS("定位当前城市失败");
                return ;
            }
        }
    };

    private void startLocateCurrentCity(){
        Log.e(TAG, "startLocateCurrentCity");
        mhandler.postDelayed(myRunnable,10*1000);
        if(aMapLocationListener != null){
            MasterApplication.aMapLocManager.removeUpdates(aMapLocationListener);
            aMapLocationListener = null;
        }
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
            @Override
            public void onLocationChanged(android.location.Location location) {}
            @Override
            public void onLocationChanged(AMapLocation arg0) {
                if (arg0 != null) {
                    String locationInfo = " province:" + arg0.getProvince() + " city:" + arg0.getCity()+" district:"+arg0.getDistrict()+
                            " street:"+ arg0.getStreet() + " address:"+arg0.getAddress() + "\n" +  " ErrorMessage:" + arg0.getAMapException().getErrorMessage()+
                            " ErrorCode:"+arg0.getAMapException().getErrorCode() + "\n";
                    MyLog.e("WeatherRunnable", locationInfo);
                    saveLog(locationInfo);
                    cityName = arg0.getCity().substring(0, arg0.getCity().lastIndexOf("市"));
                    mhandler.removeCallbacks(myRunnable);
                    mhandler.sendEmptyMessage(1);
                } else {
                    Log.e(TAG, "startLocateCurrentCity fail!");
                }
                MasterApplication.aMapLocManager.removeUpdates(aMapLocationListener);
            }
        };
        MasterApplication.aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 90000, 5, aMapLocationListener);
    }

    private void requestServerForWeatherInfo(final String cityName, final Datetime datetime) {
        Log.e(TAG, "requestServerForWeatherInfo city:" + cityName);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("area", cityName);
        RequestParams requestparams = new RequestParams();
        try {
            requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestparams.setContentType("text/plain");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Domain.getWeatherUrl(), requestparams, new RequestCallBack<String>() {
            @Override
            public void onStart() {}
            @Override
            public void onLoading(long total, long current,boolean isUploading) {}
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e(TAG, "requestweather onSuccess responseInfo=" + responseInfo.result);
                String tospeekContent = getWeatherInfoForSpeek(responseInfo.result, cityName, datetime);
                voiceTTS(tospeekContent);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e(TAG, "requestServer onFailure");
                voiceTTS("访问服务器失败");
            }
        });
    }

    private String getWeatherInfoForSpeek(String result, String cityName, Datetime datetime) {
        Log.e(TAG, "getWeatherInfoForSpeek");
        JSONArray jsonarray = null;
        try {
            JSONTokener jsontokener = new JSONTokener(result);
            JSONObject jsonobject = new JSONObject(jsontokener);
            String ret = jsonobject.optString("ret");
            if ("0".equals(ret)) {
                jsonarray = new JSONArray(jsonobject.optString("Weathers"));
                if (jsonarray == null || jsonarray.length() <= 0) {
                    MyLog.e("WeatherRunnable", "jsonarray == null || jsonarray.length() <= 0");
                    return "获取服务器天气资源失败";
                }
            } else {
                return "获取服务器天气资源失败";
            }
        } catch (Exception e) {
            MyLog.e("WeatherRunnable", MyExceptionUtils.getStringThrowable(e));
            return "获取服务器天气资源失败";
        }


        return getFullWeatherInfoText(cityName, datetime, jsonarray);
    }

    //获取完整的用于播报的天气信息文本
    private String getFullWeatherInfoText(String cityName, Datetime datetime, JSONArray jsonarray) {
        MyLog.i("WeatherRunnable", "getFullWeatherInfoText");
        String text = cityName;
        if (datetime.date.equals("CURRENT_DAY")) { //今天的天气
            text += "今天的天气情况为";
            try {
                String oneWeatherText = getOneWeatherSpeekText(jsonarray.getJSONObject(0));
                if (TextUtils.isEmpty(oneWeatherText))
                    text = "解析天气异常";
                else
                    text += oneWeatherText;
            } catch (JSONException e) {
                MyLog.e("WeatherRunnable", MyExceptionUtils.getStringThrowable(e));
                text = "解析天气异常";
            }
            return text;
        }

        if (datetime.date.contains("-")) { //eg : 2016-05-01
            text += datetime.dateOrig + "的天气情况为";
            try {
                if (datetime.date.compareTo(jsonarray.getJSONObject(0).getString("date")) < 0 ||
                        datetime.date.compareTo(jsonarray.getJSONObject(jsonarray.length() - 1).getString("date")) > 0) {
                    return "只能查询今天开始7天内的天气情况";
                }

                for (int i = 0; i < jsonarray.length(); i++) {
                    if (datetime.date.equals(jsonarray.getJSONObject(i).getString("date"))) {
                        text += getOneWeatherSpeekText(jsonarray.getJSONObject(i));
                        return text;
                    }
                }
            } catch (Exception e) {
                MyLog.e("WeatherRunnable", MyExceptionUtils.getStringThrowable(e));
                text = "解析天气异常";
            }

            return text;
        }
        return "解析天气异常";
    }

    //eg :晴 温度为 20 到 25度 风速为 微风
    private String getOneWeatherSpeekText(JSONObject jsonObject) {
        MyLog.i("WeatherRunnable", "getOneWeatherSpeekText");
        String weatherType;
        String wind_force;
        String temperature_min;
        String temperature_max;
        try {
            weatherType = jsonObject.getString("weatherType");
            wind_force = jsonObject.getString("wind_force");
            temperature_min = jsonObject.getString("temperature_min");
            temperature_max = jsonObject.getString("temperature_max");
        } catch (JSONException e) {
            MyLog.e("", MyExceptionUtils.getStringThrowable(e));
            return null;
        }
        String text = weatherType + "，温度为 : " + temperature_min + "度到" + temperature_max + "度 ， 风速为" + wind_force;
        return text;
    }

    public void voiceTTS(String text, MySynthesizerListener mySynthesizerListener) {
        MyLog.i("WeatherRunnable", "voiceTTS");
        Utils.collectInfoToServer(mWeatherBean,text);
        /*
           播放天气时，如果内容含有区有二字，会有杂音，所以将其替换为屈有
            如果地区变成地屈后，会影响地的发音，所以将地改为第
         */
        if(text.contains(MasterApplication.mAppContext.getString(R.string.shanquyou))){
            text = text.replaceAll(MasterApplication.mAppContext.getString(R.string.shanquyou),MasterApplication.mAppContext.getString(R.string.shanquyou2));
        }
        if(text.contains(MasterApplication.mAppContext.getString(R.string.diquyou))){
            text = text.replaceAll(MasterApplication.mAppContext.getString(R.string.diquyou),MasterApplication.mAppContext.getString(R.string.diquyou2));
        }
        if (mySynthesizerListener == null)
            mySynthesizerListener = new MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority, mIsEndSendRecycle, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }

    public void voiceTTS(String text) {
        voiceTTS(text, null);
    }

    private void saveLog(String locationInfo){
        File file = new File(logPath);
        if(!file.exists()){
            file.mkdirs();
        }
        File savefileName = new File(logPath + File.separator + ".weather_location");
        if(!savefileName.exists()){
            try {
                savefileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String datetime = simpleDateFormat.format(new Date());
            String info = "----------------" + datetime + "----------------\n";
            info += locationInfo;
            OutputStream out = new FileOutputStream(savefileName, true);
            OutputStreamWriter osw = new OutputStreamWriter(out,"GBK");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(info);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
