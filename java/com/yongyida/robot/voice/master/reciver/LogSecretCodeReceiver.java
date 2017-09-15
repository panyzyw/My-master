package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yongyida.robot.voice.master.activity.QueryLogActivity;

public class LogSecretCodeReceiver extends BroadcastReceiver{	
	Uri engineerUri = Uri.parse("android_secret_code://109000000");
	public void onReceive(Context context, Intent intent) {		
			if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
				Uri uri = intent.getData();
				if(uri.equals(engineerUri)){ //日志的暗码广播
					context.startActivity(new Intent(context,QueryLogActivity.class));
				}				
			}	
	}
}