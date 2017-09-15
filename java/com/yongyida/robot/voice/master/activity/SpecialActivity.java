package com.yongyida.robot.voice.master.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class SpecialActivity extends FragmentActivity implements LocationSource, PoiSearch.OnPoiSearchListener {

	private String keyWord;
    private static SpecialActivity mInstance;
    public static boolean isCreate;
    private AMap aMap;
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_special);
            Intent intent = getIntent();
            keyWord = intent.getStringExtra("locate");
            init();
            mInstance = this;
            
            isCreate = true;
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static SpecialActivity getInstance() {
        return mInstance;
    }

    public void stop() {
        finish();
    }

    private void init() {
        try {
            if (aMap == null) {
                aMap = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                setUpMap();
                doSearchQuery();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setUpMap() {
        try {
            aMap.setLocationSource(this);// 设置定位监听

            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            // aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        try {
            showProgressDialog();// 显示进度框
            currentPage = 0;
            query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);// 设置查第一页

            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        isCreate = false;
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        try {
            if (progDialog == null)
                progDialog = new ProgressDialog(this);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setIndeterminate(false);
            progDialog.setCancelable(false);
            String search = getResources().getString(R.string.search);
            progDialog.setMessage(search);
            progDialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        try {
            dissmissProgressDialog();// 隐藏对话框
            String success = getResources().getString(R.string.success);
            String fail = getResources().getString(R.string.fail);
            if (rCode == 0) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                        if (poiItems != null && poiItems.size() > 0) {
                            aMap.clear();// 清理之前的图标
                            PoiOverlay poiOverlay = new PoiOverlay(aMap,
                                    poiItems);
                            poiOverlay.removeFromMap();
                            poiOverlay.addToMap();
                            voiceTTS(success, null);
                            poiOverlay.zoomToSpan();
                        } else {
                            voiceTTS(fail, null);
                        }
                    }
                } else {
                    voiceTTS(fail, null);
                }
            } else {
                voiceTTS(fail, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void activate(OnLocationChangedListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

    }

    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, BaseRunnable.INTERRUPT_STATE_STOP, BaseRunnable.PRIORITY_OTHER, false, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }
}