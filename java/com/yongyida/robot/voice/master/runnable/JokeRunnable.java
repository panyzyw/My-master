package com.yongyida.robot.voice.master.runnable;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.bean.JokeBean;
import com.yongyida.robot.voice.master.constant.Constant;
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




import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class JokeRunnable extends BaseRunnable {
	private JokeBean mJokeBean;
	public JokeRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("JokeRunnable", "run()");
		JokeBean jokeBean = BeanUtils.parseJokeJson(mResult, JokeBean.class);
        mJokeBean = jokeBean;
		if(  jokeBean == null ){
			MyLog.e("JokeRunnable", "jokeBean == null");
			
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
		
		doJoke(jokeBean);		
	}
	
	private void doJoke(JokeBean jb) {
		try{
			if (null == jb.semantic.slots || null == jb.semantic.slots.type) {	
				MyLog.i("JokeRunnable", "type = null");
				requestJoke("");
			} else {			
				MyLog.i("JokeRunnable", "type = " + jb.semantic.slots.type);
				requestJoke(jb.semantic.slots.type);
			}
		}catch(Exception e){
			MyLog.e("JokeRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	}

	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("JokeRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mJokeBean,text);
	}
	
	private void requestJoke(String type) {
		MyLog.i("JokeRunnable", "requestJoke enter");
		// 设置请求参数的编码
		// RequestParams params = new RequestParams("GBK");
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		// post请求参数的对象
		JokeParamsBean jpb = new JokeParamsBean();
		jpb.type = type;
		
		params.setContentType("applicatin/json"); // 设置传给 BodyEntity的类型为 json
		// 设置post 请求参数的BodyEntity
		try {
			Gson gson = new Gson();
			params.setBodyEntity(new StringEntity(gson.toJson(jpb), "UTF-8"));
			MyLog.i("JokeRunnable", "json:" + gson.toJson(jpb));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Domain.getJokeRequestURl() , params,new RequestCallBack<String>() {
					@Override
					public void onStart() {}
					@Override
					public void onLoading(long total, long current,boolean isUploading) {}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {						
						MyLog.i("JokeRunnable", "requestJoke onSuccess , result = " + responseInfo.result);
						String s = getJoke(responseInfo.result);								
						if (TextUtils.isEmpty(s)) {
							voiceTTS(Constant.joke_have_not_the_type_joke,null);
						} else {
							voiceTTS(s,new MySynthesizerListener(){
								public void onCompleted(SpeechError error) {
									MyLog.i("JokeRunnable", "joke completed and send laugh");
									Utils.sendBroadcast(Constant.ACTION_LAUGH, "joke", "completed");
                                    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
                                    scheduledThreadPoolExecutor.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.sendBroadcast(MyIntent.INTENT_RECYCLE, "master(joke)", "");
                                        }
                                    },2, TimeUnit.SECONDS);
								}
							} );
						}					
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						MyLog.e("JokeRunnable", " failure : access server for joke ");
						voiceTTS(Constant.joke_have_not_the_joke,null);
					}
				});
	}
	
	public static String getJoke(String json) {		
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);	
			JSONArray array1 = new JSONArray(joResult.optString("Jokes"));
            String ret = joResult.optString("ret");
			if (!"0".equals(ret) || array1.length() == 0)
				return "";
			Random random = new Random();
			// 随机选择播报 第几个
			int rand = Math.abs(random.nextInt() % array1.length());
			int n = rand;
			for (int i = 0; i < array1.length(); i++) {
				if (i == n) {
					JSONObject jokeObj = array1.getJSONObject(i);
					String text = "";
					text = jokeObj.getString("text");
					return text;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	class JokeParamsBean {
		public String type;
		public String title;
	}

}
