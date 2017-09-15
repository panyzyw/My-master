package com.yongyida.robot.voice.master.runnable;

import java.util.Calendar;
import java.util.HashMap;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.bean.MovieInfoBean;
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

public class MovieInfoRunnable extends BaseRunnable{
	private MovieInfoBean mMovieInfoBean;
	public MovieInfoRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("MoveInfoRunnable", "run()");
		MovieInfoBean moveInfoBean = BeanUtils.parseMoveInfoJson(mResult, MovieInfoBean.class);
        mMovieInfoBean = moveInfoBean;
		if(  moveInfoBean == null ){
			MyLog.e("MoveInfoRunnable", "jokeBean == null");
			
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
		
		doMoveInfo(moveInfoBean);		
	}
	
	private void doMoveInfo(MovieInfoBean moveInfoBean) {
		HashMap<String, String> params = new HashMap<String, String>();
		String[] recentTime = getRecentTime();
		params.put("showtime_start", recentTime[0]);
		params.put("showtime_end" , recentTime[1]);
		requestMoveInfo(params);
		/*try{			
			if(moveInfoBean.semantic.slots.query_movie != null){ //查询电影资讯
				
				if(moveInfoBean.semantic.slots.area != null){
					params.put("area", moveInfoBean.semantic.slots.area);
				}
				
				if(moveInfoBean.semantic.slots.classification != null){
					params.put("classification", moveInfoBean.semantic.slots.classification);
				}
				
				if(moveInfoBean.semantic.slots.movie != null){
					params.put("movie", moveInfoBean.semantic.slots.movie);
				}
				
				if(moveInfoBean.semantic.slots.time != null){
					String[] recentTime = getRecentTime();
					params.put("showtime_start", recentTime[0]);
					params.put("showtime_end" , recentTime[1]);
				}
						
				MyLog.i("MoveInfoRunnable", "params : " + params);
				requestMoveInfo(params);
			}else{ //播放指定电影,暂时没实现
				playMove(null);
			}

		}catch(Exception e){
			MyLog.e("MoveInfoRunnable", MyExceptionUtils.getStringThrowable(e));
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
		}*/		
		
	}
	
	//获取最近的时间(要符合电影资讯的格式)
	String[] getRecentTime(){
		// 1   2   3   4   5   6   7   8   9  10  11  12
		//当前所在月对应最近的月 (当前 往 前、后 数1个月 , Calendar 对象获取的月是从0开始)
		String[] currentMonth_recentMonth_prev = {"12","01","02","03","04","05","06","07","08","09","10","11"};
		String[] currentMonth_recentMonth_back = {"02","03","04","05","06","07","08","09","10","11","12","01"};
		
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH); // 0 1 2 ... 11
		int y = cal.get(Calendar.YEAR);
		
		String recentM_prev = "01"; 
		String recentM_back = "12";		
		try{
			recentM_prev = currentMonth_recentMonth_prev[m];
			recentM_back = currentMonth_recentMonth_back[m];
		}catch(Exception e){
			MyLog.e("MoveInfoRunnable", MyExceptionUtils.getStringThrowable(e));
		}		
		
		String recentYear_prev = y + "";
		String recentYear_back = y + "";
		if( m == 0 ){  //第一个月 要推算到上一年
			recentYear_prev = (y - 1) + "" ;
		}else if(m == 11){		
			recentYear_back = (y + 1) + "" ;
		}
		
		String recentTime_prev = recentYear_prev + "-" +  recentM_prev + "-00";
		String recentTime_next = recentYear_back + "-" +  recentM_back + "-00";
		
		MyLog.i("MoveInfoRunnable", "recentTime_prev : " + recentTime_prev);
		MyLog.i("MoveInfoRunnable", "recentTime_next : " + recentTime_next);
		
		return new String[]{recentTime_prev,recentTime_next};
	}
	
	//查询电影资讯
	void requestMoveInfo(HashMap<String, String> params){

		try {
			RequestParams requestparams = new RequestParams();
			requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));
			requestparams.setContentType("text/plain");

			HttpUtils httputils = new HttpUtils();
			httputils.send(HttpMethod.POST, Domain.getMoveInfoRequestURl(), requestparams, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String msg) {
					MyLog.e("MoveInfoRunnable", msg);
					voiceTTS("访问服务器资源失败");
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					MyLog.i("MoveInfoRunnable", "responseInfo result : " + responseInfo.result);
					
					try {
						JSONTokener jsontokener = new JSONTokener(responseInfo.result);
						JSONObject jsonobject = new JSONObject(jsontokener);
						JSONArray jsonarray = new JSONArray(jsonobject.optString("Screens"));
						if (jsonarray != null && jsonarray.length() > 0) {

								StringBuffer stringbuffer = new StringBuffer();
								
								//下面判断小于10 的原因是怕有很多个电影信息 读个没玩没了
								if (jsonarray.length() < 10) {
									for (int i = 0; i < jsonarray.length(); i++) {
										stringbuffer.append(jsonarray.getJSONObject(i).getString("name_chinese") + ", ");
									}
								} else {
									for (int i = 0; i < 9; i++) {
										stringbuffer.append(jsonarray.getJSONObject(i).getString("name_chinese") + ", ");
									}
								}
								
								voiceTTS("给你推荐这些好看的电影吧:" + stringbuffer.toString());
								
						}else{
							voiceTTS("没有找到您想要的资源");
						}

					} catch (Throwable e) {
						MyLog.e("MoveInfoRunnable", MyExceptionUtils.getStringThrowable(e));
						voiceTTS("没有找到您想要的资源");
					}
				
				}
			});
		} catch (Throwable e) {
			MyLog.e("MoveInfoRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	
	}
	
	//播放指定的电影,暂时未实现
	void playMove(HashMap<String, String> params){ }

	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("MoveInfoRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mMovieInfoBean,text);
	}
	
	void voiceTTS(String text){
		voiceTTS(text,null);
	}
}
