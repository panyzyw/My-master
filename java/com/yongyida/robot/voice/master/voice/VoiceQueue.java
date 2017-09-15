package com.yongyida.robot.voice.master.voice;

import android.content.Intent;
import android.util.Log;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.service.RunVoiceService;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.zccl.ruiqianqi.brain.system.SystemPresenter;
import java.util.LinkedList;

/**
 * 存放 语音合成对象或媒体播放器对象的队列 的单利类
 *
 * @author Administrator
 */
public class VoiceQueue {
    private static final String TAG = "VoiceQueueTAG";
    private static VoiceQueue instance;

    private VoiceQueue() {
        queue = new LinkedList<BaseVoice>();
    }

    public static VoiceQueue getInstance() {
        if (instance == null) {
            synchronized (VoiceQueue.class){
                if(instance == null){
                    instance = new VoiceQueue();
                }
            }
        }
        return instance;
    }

    private LinkedList<BaseVoice> queue;

    private BaseVoice mRunningVoice;

    private boolean isSendRecycle = false;

    public LinkedList<BaseVoice> getQueue() {
        return queue;
    }

    public int getSize() {
        return queue.size();
    }

    public void popAndRun() {
        MyLog.i("VoiceQueue", "popAndRun");
        if (queue.size() <= 0) {
            MyLog.i("VoiceQueue", "voicequeue empty");
            if (mRunningVoice != null) {
                String extra = "";
                if (mRunningVoice.getIsEndSendRecycle()) {
                    Utils.sendBroadcast(MyIntent.INTENT_RECYCLE, "master", "");
                    extra = "recycle";
                    MyLog.i("VoiceQueue", "send recycle");
                }
                Intent intent = new Intent(MyIntent.QUEUE_COMPLETE);
                intent.putExtra("extra", extra);
                MasterApplication.mAppContext.sendBroadcast(intent);
                MyLog.i("VoiceQueue", "send broadcast : queue_complete");
            }
            mRunningVoice = null;
            return;
        }
        mRunningVoice = queue.pop();
        MyLog.i("VoiceQueue", "pop  : queue.size() = " + queue.size());
        if (mRunningVoice.getIsInterrupted()) {
            MyLog.i("VoiceQueue", "popAndRun : getIsInterrupted = true");
            mRunningVoice.reStart();
        } else {
            MyLog.i("VoiceQueue", "popAndRun : getIsInterrupted = false");
            mRunningVoice.start();
        }
    }

    public void clearQueue() {
        if (mRunningVoice != null) {
            Log.d("tempTAG", "clearQueue => mRunningVoice != null");
            sendLedBroad(false);
            if(mRunningVoice instanceof MP){
                mRunningVoice.stop();
            }
        }
        mRunningVoice = null;
        if (queue != null) {
            queue.clear();
        }
    }

    private void sendLedBroad(boolean onOff) {
        Intent intentSpeakOpen = new Intent("com.yongyida.robot.change.BREATH_LED");
        intentSpeakOpen.putExtra("on_off", onOff);
        intentSpeakOpen.putExtra("place", 3);
        intentSpeakOpen.putExtra("colour", 3);
        intentSpeakOpen.putExtra("frequency", 2);
        intentSpeakOpen.putExtra("Permanent", "speak");
        intentSpeakOpen.putExtra("priority", 6);
        MasterApplication.mAppContext.sendBroadcast(intentSpeakOpen);
    }

    public boolean isEmpty() {
        if (queue.size() == 0 && mRunningVoice == null) {
            return true;
        }
        return false;
    }

    public boolean isRunning() {
        if(SystemPresenter.getInstance(MasterApplication.mAppContext).isSpeaking()){
            return true;
        }
        return false;
    }

 /* 更改调用单独发音模块前的判断
   public boolean isRunning() {
        if (mRunningVoice == null){
            return false;
        }
        return true;
    }*/

    public void stopRun() {
        Intent intent = new Intent(MasterApplication.mAppContext, RunVoiceService.class);
        MasterApplication.mAppContext.stopService(intent);
        if (mRunningVoice != null)
            if (mRunningVoice.getInterrupt_state() == BaseRunnable.INTERRUPT_STATE_STOP) {
                mRunningVoice.stop();
            } else {
                mRunningVoice.pause();
                addQueue(mRunningVoice); // 将暂停的voice重新入队等待后面播放
            }
    }

    /**
     * 是不是添加更高优先级的voice
     *
     * @return
     */
    public boolean isBiggerPriority(BaseVoice baseVoice) {
        if (mRunningVoice == null || baseVoice == null)
            return false;
        if (baseVoice.getPriority() > mRunningVoice.getPriority())
            return true;
        return false;
    }

    public synchronized void add(BaseVoice baseVoice) {
        MyLog.i("VoiceQueue", "add");
        if (isRunning()) {
            if (isBiggerPriority(baseVoice)) {
                // 暂停正在运行的voice
                stopRun();
                addQueuehead(baseVoice);
                startRun();
            } else {
                addQueue(baseVoice);
            }
        } else {
            addQueue(baseVoice);
            startRun();
        }
    }

    private void addQueue(BaseVoice baseVoice) {
        for (int i = 0; i < queue.size(); i++) {
            if (baseVoice.getPriority() > queue.get(i).getPriority()) {
                queue.add(i, baseVoice);
                MyLog.i("VoiceQueue", "add queue , but not tail");
                return;
            }
        }
        queue.add(baseVoice);
        MyLog.i("VoiceQueue", "add queue tail");
    }

    private void addQueuehead(BaseVoice baseVoice) {
        queue.add(0, baseVoice);
        MyLog.i("VoiceQueue", "add queue head");
    }

    public void startRun() {
        popAndRun();
    }

}
