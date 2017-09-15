package com.yongyida.robot.voice.master.runnable;

import java.util.Random;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.bean.HealthBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class HealthRunnable extends BaseRunnable{
	
	public HealthRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("HealthRunnable", "run()");
		HealthBean healthBean = BeanUtils.parseHealthJson(mResult, HealthBean.class);
		if(  healthBean == null ){
			MyLog.e("HealthRunnable", "jokeBean == null");
			
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
				
		requestHealthListAndPlay( );
	}		
	
	/**
	 * 从服务器获取养生节目的列表 并随机播放
	 */
	void requestHealthListAndPlay(){
		MyLog.i("HealthRunnable", "requestHealthListAndPlay");

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, Domain.getHealthResourceListURl() ,new RequestCallBack<String>() {
					@Override
					public void onStart() {}
					@Override
					public void onLoading(long total, long current,boolean isUploading) {}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {						
							MyLog.i("HealthRunnable", "requestJoke onSuccess , result = " + responseInfo.result);
							String[] healthNameArr = responseInfo.result.split("\n");													
							int nRandom = Math.abs(new Random().nextInt())% healthNameArr.length;							
							String url = Domain.getHealthResourceURl() + healthNameArr[nRandom];
							MyLog.i("HealthRunnable", "url = " + url);
							voiceMp(url);
						}				
					
					@Override
					public void onFailure(HttpException error, String msg) {
						MyLog.e("HealthRunnable", " failure : access server for healthlist , msg = " + msg);
						voiceTTS("服务器查询失败",null);
					}
				});
	
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("HealthRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
	}
	
	public void voiceMp(String uri) {
		MP mp = new MP(uri,BaseVoice.MP_INTENT_RESOURCE,mInterrupt_state,mPriority,mIsEndSendRecycle,new MyOnPreparedListener(),new MyOnCompletionListener());
		VoiceQueue.getInstance().add(mp);
	}
}
