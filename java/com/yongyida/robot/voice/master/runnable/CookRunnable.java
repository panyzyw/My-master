package com.yongyida.robot.voice.master.runnable;

import android.content.Intent;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yongyida.robot.voice.master.activity.BaiduActivity;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.CookBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CookRunnable extends BaseRunnable{
	private CookBean mCookBeak;
	public CookRunnable(String result){
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
        mIsEndSendRecycle = true;
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("CookRunnable", "run()");
		CookBean cookBean = BeanUtils.parseCookJson(mResult, CookBean.class);
        mCookBeak = cookBean;
		if(  cookBean == null ){
			MyLog.e("CookRunnable", "cookBean == null");
			//voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			voiceTTS(MyData.NOT_COOK_ANSWER[new Random().nextInt(MyData.NOT_COOK_ANSWER.length)],new MySynthesizerListener());
			return ;
		}
		
		doCook(cookBean);		
	}
	
	private void doCook(CookBean cb) {
		MyLog.i("CookRunnable", "doCook");
		try{
			if( cb.operation.equals("QUERY")  ){//讯飞提供的cookbook 场景
				queryCook(cb);
			}else if( cb.operation.equals("MYQUERY") ){ //自定义的场景
				myQueryCook(cb);
			}
		}catch(Exception e){
			MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	}
	
	public void queryCook(CookBean cb){
		MyLog.i("CookRunnable", "queryCook");
        HashMap<String, String> params = null;
		if(cb.semantic.slots.dishName != null){
			params = getCookStepInfoParams(cb.semantic.slots.dishName);
			postCook(params,cb.text);
            MyLog.i("CookRunnable","params = " + params.toString());
		}else if(cb.semantic.slots.ingredient != null){
            params = getCookStepInfoParams(cb.semantic.slots.ingredient);
            postCook(params,cb.text);
            MyLog.i("CookRunnable","params = " + params.toString());
        }else{
			MyLog.i("CookRunnable", "dishName or ingredient == null");
            mIsEndSendRecycle = false;
			voiceTTS(Constant.cook_not_find_the_cook,null);
			goToBaidu(cb.text);
		}	
	}
	
	public void myQueryCook(CookBean cb){
		MyLog.i("CookRunnable", "myQueryCook");
		HashMap<String, String> params = getCookNamesParams(cb);
		myPostCook( params,cb.text );
	}

	//获取查询做菜步骤的参数
	HashMap<String, String> getCookStepInfoParams(String name){
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
		return params;
	}

	//获取查询有什么菜的参数
	HashMap<String, String> getCookNamesParams(CookBean cb){
		HashMap<String, String> params = new HashMap<String, String>();
		if(cb.semantic.slots.cookstyle != null){
			params.put("cookstyle", cb.semantic.slots.cookstyle);
		}
		if(cb.semantic.slots.person != null){
			params.put("person", cb.semantic.slots.person);
		}
		if(cb.semantic.slots.material != null){
			params.put("material", cb.semantic.slots.material);
		}
		if(cb.semantic.slots.season != null){
			params.put("season", cb.semantic.slots.season);
		}
		if(cb.semantic.slots.taste != null){
			params.put("taste", cb.semantic.slots.taste);
		}
		if(cb.semantic.slots.effect != null){
			params.put("effect", cb.semantic.slots.effect);
		}
		return params;
	}
	
	public void postCook(final HashMap<String, String> params,final String text) {
		MyLog.i("CookRunnable", "postCook");

		RequestParams requestparams = new RequestParams();
		try {
			requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		requestparams.setContentType("text/plain");

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				Domain.getCookRequestURl(), requestparams,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {}
					@Override
					public void onLoading(long total, long current,boolean isUploading) {}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						MyLog.i("CookRunnable", responseInfo.result);
						String voiceContent = parseCookJson(params.get("name"),responseInfo.result);									
						if(!TextUtils.isEmpty(voiceContent) && !voiceContent.contains(Constant.cook_not_find_the_cook)){
							voiceTTS( voiceContent );
						}else{
                            mIsEndSendRecycle = false;
							//voiceTTS( "没找到这道菜" );
                            voiceTTS(MyData.NOT_COOK_ANSWER[new Random().nextInt(MyData.NOT_COOK_ANSWER.length)],new MySynthesizerListener());
                            goToBaidu(text);
						}
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(error));									
						voiceTTS(Constant.cook_temporary_not_have);
					}
				});
	}

	private void myPostCook(final HashMap<String, String> params,final String text) {
		MyLog.i("CookRunnable", "myPostCook");

		RequestParams requestparams = new RequestParams();
		try {
			requestparams.setBodyEntity(new StringEntity(new JSONObject(params).toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		requestparams.setContentType("text/plain");
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				Domain.getCookRequestURl(), requestparams,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {}
					@Override
					public void onLoading(long total, long current,boolean isUploading) {}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String voiceContent = myPostCookParse(responseInfo.result);

						if(TextUtils.isEmpty(voiceContent) || voiceContent.contains(Constant.cook_not_find)){
                            mIsEndSendRecycle = false;
							//voiceTTS( Constant.cook_not_find );
                            voiceTTS(MyData.NOT_COOK_ANSWER[new Random().nextInt(MyData.NOT_COOK_ANSWER.length)],new MySynthesizerListener());
                            goToBaidu(text);
						}else{
							voiceTTS( voiceContent );
						}

					}
					@Override
					public void onFailure(HttpException error, String msg) {
						MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(error));						
						voiceTTS(Constant.cook_access_server_faile);
					}
				});
		
	}

    private void goToBaidu(String text){
        Intent intentBaidu = new Intent(MasterApplication.mAppContext, BaiduActivity.class);
        intentBaidu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentBaidu.putExtra("text", text);
        MasterApplication.mAppContext.startActivity(intentBaidu);
    }
	
	private String myPostCookParse(String result) {
		String name = "";		
		String names = "";		
		try {
			JSONTokener tokener = new JSONTokener(result);
			JSONObject joResult = new JSONObject(tokener);
			JSONArray array = new JSONArray(joResult.optString("Menus"));
			if(array==null || array.length() == 0){	
					MyLog.i("CookRunnable", "array==null || array.length() == 0");				
					return Constant.cook_not_find;
			}		
			for(int i = 0; i < array.length() ; i++){ //在服务器返回的菜谱数组中  寻找指定的菜名的菜
				JSONObject jokeObj = array.getJSONObject(i);				
				name = jokeObj.getString("name");				
				names += name + ",";				
			}					
		}catch(Exception e){
			MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(e));
			}
		return names;		
	}
	
	/**
	 * 从服务器查询指定的菜谱
	 * 返回:要语音读出来的内容
	 * @param dishName
	 * @param cookJson
	 * @return
	 */
	String parseCookJson(String dishName, String cookJson ){
		String steps = "";
		String name = "";
		String material = "";
		try {
			JSONTokener tokener = new JSONTokener(cookJson);
			JSONObject joResult = new JSONObject(tokener);	
			JSONArray array = new JSONArray(joResult.optString("Menus"));	
			if(array==null || array.length() == 0){
				MyLog.e("CookRunnable", "cook_not_find_the_cook");					
				return Constant.cook_not_find_the_cook;
			}		
			boolean b = false; //标记是否有同名的菜谱
			for(int i = 0; i < array.length() ; i++){ //在服务器返回的菜谱数组中  寻找指定的菜名的菜
				JSONObject jokeObj = array.getJSONObject(i);
				steps = jokeObj.getString("step"); 
				name = jokeObj.getString("name");				
				material = jokeObj.getString("material");
				if( isContainName(dishName, name) ){ 
					name = dishName;
					b = true;
					break;
				}
			}	
			if(b == false){ //没找到指定菜名的菜,就取第一个
				if(array != null && array.length() > 0){
					JSONObject jokeObj = array.getJSONObject(0);
					steps = jokeObj.getString("step");
					name = jokeObj.getString("name");	
					name = name.split("\\|")[0];
					material = jokeObj.getString("material");
				}else
					return "";
			}					
		}catch(Exception e){ 
			MyLog.e("CookRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		if(TextUtils.isEmpty(steps) || TextUtils.isEmpty(name) || TextUtils.isEmpty(material)){
			return Constant.cook_not_find_the_cook;
		}
		//获取 步骤
		String stepStr = null;
		ArrayList<String> stepAl = new ArrayList<String>();
		getSteps(stepAl,steps);	
		//连接要语音播报出来的内容: 材料 + 每个步骤
		String voiceContent = name + Constant.cook_de_step_list + material + Constant.cook_step_content;
		
		if(stepAl.size() <= 0){
			return "";
		}
		if( stepAl.size() == 1 ){
			voiceContent += stepAl.get(0);
		}else{			
			int i = 0;
			for( String step : stepAl ){
				voiceContent += Constant.cook_no + (i+1) +Constant.cook_step + step;
				i++;
			}
		}
		return voiceContent;		
	}
	
	public boolean isContainName(String dishName,String name){
		if(name == null || dishName == null){
			return false;		
		}
		if(name.contains(dishName)){
			String[] nameArr = name.split("\\|");
			for( String n : nameArr ){	
				if(dishName.equals(n.trim())){
					return true;
				}
			}
		}
		return false;
	}
	
	//把步骤文本内容(不包含图片) 添加到列表中
	void getSteps( ArrayList<String> steps , String stepStr){
		if(steps == null){			
			steps = new ArrayList<String>();
		}else{
			steps.clear();
		}	
		String[] stepArr = stepStr.split("\n");
		String[] arr;
		for(String step : stepArr){
			if( step.contains("<") ){
				arr = step.split("<");
				steps.add( arr[0] );
			}			
		}
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("CookRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mCookBeak,text);
	}
	
	void voiceTTS(String text){
		voiceTTS(text,null);
	}
	
}
