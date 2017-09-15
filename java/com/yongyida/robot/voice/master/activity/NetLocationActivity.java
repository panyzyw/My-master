package com.yongyida.robot.voice.master.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class NetLocationActivity extends BaseActivity implements LocationSource{

	private static NetLocationActivity mInstance;
	public static boolean isCreate;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
	private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_net_location);
//			SpeechUtility.createUtility(NetLocationActivity.this,
//					SpeechConstant.APPID + "=56065ce8");
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            init();
            mInstance = this;
            
            isCreate = true;
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
        mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
        mapView.onPause();
        }
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
        mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deactivate();
        if (mapView != null) {
     //       aMap.removecache();
      //      aMap.clear();
            mapView.onDestroy();
        }
        isCreate = false;
    }

    private void init() {
        try {
            String success = getResources().getString(R.string.success);
            if (aMap == null) {
                // String success = "主人,找到你想要的位置了" ;
                voiceTTS(success, null);
                aMap = mapView.getMap();
                aMap.moveCamera(CameraUpdateFactory.zoomTo(30));
                setUpMap();
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    private void setUpMap() {
        try {
        	showProgressDialog();
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
 //           aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("MapRunnable", "voiceChatTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, BaseRunnable.INTERRUPT_STATE_STOP, BaseRunnable.PRIORITY_OTHER, false, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        try {
            mListener = onLocationChangedListener;
            if (mAMapLocationManager == null) {
                mAMapLocationManager = LocationManagerProxy.getInstance(NetLocationActivity.this);
                mAMapLocationManager.requestLocationData(
                        LocationProviderProxy.AMapNetwork, 2500, 1, aMapLocationListener);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deactivate() {
        try {
            mListener = null;
            if (mAMapLocationManager != null) {
            	mAMapLocationManager.removeUpdates(aMapLocationListener);
            	mAMapLocationManager.destroy();
    			aMapLocationListener = null;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    
/*    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
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
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/
    private  AMapLocationListener aMapLocationListener = new AMapLocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("aMapLocationListener", "NetLocationActivity:onStatusChanged:");
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "NetLocationActivity:onProviderEnabled:");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "NetLocationActivity:onProviderDisabled:");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d("aMapLocationListener", "NetLocationActivity:onLocationChanged:location");
		}
		
		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {
			Log.d("aMapLocationListener", "NetLocationActivity:onLocationChanged:aMapLocation");
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
    public static NetLocationActivity getInstance() {
        return mInstance;
    }

    public void stop() {
        finish();
    }
}