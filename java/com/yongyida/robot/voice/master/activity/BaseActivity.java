package com.yongyida.robot.voice.master.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;

/**
 * @version :1.0
 * @package :com.yongyida.robot.voice.master.activity
 * @project_name :yyd-packages-apps-YYDRobotVoiceMaster
 * @anthor :hushentao
 * @time :2017/6/26 0026 下午 8:02
 * @description :灭屏情况下强制亮屏
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenOn(this);

    }


    /**
     * 强制亮屏
     * @param context
     *  <uses-permission android:name="android.permission.WAKE_LOCK"/>
     */
    private void screenOn(Context context){
        try {
            PowerManager pm = (PowerManager)context.getSystemService(Service.POWER_SERVICE);
            if (!pm.isScreenOn()){
                PowerManager.WakeLock mWakelock =pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|
                        PowerManager.SCREEN_DIM_WAKE_LOCK,"target");
                mWakelock.acquire();
                mWakelock.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
