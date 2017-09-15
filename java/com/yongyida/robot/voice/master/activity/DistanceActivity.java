package com.yongyida.robot.voice.master.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class DistanceActivity extends BaseActivity {

	private static final String TAG = "DistanceActivityTAG";
    private static DistanceActivity mInstance;
    public static boolean isCreate;
    private Double latStart;
    private Double lngStart;
    private Double latEnd;
    private Double lngEnd;
    private String poiStart;
    private String poiEnd;
    private float distance;
    private LatLng latLngStart;
    private LatLng latLngEnd;
    private MapView mapView;
    private AMap aMap;
    private Toast mToast;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        latStart = getIntent().getExtras().getDouble("latStart");
        lngStart = getIntent().getExtras().getDouble("lngStart");
        latEnd = getIntent().getExtras().getDouble("latEnd");
        lngEnd = getIntent().getExtras().getDouble("lngEnd");
        poiStart = getIntent().getExtras().getString("poiStart");
        poiEnd = getIntent().getExtras().getString("poiEnd");
        distance = getIntent().getExtras().getFloat("distance");

        Log.i(TAG, "latStart:" + latStart + ",lngStart:" + lngStart + "latEnd" + latEnd + "lngEnd" + lngEnd);
        // startPoint = new LatLonPoint(39.989614, 116.481763);
        // endPoint = new LatLonPoint(39.983456, 116.3154950);

        latLngStart = new LatLng(latStart, lngStart);
        latLngEnd = new LatLng(latEnd, lngEnd);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            // aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
        }

        mToast = Toast.makeText(DistanceActivity.this, "", Toast.LENGTH_SHORT);

        mInstance = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        // http = new HttpUtils();
        // initData();
        init();
        
        isCreate = true;
        
    }

    public static DistanceActivity getInstance() {
        return mInstance;
    }

    private void init() {
        progressDialog.dismiss();
        setUpMap();
    }

    private void setUpMap() {
        LatLngBounds bounds = new LatLngBounds.Builder().include(latLngStart).include(latLngEnd).build();

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        addMarkersToMap(latLngStart, poiStart, latLngEnd, poiEnd);// ����ͼ�����marker

        aMap.addPolyline((new PolylineOptions()).add(latLngStart, latLngEnd).color(Color.RED));
    }

    private void addMarkersToMap(LatLng latLngStart, String poiStart, LatLng latLngEnd, String poiEnd) {
        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLngStart).title(poiStart).draggable(true));

        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLngEnd).title(poiEnd).draggable(true));

        drawMarkers();
    }

    private void drawMarkers() {
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLngEnd).title(String.valueOf(distance))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));
        marker.showInfoWindow();
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

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        
        isCreate = false;
    }

    public void stop() {

        // onDestroy();
        finish();
    }
    
    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, BaseRunnable.INTERRUPT_STATE_STOP, BaseRunnable.PRIORITY_OTHER, false, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }
}
