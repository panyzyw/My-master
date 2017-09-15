package com.yongyida.robot.voice.master.runnable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechError;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.TelephoneBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.ContactNameUtils;
import com.yongyida.robot.voice.master.utils.JudgeNumberUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.UploadLinkMan;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class TelephoneRunnable extends BaseRunnable {

    public String phoneName;
    public String phoneCode;
    public String name;
    Context context;
    private TelephoneBean mTelephoneBean;
    public TelephoneRunnable(String result) {
        mIsEndSendRecycle = false;
        mPriority = PRIORITY_CALL; //电话的优先级
        mInterrupt_state = INTERRUPT_STATE_STOP;

        mResult = result;
    }

    //正在打电话给10086 中国移动
    @Override
    public void run() {
        // TODO Auto-generated method stub

        context = MasterApplication.mAppContext;
        //上传联系人
        UploadLinkMan.isUploadLinkMan(context);
        MyLog.i("TelephoneRunnable", "run()");
        MyLog.i("TelephoneRunnable", mResult);
        TelephoneBean telephoneBean = BeanUtils.parseTelephoneJson(mResult, TelephoneBean.class);
        mTelephoneBean = telephoneBean;
        if (telephoneBean == null) {
            MyLog.e("TelephoneRunnable", "telephoneBean == null");

            mIsEndSendRecycle = true;
            voiceTTS(Constant.comman_unkonw);
            return;
        }

        doTelephone(telephoneBean);
    }

    private void doTelephone(TelephoneBean tb) {
        //来电
        //...

        //下面是 拨号 逻辑
        if (tb.semantic == null || tb.semantic.slots == null) {  //拨号信息不清楚
            voiceTTS(context.getString(R.string.endcall_specific));
            return;
        }
        String text = tb.text;
        name = tb.semantic.slots.name;
        phoneCode = tb.semantic.slots.code;

        // 科大讯飞上传联系人的方法会根据地理位置的不同，传递过来的参数带有地理位置。
        // 例如：爸爸（浙江），所以必须要进行判断。
        if (!TextUtils.isEmpty(name)) {
            if (name.contains("（") || name.contains("(")) {

                //相似名字
                if (name.contains("（")) {

                    int index = name.indexOf("（");
                    String likeN = name.substring(0, index);
                    phoneName = likeN.trim();

                } else if (name.contains("(")) {

                    int index = name.indexOf("(");
                    String likeN = name.substring(0, index);
                    phoneName = likeN.trim();
                }
                startCall(phoneCode, phoneName, text);
            }
        }
        startCall(phoneCode, name, text);
    }

    //开始拨号
    void startCall(String code, String name, String text) {
        MyLog.i("TelephoneRunnable", "startCall(String code , String name)   code = " + code + " , name = " + name);

        //发送停止广播停止其他模块
        Utils.sendBroadcast(MyIntent.INTENT_STOP, "master : TelephoneRunnable", "phone");

        try {
            Thread.sleep(300); //延迟一会,释放资源 ,  若不延迟怕stop广播 影响到下面的操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String callTo = TextUtils.isEmpty(name) ? code : name;
        String callTip = context.getString(R.string.endcall_iscalling) +
                callTo +
                context.getString(R.string.endcall_waitplease);

        //拨打陌生人电话号码
        if (!TextUtils.isEmpty(code) && TextUtils.isEmpty(name) && !JudgeNumberUtils.isNumeric(text)) {

            voiceTTS(callTip, mSynthesizerListener);

            return;
        }
        //拨打联系人电话号码
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(code)) {

            phoneCode = ContactNameUtils.getPhoneContactsCallPhone(name);
            if (phoneCode != null) {
                //获取名字对应的code
                voiceTTS(callTip, mSynthesizerListener);

            } else {
                //没有找到联系人
                voiceTTS(context.getString(R.string.endcall_sorrynotfind) + name);
            }
            return;
        }
        //拨打常用电话。例如：10086
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(code)) {

            voiceTTS(callTip, mSynthesizerListener);
            return;

        }
        voiceTTS(context.getString(R.string.endcall_specific));
    }

    void startCall(String code) {
        MyLog.i("TelephoneRunnable", "startCall(String code)   code = " + code);

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + code));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MasterApplication.mAppContext.startActivity(intent);

    }

    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("TelephoneRunnable", "voiceTTS"+text);
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority, mIsEndSendRecycle, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mTelephoneBean,text);
    }

    public void voiceTTS(String text) {
        voiceTTS(text, null);
    }

    public void voiceMp(String uri) {
        MP mp = new MP(uri, BaseVoice.MP_ASSETS_RESOURCE, mInterrupt_state, mPriority, mIsEndSendRecycle, new MyOnPreparedListener(), new MyOnCompletionListener());
        VoiceQueue.getInstance().add(mp);
    }

    public TTS.MySynthesizerListener mSynthesizerListener = new TTS.MySynthesizerListener() {

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            startCall(phoneCode);
        }

        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        }
    };


}
