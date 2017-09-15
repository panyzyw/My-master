package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.ContactNameUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.TelephoneUtil;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class TelephoneReceiver extends BroadcastReceiver {
    private static final String TAG = "TelephoneReceiverTAG";
    static Context mContext;
    public static String phoneName = "";
    public static String ttsText = "";
    static TTS mTTS;

    public static Handler handlerInCall = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TelephoneUtil telephoneUtil = TelephoneUtil.getInstance(mContext);
            telephoneUtil.onPhoneStateChange(mTTS);
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i("TelephoneReceiver", "*********** onReceive ***********");
        if (intent == null) {
            return;
        }
        mContext = context;

        // TODO 开机处理SharedPreferences, 呼吸灯相关
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences sp = mContext.getSharedPreferences(TelephoneUtil.SP_FILE, Context.MODE_PRIVATE);
            // 初始化状态，在TelephoneUtil用得到
            sp.edit().putInt(TelephoneUtil.SP_KEY_LAST_STATE, TelephonyManager.CALL_STATE_IDLE).apply();

            // 获取上次led状态，如果有未关闭情况，需要开机结束时关闭，防止开机后呼吸灯瞎闪。
            if (sp.getBoolean(TelephoneUtil.SP_KEY_LED_STATUS, false)) {
                // 告诉呼吸灯要关灯了，来自电话模块
                controlLED(false, "call");
                sp.edit().putBoolean(TelephoneUtil.SP_KEY_LED_STATUS, false).apply();
            }

            if (sp.getBoolean(TelephoneUtil.SP_KEY_MMS, false)) {
                // 关短信呼吸灯及标识
                controlLEDOnReceivedMms(false);
            }
            return;
        }

        // TODO 接收短信处理
        if (intent.getAction().equals("com.yongyida.robot.MMS_RECEIVED")) {
            // TODO 弹出短信对话框或通知栏有短信通知时, 改变呼吸灯提示
            if (intent.hasExtra("turn_on")) {
                boolean isTurnLedOn = intent.getBooleanExtra("turn_on", false);
                controlLEDOnReceivedMms(isTurnLedOn);
                MyLog.i("TelephoneReceiver", "turn_on " + isTurnLedOn);
            }
            return;

        } else if(intent.getAction().equals("com.android.mms.NOTIFICATION_DELETED_ACTION")) {
            // TODO 通知栏短信通知被删除时, 关呼吸灯提示
            controlLEDOnReceivedMms(false);
            MyLog.i("TelephoneReceiver", "turn_on " + false);
            return;
        }

        // TODO 用户打开电话界面或清除通知时，关闭呼吸灯
        if (intent.getAction().equals("com.yongyida.robot.DIALER_OPEN") ||
                intent.getAction().equals("com.yongyida.robot.CANCEL_NOTIFY_MISSED_CALLS")) {
            controlLED(false, "call");
            SharedPreferences sp = mContext.getSharedPreferences(TelephoneUtil.SP_FILE, Context.MODE_PRIVATE);
            sp.edit().putBoolean(TelephoneUtil.SP_KEY_LED_STATUS, false).apply();
            return;
        }

        // TODO 来去电处理 --------------------------------------------------------------------
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {//去电
            TelephoneUtil.isOutCalling = true;
            MyLog.i("TelephoneReceiver", "getAction " + Intent.ACTION_NEW_OUTGOING_CALL);
        } else {
            TelephoneUtil.isOutCalling = false;
            MyLog.i("TelephoneReceiver", "action=" + intent.getAction());
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
               /*如果我们想要监听电话的拨打状况，需要这么几步 :
			      第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
			      第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
			      PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
			      监听的状态改变事件，除此之外，还有很多其他事件哦。
			      第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
			      第四步：这一步很重要，那就是给应用添加权限。
                    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
                    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
			        <action android:name="android.intent.action.PHONE_STATE"/>
   				    <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
			    */

            // set ring volume silent
            ((AudioManager)context.getSystemService(Context.AUDIO_SERVICE))
                    .setStreamVolume(AudioManager.STREAM_RING, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            String inCallNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            phoneName = ContactNameUtils.testContactNameByNumber(inCallNumber);
            if (!phoneName.equals("")) {
                // 存在联系人号码，语音合成, "主人，" + phoneName + "给你打电话了，快来接电话。"
                ttsText = phoneName;
            } else {
                // 陌生电话，语音合成, "主人, " + code + "给你打电话了，快来接电话。"
                ttsText = inCallNumber;
            }
            mTTS = voiceTTS(mContext.getString(R.string.endcall_owner) + ttsText +
                    mContext.getString(R.string.endcall_callyou));
            //VoiceQueue.getInstance().clearQueue();

            handlerInCall.removeMessages(0);
            handlerInCall.sendEmptyMessage(0);
        }
    }

    public TTS voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i(TAG, "voiceTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text,
                BaseVoice.TTS,
                BaseRunnable.INTERRUPT_STATE_STOP,
                BaseRunnable.PRIORITY_CALL,
                false,
                mySynthesizerListener);
//        VoiceQueue.getInstance().add(tts);
        return tts;
    }

    public TTS voiceTTS(String text) {
        return voiceTTS(text, null);
    }

    void controlLEDOnReceivedMms(boolean bool) {
        MyLog.i("TelephoneReceiver", "controlLEDOnReceivedMms " + bool);
        SharedPreferences sp = mContext.getSharedPreferences(TelephoneUtil.SP_FILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(TelephoneUtil.SP_KEY_MMS, bool).apply();
        controlLED(bool, "mms");
    }

    /**
     * 控制呼吸灯的广播
     * @param bool true为开，false为关
     */
    void controlLED(boolean bool, String module) {
        Intent intentCallOpen = new Intent("com.yongyida.robot.change.BREATH_LED");
        intentCallOpen.putExtra("on_off", bool);
        intentCallOpen.putExtra("Permanent", module);
        if (bool) {
            intentCallOpen.putExtra("colour", 2);
            intentCallOpen.putExtra("frequency", 1);
            intentCallOpen.putExtra("priority", 8);
        }
        intentCallOpen.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        MasterApplication.mAppContext.sendBroadcast(intentCallOpen);
    }
}
