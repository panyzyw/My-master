package com.yongyida.robot.voice.master.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.reciver.TelephoneReceiver;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

/**
 * Created by Bright on 2016/8/17 0017.
 * 处理来电话事件
 */
public class TelephoneUtil {
    private static final String TAG = "TelephoneUtil";
    static Context mContext;
    static TelephoneUtil mTelephoneUtil;

    static long lastTime_IDLE = 0;
    static long lastTime_RINGING = 0;

    public static boolean phoneStateRinging = false;
    static String phoneNumber = null;
    static AudioManager mAudioManager;
    static MP mp;

    public static final String SP_FILE = "telephone_led";
    public static final String SP_KEY_LED_STATUS = "status";
    public static final String SP_KEY_LAST_STATE = "state";
    public static final String SP_KEY_MMS = "mms_led_state";
    public static boolean isOutCalling = false;


    /**
     * 播放拨号语音提醒，处理各种情况下的来电提醒
     */
    public static Handler playInCallSoundHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    voiceMp("call.mp3");
                    break;

                case 1:
                    VoiceQueue.getInstance().add((TTS) msg.obj);
                    break;

            }
//            Utils.sendBroadcast(MyIntent.INTENT_INSIDEGOING_CALL, "TelephoneReceiver", "YYDBreathLed");
            if (phoneStateRinging) {
                if (msg.what == 0) {
                    playInCallSoundHandler.sendEmptyMessageDelayed(msg.what, 3000);
                } else {
                    Message message = playInCallSoundHandler.obtainMessage();
                    message.obj = msg.obj;
                    message.what = msg.what;
                    playInCallSoundHandler.removeMessages(msg.what);
                    playInCallSoundHandler.sendMessageDelayed(message, 3000);
                }

            } else {
                playInCallSoundHandler.removeMessages(msg.what);
                if (mp != null) {
                    mp.stop();
                }
            }
        }
    };

    private TelephoneUtil() {
    }

    public static TelephoneUtil getInstance(Context context) {
        if (mTelephoneUtil == null) {
            mTelephoneUtil = new TelephoneUtil();
            mContext = context;
        }
        if (mAudioManager == null) {
            mAudioManager = getAudioManager();
        }
        return mTelephoneUtil;
    }

    /**
     * 处理来电话事件
     *
     * @param tts 来电号码或联系人语音合成对象
     */
    public synchronized void onPhoneStateChange(TTS tts) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);

        TelephonyManager telephonyManager =
                (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        switch (telephonyManager.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                // TODO 来电响铃中
                MyLog.i(TAG, "CALL_STATE_RINGING, code: " + phoneNumber);
                // 判断网络类型,选择用语音合成还是播放固定的音频
                // (注:来电时4G可能不稳定,如果是wifi 就不存在这个问题)
                phoneStateRinging = true;
                judgeNetType(tts);
                // 开呼吸灯
                controlLEDOnRinging(true);
                sp.edit().putInt(SP_KEY_LAST_STATE, TelephonyManager.CALL_STATE_RINGING).apply();
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                // TODO 挂断电话,可能存在未接；未接时，呼吸灯灭灯，请接收用户打开电话界面的广播（自定义的）。
                MyLog.i(TAG, "state: " + telephonyManager.getCallState());
                phoneStateRinging = false;
                sendStop();
                setRingNormal();
                if (mp != null) {
                    mp.stop();
                }
                VoiceQueue.getInstance().clearQueue();
                playInCallSoundHandler.removeMessages(0);
                playInCallSoundHandler.removeMessages(1);
                TelephoneReceiver.handlerInCall.removeMessages(0);

                // 上一个状态是来电，本状态是挂断，则为未接。本状态可能会进入2次，最好还是不要在这做处理。
                sp.edit().putInt(SP_KEY_LAST_STATE, TelephonyManager.CALL_STATE_IDLE).apply();
                isOutCalling = false;
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                // TODO 接电话
                MyLog.i(TAG, "state: " + telephonyManager.getCallState());
                phoneStateRinging = false;
                sendStop();
                setRingNormal();
                if (mp != null) {
                    mp.stop();
                }
                VoiceQueue.getInstance().clearQueue();
                playInCallSoundHandler.removeMessages(0);
                playInCallSoundHandler.removeMessages(1);
                TelephoneReceiver.handlerInCall.removeMessages(0);
                // 关呼吸灯
                controlLEDOnRinging(false);
                sp.edit().putInt(SP_KEY_LAST_STATE, TelephonyManager.CALL_STATE_OFFHOOK).apply();
                break;

        }
    }

    /**
     * 发送Stop广播
     */
    void sendStop() {
        if (System.currentTimeMillis() - lastTime_IDLE < 3000) {
            lastTime_IDLE = System.currentTimeMillis();
            return;
        }
        Utils.sendBroadcast(MyIntent.INTENT_STOP, "TelephoneReceiver", "");
    }

    /**
     * 设置系统铃声模式为正常
     */
    private void setRingNormal() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING,
                mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(false);
        MyLog.e(TAG, "setRingSilent");
    }

    /**
     * 设置系统铃声模式为静音
     */
    public static void setRingSilent() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 获取系统声音的服务
     */
    static AudioManager getAudioManager() {
        return (AudioManager) MasterApplication.mAppContext.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 获取电话的系统服务
     */
    TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
    }

    /**
     * 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
     */
    ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断是否是什么网络状态
     */
    public void judgeNetType(TTS tts) {
        Context context = MasterApplication.mAppContext;

        long off = System.currentTimeMillis() - getLasttimeRINGING();
        MyLog.i(TAG, "off = " + off);
        if (off < 3000) {
            putLasttimeRINGING();
            return;
        }

        if (getConnectivityManager(context) != null) {
            NetworkInfo info = getConnectivityManager(context).getActiveNetworkInfo();

            // 判断是否有网络, 并判断是否为wifi状态
            if (info != null && info.isAvailable() &&
                    info.getType() == ConnectivityManager.TYPE_WIFI) {

                //判断来电是否为联系人，是则报出联系人姓名，反之读出电话号码
                if (tts == null) {
                    playInCallSoundHandler.removeMessages(0);
                    playInCallSoundHandler.sendEmptyMessage(0);
                    return;
                }
                judgeLinkMan(tts);

            } else {
                MyLog.e(TAG, "info.getType() !=  ConnectivityManager.TYPE_WIFI");
                playInCallSoundHandler.removeMessages(0);
                playInCallSoundHandler.sendEmptyMessage(0);
            }

        } else {
            MyLog.e(TAG, "connectivityManager ==  null");
            playInCallSoundHandler.removeMessages(0);
            playInCallSoundHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 判断电话号码是否是联系人中存在
     */
    public void judgeLinkMan(TTS tts) {
        Message message = playInCallSoundHandler.obtainMessage();
        playInCallSoundHandler.removeMessages(1);
        message.obj = tts;
        message.what = 1;
        playInCallSoundHandler.sendMessage(message);
    }


    public static void voiceMp(String uri) {
        MyLog.i(TAG, "voiceMp ： " + uri);
        mp = new MP(uri,
                BaseVoice.MP_ASSETS_RESOURCE,
                BaseRunnable.INTERRUPT_STATE_STOP,
                BaseRunnable.PRIORITY_CALL,
                false,
                new MP.MyOnPreparedListener(),
                new MP.MyOnCompletionListener());
        VoiceQueue.getInstance().add(mp);
    }

    void putLasttimeRINGING() {
        MasterApplication.mAppContext.getSharedPreferences("TelephoneRunnable",
                Context.MODE_PRIVATE).edit().putLong("RINGING", System.currentTimeMillis()).apply();
    }

    long getLasttimeRINGING() {
        return MasterApplication.mAppContext.getSharedPreferences("TelephoneRunnable",
                Context.MODE_PRIVATE).getLong("RINGING", 0);
    }

    /**
     * 开关呼吸灯
     *
     * @param isRinging 是否响铃中
     */
    void controlLEDOnRinging(boolean isRinging) {
        MyLog.i(TAG, "controlLEDOnRinging " + isRinging);
        if (!isOutCalling) {
            SharedPreferences sp = mContext.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
            sp.edit().putBoolean(SP_KEY_LED_STATUS, isRinging).apply();

            Intent intentCallOpen = new Intent("com.yongyida.robot.change.BREATH_LED");
            intentCallOpen.putExtra("on_off", isRinging);
            intentCallOpen.putExtra("Permanent", "call");
            if (isRinging) {
                intentCallOpen.putExtra("colour", 2);
                intentCallOpen.putExtra("frequency", 1);
                intentCallOpen.putExtra("priority", 8);
            }
            intentCallOpen.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            MasterApplication.mAppContext.sendBroadcast(intentCallOpen);
        }
    }

}
