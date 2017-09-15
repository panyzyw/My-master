package com.yongyida.robot.voice.master.runnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.SystemProperties;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.MusicBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.MyRegularUtils;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.utils.VideoMonitoring;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;
 

//处理music story opera
public class MusicRunnable extends BaseRunnable {

    private static final String TAG = "MusicRunnableTAG";
    private static Map<String, String> mHashmap;
    private boolean mIsContinuousPlay;
    private boolean isRegisted;
    private MusicRecycleReceiver musicRecycleReceiver;

    private static String mRequestURl;
    private static String mResourceURl;
    private static String mMusicType; //Musics , Storys , ...

    MusicBean musicBean;

    private MusicBean mMusicBean;
    private boolean isOnce;
    
    public MusicRunnable(String result) {
        mIsEndSendRecycle = false;
        mPriority = PRIORITY_OTHER;
        mInterrupt_state = INTERRUPT_STATE_STOP;

        isRegisted = false;
        mIsContinuousPlay = true;
        //--说话的结果
        mResult = result;
        isOnce=false;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        MyLog.i("MusicRunnable", "run()");

		musicBean = BeanUtils.parseMusicJson(mResult, MusicBean.class);
        mMusicBean = musicBean;

        if (VideoMonitoring.isVideoing(MasterApplication.mAppContext)) { // 如果正在视频监控就不执行下面唱歌
            MyLog.e("MusicRunnable", "want to play music ,but is videoing or monitoring");
            return;
        }

        if(musicBean!=null){
            Intent broadcast = new Intent("com.zccl.action.player");
            broadcast.putExtra("category", "startMusic");
            broadcast.putExtra("result", mResult);
            MasterApplication.mAppContext.sendBroadcast(broadcast);
            return;
        }

        if (musicBean == null) {
            MyLog.e("MusicRunnable", "musicBean == null");

            mIsEndSendRecycle = true;
            voiceTTS(Constant.comman_unkonw);
            return;
        }

        MyLog.i("MusicRunnable", "text = " + musicBean.text);
        if (isNotDoMusic(musicBean.text)) {
            voiceTTS("我都晕了,到底要不要听音乐啊,要听就直接点撒");
            return;
        }

        if (isSingleMusic(musicBean.text)) {
            mIsContinuousPlay = false;
        }

        //获取音乐(音乐 戏曲 故事)请求的参数和链接
        initRequest(musicBean);

        requestServerAndPlay();
    }
	
	//add by yihuapeng 20161026
	private String getDeviceProp() {
		return SystemProperties.get("ro.product.device", "y20a_dev");
	}

    private void initRequest(MusicBean musicBean) {
        MyLog.i("MusicRunnable", "server : " + musicBean.service);
        if (musicBean.service.equals("music")) {
            mRequestURl = Domain.getMusicRequestURl();
            mHashmap = getMusicParam(musicBean);

            mResourceURl = Domain.getMusicResourceURl();
            mMusicType = "Musics";
        } else if (musicBean.service.equals("story")) { //故事
            //
            mRequestURl = Domain.getStoryRequestURl();
            mHashmap = getStoryParam(musicBean);

            mResourceURl = Domain.getStoryResourceURl();
            mMusicType = "Storys";
        } else if (musicBean.service.equals("opera")) {//戏曲
            mRequestURl = Domain.getOperaRequestURl();
            mHashmap = getMusicParam(musicBean);

            mResourceURl = Domain.getOperaResourceURl();
            mMusicType = "ChineseOperas";
        }

        MyLog.i("MusicRunnable", "requestURl : " + mRequestURl);
        MyLog.i("MusicRunnable", "param : " + mHashmap.toString());
    }

    private boolean isNotDoMusic(String text) {
        String[] regexs = {".*(不|别).*(唱|听|放|播).*"};
        if (MyRegularUtils.myMatch(regexs, text)) {
            MyLog.i("MusicRunnable", "isNotDoMusic : true");
            return true;
        }
        MyLog.i("MusicRunnable", "isNotDoMusic : false");
        return false;
    }

    //判断是不是只要1首
    public boolean isSingleMusic(String text) {
        String[] regexs = {".*(一|1|讲|唱|听|放|播|来)(首|个).*"};
        if (MyRegularUtils.myMatch(regexs, text)) {
            MyLog.i("MusicRunnable", "isSingleMusic : true");
            return true;
        }

        MyLog.i("MusicRunnable", "isSingleMusic : false");
        return false;
    }

