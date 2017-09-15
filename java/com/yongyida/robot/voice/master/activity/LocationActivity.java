package com.yongyida.robot.voice.master.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

import java.util.List;

public class LocationActivity extends BaseActivity implements LocationSource, PoiSearch.OnPoiSearchListener
{

	private MapView mapView;
    private static LocationActivity mInstance;
    public static boolean isCreate;
    private AMap aMap;
    private int currentPage = 0;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private ProgressDialog progDialog = null;
    private PoiResult poiResult;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_location);
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);// 此方法必须重写
            init();
            Intent intent = getIntent();
            String locateResult = intent.getStringExtra("locate");
            SearchLocation(locateResult);
            mInstance = this;
            isCreate = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            if (aMap == null) {
                aMap = mapView.getMap();
                mapView.setVisibility(View.INVISIBLE);
                setUpMap();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        try {
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // 开始poi搜索
    private void SearchLocation(String locate) {
        try {
            showProgressDialog();// 显示进度框
            currentPage = 0;
            query = new PoiSearch.Query(locate, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
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

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        try {
            if (progDialog != null) {
                progDialog.dismiss();
            }
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
        	mapView.setVisibility(View.VISIBLE);
            String success = getResources().getString(R.string.success);
            String fail = getResources().getString(R.string.fail);
            if (rCode == 0) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                        if (poiItems != null && poiItems.size() > 0) {
                            aMap.clear();// 清理之前的图标
                            PoiOverlay poiOverlay = new PoiOverlay(aMap,
                                    poiItems);
                            poiOverlay.removeFromMap();
                            poiOverlay.addToMap();

                            voiceTTS(success, null);
                            // 设置定位的类型为根据地图面向方向旋转
                            // aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(1));
                            poiOverlay.zoomToSpan();
                            dissmissProgressDialog();
                        } else {
                            voiceTTS(fail, null);
                        //    finish();
                            initLocationMap();
                        }
                    }
                } else {
                    voiceTTS(fail, null);
                    initLocationMap();
                }
            } else {
                voiceTTS(fail, null);
                initLocationMap();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initLocationMap() {
		// TODO Auto-generated method stub
    	 mAMapLocationManager = LocationManagerProxy.getInstance(this);
         mAMapLocationManager.requestLocationData(
                 LocationProviderProxy.AMapNetwork, 2000, 1, aMapLocationListener);
	}

	@Override
    public void activate(OnLocationChangedListener listener) {
        try {
        	
        	/*Log.d("LocationActivity", "activate:OnLocationChangedListener");
            mListener = listener;
            if (mAMapLocationManager == null) {
            	Log.d("LocationActivity", "activate:LocationManagerProxy");
                mAMapLocationManager = LocationManagerProxy.getInstance(this);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用destroy()方法
                // 其中如果间隔时间为-1，则定位只定一次
                // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
                mAMapLocationManager.requestLocationData(
                        LocationProviderProxy.AMapNetwork, 2500, 1, aMapLocationListener);
            }*/
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
       
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        deactivateLocation();
        if (mapView != null) {
       // 	 aMap.removecache();
       //      aMap.clear();
            mapView.onDestroy();
        }
        isCreate = false;
    }

    public static LocationActivity getInstance() {
        return mInstance;
    }

    public void stop() {
        finish();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        try {
          /*  mListener = null;
            if (mAMapLocationManager != null) {
              //  mAMapLocationManager.removeUpdates(this);
                mAMapLocationManager.removeUpdates(aMapLocationListener);
                mAMapLocationManager.destroy();
    			aMapLocationListener = null;
            }
            mAMapLocationManager = null;*/
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void deactivateLocation() {
        try {
            mListener = null;
            if (mAMapLocationManager != null) {
              //  mAMapLocationManager.removeUpdates(this);
                mAMapLocationManager.removeUpdates(aMapLocationListener);
                mAMapLocationManager.destroy();
    			aMapLocationListener = null;
            }
            mAMapLocationManager = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

private  AMapLocationListener aMapLocationListener = new AMapLocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("aMapLocationListener", "LocationActivity:onStatusChanged:");
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "LocationActivity:onProviderEnabled:");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "LocationActivity:onProviderDisabled:");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "LocationActivity:onLocationChanged:location");
		}
		
		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {
			Log.d("aMapLocationListener", "LocationActivity:onLocationChanged:aMapLocation");
			MasterApplication.mAMapLocation = aMapLocation;
			
			dissmissProgressDialog();
			 try {
		            if (mListener != null && aMapLocation != null) {
		                if (aMapLocation != null
		                        && aMapLocation.getAMapException().getErrorCode() == 0) {
		                    mListener.onLocationChanged(aMapLocation);
		                } else {
		                    Log.e("AmapErr", "Location ERR:"
		                            + aMapLocation.getAMapException().getErrorCode());
		                }
		            }
		            deactivate();
		        } catch (Throwable e) {
		            e.printStackTrace();
		        }
			
		}
	};   
    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, BaseRunnable.INTERRUPT_STATE_STOP, BaseRunnable.PRIORITY_OTHER, false, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }
}