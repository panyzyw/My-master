package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 这是一个用于被继承的基类
 * @author Administrator
 *
 */
public class MyBroadcastReceiver extends BroadcastReceiver{

	private String mInfo; //描述这个广播的简单信息
	
	public MyBroadcastReceiver(String info){
		mInfo = info;
	}
	
	public String getInfo(){
		return mInfo;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}

}
