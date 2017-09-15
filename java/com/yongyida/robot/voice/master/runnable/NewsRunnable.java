package com.yongyida.robot.voice.master.runnable;

import java.util.HashMap;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.bean.NewsBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class NewsRunnable extends BaseRunnable{
    private NewsBean mNewsBean;
	public NewsRunnable(String result){
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("NewsRunnable", "run()");
		NewsBean newsBean = BeanUtils.parseNewsJson(mResult, NewsBean.class);
		mNewsBean = newsBean;
		if(  newsBean == null ){
			MyLog.e("NewsRunnable", "newsBean == null");
			voiceTTS(Constant.comman_unkonw);
			return ;
		}
		
		doNews(newsBean);		
	}
	
	private void doNews(NewsBean newsBean) {
		
		HashMap<String, String> params = getParams(newsBean);
		
		requestNews(params);
		
	}

	private HashMap<String, String> getParams(NewsBean newsBean) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", "");
		try{
			if(newsBean.semantic.slots.type != null){
				params.put("type", newsBean.semantic.slots.type);
			}
		}catch(Exception e){
			MyLog.e("NewsRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		MyLog.i("NewsRunnable", "params : " + params);
		return params;		
	}

	void requestNews(HashMap<String, String> params){
		try {
			RequestParams requestparams = new RequestParams();
			requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));
			requestparams.setContentType("text/plain");

			HttpUtils httputils = new HttpUtils();
			httputils.send(HttpMethod.POST, Domain.getNewsRequestURl(), requestparams, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String msg) {
					MyLog.e("NewsRunnable", msg);
					voiceTTS("访问服务器资源失败");
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					MyLog.i("NewsRunnable", "responseInfo result : " + responseInfo.result);					
					
					try {
						JSONTokener jsontokener = new JSONTokener(responseInfo.result);
						JSONObject jsonobject = new JSONObject(jsontokener);
						
						String ret = jsonobject.getString("ret");
						if(!ret.equals("0")){
							voiceTTS("抱歉,没有找到新闻");
							return ;
						}
						
						JSONObject jsonNews = new JSONObject(jsonobject.optString("news"));
						String title = jsonNews.getString("title");
						String content = jsonNews.getString("content");
						
						if(title== null || content== null){
							voiceTTS("抱歉,没有找到新闻");
							return ;
						}
						
						voiceTTS(title + "，，，。。" + content);
						
					} catch (Throwable e) {
						MyLog.e("NewsRunnable", MyExceptionUtils.getStringThrowable(e));
						voiceTTS("抱歉,没有找到新闻");
					}
				
				}
			});
		} catch (Throwable e) {
			MyLog.e("MoveInfoRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	
	
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("NewsRunnable", "voiceTTS");
		
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mNewsBean,text);
	}
	
	public void voiceTTS(String text) {
		voiceTTS(text,null);
	}
	
}