    Map<String, String> getMusicParam(MusicBean mb) {
        MyLog.i("MusicRunnable", "getMusicParam");

        Map<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("name", "");
        hashmap.put("singer", "");
        hashmap.put("type", "");
        hashmap.put("area", "");
        hashmap.put("language", "");
        try {
            if (mb.semantic != null && mb.semantic.slots != null) {

                if (!TextUtils.isEmpty(mb.semantic.slots.song)) {
                    mIsContinuousPlay = false; // 若指定了具体的名字就不连续播放,否则就连续播放
                    MyLog.i("MusicRunnable", "name:" + mb.semantic.slots.song);
                    hashmap.put("name", mb.semantic.slots.song);
                }
                if (!TextUtils.isEmpty(mb.semantic.slots.artist)) {
                    MyLog.i("MusicRunnable", "singer:" + mb.semantic.slots.artist);
                    hashmap.put("singer", mb.semantic.slots.artist);
                }
                if (!TextUtils.isEmpty(mb.semantic.slots.category)) {
                    MyLog.i("MusicRunnable", "type:" + mb.semantic.slots.category);
                    hashmap.put("type", mb.semantic.slots.category);
                }
            } else if (!TextUtils.isEmpty(mb.service) && mb.service.equals("music")) {
                MyLog.i("MusicRunnable", "choose randomly");
            }

        } catch (Exception e) {
            MyLog.e("MusicRunnable", MyExceptionUtils.getStringThrowable(e));
            hashmap.put("name", "");
            hashmap.put("singer", "");
            hashmap.put("type", "");
            hashmap.put("area", "");
            hashmap.put("language", "");
        }
        return hashmap;
    }

    Map<String, String> getOperaParam(MusicBean musicBean) {
        return null;
//		MyLog.i("MusicRunnable", "getOperaParam");
//		Map<String, String> hashmap = new HashMap<String, String>();
//		try{
//			if (!TextUtils.isEmpty(musicBean.semantic.slots.name)) {
//				mIsContinuousPlay = false; // 若指定了具体的名字就不连续播放,否则就连续播放
//				MyLog.i("MusicRunnable", "name:" + musicBean.semantic.slots.name);
//				hashmap.put("title", musicBean.semantic.slots.name);
//			}
//		}catch(Exception e){
//			MyLog.e("MusicRunnable",MyExceptionUtils.getStringThrowable(e));
//			hashmap.put("type", "");
//			hashmap.put("classification", "");
//			hashmap.put("title", "");
//		}
//		return hashmap;
    }

    Map<String, String> getStoryParam(MusicBean musicBean) {
        MyLog.i("MusicRunnable", "getStoryParam");
        Map<String, String> hashmap = new HashMap<String, String>();
        try {
            if (!TextUtils.isEmpty(musicBean.semantic.slots.name)) {
                mIsContinuousPlay = false; // 若指定了具体的名字就不连续播放,否则就连续播放
                MyLog.i("MusicRunnable", "name:" + musicBean.semantic.slots.name);
                hashmap.put("title", musicBean.semantic.slots.name);
               
            }else if(TextUtils.isEmpty(musicBean.semantic.slots.name)&&musicBean.text.contains("一个故事")){
            	   mIsContinuousPlay=false;
            }else if(TextUtils.isEmpty(musicBean.semantic.slots.name)&&!storyMeaning()){
            	mIsContinuousPlay=false;
            }
        } catch (Exception e) {
            MyLog.e("MusicRunnable", MyExceptionUtils.getStringThrowable(e));
            hashmap.put("type", "");
            hashmap.put("classification", "");
            hashmap.put("title", "");
        }
        return hashmap;
    }

    // 离线资源索引
    private int offIndex = 0;

    public void requestServerAndPlay() {
        MyLog.i("MusicRunnable", "requestServerAndPlay");
        try {
            RequestParams requestparams = new RequestParams();
            requestparams.setBodyEntity(new StringEntity(new JSONObject(mHashmap).toString(), "UTF-8"));
            requestparams.setContentType("text/plain");

            HttpUtils httputils = new HttpUtils();
            httputils.send(HttpMethod.POST, mRequestURl, requestparams, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    MyLog.e("MusicRunnable", "get data from yyd failed:" + arg1);
                    if("Musics".equals(mMusicType)){
                        if(offIndex++==0) {
                            voiceMp("offline/pp.mp3");
                        }else {
                            voiceMp("offline/waltz.mp3");
                            offIndex = 0;
                        }
                    }else {
                        voiceTTS(Constant.cannot_find_music_server);
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {


                    MyLog.i("MusicRunnable", "result = " + arg0.result);
                    try {
                        JSONObject jsonobject0 = new JSONObject(new JSONTokener(arg0.result));
                        JSONArray jsonArray = new JSONArray(jsonobject0.optString(mMusicType));
                        MyLog.i("MusicRunnable", "jsonArray.length:" + jsonArray.length());

                        if (jsonArray.length() > 0) {
                            if (mIsContinuousPlay) {
                                //注册接收音乐播放结束的广播
                                registMusicRecycleReceiver();
                            }
                            String musicUri = jsonArray.getJSONObject(new Random().nextInt(jsonArray.length())).getString("uri");
                            String url = mResourceURl + musicUri;
                            MyLog.i("MusicRunnable", "url = " + url);

//                            MusicBean musicBean = BeanUtils.parseMusicJson(mResult, MusicBean.class);

                            if (musicBean.service.equals("story")) {
                                if (musicBean.service.equals("story") && mResult.contains("name")) {
                                    voiceTTS("好的我给你讲个" + musicBean.semantic.slots.name + "的故事吧");
                                    voiceMp(url);
                                } else if (!mResult.contains("name") && !storyMeaning()) {
                                    mRequestURl = Domain.getStoryRequestURl();
                                    mHashmap = getStoryParam(musicBean);
                                    mResourceURl = Domain.getStoryResourceURl();
                                    mMusicType = "Storys";
                                    String url2 = mResourceURl + musicUri;
                                    String storyName = musicUri.substring(musicUri.indexOf(""), musicUri.lastIndexOf("."));
                                    voiceTTS("这个故事我还不会，我给你讲个" + storyName + "的故事吧");
                                    voiceMp(url2);
                                } else if (storyMeaning()) {
                                    mRequestURl = Domain.getStoryRequestURl();
                                    mHashmap = getStoryParam(musicBean);
                                    mResourceURl = Domain.getStoryResourceURl();
                                    mMusicType = "Storys";
                                    String url3 = mResourceURl + musicUri;
                                    String storyName = musicUri.substring(musicUri.indexOf(""), musicUri.lastIndexOf("."));
                                    if(!isOnce){
                                    	      voiceTTS("好的，我给你讲个" + storyName + "的故事吧");
                                    	      isOnce=true;
                                     }
                                     voiceMp(url3);
                                }
                            }else{
                            	voiceMp(url);
                            }
                            
//						} else {
//							voiceTTS(MasterApplication.mAppContext.getString(R.string.tq007_yinyuemeizhaodao));
                        } else {
                            Utils.sendBroadcast(MyIntent.INTENT_MUSIC_NO_FOUND
                                    , new JSONObject(mHashmap).toString());
                        }

                    } catch (JSONException e) {
                        MyLog.e("MusicRunnable", MyExceptionUtils.getStringThrowable(e));
                        voiceTTS(MasterApplication.mAppContext.getString(R.string.tq007_yinyuemeizhaodao));
                    }
                }
            });
        } catch (Throwable e) {
            MyLog.e("MusicRunnable", MyExceptionUtils.getStringThrowable(e));
            voiceTTS(MasterApplication.mAppContext.getString(R.string.tq007_yinyuemeizhaodao));
        }
    }

