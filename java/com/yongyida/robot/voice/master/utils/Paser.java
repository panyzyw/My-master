package com.yongyida.robot.voice.master.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.yongyida.robot.voice.master.bean.AnswerInfoBean;
import com.yongyida.robot.voice.master.bean.TypeInfoBean;

public class Paser {

	public static TypeInfoBean parseTypeResult(String json) {
        TypeInfoBean type = null;
        if (type == null) {
            type = new TypeInfoBean();
        }
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject result = new JSONObject(tokener);
            type.setmService(result.getString("service"));
            type.setmOperation(result.getString("operation"));
            type.setmText(result.getString("text"));
            type.setmRc(result.getInt("rc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 数据类型结果.
     *
     * @param json
     * @return
     */
    public static AnswerInfoBean parseAnswerResult(String json) {
        AnswerInfoBean answer = null;
        if (answer == null) {
            answer = new AnswerInfoBean();
        }
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject result = new JSONObject(tokener);
            JSONObject semantic = result.getJSONObject("semantic");
            JSONObject slots = semantic.getJSONObject("slots");
            JSONObject location = slots.getJSONObject("location");
            // {
            // "operation": "POSITION",
            // "rc": 0,
            // "semantic": {
            // "slots": {
            // "location": {
            // "area": "麦积区",
            // "areaAddr": "麦积",
            // "city": "天水市",
            // "cityAddr": "天水",
            // "poi": "元龙镇",
            // "province": "甘肃省",
            // "provinceAddr": "甘肃",
            // "type": "LOC_POI"
            // }
            // }
            // },
            // "service": "map",
            // "text": "甘肃省天水市麦积区元龙镇在哪里"
            // }
            if (location.has("area")) {
                answer.setmArea(location.getString("area"));
            }
            if (location.has("areaAddr")) {
                answer.setmAreaAddr(location.getString("areaAddr"));
            }
            if (location.has("city")) {
                answer.setmCity(location.getString("city"));
            }
            if (location.has("cityAddr")) {
                answer.setmCityAddr(location.getString("cityAddr"));
            }
            if (location.has("poi")) {
                answer.setmPoi(location.getString("poi"));
            }
            if (location.has("province")) {
                answer.setProvince(location.getString("province"));
            }
            if (location.has("provinceAddr")) {
                answer.setProvinceAddr(location.getString("provinceAddr"));
            }
            if (location.has("type")) {
                answer.setmType(location.getString("type"));
            }

            // if (location.getString("type").equals("LOC_POI")) {
            // answer.setmType(location.getString("type")) ;
            // answer.setmCity(location.getString("city"));
            // answer.setmPoi(location.getString("poi"));
            // }
            // if (location.getString("type").equals("LOC_BASIC")) {
            // answer.setmType(location.getString("type")) ;
            // if (location.has("area")) {
            // answer.setmArea(location.getString("area"));
            // } else if (location.has("city")) {
            // answer.setmCity(location.getString("city"));
            // } else if (location.has("province")) {
            // answer.setProvince(location.getString("province"));
            // } else {
            // Log.e("没有结果", "没有结果");
            // }
            // }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Paser", answer.toString());
        return answer;
    }
}