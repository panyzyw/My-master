package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yongyida.robot.voice.master.activity.DistanceActivity;
import com.yongyida.robot.voice.master.activity.LocationActivity;
import com.yongyida.robot.voice.master.activity.NetLocationActivity;
import com.yongyida.robot.voice.master.activity.RouteActivity;
import com.yongyida.robot.voice.master.activity.SpecialActivity;

/**
 * 地图相关模块的销毁
 * @author Administrator
 *
 */
public class DistanceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		if (arg1.getAction().equals("com.yydrobot.STOP")) {
			if (NetLocationActivity.isCreate) {
				NetLocationActivity.getInstance().stop();
			}
			if (LocationActivity.isCreate) {
				LocationActivity.getInstance().stop();
			}
			if (SpecialActivity.isCreate) {
				SpecialActivity.getInstance().stop();
			}
			if (RouteActivity.isCreate) {
				RouteActivity.getInstance().stop();
			}
			if (DistanceActivity.isCreate) {
				DistanceActivity.getInstance().stop();
			}
		}
	}
}
