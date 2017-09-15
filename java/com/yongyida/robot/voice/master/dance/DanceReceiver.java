package com.yongyida.robot.voice.master.dance;

import com.yongyida.robot.voice.master.constant.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DanceReceiver extends BroadcastReceiver{
	private String tag = "doDance";
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(arg1 != null)
		{
			Log.d(tag, "----startService-----");
			Intent intent = new Intent();
			intent.setAction(Constant.ACTION_DODANCE);
			arg0.startService(intent);
		}
	}

}
