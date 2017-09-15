package com.yongyida.robot.voice.master.runnable;

import java.util.HashMap;
import java.util.Random;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.SinologyBean;
import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;


public class SinologyRunnable extends BaseRunnable{

	private SinologyBean mSinologyBean;
	public SinologyRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("SinologyRunnable", "run()");
		SinologyBean sinologyBean = BeanUtils.parseSinologyJson(mResult, SinologyBean.class);
        mSinologyBean = sinologyBean;
		if(  sinologyBean == null ){
			MyLog.e("SinologyRunnable", "jokeBean == null");
			mIsEndSendRecycle = true;
			voiceTTS(MyData.NOT_SINOLOGY_ANSWER[new Random().nextInt(MyData.NOT_SINOLOGY_ANSWER.length)],new MySynthesizerListener());
			return ;
		}
		
		doSinology(sinologyBean);
	}
	
	private void doSinology(SinologyBean sb) {
		HashMap<String , String> params = new HashMap<String, String>();
		params.put("author", "");
		params.put("dynasty", "");
		params.put("title", "");
//		params.put("motion", "");
				
		try{
			if(sb.semantic.slots.author != null){
				params.put("author", sb.semantic.slots.author);
			}
			if(sb.semantic.slots.dynasty != null){
				params.put("dynasty", sb.semantic.slots.dynasty);
			}
			if(sb.semantic.slots.title != null){
				params.put("title", sb.semantic.slots.title);
			}		
//			if(sb.semantic.slots.author != null){
//				params.put("motion", sb.semantic.slots.motion);
//			}
		}catch(Exception e){
			MyLog.e("SinologyRunnable", MyExceptionUtils.getStringThrowable(e));
		}		
		MyLog.i("SinologyRunnable", "params : " + params.toString());
		
		requestServerSinology(params);
	}

	private void requestServerSinology(HashMap<String , String> params) {
		MyLog.i("SinologyRunnable", "requestServerSinology");
		RequestParams requestparams = new RequestParams();
		try {	
			requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));			
		} catch (Exception e) {
			MyLog.e("SinologyRunnable",MyExceptionUtils.getStringThrowable(e));
		}
		requestparams.setContentType("text/plain");
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Domain.getSinology() , requestparams,new RequestCallBack<String>() {
					@Override
					public void onStart() {}
					@Override
					public void onLoading(long total, long current,boolean isUploading) {}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {						
						MyLog.i("SinologyRunnable", "requestJoke onSuccess , result = " + responseInfo.result);
						String s = getSinology(responseInfo.result);								
						if (TextUtils.isEmpty(s)) {
                            voiceTTS(MyData.NOT_SINOLOGY_ANSWER[new Random().nextInt(MyData.NOT_SINOLOGY_ANSWER.length)],new MySynthesizerListener());
						} else {
					      voiceTTS(s, new MySynthesizerListener() {
                          @Override
                           public void onCompleted(SpeechError error) {
                            super.onCompleted(error);
                            Utils.sendBroadcast(MyIntent.INTENT_RECYCLE, "master", "");
                        }
                    });					
					}					
		}
					@Override
					public void onFailure(HttpException error, String msg) {
						MyLog.e("SinologyRunnable", " failure : access server for Sinology");
						voiceTTS("网络异常,访问服务器失败",null);
					}
				});
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("SinologyRunnable", "voiceChatTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mSinologyBean,text);
	}

	private String getSinology(String responseinfo) {
		String sinologyText = "";
		try {
			JSONObject jsonobject = new JSONObject(responseinfo);
			JSONArray jsonarray_Poetrys = new JSONArray(jsonobject.optString("Poetrys"));
			if (jsonarray_Poetrys != null && jsonarray_Poetrys.length() > 0) {
				JSONObject jsonobjcet_random = jsonarray_Poetrys.getJSONObject(new Random().nextInt(jsonarray_Poetrys.length()));
				if (!TextUtils.isEmpty(jsonobjcet_random.getString("text"))) {
					Log.d("SinologyRunnable", "text:" + jsonobjcet_random.getString("text"));
					String dynasty = jsonobjcet_random.getString("dynasty");
					String author = jsonobjcet_random.getString("author");
					String title = jsonobjcet_random.getString("title");
					String text = jsonobjcet_random.getString("text");
					sinologyText =  dynasty + "," + author + "," + title + "," + text;
				}
			} else {
				sinologyText = MasterApplication.mAppContext.getString(R.string.tq001_meiyouzheshoushi);
			}
		} catch (Throwable e) {
			MyLog.e("SinologyRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		
		return sinologyText;
	}
	
}