    public void voiceTTS(String text) {
        TTS tts = new TTS(text, BaseVoice.TTS, INTERRUPT_STATE_STOP, mPriority, true, new MySynthesizerListener());
        VoiceQueue.getInstance().add(tts);
    }

    public void voiceMp(String uri) {

        Log.d(TAG, "歌曲URI" + uri);

        MP mp = new MP(uri
                , BaseVoice.MP_INTENT_RESOURCE
                , mInterrupt_state
                , mPriority, mIsEndSendRecycle
                , new MyOnPreparedListener()
                , new MyOnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Log.d(TAG, "歌已唱玩");
                Utils.sendBroadcast(MyIntent.INTENT_VOICE_MUSIC_END, "MP", "YYDBreathLed");
            }
        });
        VoiceQueue.getInstance().add(mp);
    }

    public void registMusicRecycleReceiver() {
        if (isRegisted)
            return;
        MyLog.i("MusicRunnable", "registMusicRecycleReceiver");
        musicRecycleReceiver = new MusicRecycleReceiver("MusicRecycleReceiver");
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(MyIntent.QUEUE_COMPLETE);
        registerBroadcast(musicRecycleReceiver);

        //注册到自己的广播队列(统一管理)
        MasterApplication.mAppContext.registerReceiver(musicRecycleReceiver, intentFilter);
        isRegisted = true;
    }

    public class MusicRecycleReceiver extends MyBroadcastReceiver {

        public MusicRecycleReceiver(String info) {
            super(info);
        }

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            MyLog.i("MusicRunnable", "MusicRecycleReceiver onReceive");
            requestServerAndPlay();
        }
    }

    public boolean storyWordMeaning() {
        MusicBean musicBean = BeanUtils.parseMusicJson(mResult, MusicBean.class);
        if (musicBean.text.equals("你会讲故事吗？") || musicBean.text.equals("给我讲个故事吧！") ||
                musicBean.text.equals("讲个故事吧！") || musicBean.text.equals("给讲个故事吧！") ||
                musicBean.text.equals("讲个故事呗！") || musicBean.text.equals("讲故事。") ||
                musicBean.text.equals("有什么好听的故事？") || musicBean.text.equals("讲个故事呗！") ||
                musicBean.text.equals("来讲个故事。") || musicBean.text.equals("有啥好听的故事？") ||
                musicBean.text.equals("讲个故事给我听。") || musicBean.text.equals("你给讲个故事吧！")
                || musicBean.text.equals("来讲故事。") || musicBean.text.equals("讲个故事来听听。")
                || musicBean.text.equals("讲个故事。") || musicBean.text.equals("你讲个故事。")
                || musicBean.text.equals("你给我讲个故事吧！")) {
            return true;
        }
        return false;
    }

    public boolean storyMeaning() {
        if (musicBean.text.contains("讲故事") || musicBean.text.contains("好听的故事")
                || musicBean.text.contains("睡前故事") || musicBean.text.contains("个故事")
                || musicBean.text.contains("听故事") || musicBean.text.contains("说故事")) {
            return true;
        }
        return false;
    }
}
