package com.yongyida.robot.voice.master.runnable;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.bean.StockBean2;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class StockRunnable extends BaseRunnable {
    private StockBean2 mStockBean2;
    private final static String TAG = "StockRunnable";
    public StockRunnable(String result) {
        mPriority = PRIORITY_OTHER;
        mInterrupt_state = INTERRUPT_STATE_STOP;
        mResult = result;
    }

    @Override
    public void run() {
        Log.i(TAG, "run");
        String stockCode = getStockCode(mResult);
        mStockBean2 = BeanUtils.parseStockBean2Json(mResult,StockBean2.class);
        if(TextUtils.isEmpty(stockCode)){
            voiceTTS(Constant.comman_unkonw,new TTS.MySynthesizerListener());
        }else{
            requestStock(stockCode);
        }
    }

    private void requestStock(String stockCode){
        Log.i(TAG,"requetStock");
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("stockCode",stockCode);
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.setBodyEntity(new StringEntity(new JSONObject(params).toString(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestParams.setContentType("text/plain");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Domain.getStockRequestUrl(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i(TAG,"responseInfo:"+responseInfo.result);
                String speakText = parseStockJsonForSpeak(responseInfo.result);
                String emotion = parseStockJsonForEmotion(responseInfo.result);
                if(!TextUtils.isEmpty(emotion)){
                    if(emotion.equals("cry")){
                        Utils.sendBroadcast(Constant.ACTION_CRY, "stock", "showAction");
                    }else if(emotion.equals("laugh")){
                        Utils.sendBroadcast(Constant.ACTION_LAUGH, "stock", "showAction");
                    }
                }
                voiceTTS(speakText,null);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG,"onFailure:"+s);
                voiceTTS("访问服务器失败",null);
            }
        });
    }

    private String getStockCode(String mResult){
        String stockCode = "";
        try {
            JSONObject resultObject = new JSONObject(mResult);
            JSONObject semanticObj = resultObject.getJSONObject("semantic");
            JSONObject slots = semanticObj.getJSONObject("slots");
            if (slots.has("category")) {
                String category = slots.getString("category");
                String code = slots.getString("code");
                stockCode = category + code;
            }else{
                String name = slots.getString("name");
                if(name.equals("天彩控股")){
                    stockCode = "hk03882";
                }else if(name.equals("青海春天")){
                    stockCode = "sh600381";
                }
            }
            Log.i(TAG, "stockCode:" + stockCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockCode;
    }

    private String parseStockJsonForSpeak(String stockJson){
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(stockJson);
            result = jsonObject.optString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseStockJsonForEmotion(String stockJson){
        String emotion = "";
        try {
            JSONObject jsonObject = new JSONObject(stockJson);
            emotion = jsonObject.optString("emotion");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emotion;
    }


    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        Log.i(TAG, "voiceTTS");
        if( mySynthesizerListener == null )
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority, mIsEndSendRecycle, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mStockBean2,text);
    }
}