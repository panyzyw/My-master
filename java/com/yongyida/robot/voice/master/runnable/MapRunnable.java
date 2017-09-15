package com.yongyida.robot.voice.master.runnable;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.activity.DistanceActivity;
import com.yongyida.robot.voice.master.activity.LocationActivity;
import com.yongyida.robot.voice.master.activity.NetLocationActivity;
import com.yongyida.robot.voice.master.activity.RouteActivity;
import com.yongyida.robot.voice.master.activity.SpecialActivity;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.AddressBean;
import com.yongyida.robot.voice.master.bean.AnswerInfoBean;
import com.yongyida.robot.voice.master.bean.LocationBean;
import com.yongyida.robot.voice.master.bean.MapBean;
import com.yongyida.robot.voice.master.bean.TypeInfoBean;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.ParserUtil;
import com.yongyida.robot.voice.master.utils.Paser;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class MapRunnable extends BaseRunnable {

    private static final String TAG = "MapRunnableTAG";
    private AddressBean addressBean = null;
    private String text = null;
    private Double latStart;
    private Double latEnd;
    private LatLng latLngStart;
    private Double lngStart;
    private LatLng latLngEnd;
    private Double lngEnd;
    private float distance;
    private static int flag = 0;
    private static int num = 0;
    private String poiStart;
    private String poiEnd;
    private String cityStart;
    private String cityEnd;
    private AMapLocation aMapLocation;// 用于判断定位超时

    private int number;
    private Double geoLat;
    private Double geoLng;
    private HttpUtils http;
    static String locate;
    static boolean currentFlag = false;
    private Intent currentIntent;
    static boolean flagSpecial = false;
    private Intent specialIntent;
    static boolean locationFlag = false;
    static String addlocate;
    private Intent locationIntent;
    private boolean isStartSpecilarea = false;
    private boolean isEndSpecilarea = false;
    Intent intentTestDistance;
    private final static String TAIWAN = "台北，台北市，高雄，高雄市，新北市、台中市、台南市，新北、台中、台南";
    private MapBean mMapBean;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                if (num == 2) {
                    num = 0;
                    if (addressBean.getText().contains("距离") | addressBean.getText().contains("多远")) { //问距离
                        if (latStart != null && latEnd != null && !isStartSpecilarea && !isEndSpecilarea) {
                            if (DistanceActivity.getInstance() != null) {
                                DistanceActivity.getInstance().finish();
                            }
                            latLngStart = new LatLng(latStart, lngStart);
                            latLngEnd = new LatLng(latEnd, lngEnd);
                            distance = AMapUtils.calculateLineDistance(latLngStart, latLngEnd);
                            distance = (float) (Math.round(distance / 100d) / 10d);

                            flag = 1;
                            Intent it = new Intent(MasterApplication.mAppContext, DistanceActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("latStart", latStart);
                            bundle.putDouble("lngStart", lngStart);
                            bundle.putDouble("latEnd", latEnd);
                            bundle.putDouble("lngEnd", lngEnd);
                            bundle.putString("poiStart", poiStart);
                            bundle.putString("poiEnd", poiEnd);
                            bundle.putFloat("distance", distance);
                            it.putExtras(bundle);
                            MasterApplication.mAppContext.startActivity(it);

                            if (poiStart == cityStart && poiEnd == cityEnd) {
                                text = "如果我没记错的话，" + cityStart + "到" + cityEnd + "的距离大概是" + distance + "公里";
                            } else if (poiStart == cityStart && poiEnd != cityEnd) {
                                if (poiEnd.equals("CURRENT_POI")) {
                                    text = "如果我没记错的话，" + cityStart + "到" + "这里" + "距离" + distance + "公里";
                                } else {
                                    text = "如果我没记错的话，" + cityStart + poiStart + "到" + cityEnd + "距离" + distance + "公里";
                                }
                                text = "如果我没记错的话，" + cityStart + "到" + cityEnd + poiEnd + "距离" + distance + "公里";
                            } else if (poiStart != cityStart && poiEnd == cityEnd) {
                                if (poiStart.equals("CURRENT_POI")) {
                                    text = "如果我没记错的话，" + "这里" + "到" + cityEnd + "距离" + distance + "公里";
                                } else {
                                    text = "如果我没记错的话，" + cityStart + poiStart + "到" + cityEnd + "距离" + distance + "公里";
                                }
                            } else {
                                if (poiStart.equals("CURRENT_POI")) {
                                    text = "如果我没记错的话，" + "这里" + "到" + cityEnd + poiEnd + "距离" + distance + "公里";
                                } else if (poiEnd.equals("CURRENT_POI")) {
                                    text = "如果我没记错的话，" + cityStart + poiStart + "到" + "这里" + "距离" + distance + "公里";
                                } else {
                                    text = "如果我没记错的话，" + cityStart + poiStart + "到" + cityEnd + poiEnd + "距离" + distance + "公里";
                                }
                            }

                        } else {
                            text = "对不起，没有搜索到相关数据！";
                        }
                        stopLocation();
                        voiceTTS(text, null);

                    } else {        //问怎么走
                        /**
                         String mtsText = getResources().getString(R.string.dyc_map_route_start);
                         int code = mTts.startSpeaking(mtsText, mTtsListener);
                         Log.i(TAG, "mtsText:" + mtsText +"code:" + code);
                         */
                        if (latStart != null && latEnd != null && !isStartSpecilarea && !isEndSpecilarea) {
                            flag = 2;
                            Intent it = new Intent(MasterApplication.mAppContext, RouteActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("latStart", latStart);
                            bundle.putDouble("lngStart", lngStart);
                            bundle.putDouble("latEnd", latEnd);
                            bundle.putDouble("lngEnd", lngEnd);
                            it.putExtras(bundle);
                            MasterApplication.mAppContext.startActivity(it);
                            String mtsText = "我给你提供一条速度最快的路线。";
                            voiceTTS(mtsText, null);
                        } else {
                            text = "对不起，没有搜索到相关数据！";
                            voiceTTS(text, null);
                            stopLocation();
                        }
                    }
                } else if (msg.what == 0) {
                    if (aMapLocation == null) {
                        voiceTTS("对不起，没有搜索到相关数据！", null);
                    }
                    stopLocation();
                }
            }
        }
    };
    private boolean isStartQuery;
    public static LocationManagerProxy aMapLocManager;

    private void stopLocation() {
        if (aMapLocManager != null) {
            aMapLocManager.removeUpdates(aMapLocationListener);
            //aMapLocationListener = null;
            //aMapLocManager = null;
        }
        // aMapLocManager = null;
    }

    public MapRunnable(String result) {
        mIsEndSendRecycle = false;
        mPriority = PRIORITY_OTHER;
        mInterrupt_state = INTERRUPT_STATE_STOP;

        mResult = result;
    }
    private boolean isQueryOver;
    @Override
    public void run() {
        JSONObject joResult = null;
        try {
            joResult = new JSONObject(mResult);
            mMapBean = BeanUtils.parseMapJson(mResult, MapBean.class);
            if ((joResult.getString("operation")).equals("ROUTE")) {
                if (RouteActivity.getInstance() != null) {
                    RouteActivity.getInstance().finish();
                }
                if (DistanceActivity.getInstance() != null) {
                    DistanceActivity.getInstance().finish();
                }
                isStartQuery = true;
                isQueryOver = true;
                addressBean = ParserUtil.parseResult(mResult, AddressBean.class);
                http = new HttpUtils();
                number = 0;
                //   aMapLocManager   = MasterApplication.aMapLocManager;
                if (aMapLocManager == null) {
                    aMapLocManager = LocationManagerProxy.getInstance(MasterApplication.mAppContext);
                }
                aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2500, 1, aMapLocationListener);
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                mHandler.sendMessageDelayed(msg, 12000);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isQueryOver) {
                            Log.d("aMapLocationListener", "超时查询:重新初始化LocationManagerProxy");
                            isQueryOver = false;
                            isStartQuery = false;
                            if (aMapLocManager != null) {
                                if (aMapLocationListener != null) {
                                    aMapLocManager.removeUpdates(aMapLocationListener);
                                }
                                aMapLocManager.destroy();
                            }
                            aMapLocManager = LocationManagerProxy.getInstance(MasterApplication.mAppContext);
                            if (MasterApplication.mAMapLocation != null) {
                                geoLat = MasterApplication.mAMapLocation.getLatitude();
                                geoLng = MasterApplication.mAMapLocation.getLongitude();
                                Log.i(TAG, "geoLat:" + geoLat + geoLng);
                                initData();
                            }
                        }
                    }
                }, 3500);
                // mHandler.postDelayed(thread, 12000);
                flag = 0;
            } else if ((joResult.getString("operation")).equals("POSITION")) {
                if (LocationActivity.getInstance() != null) {
                    LocationActivity.getInstance().finish();
                }
                TypeInfoBean type = Paser.parseTypeResult(mResult);
                if (type.getmOperation().equals("POSITION")
                        && type.getmRc() == 0) {
                    AnswerInfoBean answer = Paser
                            .parseAnswerResult(mResult);
                    if (answer.getmArea() != null) {
                        if (answer.getmCity() != null) {
                            if (answer.getProvince() != null) {
                                locate = answer.getProvince()
                                        + answer.getmCity()
                                        + answer.getmArea();
                            } else {
                                locate = answer.getmCity()
                                        + answer.getmArea();
                            }
                        } else {
                            if (answer.getProvince() != null) {
                                locate = answer.getProvince()
                                        + answer.getmArea();
                            } else {
                                locate = answer.getmArea();
                            }
                        }
                    } else {
                        if (answer.getProvince() != null) {
                            if (answer.getmCity() != null) {
                                locate = answer.getProvince()
                                        + answer.getmCity();
                            } else {
                                locate = answer.getProvinceAddr();
                            }
                        } else {
                            if (answer.getmCity() != null) {
                                locate = answer.getmCity();
                            } else {
                                locate = "";
                            }
                        }
                    }
                    // 当前位置的定位
                    if (answer.getmPoi() != null) {
                        if (answer.getmPoi().equals("CURRENT_POI")) {
                            if (NetLocationActivity.getInstance() != null) {
                                NetLocationActivity.getInstance().finish();
                            }
                            currentFlag = true;
                            currentIntent = new Intent(MasterApplication.mAppContext,
                                    NetLocationActivity.class);
                            // 开启activity
                            currentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MasterApplication.mAppContext.startActivity(currentIntent);
                        } else if (answer.getmPoi().equals("钓鱼岛") || answer.getmPoi().equals("黄岩岛")) {
                            flagSpecial = true;
                            String locates = answer.getmPoi();
                            specialIntent = new Intent(MasterApplication.mAppContext, SpecialActivity.class);
                            // 开启activity
                            specialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            specialIntent.putExtra("locate",locates);
                            MasterApplication.mAppContext.startActivity(specialIntent);
                        } else {
                            if (answer.getmCity().equals("CURRENT_CITY")) {
                                locationFlag = true;
                                addlocate = answer.getmPoi();
                                locationIntent = new Intent(MasterApplication.mAppContext, LocationActivity.class);
                                // 开启activity
                                locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                locationIntent.putExtra("locate", addlocate);
                                MasterApplication.mAppContext.startActivity(locationIntent);
                            } else {
                                locationFlag = true;
                                addlocate = locate + answer.getmPoi();
                                locationIntent = new Intent(MasterApplication.mAppContext, LocationActivity.class);
                                // 开启activity
                                locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                locationIntent.putExtra("locate", addlocate);
                                MasterApplication.mAppContext.startActivity(locationIntent);
                            }
                        }
                    } else {
                        locationFlag = true;
                        locationIntent = new Intent(MasterApplication.mAppContext,
                                LocationActivity.class);
                        // 开启activity
                        locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        locationIntent.putExtra("locate", locate);
                        MasterApplication.mAppContext.startActivity(locationIntent);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority, mIsEndSendRecycle, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mMapBean,text);
    }

    private void initData() {
        num = 0;//重新初始化
        String typeStart = addressBean.getSemantic().getSlots().getStartLoc().getType();//"type": "LOC_POI"北京怎么走  "type": "LOC_BASIC"深圳到北京怎么走
        String provinceStart = addressBean.getSemantic().getSlots().getStartLoc().getProvince();
        cityStart = addressBean.getSemantic().getSlots().getStartLoc().getCity(); //"city": "CURRENT_CITY" 北京怎么走
        String areaStart = addressBean.getSemantic().getSlots().getStartLoc().getArea();
        poiStart = addressBean.getSemantic().getSlots().getStartLoc().getPoi(); //"poi": "CURRENT_POI" 北京怎么走
        if (cityStart != null) {
            if ((!cityStart.equals("CURRENT_CITY")) && (!cityStart.contains("市")) && (!cityStart.contains("香港")) && (!cityStart.contains("澳门"))) {
                cityStart = null;
            }
        }
        isStartSpecilarea = isSpecilArea(provinceStart, cityStart); //台湾
        if (cityStart == null && areaStart == null && provinceStart != null) { //省
            cityStart = provinceStart;
        } else if (areaStart != null) { //区、县
            cityStart = areaStart;
        }
        if (poiStart == null && cityStart == null) {
            latStart = null;
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            num++;
            mHandler.sendMessage(msg);
        } else if (poiStart == null && cityStart != null) {
            poiStart = cityStart;
            Log.i("service", "poiStart:" + poiStart);
            //String httpArg1 = "callback=‘’" + "&address=" + poiStart + "&city=" + cityStart;
            String httpArg1 =  poiStart;
            //String json1 = HttpUtil.request(DataUtil.httpUrl, httpArg1);http://restapi.amap.com/v3/geocode/geo?key=4ec8e22c0ffa8b7ac23890ab09901bc8&address=北京
            http.send(HttpRequest.HttpMethod.GET, getGaodeMapUrl(httpArg1), new RequestCallBack<String>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    //testTextView.setText(current + "/" + total);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    //textView.setText(responseInfo.result);
                    String json1 = responseInfo.result.toString();
                    Log.i("service", "json:" + json1);
                    LocationBean location = ParserUtil.parserLocation(json1);
                    if (location == null) {
                        latStart = null;
                    } else {
                        latStart = location.getLat();
                        lngStart = location.getLng();
                        Log.i("service", "latStart:" + location.getLat() + "lngStart:" + location.getLng());
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    num++;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                    voiceTTS("请求失败", null);

                    latStart = null;
                    Message msg2 = mHandler.obtainMessage();
                    msg2.what = 1;
                    num++;
                    mHandler.sendMessage(msg2);
                }
            });
        } else if (poiStart.equals("CURRENT_POI")) {
            if (geoLat != null && geoLng != null) {
                latStart = geoLat;
                lngStart = geoLng;
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                num++;
                mHandler.sendMessage(msg);
            }
        } else {
            if (cityStart.equals("CURRENT_CITY")) {
                cityStart = poiStart;
            }
            Log.i("service", "poiStart:" + poiStart);
            //String httpArg1 = "callback=‘’" + "&address=" + poiStart + "&city=" + cityStart;
            String httpArg1 = null;
            if (cityStart.equals(poiStart)) {
                httpArg1 = poiStart;
                //String json1 = HttpUtil.request(DataUtil.httpUrl, httpArg1);
            } else {
                //httpArg1 = "key=4d92bd768cf44d087560c13e706482a4" + "&address=" + cityStart + poiStart + "&city=" + cityStart;
                // httpArg1 = "key=" + "b474540f45b95d5911c76f399fa5af14" + "&address=" + cityStart + poiStart;
                httpArg1 = cityStart + poiStart;
            }

            http.send(HttpRequest.HttpMethod.GET, getGaodeMapUrl(httpArg1), new RequestCallBack<String>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    //testTextView.setText(current + "/" + total);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //textView.setText(responseInfo.result);
                    String json1 = responseInfo.result.toString();
                    Log.i(TAG, "json:" + json1);
                    LocationBean location = ParserUtil.parserLocation(json1);
                    if (location == null) {
                        latStart = null;
                    } else {
                        latStart = location.getLat();
                        lngStart = location.getLng();
                        Log.i(TAG, "latStart:" + location.getLat() + "lngStart:" + location.getLng());
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    num++;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    latStart = null;
                    Message msg2 = mHandler.obtainMessage();
                    msg2.what = 1;
                    num++;
                    mHandler.sendMessage(msg2);
                }
            });

        }
        poiEnd = addressBean.getSemantic().getSlots().getEndLoc().getPoi();
        cityEnd = addressBean.getSemantic().getSlots().getEndLoc().getCity();
        String typeEnd = addressBean.getSemantic().getSlots().getEndLoc().getType();
        String areaEnd = addressBean.getSemantic().getSlots().getEndLoc().getArea();
        String provinceEnd = addressBean.getSemantic().getSlots().getEndLoc().getProvince();
        isEndSpecilarea = isSpecilArea(provinceEnd, cityEnd);
        if (cityEnd != null) {
            if ((!cityEnd.equals("CURRENT_CITY")) && (!cityEnd.contains("市")) && (!cityEnd.contains("香港")) && (!cityEnd.contains("澳门"))) {
                cityEnd = null;
            }
        }
        //if(typeEnd.equals("LOC_BASIC")){
        if (cityEnd == null && areaEnd == null && provinceEnd != null) {
            cityEnd = provinceEnd;
        } else if (areaEnd != null) {
            cityEnd = areaEnd;
        }
        //}
        if (poiEnd == null && cityEnd == null) {
            latEnd = null;
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            num++;
            mHandler.sendMessage(msg);
        } else if (poiEnd == null) {
            poiEnd = cityEnd;
            Log.i(TAG, "poiEndnull:" + poiEnd);
            //String httpArg2 = "?callback=‘’" + "&address=" + poiEnd + "&city=" + cityEnd;
            String httpArg2 = poiEnd;
            http.send(HttpRequest.HttpMethod.GET, getGaodeMapUrl(httpArg2), new RequestCallBack<String>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    //testTextView.setText(current + "/" + total);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //textView.setText(responseInfo.result);
                    String json = responseInfo.result.toString();
                    Log.i(TAG, "json" + json);
                    LocationBean locationEnd = ParserUtil.parserLocation(json);
                    if (locationEnd == null) {
                        latEnd = null;
                    } else {
                        latEnd = locationEnd.getLat();
                        lngEnd = locationEnd.getLng();
                        Log.i(TAG, "latEnd:" + latEnd + "lngEnd:" + lngEnd);
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    num++;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onStart() {
                    Log.i(TAG, "onStart:");
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    latEnd = null;
                    Message msg2 = mHandler.obtainMessage();
                    msg2.what = 1;
                    num++;
                    mHandler.sendMessage(msg2);
                }
            });
        } else if (poiEnd.equals("CURRENT_POI")) {
            if (geoLat != null && geoLng != null) {
                latEnd = geoLat;
                lngEnd = geoLng;
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                num++;
                mHandler.sendMessage(msg);
            }
        } else {
            if (cityEnd.equals("CURRENT_CITY")) {
                cityEnd = poiEnd;
            }
            Log.i(TAG, "poiEnd:" + poiEnd);
            //String httpArg2 = "?callback=‘’" + "&address=" + poiEnd + "&city=" + cityEnd;
            String httpArg2 = null;
            if (cityEnd.equals(poiEnd)) {
                httpArg2 = poiEnd;
            } else {
                httpArg2 = cityEnd + poiEnd;
            }//String json = HttpUtil.request(DataUtil.httpUrl, httpArg2);
            http.send(HttpRequest.HttpMethod.GET, getGaodeMapUrl(httpArg2), new RequestCallBack<String>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    //testTextView.setText(current + "/" + total);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //textView.setText(responseInfo.result);
                    String json = responseInfo.result.toString();
                    Log.i(TAG, "json" + json);
                    LocationBean locationEnd = ParserUtil.parserLocation(json);
                    if (locationEnd == null) {
                        latEnd = null;
                    } else {
                        latEnd = locationEnd.getLat();
                        lngEnd = locationEnd.getLng();
                        Log.i(TAG, "latEnd:" + latEnd + "lngEnd:" + lngEnd);
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    num++;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onStart() {
                    Log.i(TAG, "onStart:");
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    latEnd = null;
                    Message msg2 = mHandler.obtainMessage();
                    msg2.what = 1;
                    num++;
                    mHandler.sendMessage(msg2);
                }
            });
        }
    }
    private String getGaodeMapUrl(String address){
        String key ="http://restapi.amap.com/v3/geocode/geo?key=4ec8e22c0ffa8b7ac23890ab09901bc8&address=";
        StringBuilder stringBuilder = new StringBuilder(key);
        if (TAIWAN.contains(address)){
            stringBuilder.append("台湾省");
        }
        stringBuilder.append(address);
        return stringBuilder.toString();
    }
    private boolean isSpecilArea(String province, String city) {
        if (province != null && province.contains("台湾")) {
            return true;
        }
        return false;
    }

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("aMapLocationListener", "MapRunnable:onStatusChanged:");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("aMapLocationListener", "MapRunnable:onProviderEnabled:");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("aMapLocationListener", "MapRunnable:onProviderDisabled:");
        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            Log.d("aMapLocationListener", "MapRunnable:onLocationChanged:location");
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocations) {
            Log.d("aMapLocationListener", "MapRunnable:onLocationChanged:aMapLocations");
            MasterApplication.mAMapLocation = aMapLocations;
            aMapLocation = aMapLocations;
            isQueryOver = false;
            if (isStartQuery) {
                isStartQuery = false;
                number++;
                if (aMapLocations != null) {
                    aMapLocation = aMapLocations;// 判断超时机制
                    if (number == 1) {
                        geoLat = aMapLocation.getLatitude();
                        geoLng = aMapLocation.getLongitude();
                        Log.i(TAG, "geoLat:" + geoLat + geoLng);
                        initData();
                        stopLocation();
                    }
                    Log.i(TAG, "arg0:" + number + ":" + aMapLocation.getLatitude() + aMapLocation.getLongitude());
                } else {
                    Toast.makeText(MasterApplication.mAppContext, "获取本地GPS失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}