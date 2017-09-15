package com.yongyida.robot.voice.master.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class RouteActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener, AMap.InfoWindowAdapter {

	private static final String TAG = "RouteActivityTAG";
    private static RouteActivity mInstance;
    public static boolean isCreate;
    private MapView mapView;
    private AMap aMap;
    private RouteSearch routeSearch;
    private Double latStart;
    private Double lngStart;
    private Double latEnd;
    private Double lngEnd;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private int routeType = 1;
    private int busMode = RouteSearch.BusDefault;
    private int drivingMode = RouteSearch.DrivingDefault;
    private int walkMode = RouteSearch.WalkDefault;
    private ProgressDialog progDialog = null;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        mInstance = this;
        isCreate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        isCreate = false;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.clear();
            mapView.setVisibility(View.INVISIBLE);
            registerListener();
        }
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        latStart = getIntent().getExtras().getDouble("latStart");
        lngStart = getIntent().getExtras().getDouble("lngStart");
        latEnd = getIntent().getExtras().getDouble("latEnd");
        lngEnd = getIntent().getExtras().getDouble("lngEnd");
        Log.i(TAG, "latStart:" + latStart + ",lngStart:" + lngStart + "latEnd" + latEnd + "lngEnd" + lngEnd);
        // startPoint = new LatLonPoint(39.989614, 116.481763);
        // endPoint = new LatLonPoint(39.983456, 116.3154950);

        startPoint = new LatLonPoint(latStart, lngStart);
        endPoint = new LatLonPoint(latEnd, lngEnd);

        drivingRoute();
        searchRouteResult(startPoint, endPoint);
    }

    public static RouteActivity getInstance() {
        return mInstance;
    }

    private void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        if (routeType == 1) {
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, busMode, "深圳", 0);
            routeSearch.calculateBusRouteAsyn(query);
        } else if (routeType == 2) {
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);
        } else if (routeType == 3) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, walkMode);
            routeSearch.calculateWalkRouteAsyn(query);
        }
    }

    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage(getResources().getString(R.string.dyc_map_search));
        progDialog.show();
    }

    private void drivingRoute() {
        routeType = 2;
    }

    private void registerListener() {
        aMap.setInfoWindowAdapter(RouteActivity.this);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
        mapView.setVisibility(View.VISIBLE);
        if (rCode == 0) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                driveRouteResult = driveRouteResult;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this, aMap, drivePath,
                        driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                Toast.makeText(RouteActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else if (rCode == 27) {
            Toast.makeText(RouteActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
        } else if (rCode == 32) {
            Toast.makeText(RouteActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RouteActivity.this, "未知错误，请稍后重试!错误码为:"+rCode, Toast.LENGTH_SHORT).show();
        }
        handler.postDelayed(runnable, 1500);
    }
    Runnable runnable = new Runnable() {
  		public void run() {
  			dissmissProgressDialog();
  		}
  	};
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
    
    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, BaseRunnable.INTERRUPT_STATE_STOP, BaseRunnable.PRIORITY_OTHER, false, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }
    
    public void stop() {
        finish();
    }
}