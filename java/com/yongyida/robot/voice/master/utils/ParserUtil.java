package com.yongyida.robot.voice.master.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yongyida.robot.voice.master.bean.AddressBean;
import com.yongyida.robot.voice.master.bean.EndLocBean;
import com.yongyida.robot.voice.master.bean.LocationBean;
import com.yongyida.robot.voice.master.bean.SemanticBean;
import com.yongyida.robot.voice.master.bean.SlotsBean;
import com.yongyida.robot.voice.master.bean.StartLocBean;
import com.yongyida.robot.voice.master.bean.StepBean;

public class ParserUtil {

	public static <T> T parseResult(String json, Class<T> T) {
        Gson gson = new Gson();

        return gson.fromJson(json, T);

    }

    public static List<StepBean> parseStep(String json) {
        List<StepBean> stepList = new ArrayList<StepBean>();
        StepBean step = null;
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.getInt("status") == 1) {
                JSONArray array = obj.getJSONObject("route").getJSONArray("paths").getJSONObject(0)
                        .getJSONArray("steps");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobj = array.getJSONObject(i);
                    String instruction = jsonobj.getString("instruction");
                    step = new StepBean(instruction);
                    stepList.add(step);
                }

            }

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return stepList;
    }

    /**
     * { "semantic": { "slots": { "distance": "三公里", "endLoc": { "keyword":
     * "望江西路", "poi": "科大讯飞", "city": "CURRENT_CITY", "type": "LOC_POI" },
     * "startLoc": { "keyword": "家乐福", "poi": "市政府", "city": "CURRENT_CITY",
     * "type": "LOC_POI" } } }, "rc": 0, "operation": "ROUTE", "service": "map",
     * "text": "从家乐福附近三公里的市政府导航到望江西路的科大讯飞"
     *
     * @param json
     * @return
     */

    public static AddressBean parserPoint(String json) {
        AddressBean address = new AddressBean();
        SemanticBean semantic = new SemanticBean();
        SlotsBean slots = new SlotsBean();
        StartLocBean startLoc = new StartLocBean();
        EndLocBean endLoc = new EndLocBean();
        JSONObject joResult;

        try {

            joResult = new JSONObject(json).getJSONObject("semantic").getJSONObject("slots");
            JSONObject obj1 = joResult.getJSONObject("startLoc");
            if (obj1.has("province")) {
                startLoc.setProvince(obj1.getString("province"));
            }
            if (obj1.has("city")) {
                startLoc.setCity(obj1.getString("city"));
            }
            if (obj1.has("type")) {
                startLoc.setType(obj1.getString("type"));
            }
            if (obj1.has("area")) {
                startLoc.setArea(obj1.getString("area"));
            }
            if (obj1.has("poi")) {
                startLoc.setPoi(obj1.getString("poi"));
            }

            JSONObject obj2 = joResult.getJSONObject("endLoc");
            if (obj2.has("province")) {
                endLoc.setProvince(obj2.getString("province"));
            }
            if (obj2.has("city")) {
                endLoc.setCity(obj2.getString("city"));
            }
            if (obj2.has("type")) {
                endLoc.setType(obj2.getString("type"));
            }
            if (obj2.has("area")) {
                endLoc.setArea(obj2.getString("area"));
            }
            if (obj2.has("poi")) {
                endLoc.setPoi(obj2.getString("poi"));
            }
            slots.setStartLoc(startLoc);
            slots.setEndLoc(endLoc);
            semantic.setSlots(slots);
            address.setSemantic(semantic);

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return address;

    }

    /**
     * { "count": "1", "geocodes": [ { "adcode": "430102", "building": { "name":
     * [], "type": [] }, "city": "长沙市", "citycode": "0731", "district": "芙蓉区",
     * "formatted_address": "湖南省长沙市芙蓉区长沙火车站", "level": "兴趣点", "location":
     * "113.013092,28.194304", "neighborhood": { "name": [], "type": [] },
     * "number": [], "province": "湖南省", "street": [], "township": [] } ],
     * "info": "OK", "infocode": "1000", "status": "1" }
     *
     * @param json
     * @return
     */

    public static LocationBean parserLocation(String json) {
        LocationBean location = new LocationBean();
        JSONObject joResult;
        try {
            joResult = new JSONObject(json);
            if (joResult.getInt("status") == 1) {
                JSONArray array = joResult.getJSONArray("geocodes");
                JSONObject obj = array.getJSONObject(0);
                String lnglag = obj.getString("location");
                String lng = lnglag.substring(0, lnglag.indexOf(","));
                String lat = lnglag.substring(lnglag.indexOf(",") + 1, lnglag.length());
                location.setLng(Double.valueOf(lng));
                location.setLat(Double.valueOf(lat));

            }
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return location;

    }
}