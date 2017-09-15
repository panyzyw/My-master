package com.yongyida.robot.voice.master.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.RobotAgeBean;
import com.yongyida.robot.voice.master.bean.StockBean;

public class JsonParser {
	public static StockBean getStock(String json) {
        StockBean stock = null;
        try {
            JSONObject joResult = new JSONObject(json);
            int number = joResult.getInt("errNum");
            if (number == 0) {
                JSONObject words = joResult.getJSONObject("retData");
                JSONArray array = words.getJSONArray("stockinfo");
                String id = "";
                String name = "";
                String currentPrice = "";
                String minurl = "";
                String increase = "";
                String growth = "";

                JSONObject jo = array.getJSONObject(0);
                id = jo.getString("code");
                name = jo.getString("name");
                currentPrice = jo.getString("currentPrice");
                if (jo.has("minurl")) {
                    minurl = jo.getString("minurl");
                }
                Double increasedb = jo.getDouble("increase");
                increasedb = ConvertUtil.roundDouble(increasedb, 2);
                increase = String.valueOf(increasedb);

                Double closingPricedb = jo.getDouble("closingPrice");
                Double currentPricedb = jo.getDouble("currentPrice");
                Double growthdb = ConvertUtil.roundDouble((currentPricedb - closingPricedb), 2);
                growth = String.valueOf(growthdb);
                Log.i("JSON", "growth:" + growth);

                String shanghai = "";
                String shcurprice = "";
                String shrate = "";
                String shenzhen = "";
                String szrate = "";
                String szcurprice = "";
                JSONObject zhishu = words.getJSONObject("market");
                JSONObject sh = zhishu.getJSONObject("shanghai");
                Double shanghaidb = sh.getDouble("curdot");
                shanghaidb = ConvertUtil.roundDouble(shanghaidb, 2);
                shanghai = String.valueOf(shanghaidb);

                Double shcurpricedb = sh.getDouble("curprice");
                shcurpricedb = ConvertUtil.roundDouble(shcurpricedb, 2);
                shcurprice = String.valueOf(shcurpricedb);

                Double shratedb = sh.getDouble("rate");
                shratedb = ConvertUtil.roundDouble(shratedb, 2);
                shrate = String.valueOf(shratedb);

                JSONObject sz = zhishu.getJSONObject("shenzhen");
                Double shenzhendb = sz.getDouble("curdot");
                shenzhendb = ConvertUtil.roundDouble(shenzhendb, 2);
                shenzhen = String.valueOf(shenzhendb);

                Double szcurpricedb = sz.getDouble("curprice");
                szcurpricedb = ConvertUtil.roundDouble(szcurpricedb, 2);
                szcurprice = String.valueOf(szcurpricedb);

                Double szratedb = sz.getDouble("rate");
                szratedb = ConvertUtil.roundDouble(szratedb, 2);
                szrate = String.valueOf(szratedb);

                stock = new StockBean(id, name, currentPrice, growth, increase, minurl, shanghai, shrate, shcurprice,
                        shenzhen, szcurprice, szrate);
                Log.i("JSON", "stock" + stock.toString());
            } else {
                stock = new StockBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    /**
     * { "errNum": 0, "errMsg": "success", "retData": { "stockinfo": [ { "name":
     * "腾讯控股", "code": "00700", "date": "2015/11/12 10:03", "openningPrice":
     * 153.5, "closingPrice": 151.5, "hPrice": 155, "lPrice": 152.5,
     * "currentPrice": 154.6, "growth": 3.1, "growthPercent": 2.046,
     * "dealnumber": 4369877, "turnover": 673842792, "52hPrice": 171,
     * "52lPrice": 104.5 } ], "market": { "shanghai": { "name": "上证指数",
     * "curdot": 3628.91, "curprice": -21.339, "rate": -0.58, "dealnumber":
     * 1158244, "turnover": 15464180 }, "shenzhen": { "name": "深证成指", "curdot":
     * 12618.874, "curprice": -58.663, "rate": -0.46, "dealnumber": 142669567,
     * "turnover": 26938322 },
     *
     * @param json
     * @return
     */

    public static StockBean getHKStock(String json) {
        StockBean stock = null;
        try {
            JSONObject joResult = new JSONObject(json);
            int number = joResult.getInt("errNum");
            if (number == 0) {
                JSONObject words = joResult.getJSONObject("retData");
                JSONArray array = words.getJSONArray("stockinfo");
                String id = "";
                String name = "";
                String currentPrice = "";
                String minurl = "";
                String increase = "";
                String growth = "";

                JSONObject jo = array.getJSONObject(0);
                id = jo.getString("code");
                name = jo.getString("name");
                currentPrice = jo.getString("currentPrice");
                if (jo.has("minurl")) {
                    minurl = jo.getString("minurl");
                }
                Double increasedb = jo.getDouble("growthPercent");
                increasedb = ConvertUtil.roundDouble(increasedb, 2);
                increase = String.valueOf(increasedb);

                Double growthdb = jo.getDouble("growth");
                growthdb = ConvertUtil.roundDouble(growthdb, 2);
                growth = String.valueOf(growthdb);
                Log.i("JSON", "growth:" + growth);

                String hongkong = "";
                String hkcurprice = "";
                String hkrate = "";

                JSONObject zhishu = words.getJSONObject("market");
                JSONObject sh = zhishu.getJSONObject("HSI");
                Double shanghaidb = sh.getDouble("curdot");
                shanghaidb = ConvertUtil.roundDouble(shanghaidb, 2);
                hongkong = String.valueOf(shanghaidb);

                Double shcurpricedb = sh.getDouble("growth");
                shcurpricedb = ConvertUtil.roundDouble(shcurpricedb, 2);
                hkcurprice = String.valueOf(shcurpricedb);

                Double shratedb = sh.getDouble("rate");
                shratedb = ConvertUtil.roundDouble(shratedb, 2);
                hkrate = String.valueOf(shratedb);

                stock = new StockBean(id, name, currentPrice, growth, increase, minurl, hongkong, hkrate, hkcurprice);
                Log.i("JSON", "stock" + stock.toString());
            } else {
                stock = new StockBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }




    public static String parseStock(String json) {
        String ret = null;
        try {
            // JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(json);
            Log.i("JSON", "jsonResult" + joResult);
            // JSONObject obj = joResult.getJSONObject("semantic");
            // JSONArray array = joResult.getJSONArray("moreResults");
            // Log.i("array", array.toString());
            // for(int i = 0; i < array.length(); i++){
            // JSONObject stockObj = array.getJSONObject(0);
            JSONObject semanticObj = joResult.getJSONObject("semantic");
            JSONObject slots = semanticObj.getJSONObject("slots");
            if (slots.has("category")) {
                String category = slots.getString("category");
                String code = slots.getString("code");
                ret = category + code;
                Log.i("String", "ret" + ret);
            }else{
                String name = slots.getString("name");
                if(name.equals("天彩控股")){
                    ret = "hk03882";
                }else if(name.equals("青海春天")){
                    ret = "sh600381";
                }
            }
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
	 /**
	  * 薄言超脑json解析
	  * */   	
	 public static String praseRSVPJsonResult(String result){
		   String message = null ;
		   try {
			   JSONObject jsons = new JSONObject(result);
			   int status = jsons.getInt("status");
			   switch (status) {
			case 0:
				 message = jsons.getJSONArray("stage").getJSONObject(0).getString("message");	
				break;
			case -1:
				message = null;
				break;
			default:
				break;
			}
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return message;
	   }
	 /**
	  * 向服务器请求薄言的json解析
	  * */
	 public static String praseServiceJsonResult(String result){
		   String message = null ;
		   try {
			   JSONObject jsons = new JSONObject(result);
			   int status = jsons.getInt("ret");
			   switch (status) {
			case 0:
				message = jsons.getString("answer");
				// message = jsons.getJSONArray("stage").getJSONObject(0).getString("message");	
				break;
			case -1:
				message = null;
				break;
			default:
				message = "";
				break;
			}
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return message;
	   }
	 
	 /**
	  * 向服务器请求年龄的json解析
	  * */
	 public static RobotAgeBean praseServiceAgeJsonResult(String result){
		   RobotAgeBean age_info = null;
		   try {
			   JSONObject jsons = new JSONObject(result);
			   int status = jsons.getInt("ret");
			   switch (status) {
			case 0:
				age_info = new RobotAgeBean();
				age_info.setOldTime(jsons.getLong("old_age"));
				age_info.setNewTime(jsons.getLong("new_age"));
				age_info.setRid(MasterApplication.rid);
				age_info.setRet(status);
				break;
			case -1:
				age_info = new RobotAgeBean();
				age_info.setRet(status);
				break;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return age_info;

	   }
}