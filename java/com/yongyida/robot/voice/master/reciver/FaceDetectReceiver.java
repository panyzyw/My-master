package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.FaceDetect;

public class FaceDetectReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(FaceDetect.TAG_FACE, "收集人脸信息的广播");
        if (intent == null) {
            return;
        }
        MasterApplication.isFirstFaceDetec = true;
        String result = intent.getStringExtra("result");
        Log.d(FaceDetect.TAG_FACE, "result : " + result);
        if (!result.isEmpty()) {
            FaceDetect.getInstance().setWho(result);
        }
    }
}
