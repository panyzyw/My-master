package com.yongyida.robot.voice.master.runnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.PoetryLearnBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.recognizer.MySpeechRecognizer;
import com.yongyida.robot.voice.master.recognizer.MySpeechRecognizer.MyRecognizerListener;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class PoetryLearnRunnable extends BaseRunnable{
	private static Map<String, String> mHashmapParam;
	RecognitionBr mRbr;
	RecycleBr         mRb;
	
	static String mAnswer;
	String listen;
	String de;
	String zai;
	String limiande;
	String nextSentence;
	String answerCorrectly;
	String answerWrong;
	private PoetryLearnBean mPoetryLearnBean;
	public PoetryLearnRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("PoetryLearnRunnable", "run()");
		PoetryLearnBean poetryLearnBean = BeanUtils.parsePoetryLearnJson(mResult, PoetryLearnBean.class);
        mPoetryLearnBean = poetryLearnBean;
		if(  poetryLearnBean == null ){
			MyLog.e("PoetryLearnRunnable", "jokeBean == null");
			
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
				
		initData();
		registReceiver();
		
		doPoetryLearn(poetryLearnBean);
	}
	
	private void initData() {
		listen = MasterApplication.mAppContext.getString(R.string.learnsinology_qingtingti);
		de = MasterApplication.mAppContext.getString(R.string.learnsinology_de);
		nextSentence = MasterApplication.mAppContext.getString(R.string.learnsinology_dexiayijushishenme);
		answerCorrectly = MasterApplication.mAppContext.getString(R.string.learnsinology_huidazhengque);
		limiande = MasterApplication.mAppContext.getString(R.string.learnsinology_limiande);
		zai = MasterApplication.mAppContext.getString(R.string.learnsinology_zai);
		answerWrong = MasterApplication.mAppContext.getString(R.string.learnsinology_huidacuowu);
	}

	void doPoetryLearn(PoetryLearnBean poetryLearnBean){
		//没参数
		mHashmapParam = new HashMap<String, String>();
//		mHashmapParam.put("title", "");
//		mHashmapParam.put("dynasty", "");
//		mHashmapParam.put("author", "");
		
		requestServerPoetry();
	}
	
	void requestServerPoetry(){

		try {
			RequestParams rp = new RequestParams();
			rp.setBodyEntity(new StringEntity(new JSONObject(mHashmapParam).toString(), "UTF-8"));
			rp.setContentType("text/plain");

			HttpUtils myHttpUtils = new HttpUtils();
			myHttpUtils.send(HttpMethod.POST, com.yongyida.robot.voice.master.utils.Domain.getPoetryLearn(), rp, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					try {
						MyLog.i("PoetryLearnRunnable", responseInfo.result);
						JSONObject jsonobject = new JSONObject(responseInfo.result);
						JSONArray jsonarray = new JSONArray(jsonobject.optString("Poetrys"));
						if (jsonarray != null && jsonarray.length() > 0) {
							JSONObject jsonObject2 = jsonarray.getJSONObject(new Random().nextInt(jsonarray.length()));

							String dynasty = jsonObject2.getString("dynasty");
							String author = jsonObject2.getString("author");
							String title = jsonObject2.getString("title");
							String ensemble = jsonObject2.getString("text");

							int index001 = ensemble.indexOf(".");
							if (index001 == -1) {
								index001 = ensemble.indexOf(MasterApplication.mAppContext.getString(R.string.learnsinology_zhongwen_juhao));
							}

							String pre = ensemble.substring(0, index001);
							int index002 = ensemble.indexOf(",", index001);
							if (index002 == -1) {
								index002 = ensemble.indexOf(MasterApplication.mAppContext.getString(R.string.learnsinology_zhongwen_douhao), index001);
							}

							mAnswer = ensemble.substring(index001 + 1, index002);
							MyLog.i("LearnSinology_startCommunication", "author:" + author + ",title:" + title + ",prev:" + pre + ",answer:"
									+ mAnswer);

							
							voiceTTS(listen + dynasty + de + author + zai + title + limiande + pre + nextSentence, new MySynthesizerListener(){
								public void onCompleted(com.iflytek.cloud.SpeechError error) {
									Utils.sendBroadcast(MyIntent.POETRYLEARN_RECOGNITION);
								};
							});

						} else {
							MyLog.e("PoetryLearnRunnable", "jsonarray == null || jsonarray.length() <= 0");
						}
					} catch (Throwable e) {
						MyLog.e("PoetryLearnRunnable", MyExceptionUtils.getStringThrowable(e));						
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					MyLog.e("PoetryLearnRunnable","HttpException : " + MyExceptionUtils.getStringThrowable(error));					
				}
			});
		} catch (Throwable e) {
			MyLog.e("PoetryLearnRunnable",MyExceptionUtils.getStringThrowable(e));		
		}
	
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("PoetryLearnRunnable", "voiceChatTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mPoetryLearnBean,text);
	}
	
	private void registReceiver() {
		registRecognitionReceiver();
		registPoetryLearnRecycleReceiver();
	}

	private void registRecognitionReceiver() {
		mRbr = new RecognitionBr("RecognitionReceiver");		
		//实例化过滤器并设置要过滤的广播   
		IntentFilter intentFilter = new IntentFilter(MyIntent.POETRYLEARN_RECOGNITION); 		
		registerBroadcast(mRbr);
		
		//注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(mRbr, intentFilter); 
	}

	private void registPoetryLearnRecycleReceiver() {
		mRb = new RecycleBr("PoetryLearnRecycleReceiver");		
		//实例化过滤器并设置要过滤的广播   
		IntentFilter intentFilter = new IntentFilter(MyIntent.POETRYLEARN_RECYCLE); 		
		registerBroadcast(mRb);
		
		//注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(mRb, intentFilter); 
	}		
	
	//监听需要开启语音识别的广播
	public class RecognitionBr extends MyBroadcastReceiver {

		public RecognitionBr(String info) {
			super(info);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent == null){
				return ;
			}
			
			//开启语音识别
			MySpeechRecognizer.getInstance(
					new MyRecognizerListener(){
						public void onResult(String results) {
							// TODO Auto-generated method stub
							
							MySynthesizerListener listener = new MySynthesizerListener(){
								public void onCompleted(com.iflytek.cloud.SpeechError error) {
									Utils.sendBroadcast(MyIntent.POETRYLEARN_RECYCLE);
								};
							};
							
							if(isAnswerCorrect(results,mAnswer)){
								voiceTTS(answerCorrectly, listener);
							}else{
								voiceTTS(answerWrong + ",正确答案是:" + mAnswer, listener);
							}
								
							super.onResult(results);
						}
					}
				).startSR();
			
			super.onReceive(context, intent);
		}
		
	}
		
	
	//监听需要重新执行的广播
	public class RecycleBr extends MyBroadcastReceiver {

		public RecycleBr(String info) {
			super(info);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent == null){
				return ;
			}
			
			requestServerPoetry();
			
			super.onReceive(context, intent);
		}
	}
	
	
	/**
	 * 判断答案是否正确
	 * @param sr 录音的内容
	 * @param answer 正确的答案
	 * @return
	 */
	boolean isAnswerCorrect(String sr,String answer){
		if(sr.contains(answer)){
			return true;
		}
		return false;
	}
	
}
