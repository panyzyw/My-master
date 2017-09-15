package com.yongyida.robot.voice.master.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class RunVoiceService extends Service{

	public IBinder onBind(Intent intent) {return null;}

	public void onCreate() {
		super.onCreate();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if( intent == null ){
			myStopSelfServer();
			return super.onStartCommand(intent, flags, startId); 
		}
		
		VoiceQueue.getInstance().popAndRun();
		
		myStopSelfServer();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void myStopSelfServer(){
		Intent intent = new Intent(MasterApplication.mAppContext,RunVoiceService.class);
		MasterApplication.mAppContext.stopService(intent);
	}
	
}
